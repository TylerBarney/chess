package model.webSocketMessages;

public class LeaveCommand extends UserGameCommand{
    public int gameID;
    public LeaveCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.LEAVE;
    }
}
