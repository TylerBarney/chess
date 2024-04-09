package ui;

import chess.*;
import model.webSocketMessages.ErrorMessage;
import model.webSocketMessages.LoadGameMessage;
import model.webSocketMessages.NotificationMessage;
import model.webSocketMessages.ServerMessage;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Vector;

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
                        System.out.println("Move starting: COLUMN ROW ending: COLUMN ROW if promotion: PIECE");
                        System.out.println("Resign");
                        System.out.println("Highlight COLUMN ROW - legal moves");
                    } else if (input1[0].equalsIgnoreCase("Redraw")){
                        gamePrinter.printBoard(teamColor, game.getBoard(), null);
                    } else if (input1[0].equalsIgnoreCase("Resign")){
                        inGame = false;
                        facade.resign(gameID);
                    } else if (input1[0].equalsIgnoreCase("Leave")){
                        inGame = false;
                        facade.leave(gameID);
                    } else if (input1[0].equalsIgnoreCase("move") && input1.length >= 5){
                        proccessMove(input1);
                    } else if (input1[0].equalsIgnoreCase("Highlight") && input1.length >= 3){
                        processPosition(input1);
                    }
                } catch (Exception ex){
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
    }

    private void processPosition(String[] input){
        int col = charToNum(input[1].charAt(0));
        int row = Integer.parseInt(input[2]);
        if (col < 1 || col > 8 || row < 1 || row > 8){
            System.out.println("Error: invalid poisition");
        } else {
            ChessPosition position = new ChessPosition(row, col);
            HashSet<ChessMove> possibleMoves = (HashSet<ChessMove>) game.validMoves(position);
            Vector<ChessPosition> endPositions = new Vector<>();
            endPositions.add(position);
            for (var move : possibleMoves){
                endPositions.add(move.getEndPosition());
            }
            gamePrinter.printBoard(teamColor, game.getBoard(), endPositions);
        }
    }

    private void proccessMove(String[] input) throws IOException {
        int startCol = charToNum(input[1].charAt(0));
        int startRow = Integer.parseInt(input[2]);
        int endCol = charToNum(input[3].charAt(0));
        int endRow = Integer.parseInt(input[4]);
        boolean isInvalid = false;
        ChessPiece.PieceType promotion = null;
        if (input.length >= 6){
            switch(ChessPiece.PieceType.valueOf(input[5])){
                case KING -> promotion = ChessPiece.PieceType.KING;
                case KNIGHT -> promotion = ChessPiece.PieceType.KNIGHT;
                case QUEEN -> promotion = ChessPiece.PieceType.QUEEN;
                case BISHOP -> promotion = ChessPiece.PieceType.BISHOP;
                case ROOK -> promotion = ChessPiece.PieceType.ROOK;
                default -> isInvalid = true;
            }
        }

        if (startCol > 8 || startCol < 1 || startRow > 8 || startRow < 1 || endCol > 8 || endCol < 1 || endRow > 8 || endRow < 1 || isInvalid){
            System.out.println("Move invalid");
        } else {
            ChessMove move = new ChessMove(new ChessPosition(startRow, startCol), new ChessPosition(endRow, endCol), promotion);
            facade.makeMove(gameID, move);
        }
    }

    private int charToNum(char c){
        return (c-96);
    }

    @Override
    void notify(ServerMessage serverMessage){
        GamePrinter gamePrinter = new GamePrinter();
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
            NotificationMessage notificationMessage = (NotificationMessage) serverMessage;
            System.out.print(notificationMessage.message+ "\n>>>");
        } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            LoadGameMessage loadGameMessage = (LoadGameMessage) serverMessage;
            //synchronized (System.out) {System.out.println("\n\n"); gamePrinter.printBoard(teamColor, loadGameMessage.getGame().getBoard());}
            System.out.println("\n\n");
            gamePrinter.printBoard(teamColor, loadGameMessage.getGame().getBoard(), null);
            System.out.println(">>>");
            game = loadGameMessage.getGame();
        } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
            ErrorMessage errorMessage = (ErrorMessage) serverMessage;
            System.out.print(errorMessage.getErrorMessage()+ "\n>>>");
        }
    }
}
