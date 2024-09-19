package chess;


import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    protected ChessGame.TeamColor myColor;
    protected PieceType myType;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        myColor = pieceColor;
        myType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return myColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return myType;
    }

    /**
     * returns when
     *
     * @param board
     * @param myPosition
     * @param rowDir
     * @param colDir
     * @return moves
     */
    public Collection<ChessMove> checkLine(ChessBoard board, ChessPosition myPosition, int rowDir, int colDir) {
        List<ChessMove> moves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            int nextRow = i * rowDir + myPosition.getRow();
            int nextCol = i * colDir + myPosition.getColumn();
            Optional<ChessMove> optNextMove = checkMove(board, myPosition, nextRow, nextCol, true,
                    null);
            if (optNextMove.isEmpty()) {
                return moves;
            } else {
                moves.add(optNextMove.get());
                if (board.getPiece(optNextMove.get().getEndPosition()) != null) {
                    return moves;
                }
            }
        }
        return moves;
    }

    public Optional<ChessMove> checkMove(ChessBoard board, ChessPosition myPosition, int rowCheck,
                                         int colCheck, boolean canTake, ChessPiece.PieceType promo) {
        ChessPosition endPos = new ChessPosition(rowCheck, colCheck);
        if (8 < rowCheck || 8 < colCheck || rowCheck < 1 || colCheck < 1) {
            return Optional.empty();
        }
        if (board.getPiece(endPos) != null) {
            if (board.getPiece(endPos).getTeamColor() == myColor) {
                return Optional.empty();
            }
        }
        return Optional.of(new ChessMove(myPosition, endPos, promo));
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();
        if (getPieceType() == PieceType.BISHOP) {
            // Checks the line in the positive positive direction
            moves.addAll(checkLine(board, myPosition, 1, 1));
            // Checks the line in the positive negative direction
            moves.addAll(checkLine(board, myPosition, 1, -1));
            // Checks the line in the negative positive direction
            moves.addAll(checkLine(board, myPosition, -1, 1));
            // Checks the line in the negative negative direction
            moves.addAll(checkLine(board, myPosition, -1, -1));
        }
        if (getPieceType() == PieceType.ROOK) {
            // Checks the line in the 0 positive direction
            moves.addAll(checkLine(board, myPosition, 0, 1));
            // Checks the line in the 0 negative direction
            moves.addAll(checkLine(board, myPosition, 0, -1));
            // Checks the line in the negative positive direction
            moves.addAll(checkLine(board, myPosition, 1, 0));
            // Checks the line in the negative negative direction
            moves.addAll(checkLine(board, myPosition, -1, 0));
        }
        if (getPieceType() == PieceType.QUEEN) {
            // Checks the line in the positive positive direction
            moves.addAll(checkLine(board, myPosition, 1, 1));
            // Checks the line in the positive negative direction
            moves.addAll(checkLine(board, myPosition, 1, -1));
            // Checks the line in the negative positive direction
            moves.addAll(checkLine(board, myPosition, -1, 1));
            // Checks the line in the negative negative direction
            moves.addAll(checkLine(board, myPosition, -1, -1));

            // Checks the line in the 0 positive direction
            moves.addAll(checkLine(board, myPosition, 0, 1));
            // Checks the line in the 0 negative direction
            moves.addAll(checkLine(board, myPosition, 0, -1));
            // Checks the line in the negative positive direction
            moves.addAll(checkLine(board, myPosition, 1, 0));
            // Checks the line in the negative negative direction
            moves.addAll(checkLine(board, myPosition, -1, 0));
        }
        return moves;

    }
}
