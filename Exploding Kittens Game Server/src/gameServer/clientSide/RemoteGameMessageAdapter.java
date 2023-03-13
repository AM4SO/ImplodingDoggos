package gameServer.clientSide;

import org.json.JSONObject;

import gameServer.CheatGameState;
import gameServer.GameState;

public interface RemoteGameMessageAdapter{
	public void onTurnStarted(int playerId);
	public void onCardPlayed(int cardId, int playerId);
	public void onGameStateReceived(GameState gameState);
	public void onPlayerJoined(int playerId);
	public void onPlayerDied(int playerId);
	public void onCardDrawn(int cardId);
	public void onCheatGameStateReceived(CheatGameState cheatGameState);
	public void onMessageFromPeers(JSONObject message);
}