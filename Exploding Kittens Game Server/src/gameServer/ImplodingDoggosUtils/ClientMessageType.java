package gameServer.ImplodingDoggosUtils;

public enum ClientMessageType{
	TurnStarted, // playerId
	TurnEnded, // playerId
	CardPlayed, // playerId, cardId
	FullGameState, // GameState
	PlayerDied, // playerId
	PlayerJoined, // playerId
	CardDrawn, // playerId (drawer), cardId
	MessageFromPeers, // PeerMessage(type, fromId, etc) -- This message may be received immediately after playerJoined.  ****A****
	CheatGameState, // A game state including information which only a cheater should be able to access. Used by AI players. 
	GameStarted,
	GameEnded,
	RequestAcknowledge,
	RequestClientReady,
}
/// ****A**** Information which isn't strictly necessary for the functioning of the game is sent using RequestType.MessagePeers,
///           received using ClientMessageType.MessageFromPeers, and it can be further split up by enum "PeerMessageType". It gets 
///           immediately passed to all other clients after being received by the game server. These messages include functions such 
///           as player image icons and player chat messages, which are specifically used by the client function. These messages
///           don't get processed by the server in order to keep the client project independent of the server side to increase modularity,
///           i.e. the server project can be reused if building a completely new client program to go with it.