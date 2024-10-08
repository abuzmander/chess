package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor turn;
    private ChessPosition whiteKing;
    private ChessPosition blackKing;


    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
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
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<ChessMove>();
        for(ChessMove move : moves){
            ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), piece);
            if (!isInCheck(piece.getTeamColor())){
                validMoves.add(move);
            }
            board.addPiece(move.getStartPosition(), piece);
            board.addPiece(move.getEndPosition(), capturedPiece);
        }
        return validMoves;

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = board.getPiece(start);
        if(piece == null){
            throw new InvalidMoveException("Invalid Move: No piece at starting position");
        }
        if(piece.getTeamColor() != turn){
          throw new InvalidMoveException("Invalid Move: Moving the Wrong Team Color");
        }
        boolean isValidMove = false;
        Collection <ChessMove> validMoves = validMoves(start);
        for(ChessMove validMove : validMoves){
            if (validMove.equals(move)) {
                isValidMove = true;
                break;
            }
        }
        if(!isValidMove){
            throw new InvalidMoveException("Invalid Move: Not a real move for the piece");
        }
        ChessPiece.PieceType promo = move.getPromotionPiece();
        board.addPiece(start, null);
        if (promo == null) {
            board.addPiece(end, piece);
        }
        else{
            board.addPiece(end, new ChessPiece(turn, promo));
        }
        if(turn == TeamColor.WHITE){
            turn = TeamColor.BLACK;
        }
        else{
            turn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    private ChessPosition findKingPosition(TeamColor teamColor){
        for(int i = 1; i < 9; i ++){
            for(int j = 1; j < 9; j++){
                ChessPosition currPosition = new ChessPosition(i, j);
                ChessPiece currPiece =board.getPiece(currPosition);
                if(currPiece != null) {
                    if (currPiece.getPieceType() == ChessPiece.PieceType.KING && currPiece.getTeamColor() == teamColor) {
                        return currPosition;
                    }
                }
            }
        }
        return null;
    }

    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor);
        if (kingPosition == null){
            return false;
        }
        for(int i = 1; i < 9; i ++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currPosition = new ChessPosition(i, j);
                ChessPiece currPiece =board.getPiece(currPosition);
                if(currPiece != null) {
                    if (currPiece.getTeamColor() != teamColor) {
                        Collection<ChessMove> moves = currPiece.pieceMoves(board, currPosition);
                        for (ChessMove move : moves) {
                            if (move.getEndPosition().equals(kingPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean hasValidMoves(TeamColor teamColor) {
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPosition currPosition = new ChessPosition(i, j);
                ChessPiece currPiece = board.getPiece(currPosition);
                if(currPiece != null) {
                    if(currPiece.getTeamColor().equals(teamColor)) {
                        Collection<ChessMove> moves = validMoves(currPosition);
                        if(!moves.isEmpty()){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return false;
        }
        return !hasValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)){
            return false;
        }
        return !hasValidMoves(teamColor);
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
        return board;
    }
}
