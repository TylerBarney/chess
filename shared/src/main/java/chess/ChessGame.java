package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //Get Piece info
        ChessPiece piece = board.getPiece(startPosition);
        //get Moves
        HashSet<ChessMove> pieceMoves = (HashSet<ChessMove>) piece.pieceMoves(board, startPosition);
        //remove moves that will put team in check
        Iterator<ChessMove> iter = pieceMoves.iterator();
        ChessBoard copyBoard;
        while(iter.hasNext()){
            copyBoard = board.copy();
            TeamColor myteam = piece.getTeamColor();
            makeMove(iter.next(), copyBoard);
            if(isInCheck(myteam, copyBoard) ) {
                iter.remove();
            }
        }
        //return set of moves based on piece and board
        return pieceMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startingPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = board.getPiece(startingPosition);
        //check if team turn
        if (piece.getTeamColor() != teamTurn) throw new InvalidMoveException("Not team turn");
        //check valid moves
        if (!validMoves(startingPosition).contains(move)) throw new InvalidMoveException("Invalid Move");
        //move piece
        board.addPiece(endPosition, piece);
        //empty starting position
        board.addPiece(startingPosition, null);
        teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    //used for simulating chess moves
    public void makeMove(ChessMove move, ChessBoard board){

        ChessPosition startingPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = board.getPiece(startingPosition);
        //move piece
        board.addPiece(endPosition, piece);
        //empty starting position
        board.addPiece(startingPosition, null);
        teamTurn = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, this.board);
    }

    //variation of isInCheck method with board parameter
    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {

        //get enemy moves and team king position
        HashSet<ChessMove> enemyMoves = getEnemyMoves(teamColor, board);
        ChessPosition kingPosition = board.searchBoardFor(new ChessPiece(teamColor, ChessPiece.PieceType.KING));

        //check if teamColor King is in any endposition
        Iterator<ChessMove> iter = enemyMoves.iterator();

        while (iter.hasNext()){
            if (iter.next().getEndPosition().equals(kingPosition)){
                return true;
            }
        }
        return false;
    }

    //Gets all enemy moves and stores in HashSet
    private HashSet<ChessMove> getEnemyMoves(TeamColor teamColor, ChessBoard board){
        ChessPosition iteratorPosition;
        HashSet<ChessMove> enemyMoves = new HashSet<>();
        ChessPiece currentPiece;
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                iteratorPosition = new ChessPosition(i, j);
                currentPiece = board.getPiece(iteratorPosition);
                if (currentPiece != null && currentPiece.pieceColor != teamColor){
                    enemyMoves.addAll(currentPiece.pieceMoves(board, iteratorPosition));
                }
            }
        }
        return enemyMoves;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor){
        TeamColor oppositeColor;
        if(teamColor == TeamColor.WHITE) oppositeColor = TeamColor.BLACK;
        else oppositeColor = TeamColor.WHITE;
        //checkmate if 1.King is in check, 2.Team Pieces can't stop check
        if (isInCheck(teamColor)){
            //2.Team Pieces cant stop check
            HashSet<ChessMove> teamMoves = new HashSet<>(getEnemyMoves(oppositeColor, board));
            //simulate moves and check for check
            ChessBoard copyBoard;
            Iterator<ChessMove> iter = teamMoves.iterator();

            while (iter.hasNext()){
                copyBoard = this.getBoard().copy();
                makeMove(iter.next(), copyBoard);
                if (!isInCheck(teamColor, copyBoard)) return false;
            }
            return true;

        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
