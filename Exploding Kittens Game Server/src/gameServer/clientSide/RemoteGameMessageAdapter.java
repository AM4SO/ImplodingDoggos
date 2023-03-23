package gameServer.clientSide;

import org.json.JSONObject;

import gameServer.CheatGameState;
import gameServer.GameState;
import gameServer.ImplodingDoggosUtils.ClientMessageContent;
import gameServer.ImplodingDoggosUtils.ClientMessageType;

public interface RemoteGameMessageAdapter{/// TODO: CONVERT ALL THINGYS TO JSON OBJECTS BEFORE SENDING
	public default void onMessageReceived(ClientMessageContent messageCont) {
		ClientMessageType messageType = messageCont.messageType;
		if (messageType == ClientMessageType.CardDrawn) {
			onCardDrawn((int)messageCont.args[0]);
		}else if (messageType == ClientMessageType.CardPlayed) {
			onCardPlayed((int)messageCont.args[0], (int)messageCont.args[1]);
		}else if (messageType == ClientMessageType.CheatGameState) {
			onCheatGameStateReceived((CheatGameState)messageCont.args[0]);
		}else if (messageType == ClientMessageType.FullGameState) {
			onGameStateReceived((GameState)messageCont.args[0]);
		}else if (messageType == ClientMessageType.MessageFromPeers) {
			onMessageFromPeers((JSONObject)messageCont.args[0]);
		}else if (messageType == ClientMessageType.PlayerDied) {
			onPlayerDied((int)messageCont.args[0]);
		}else if (messageType == ClientMessageType.PlayerJoined) {
			onPlayerJoined((int)messageCont.args[0]);
		}else if (messageType == ClientMessageType.TurnEnded) {
			onTurnEnded((int)messageCont.args[0]);
		}else if (messageType == ClientMessageType.TurnStarted) {
			onTurnStarted((int)messageCont.args[0]);
		}
	}
	
	public void onTurnStarted(int playerId);
	public void onCardPlayed(int cardId, int playerId);
	public void onGameStateReceived(GameState gameState);
	public void onPlayerJoined(int playerId);
	public void onPlayerDied(int playerId);
	public void onCardDrawn(int cardId);
	public void onCheatGameStateReceived(CheatGameState cheatGameState);
	public void onMessageFromPeers(JSONObject message);
	public void onTurnEnded(int playerId);
}