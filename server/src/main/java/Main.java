import chess.*;
import dataAccess.*;
import model.UserData;
import server.Server;
import service.GameService;
import service.UserService;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, DataAccessException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        new Server().run(8080);
        System.out.println("♕ 240 Chess Server: " + piece);

    }
}