package model.webSocketMessages;

public class ResignCommand extends UserGameCommand{
    public int gameID;
    public ResignCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.RESIGN;
    }
}
