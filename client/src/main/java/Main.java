import chess.*;
import model.webSocketMessages.NotificationMessage;
import model.webSocketMessages.ServerMessage;
import ui.GameplayUI;
import ui.NotificationHandler;
import ui.PreloginUI;
import ui.ServerFacade;

public class Main extends NotificationHandler {
    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        new PreloginUI();
    }
}