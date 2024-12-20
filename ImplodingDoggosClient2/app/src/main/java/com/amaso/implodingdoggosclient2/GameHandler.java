package com.amaso.implodingdoggosclient2;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;

import gameServer.CheatGameState;
import gameServer.GameMaker;
import gameServer.GameServer;
import gameServer.GameState;
import gameServer.HandState;
import gameServer.PlayerState;
import gameServer.Request;
import gameServer.clientSide.ImplodingDoggosUser;
import gameServer.clientSide.RemoteGameAdapter;
import gameServer.clientSide.RemoteGameMessageAdapter;

public class GameHandler {
    public static GameHandler gameHandler;
    public static GameMaker gameMaker = null;

    public ArrayList<ClientSidePlayer> players;
    public Chat chat = null;
    public PlayerList plrList = null;
    public HandState localPlayerHand;
    public ClientSidePlayer localPlayer;
    public PeerMessageInterface peerMessageHandler;
    public RemoteGameAdapter game;
    public ImplodingDoggosUser localUser;
    public GameHandler(RemoteGameAdapter game){// Pass through a connector object to the game.

        this.game = game;
        localUser = game.getUser();

        GameHandler.gameHandler = this;
        game.setRemoteGameMessageAdapter(new GameMessageHandler(this));
        peerMessageHandler = new PeerMessageHandler(this);
        // Temporary: Should import player details from ImplodingDoggosUser
        localPlayer = new ClientSidePlayer();
        localPlayer.playerState = new PlayerState();
        localPlayer.playerDetails = new PlayerDetails();
        localPlayer.playerDetails.playerName = "AMASO";
        localPlayer.playerState.userId = localUser.userId;
        localPlayer.playerState.playerId = 1;
        localPlayer.playerDetails.assetPack = new PlayerAssetPack();
        localPlayer.playerState = new PlayerState();

        players = new ArrayList<ClientSidePlayer>();
        players.add(localPlayer);
        if(plrList != null) plrList.adapter.setPlayers(players);

        GameServer.startNewThread(() -> {
            game.connectToGame();
        });
    }

    public void sendChatMessage(String chatMessage){
        PeerMessage peerMessage = new PeerMessage(PeerMessageType.ChatMessage);
        peerMessage.playerId = localPlayer.playerState.playerId;
        peerMessage.args = new Object[]{chatMessage};
        Request req = Request.MessagePeersRequest(localUser.userId,PeerMessage.Serialize(peerMessage));
        game.makeRequestAsync(req);
    }
    public void addReceivedChatMessage(ChatMessageDetails message){
        chat.addMessage(message);
    }
    public ArrayList<String> getPlayers(){// TODO: String is temporary
        Log.e("GetPlayers()", "NOT IMPLEMENTED");
        return null;
    }
}
class ClientSidePlayer{
    private static ArrayList<ClientSidePlayer> players = new ArrayList<>();
    public static ClientSidePlayer getPlayerByUserId(long userId) {
        for (ClientSidePlayer p : ClientSidePlayer.players) { // Won't work until gameState has been received.
            if (p.playerState.userId == userId) return p;
        }
        return null;
    }public static ClientSidePlayer getPlayerByPlayerId(int playerId){
        for (ClientSidePlayer p : players){
            if (p.playerState.playerId == playerId) return p;
        }
        return null;
    }

    PlayerDetails playerDetails = null;
    PlayerState playerState = null;
    public ClientSidePlayer(){
        ClientSidePlayer.players.add(this);
    }
}
interface PeerMessageInterface{
    default void handlePeerMessage(String peerMessage){
        handlePeerMessage(PeerMessage.Deserialize(peerMessage));
    }
    default void handlePeerMessage(PeerMessage message){
        if (message.messageType == PeerMessageType.ChatMessage){
            onChatMessageReceived(message);
            System.out.println();
        }else if (message.messageType == PeerMessageType.PlayerDetails){
            onPlayerDetailsReceived(message);
        }
    }
    void onChatMessageReceived(PeerMessage message);
    void onPlayerDetailsReceived(PeerMessage message);
}
class PeerMessageHandler implements  PeerMessageInterface{
    GameHandler parentGame;
    public PeerMessageHandler(GameHandler game){
        parentGame = game;
    }
    @Override
    public void onChatMessageReceived(PeerMessage message) {
        //if (message.args == null) return;
        String messageText = (String)message.args[0];
        //String messageText = message.
        ClientSidePlayer sender = ClientSidePlayer.getPlayerByPlayerId(message.playerId);
        assert sender != null;
        Log.i("PLAYER NAME", String.valueOf(sender == parentGame.localPlayer));
        ChatMessageDetails chatMessage = new ChatMessageDetails(sender.playerDetails,messageText);
        parentGame.addReceivedChatMessage(chatMessage);
    }

