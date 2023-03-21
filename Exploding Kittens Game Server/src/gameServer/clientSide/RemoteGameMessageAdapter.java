package gameServer.clientSide;

import org.json.JSONObject;

import gameServer.CheatGameState;
import gameServer.GameState;
import gameServer.ImplodingDoggosUtils.ClientMessageContent;

public interface RemoteGameMessageAdapter{
	public default void onMessageReceived(ClientMessageContent messageCont) {
		switch(messageCont.messageType) {
		case CardDrawn:
			onCardDrawn((int)messageCont.args[0]);
			break;
		case CardPlayed:
			onCardPlayed((int)messageCont.args[0], (int)messageCont.args[1]);
			break;
		case CheatGameState:
			onCheatGameStateReceived((CheatGameState)messageCont.args[0]);
			break;
		case FullGameState:
			onGameStateReceived((GameState)messageCont.args[0]);
			break;
		case MessageFromPeers:
			onMessageFromPeers((JSONObject)messageCont.args[0]);
			break;
		case PlayerDied:
			onPlayerDied((int)messageCont.args[0]);
			break;
		case PlayerJoined:
			onPlayerJoined((int)messageCont.args[0]);
			break;
		case TurnEnded:
			onTurnEnded((int)messageCont.args[0]);
			break;
		case TurnStarted:
			onTurnStarted((int)messageCont.args[0]);
			break;
		default:
			break;
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