package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.webSocketMessages.NotificationMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class GameplayUI implements NotificationHandler{
    final int BOARD_SIZE_IN_SQUARES = 10;
    static final int SQUARE_SIZE_IN_CHARS = 3;
    ServerFacade facade;

    public GameplayUI(ServerFacade facade, ChessGame game, String teamColor) {
        this.facade = facade;
        printBoard("white", new ChessGame(true).getBoard());
        printBoard("black", new ChessGame(true).getBoard());
    }
    void printBoard(String playerColor, ChessBoard board){


        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        if (playerColor.equalsIgnoreCase("White")) printWhiteBoard(board, out);
        else printBlackBoard(board, out);
        out.print(RESET_BG_COLOR);
        out.print(SET_TEXT_COLOR_WHITE);


    }

    void printChessBoard(PrintStream out, ChessBoard board, int startingRow, int endRow, int startingCol, int endCol){
        int i;
        int j;
        if (startingRow == 1) i = 1;
        else i = -1;
        if (startingCol == 1) j = 1;
        else j = -1;
        endRow += i;
        endCol += j;
        for (int boardRow = startingRow; boardRow != endRow; boardRow += i){
            for (int q = 0; q < 3; q++){
                if (q == 1) printSquare(out, " " + String.valueOf(boardRow) + " ", SET_BG_COLOR_LIGHT_GREY, SET_TEXT_COLOR_WHITE);
                else printSquare(out, null, SET_BG_COLOR_LIGHT_GREY, SET_TEXT_COLOR_WHITE);
                for (int boardCol = startingCol; boardCol != endCol; boardCol += j){
                    ChessPiece piece = board.getPiece(new ChessPosition(boardRow, boardCol));
                    String bgCOLOR;
                    if (boardRow % 2 != 0){
                        if (boardCol % 2 == 0){
                            //white square
                            bgCOLOR = (SET_BG_COLOR_WHITE);
                        } else {
                            //black square
                            bgCOLOR = (SET_BG_COLOR_BLACK);
                        }
                    }  else {
                        if (boardCol % 2 != 0){
                            //white square
                            bgCOLOR = (SET_BG_COLOR_WHITE);
                        } else {
                            //black square
                            bgCOLOR = (SET_BG_COLOR_BLACK);
                        }
                    }
                    String txtColor = SET_TEXT_COLOR_WHITE;
                    String text = null;
                    if (piece != null && piece.getTeamColor() == WHITE){
                        txtColor = SET_TEXT_COLOR_BLUE;
                        if (piece.getPieceType() == ChessPiece.PieceType.KING) text = WHITE_KING;
                        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) text = WHITE_QUEEN;
                        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) text = WHITE_BISHOP;
                        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) text = WHITE_KNIGHT;
                        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) text = WHITE_ROOK;
                        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) text = WHITE_PAWN;
                    } else if (piece != null && piece.getTeamColor() == BLACK){
                        txtColor = SET_TEXT_COLOR_DARK_GREY;
                        if (piece.getPieceType() == ChessPiece.PieceType.KING) text = BLACK_KING;
                        if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) text = BLACK_QUEEN;
                        if (piece.getPieceType() == ChessPiece.PieceType.BISHOP) text = BLACK_BISHOP;
                        if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) text = BLACK_KNIGHT;
                        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) text = BLACK_ROOK;
                        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) text = BLACK_PAWN;
                    }
                    if (q == 1) printSquare(out, text, bgCOLOR, txtColor);
                    else printSquare(out, null, bgCOLOR, SET_TEXT_COLOR_BLUE);
                }
                if (q == 1) printSquare(out, " " + String.valueOf(boardRow) + " ", SET_BG_COLOR_LIGHT_GREY, SET_TEXT_COLOR_WHITE);
                else printSquare(out, null, SET_BG_COLOR_LIGHT_GREY, SET_TEXT_COLOR_WHITE);
                setBlack(out);
                out.println();
            }
        }
    }
    void printBlackBoard(ChessBoard board, PrintStream out){
        String[] headers = {null, " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", null};
        printHeaders(headers, out);
        printChessBoard(out, board, 1, 8, 8, 1);
        printHeaders(headers, out);
    }
    void printWhiteBoard(ChessBoard board, PrintStream out){
        String[] headers = {null, " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", null};
        printHeaders(headers, out);
        printChessBoard(out, board, 8, 1, 1, 8);
        printHeaders(headers, out);
    }

    void printSquare(PrintStream out, String text, String bgColor, String txtColor){
        out.print(bgColor);
        out.print(txtColor);
        if (text == null){
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
        } else {
            int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
            int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;
            out.print(EMPTY.repeat(prefixLength));
            out.print(text);
            out.print(EMPTY.repeat(suffixLength));
        }
    }

    void printHeaders(String[] header, PrintStream out) {
        for (int i = 0; i < header.length; i++){
            printSquare(out, header[i], SET_BG_COLOR_LIGHT_GREY, SET_TEXT_COLOR_WHITE);
        }
        setReset(out);
        out.println();
    }
    void printHeader(PrintStream out, String headerText){
        setGrey(out);
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength;
        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    void printHeaderText(PrintStream out, String text){
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(SET_TEXT_BOLD);
        out.print(text);
        setGrey(out);
    }

    void setGrey(PrintStream out){
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    void setBlack(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    void setReset(PrintStream out){
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    @Override
    public void notify(NotificationMessage notificationMessage) {
        System.out.println(notificationMessage.message);
    }
}
