package com.nullprogram.chess;

import com.nullprogram.chess.boards.BoardFactory;
import com.nullprogram.chess.pieces.King;
import com.nullprogram.chess.pieces.PieceFactory;
import java.io.Serializable;

/**
 * Board data structure.
 *
 * After the initial setup, the board <i>must</i> only be modified
 * through move transaction. This allows undo() and copy(), which many
 * other things depends on, to work properly.
 */
public abstract class Board implements Serializable {

    /** Versioning for object serialization. */
    private static final long serialVersionUID = 244162996302362607L;

    /** The internal board array. */
    private Piece[][] board;

    /** The size of this game board. */
    private int boardWidth, boardHeight;

    /** Moves taken in this game so far. */
    private final MoveList moves = new MoveList(this);

    /**
     * Create a new Piece array, effectively clearing the board.
     */
    public final void clear() {
        board = new Piece[boardWidth][boardHeight];
    }

    /**
     * Determine if board is in a state of checkmate.
     *
     * @param       side side to be checked
     * @return true if board is in a state of checkmate
     */
    public abstract Boolean checkmate(Piece.Side side);

    /**
     * Determine if board is in a state of stalemate.
     *
     * @param       side side to be checked
     * @return true if board is in a state of stalemate
     */
    public abstract Boolean stalemate(Piece.Side side);

    /**
     * Determine if board is in a state of check.
     *
     * @param side side to check for check
     * @return     true if board is in a state of check
     */
    public abstract Boolean check(Piece.Side side);

    /**
     * Determine if board is in a state of check.
     *
     * @return     true if board is in a state of check
     */
    public final Boolean check() {
        return check(Piece.Side.WHITE) || check(Piece.Side.BLACK);
    }

    /**
     * Determine if board is in checkmate.
     *
     * @return     true if board is in checkmate
     */
    public final Boolean checkmate() {
        return checkmate(Piece.Side.WHITE) || checkmate(Piece.Side.BLACK);
    }

    /**
     * Determine if board is in stalemate.
     *
     * @return     true if board is in stalemate
     */
    public final Boolean stalemate() {
        return stalemate(Piece.Side.WHITE) || stalemate(Piece.Side.BLACK);
    }