    @Override
    public void onPlayerDetailsReceived(PeerMessage message) {
        ClientSidePlayer player = ClientSidePlayer.getPlayerByPlayerId(message.playerId);
        assert player != null;
        PlayerDetails receivedDetails = (PlayerDetails)message.args[0];
        player.playerDetails = receivedDetails;
    }
}
class ChatMessageDetails{
    PlayerDetails player;
    String message;
    public ChatMessageDetails(PlayerDetails player, String message){
        this.player = player;
        this.message = message;
    }
}
class PlayerInformationPack{

}
class PeerMessage implements Serializable {
    static int defaultPlayerId = -1;
    public static String Serialize(PeerMessage message){
        try {
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ObjectOutputStream objOutput = new ObjectOutputStream(byteOutput);
            objOutput.writeObject(message);
            objOutput.close();
            return Base64.getEncoder().encodeToString(byteOutput.toByteArray());
        }catch(IOException e){
            Log.e("PeerMessage.Serialize","Failed to serialize message");
            e.printStackTrace();
            return null;
        }
    }
    public static PeerMessage Deserialize(String message) {
        try {
            ObjectInputStream objInput = new ObjectInputStream(
                    new ByteArrayInputStream(Base64.getDecoder().decode(message)));
            return (PeerMessage)objInput.readObject();
        }catch (IOException | ClassNotFoundException e){
            Log.e("PeerMessage.deserialize","Failed to deserialize message");
            e.printStackTrace();
            return null;
        }
    }
    //public long userId;
    public int playerId;
    public PeerMessageType messageType;
    public Object[] args;
    public PeerMessage(PeerMessageType messageType){
        this.messageType = messageType;
        args = new Object[]{};
        playerId = PeerMessage.defaultPlayerId;
    }
    public String serialize(){return PeerMessage.Serialize(this);}
}
enum PeerMessageType{
    ChatMessage, // messageText
    PlayerDetails, // playerDetails
    //RequestPlayerDetails, //
}
class PlayerDetails implements Serializable{
    static ArrayList<PlayerDetails> players = new ArrayList<>();

    public PlayerDetails(){
        players.add(this);
    }
    public String playerName;
    public PlayerAssetPack assetPack;
}
class PlayerAssetPack implements  Serializable{
    public int profilePictureId;
    public String playerName;
}
class GameMessageHandler implements RemoteGameMessageAdapter {
    GameHandler parentGame;
    public GameMessageHandler(GameHandler parent){
        parentGame = parent;
    }
    @Override
    public void onTurnStarted(int playerId) {

    }

    @Override
    public void onCardPlayed(int playerId, int cardId) {

    }

    @Override
    public void onGameStateReceived(GameState gameState) { // Requires to acknowledge
        gameState.players.forEach((plrState) -> {
            ClientSidePlayer plr = ClientSidePlayer.getPlayerByPlayerId(plrState.playerId);
            if (plr == null){
                plr = new ClientSidePlayer();
                parentGame.players.add(plr);
                plr.playerDetails = new PlayerDetails();
                plr.playerDetails.playerName = "BOT";
            }
            plr.playerState = plrState;
        });
        if (parentGame.plrList != null) parentGame.plrList.adapter.setPlayers(parentGame.players);
        //parentGame.game.sendAcknowledge();
    }

    @Override
    public void onPlayerJoined(int playerId) {
        ClientSidePlayer player = new ClientSidePlayer();
        player.playerState = new PlayerState();
        player.playerState.playerId = playerId;
        player.playerDetails = new PlayerDetails();
        player.playerDetails.playerName = "BOT";
        parentGame.players.add(player);
        if(parentGame.plrList != null) parentGame.plrList.adapter.setPlayers(parentGame.players);
        PlayerListTurnView.ActiveView.playerAdded(player);
    }

    @Override
    public void onPlayerDied(int playerId) {

    }

    @Override
    public void onCardDrawn(int playerId, int cardId) {

    }

    @Override
    public void onCheatGameStateReceived(CheatGameState cheatGameState) {

    }

    @Override
    public void onMessageFromPeers(String message) {
        parentGame.peerMessageHandler.handlePeerMessage(message);
    }

    @Override
    public void onTurnEnded(int playerId) {

    }
}