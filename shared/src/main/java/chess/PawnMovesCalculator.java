package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements MoveCalculator{
    private ChessPiece thisPiece;
    public PawnMovesCalculator(ChessPiece thisPiece){
        this.thisPiece = thisPiece;
    }

    @Override
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> moves = new HashSet<>();
        ChessPosition endPosition;
        //move up
        if (thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
            endPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        } else {
            endPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        }
        if (canMove(board, endPosition) == 0){
            if (canPromote(endPosition)){
                moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
            } else {
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            if (hasntMoved(myPosition)){
                if (thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                    endPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                } else {
                    endPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                }
                if (canMove(board, endPosition) == 0){
                    moves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }

        //up right
        if (thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
            endPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        } else {
            endPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        }
        if (canMove(board, endPosition) == 1){
            if (canPromote(endPosition)){
                moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
            } else {
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        //up left
        if (thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
            endPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        } else {
            endPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        }
        if (canMove(board, endPosition) == 1){
            if (canPromote(endPosition)){
                moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
            } else {
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        //enPassant right
        if (thisPiece.isRightEnPassant()){
            if (thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                endPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                moves.add( new ChessMove(myPosition, endPosition, null));
            } else {
                endPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        //enPassant left
        if(thisPiece.isLeftEnPassant()){
            if (thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                endPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                moves.add(new ChessMove(myPosition, endPosition, null));
            } else {
                endPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return moves;
    }


    private boolean hasntMoved(ChessPosition myPosition){
        if (thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) return true;
        if (thisPiece.getTeamColor() == ChessGame.TeamColor. BLACK && myPosition.getRow() == 7) return true;
        return false;
    }

    private boolean canPromote(ChessPosition endPosition){
        if (thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE && endPosition.getRow() == 8) return true;
        if (thisPiece.getTeamColor() == ChessGame.TeamColor. BLACK && endPosition.getRow() == 1) return true;
        return false;
    }
    private int canMove(ChessBoard board, ChessPosition endPosition){
        if (endPosition.getRow() <= 0 || endPosition.getRow() >= 9) return 3;
        if (endPosition.getColumn() <= 0 || endPosition.getColumn() >= 9) return 3;
        if (board.getPiece(endPosition) == null) return 0;
        if (board.getPiece(endPosition).getTeamColor() == this.thisPiece.getTeamColor()) return 2;
        if (board.getPiece(endPosition).getTeamColor() != this.thisPiece.getTeamColor()) return 1;
        return 0;
    }
}
