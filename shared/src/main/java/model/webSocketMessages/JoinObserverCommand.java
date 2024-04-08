package model.webSocketMessages;

public class JoinObserverCommand extends UserGameCommand{
    public int gameID;
    public JoinObserverCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.JOIN_OBSERVER;
    }
}
