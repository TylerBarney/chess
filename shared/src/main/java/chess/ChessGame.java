package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

import static java.lang.Math.abs;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn = TeamColor.WHITE;
    private ChessBoard board;
    public ChessGame() {

    }
    public ChessGame(boolean newGame){
        this.board = new ChessBoard(); //added this to instantiate a board
        board.resetBoard();
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
            ChessMove move = iter.next();
            makeMove(move, copyBoard);
            if(isInCheck(myteam, copyBoard) ) {
                iter.remove();
            } else {
                //repurpose isInCheck to see if rook position will be safe after castle
                ChessPosition endPosition = move.getEndPosition();
                if (piece.getPieceType() == ChessPiece.PieceType.KING & abs(startPosition.getColumn() - endPosition.getColumn()) == 2){
                    copyBoard = board.copy();
                    //check if king side will be safe for rook
                    int rookEndCol = (endPosition.getColumn() == 7) ? 6 : 4;
                    ChessPosition rookEndPos = new ChessPosition(endPosition.getRow(), rookEndCol);
                    ChessMove rookMove = new ChessMove(startPosition, rookEndPos, null);
                    makeMove(rookMove, copyBoard);
                    if(isInCheck(myteam, copyBoard)){
                        iter.remove();
                    }
                }
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
        ChessPiece.PieceType promotionType = move.getPromotionPiece();

        //move where there is no piece
        if (piece == null) throw new InvalidMoveException("No piece there");
        //check if team turn
        if (piece.getTeamColor() != teamTurn) throw new InvalidMoveException("Not team turn");
        //check valid moves
        if (!validMoves(startingPosition).contains(move)) throw new InvalidMoveException("Invalid Move");
        //move piece
        board.addPiece(endPosition, piece);
        //empty starting position
        board.addPiece(startingPosition, null);
        //check for castle move
        if (piece.getPieceType() == ChessPiece.PieceType.KING & abs(startingPosition.getColumn() - endPosition.getColumn()) == 2){
            makeCastleMove(piece, endPosition);
        }

        //check for enpassant update
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN & abs(startingPosition.getRow() - endPosition.getRow()) == 2){
            updateEnPassant(piece, endPosition);
        }
        //check for pawn taking advantage of en passant
        if ((piece.isLeftEnPassant() || piece.isRightEnPassant()) & startingPosition.getColumn() != endPosition.getColumn()){
            makeEnPassantMove(piece, startingPosition, endPosition);
        }

        //if piece has promotion
        if (promotionType != null){
            board.addPiece(endPosition, new ChessPiece(piece.getTeamColor(), promotionType));
        }
        //reset en passant values to false
        for (int i = 0; i < board.unCapturedPieces.length; i++){
            for (int j = 0; j < board.unCapturedPieces.length; j++){
                if (board.unCapturedPieces[i][j] == null || board.unCapturedPieces[i][j].getTeamColor() != teamTurn) continue;
                board.unCapturedPieces[i][j].setRightEnPassant(false);
                board.unCapturedPieces[i][j].setLeftEnPassant(false);
            }
        }
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
        if (piece != null) piece.setHasMoved(true);
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
        //iterate through ever piece, check if its team color
        ChessPosition iteratorPosition;
        for (int i = 1; i < board.unCapturedPieces.length+1; i++){
            for (int j = 1; j < board.unCapturedPieces.length+1; j++){
                iteratorPosition = new ChessPosition(i,j);
                if (board.getPiece(iteratorPosition) != null && board.getPiece(iteratorPosition).getTeamColor() == teamColor){
                    //check if it has any moves if so return false
                    if (!validMoves(iteratorPosition).isEmpty()) return false;
                }
            }
        }
        return true;
    }

    //perform the castle Move
    private void makeCastleMove(ChessPiece piece, ChessPosition endPosition) {
        int rookRow = (piece.getTeamColor() == TeamColor.WHITE) ? 1 : 8;
        int rookCol = (endPosition.getColumn() == 7) ? 8 : 1;
        ChessPosition rookStartingPos = new ChessPosition(rookRow, rookCol);
        int rookEndCol = (endPosition.getColumn() == 7) ? 6 : 4;
        ChessPosition rookEndPos = new ChessPosition(rookRow, rookEndCol);
        ChessPiece rookPiece = board.getPiece(rookStartingPos);
        board.addPiece(rookEndPos, rookPiece);
        board.addPiece(rookStartingPos, null);
    }

    //if pawn moves up two, enpassant updates are applied
    private void updateEnPassant(ChessPiece piece, ChessPosition endPosition){
        ChessPiece leftPiece = board.getPiece(new ChessPosition(endPosition.getRow(), endPosition.getColumn() - 1));
        ChessPiece rightPiece = board.getPiece(new ChessPosition(endPosition.getRow(), endPosition.getColumn() + 1));
        if (leftPiece != null){
            if (leftPiece.getPieceType() == ChessPiece.PieceType.PAWN & leftPiece.getTeamColor() != piece.getTeamColor()){
                leftPiece.setRightEnPassant(true);
            }
        }
        if (rightPiece != null){
            if (rightPiece.getPieceType() == ChessPiece.PieceType.PAWN & rightPiece.getTeamColor() != piece.getTeamColor()){
                rightPiece.setLeftEnPassant(true);
            }
        }
    }

    //make enpassant move
    private void makeEnPassantMove(ChessPiece piece, ChessPosition startingPosition, ChessPosition endPosition) {
        ChessPosition enemyPawnPos;
        board.addPiece(endPosition, piece);
        if (teamTurn == TeamColor.WHITE){
            enemyPawnPos = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn());
        } else {
            enemyPawnPos = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn());
        }
        board.addPiece(startingPosition, null);
        board.addPiece(enemyPawnPos, null);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}
