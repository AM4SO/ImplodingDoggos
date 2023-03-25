package com.amaso.implodingdoggosclient2;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import gameServer.CheatGameState;
import gameServer.GameState;
import gameServer.clientSide.RemoteGameAdapter;
import gameServer.clientSide.RemoteGameMessageAdapter;

public class GameHandler {
    public static GameHandler gameHandler;

    public RemoteGameAdapter game;
    public GameHandler(RemoteGameAdapter game){// Pass through a connector object to the game.
        GameHandler.gameHandler = this;
        game.setRemoteGameMessageAdapter(new GameMessageHandler());
    }

    public void sendChatMessage(String chatMessage){
        Log.e("sendChatMessage()", "NOT IMPLEMENTED");
    }
    public ArrayList<String> getPlayers(){// TODO: String is temporary
        Log.e("GetPlayers()", "NOT IMPLEMENTED");
        JSONObject x = new JSONObject();
        x.pu
        return null;
    }
}
class PlayerInformationPack{

}
class PeerMessage{

}
class GameMessageHandler implements RemoteGameMessageAdapter {

    @Override
    public void onTurnStarted(int playerId) {

    }

    @Override
    public void onCardPlayed(int playerId, int cardId) {

    }

    @Override
    public void onGameStateReceived(GameState gameState) {

    }

    @Override
    public void onPlayerJoined(int playerId) {

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

    }

    @Override
    public void onTurnEnded(int playerId) {

    }
}