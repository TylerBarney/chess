import chess.*;
import model.webSocketMessages.NotificationMessage;
import ui.GameplayUI;
import ui.NotificationHandler;
import ui.PreloginUI;
import ui.ServerFacade;

public class Main implements NotificationHandler {
    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        new PreloginUI();
    }

    @Override
    public void notify(NotificationMessage notificationMessage) {
        System.out.println(notificationMessage.message);
    }
}