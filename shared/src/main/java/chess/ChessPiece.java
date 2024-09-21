package chess;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return myColor == that.myColor && myType == that.myType;
    }
    @Override
    public int hashCode() {
        return Objects.hash(myColor, myType);
    }
    @Override
    public String toString() {
        return "ChessPiece{" +
                "myColor=" + myColor +
                ", myType=" + myType +
                '}';
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

    public enum CaptureType {
        ANY,
        CANT_TAKE,
        MUST_TAKE
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
            Optional<ChessMove> optNextMove = checkMove(board, myPosition, nextRow, nextCol, CaptureType.ANY
            );
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
                                         int colCheck, CaptureType captureType) {
        ChessPosition endPos = new ChessPosition(rowCheck, colCheck);
        if (8 < rowCheck || 8 < colCheck || rowCheck < 1 || colCheck < 1) {
            return Optional.empty();
        }
        if (board.getPiece(endPos) != null) {
            if (board.getPiece(endPos).getTeamColor() == myColor || captureType == CaptureType.CANT_TAKE) {
                return Optional.empty();
            }
        } else if (captureType == CaptureType.MUST_TAKE) {
            return Optional.empty();
        }
        return Optional.of(new ChessMove(myPosition, endPos, null));
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
        //Bishop Moves
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
        //Rook Moves
        if (getPieceType() == PieceType.ROOK) {
            // Checks the line in the 0 positive direction
            moves.addAll(checkLine(board, myPosition, 0, 1));
            // Checks the line in the 0 negative direction
            moves.addAll(checkLine(board, myPosition, 0, -1));
            // Checks the line in the positive  0 positive direction
            moves.addAll(checkLine(board, myPosition, 1, 0));
            // Checks the line in the negative 0 direction
            moves.addAll(checkLine(board, myPosition, -1, 0));
        }
        // Queen Moves
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
            // Checks the line in the positive 0 direction
            moves.addAll(checkLine(board, myPosition, 1, 0));
            // Checks the line in the negative 0 direction
            moves.addAll(checkLine(board, myPosition, -1, 0));
        }
        // King Moves
        if (getPieceType() == PieceType.KING) {
            // Checks the diagonal directions
            checkMove(board, myPosition, myPosition.getRow() + 1, myPosition.getColumn() +1, CaptureType.ANY).ifPresent(moves::add);
            checkMove(board, myPosition, myPosition.getRow() + 1, myPosition.getColumn() -1, CaptureType.ANY).ifPresent(moves::add);
            checkMove(board, myPosition, myPosition.getRow() - 1, myPosition.getColumn() +1, CaptureType.ANY).ifPresent(moves::add);
            checkMove(board, myPosition, myPosition.getRow() - 1, myPosition.getColumn() -1, CaptureType.ANY).ifPresent(moves::add);

            // Checks the straight directions
            checkMove(board, myPosition, myPosition.getRow() + 1, myPosition.getColumn(), CaptureType.ANY).ifPresent(moves::add);
            checkMove(board, myPosition, myPosition.getRow() - 1, myPosition.getColumn(), CaptureType.ANY).ifPresent(moves::add);
            checkMove(board, myPosition, myPosition.getRow(), myPosition.getColumn() +1, CaptureType.ANY).ifPresent(moves::add);
            checkMove(board, myPosition, myPosition.getRow(), myPosition.getColumn() -1, CaptureType.ANY).ifPresent(moves::add);
        }

        // KNIGHT Moves
        if (getPieceType() == PieceType.KNIGHT) {
            // Checks the knight positions
            checkMove(board, myPosition, myPosition.getRow() + 2, myPosition.getColumn() +1, CaptureType.ANY).ifPresent(moves::add);
            checkMove(board, myPosition, myPosition.getRow() + 2, myPosition.getColumn()-1, CaptureType.ANY).ifPresent(moves::add);
            checkMove(board, myPosition, myPosition.getRow() - 2, myPosition.getColumn() +1, CaptureType.ANY).ifPresent(moves::add);
            checkMove(board, myPosition, myPosition.getRow() - 2, myPosition.getColumn()-1, CaptureType.ANY).ifPresent(moves::add);
            checkMove(board, myPosition, myPosition.getRow() + 1, myPosition.getColumn() + 2, CaptureType.ANY).ifPresent(moves::add);
            checkMove(board, myPosition, myPosition.getRow() - 1, myPosition.getColumn() +2, CaptureType.ANY).ifPresent(moves::add);
            checkMove(board, myPosition, myPosition.getRow() +1, myPosition.getColumn() -2, CaptureType.ANY).ifPresent(moves::add);
            checkMove(board, myPosition, myPosition.getRow()-1, myPosition.getColumn()-2, CaptureType.ANY).ifPresent(moves::add);

        }

        // PAWN Moves
        if (getPieceType() == PieceType.PAWN) {
            boolean canPromote = false;
            // Gets the Pawn Moves when the PAWN is WHITE
            if(getTeamColor() == ChessGame.TeamColor.WHITE) {
                // Checks to see if we are right before the promotion row
                canPromote = myPosition.getRow() == 7;

                // Checks our forward movement
                Optional<ChessMove> move = checkMove(board, myPosition, myPosition.getRow() + 1, myPosition.getColumn(), CaptureType.CANT_TAKE);
                move.ifPresent(moves::add);

                // If we haven't moved from our starting position and we can move forward 1 then we check our opening 2 move
                if(myPosition.getRow() == 2 && move.isPresent()){
                    checkMove(board, myPosition, myPosition.getRow() + 2, myPosition.getColumn(), CaptureType.CANT_TAKE).ifPresent(moves::add);
                }

                // Checks our Pawn attacking moves
                checkMove(board, myPosition, myPosition.getRow() + 1, myPosition.getColumn() +1, CaptureType.MUST_TAKE).ifPresent(moves::add);
                checkMove(board, myPosition, myPosition.getRow() + 1, myPosition.getColumn() -1, CaptureType.MUST_TAKE).ifPresent(moves::add);

            }
            // Gets the Pawn Moves when the PAWN is BLACK
            else if (getTeamColor() == ChessGame.TeamColor.BLACK) {
                // Checks to see if we are right before the promotion row
                canPromote = myPosition.getRow() == 2;

                // Checks our forward movement
                Optional<ChessMove> move = checkMove(board, myPosition, myPosition.getRow() - 1, myPosition.getColumn(), CaptureType.CANT_TAKE);
                move.ifPresent(moves::add);

                // If we haven't moved from our starting position and we can move forward 1 then we check our opening 2 move
                if(myPosition.getRow() == 7 && move.isPresent()){
                    checkMove(board, myPosition, myPosition.getRow() - 2, myPosition.getColumn(), CaptureType.CANT_TAKE).ifPresent(moves::add);
                }
                // Checks our Pawn attacking moves
                checkMove(board, myPosition, myPosition.getRow() - 1, myPosition.getColumn() +1, CaptureType.MUST_TAKE).ifPresent(moves::add);
                checkMove(board, myPosition, myPosition.getRow() - 1, myPosition.getColumn() -1, CaptureType.MUST_TAKE).ifPresent(moves::add);
            }
            // If we can promote then we change our moves to promotion moves
            if (canPromote){
                moves = moves.stream().flatMap(ChessPiece::streamPromotions).collect(Collectors.toList());
            }
        }
        return moves;
    }
    public static Stream<ChessMove> streamPromotions(ChessMove move){
        return Stream.of(
                new ChessMove(move, PieceType.QUEEN),
                new ChessMove(move, PieceType.BISHOP),
                new ChessMove(move, PieceType.ROOK),
                new ChessMove(move, PieceType.KNIGHT)
        );
    }
}
