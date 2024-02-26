package chess.moveCalculator;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator extends MoveCalculator{

    private ChessPiece thisPiece;
    public KingMovesCalculator(ChessPiece thisPiece){
        this.thisPiece = thisPiece;
    }

    @Override
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> moves = new HashSet<>();
        int[][] possibleMoves = {{1,0}, {1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
        ChessPosition endPosition;
        for (int i = 0; i < possibleMoves.length; i++){
            endPosition = new ChessPosition(myPosition.getRow() + possibleMoves[i][0], myPosition.getColumn() + possibleMoves[i][1]);
            if (canMove(board, endPosition, thisPiece.getTeamColor()) == 0 || canMove(board, endPosition, thisPiece.getTeamColor()) == 1){
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        //castle King-side
        //king hasn't moved
        if (!thisPiece.getHasMoved()){
            //get castle, check if it has moved
            ChessPosition kingCastlePosition = (thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE) ? new ChessPosition(1, 8) : new ChessPosition(8, 8);
            ChessPiece kingCastle = board.getPiece(kingCastlePosition);
            //check for pieces between king and castle
            if (!board.isPlaceEmpty(new ChessPosition(myPosition.getRow(), 6)) || !board.isPlaceEmpty(new ChessPosition(myPosition.getRow(), 7))){
                return moves;
            }
            if (kingCastle != null && kingCastle.getPieceType() == ChessPiece.PieceType.ROOK){
                if (!kingCastle.getHasMoved()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 2), null));
                }
            }
            //castle queen-side
            ChessPosition queenCastlePosition = (thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE) ? new ChessPosition(1, 1) : new ChessPosition(8, 1);
            ChessPiece queenCastle = board.getPiece(queenCastlePosition);
            //check for pieces between king and castle
            if (!board.isPlaceEmpty(new ChessPosition(myPosition.getRow(), 4)) || !board.isPlaceEmpty(new ChessPosition(myPosition.getRow(), 3)) || !board.isPlaceEmpty(new ChessPosition(myPosition.getRow(), 2))){
                return moves;
            }
            if (queenCastle != null && queenCastle.getPieceType() == ChessPiece.PieceType.ROOK){
                if (!queenCastle.getHasMoved()){
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 2), null));
                }
            }
        }
        return moves;
    }



}
