package com.nullprogram.chess;

/**
 * Board data structure.
 */
public abstract class Board {

    /**
     * The internal board array.
     */
    private Piece[][] board;

    /**
     * The size of this game board.
     */
    private int boardWidth, boardHeight;

    /**
     * Create a new Piece array, effectively clearing the board.
     */
    public final void clear() {
        board = new Piece[boardWidth][boardHeight];
    }

    /**
     * Determine if board is in a state of checkmate.
     *
     * @return true if board is in a state of checkmate
     */
    public abstract Boolean checkmate();

    /**
     * Determine if board is in a state of stalemate.
     *
     * @return true if board is in a state of stalemate
     */
    public abstract Boolean stalemate();

    /**
     * Determine if board is in a state of check.
     *
     * @return true if board is in a state of check
     */
    public abstract Boolean check();

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
        p.setPosition(pos);
        p.setBoard(this);
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
        Position a = move.getOrigin();
        Position b = move.getDest();
        board[b.getX()][b.getY()] = board[a.getX()][a.getY()];
        board[a.getX()][a.getY()] = null;
        getPiece(b).setPosition(b);
        getPiece(b).moved(true);
        if (move.getNext() != null) {
            move(move.getNext());
        }
    }

    /**
     * Return true if position has no piece on it.
     *
     * @param pos position to be tested
     * @return    emptiness of position
     */
    public final Boolean isEmpty(final Position pos) {
        return (getPiece(pos) == null);
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
        return (pos.getX() >= 0) && (pos.getY() >= 0)
               && (pos.getX() < boardWidth) && (pos.getY() < boardHeight);
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
}