    /**
     * Find the king belonging to the given side.
     *
     * @param side whose king
     * @return     the king's board position
     */
    public final Position findKing(final Piece.Side side) {
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Position pos = new Position(x, y);
                Piece p = getPiece(pos);
                if (p instanceof King &&
                    p.getSide() == side) {

                    return pos;
                }
            }
        }
        return null;
    }

    /**
     * Set the width of the board.
     *
     * @param width the new width
     */
    protected final void setWidth(final int width) {
        boardWidth = width;
    }

    /**
     * Get the width of the board.
     *
     * @return the width of the board
     */
    public final int getWidth() {
        return boardWidth;
    }

    /**
     * Set the height of the board.
     *
     * @param height the new height
     */
    protected final void setHeight(final int height) {
        boardHeight = height;
    }

    /**
     * Get the height of the board.
     *
     * @return the height of the board
     */
    public final int getHeight() {
        return boardHeight;
    }

    /**
     * Put the given Piece at the given position on the board.
     *
     * @param x horizontal part of the position
     * @param y vertical part of the position
     * @param p the piece object to be placed
     */
    public final void setPiece(final int x, final int y, final Piece p) {
        setPiece(new Position(x, y), p);
    }

    /**
     * Put the given Piece at the given Position on the board.
     *
     * @param pos the position on the board
     * @param p   the piece object to be placed
     */
    public final void setPiece(final Position pos, final Piece p) {
        board[pos.getX()][pos.getY()] = p;
        if (p != null) {
            p.setPosition(pos);
            p.setBoard(this);
        }
    }

    /**
     * Get the Piece at the given Position.
     *
     * @param pos the position on the board
     * @return    the Piece at the position
     */
    public final Piece getPiece(final Position pos) {
        return board[pos.getX()][pos.getY()];
    }

    /**
     * Perform the given move action.
     *
     * @param move the move
     */
    public final void move(final Move move) {
        moves.add(move);
        execMove(move);
    }

    /**
     * Actually execute the move.
     *
     * @param move the move
     */
    private void execMove(final Move move) {
        if (move == null) {
            return;
        }
        Position a = move.getOrigin();
        Position b = move.getDest();
        if (a != null && b != null) {
            move.setCaptured(getPiece(b));
            setPiece(b, getPiece(a));
            setPiece(a, null);
            getPiece(b).setPosition(b);
            getPiece(b).incMoved();
        } else if (a != null && b == null) {
            move.setCaptured(getPiece(a));
            setPiece(a, null);
        } else {
            setPiece(b, PieceFactory.create(move.getReplacement(),
                                            move.getReplacementSide()));
        }
        execMove(move.getNext());
    }

    /**
     * Undo the last move.
     */
    public final void undo() {
        execUndo(moves.pop());
    }

    /**
     * Actually perform the undo action.
     *
     * @param move the move
     */
    private void execUndo(final Move move) {
        if (move == null) {
            return;
        }
        execUndo(move.getNext()); // undo in reverse
        Position a = move.getOrigin();
        Position b = move.getDest();
        if (a != null && b != null) {
            setPiece(a, getPiece(b));
            setPiece(b, move.getCaptured());
            getPiece(a).setPosition(a);
            getPiece(a).decMoved();
        } else if (a != null && b == null) {
            setPiece(a, move.getCaptured());
        } else {
            setPiece(b, null);
        }
    }

    /**
     * Return the last move made.
     *
     * @return the previous move
     */
    public final Move last() {
        return moves.peek();
    }

    /**
     * Return true if position has no piece on it.
     *
     * @param pos position to be tested
     * @return    emptiness of position
     */
    public final Boolean isEmpty(final Position pos) {
        return getPiece(pos) == null;
    }

    /**
     * Return true if position has no piece of same side on it.
     *
     * @param pos  position to be tested
     * @param side side of the piece wanting to move
     * @return    emptiness of position
     */
    public final Boolean isEmpty(final Position pos, final Piece.Side side) {
        Piece p = getPiece(pos);
        if (p == null) {
            return true;
        }
        return p.getSide() != side;
    }

    /**
     * Return true if position is on the board.
     *
     * @param pos position to be tested
     * @return    validity of position
     */
    public final Boolean inRange(final Position pos) {
        return (pos.getX() >= 0) && (pos.getY() >= 0) &&
               (pos.getX() < boardWidth) && (pos.getY() < boardHeight);
    }

    /**
     * Return true if position is in range <i>and</i> empty.
     *
     * @param pos position to be tested
     * @return    validity of position
     */
    public final Boolean isFree(final Position pos) {
        return inRange(pos) && isEmpty(pos);
    }

    /**
     * Return true if position is in range and empty of given side.
     *
     * @param pos  position to be tested
     * @param side side of the piece wanting to move
     * @return     validity of position
     */
    public final Boolean isFree(final Position pos, final Piece.Side side) {
        return inRange(pos) && isEmpty(pos, side);
    }

    /**
     * Copy this board.
     *
     * @return deep copy of the board.
     */
    public final Board copy() {
        Board fresh = BoardFactory.create(this.getClass());
        for (Move move : moves) {
            fresh.move(new Move(move));
        }
        return fresh;
    }

    /**
     * Generate a list of all moves for the given side.
     *
     * @param side  side to get moves for
     * @param check check for check
     * @return      list of all moves
     */
    public final MoveList allMoves(final Piece.Side side, final boolean check) {
        MoveList list = new MoveList(this, false);
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                Piece p = board[x][y];
                if (p != null && p.getSide() == side) {
                    list.addAll(p.getMoves(check));
                }
            }
        }
        return list;
    }

    /**
     * Return the number of moves taken on this board.
     *
     * @return number of moves taken on this board
     */
    public final int moveCount() {
        return moves.size();
    }
}
