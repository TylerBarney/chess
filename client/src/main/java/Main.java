import chess.*;
import ui.GameplayUI;
import ui.PreloginUI;
import ui.ServerFacade;

public class Main {
    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ServerFacade facade = new ServerFacade(8080);
        new PreloginUI(facade);
    }
}