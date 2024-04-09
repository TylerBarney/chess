package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.webSocketMessages.ErrorMessage;
import model.webSocketMessages.LoadGameMessage;
import model.webSocketMessages.NotificationMessage;
import model.webSocketMessages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class GameplayUI extends NotificationHandler{
    ServerFacade facade;
    GamePrinter gamePrinter = new GamePrinter();
    String teamColor;
    int gameID;
    ChessGame game;

    public GameplayUI(ServerFacade facade, String[] input) throws Exception {
        this.facade = facade;
        this.teamColor = (input[2] == null) ? "white" : input[2];
        facade.setNotificationHandler(this);
        if (input[2] == null){
            facade.observeGame(facade.getGameID(Integer.parseInt(input[1])));
        } else {
            game = facade.joinGame(input[2], facade.getGameID(Integer.parseInt(input[1])));
        }
        this.gameID = facade.getGameID(Integer.parseInt(input[1]));
        boolean inGame = true;
//        if (teamColor.equalsIgnoreCase("white"))
//            gamePrinter.printBoard("white", new ChessGame(true).getBoard());
//        else if (teamColor.equalsIgnoreCase("black"))
//            gamePrinter.printBoard("black", new ChessGame(true).getBoard());
//        else
//            gamePrinter.printBoard("white", new ChessGame(true).getBoard());
        synchronized (System.out) {
            while(inGame){
                try{
                    System.out.print("[IN_GAME] >>> ");
                    Scanner scanner = new Scanner(System.in);
                    String line = scanner.nextLine();
                    var input1 = line.split(" ");
                    if (input1[0].equalsIgnoreCase("help")){
                        System.out.println("Redraw");
                        System.out.println("Leave");
                        System.out.println("Move COLUMN,ROW");
                        System.out.println("Resign");
                        System.out.println("Highlight COLUMN,ROW - legal moves");
                    } else if (input1[0].equalsIgnoreCase("Redraw")){
                        gamePrinter.printBoard(teamColor, game.getBoard());
                    } else if (input1[0].equalsIgnoreCase("Resign")){
                        inGame = false;
                        facade.resign(gameID);
                    } else if (input1[0].equalsIgnoreCase("Leave")){
                        inGame = false;
                        facade.leave(gameID);
                    }
                } catch (Exception ex){
                    System.out.println("Error");
                }
            }
        }
    }



    @Override
    void notify(ServerMessage serverMessage){
        GamePrinter gamePrinter = new GamePrinter();
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
            NotificationMessage notificationMessage = (NotificationMessage) serverMessage;
            System.out.print(notificationMessage.message+ "\n>>>");
        } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            LoadGameMessage loadGameMessage = (LoadGameMessage) serverMessage;
            synchronized (System.out) {gamePrinter.printBoard(teamColor, loadGameMessage.getGame().getBoard());}
            game = loadGameMessage.getGame();
        } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
            ErrorMessage errorMessage = (ErrorMessage) serverMessage;
            System.out.print(errorMessage.getErrorMessage()+ "\n>>>");
        }
    }
}
