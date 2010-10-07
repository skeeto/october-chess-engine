package com.nullprogram.chess;

/**
 * Board data structure.
 */
public abstract class Board {

    private Piece[][] board;
    private int width, height;

    /**
     * Create a new Piece array, effectively clearing the board.
     */
    public void clear() {
        board = new Piece[width][height];
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
    protected void setWidth(int width) {
        this.width = width;
    }

    /**
     * Get the width of the board.
     *
     * @return the width of the board
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set the height of the board.
     *
     * @param height the new height
     */
    protected void setHeight(int height) {
        this.height = height;
    }

    /**
     * Get the height of the board.
     *
     * @return the height of the board
     */
    public int getHeight() {
        return height;
    }

    /**
     * Put the given Piece at the given position on the board.
     *
     * @param x horizontal part of the position
     * @param y vertical part of the position
     * @param p the piece object to be placed
     */
    public void setPiece(int x, int y, Piece p) {
        setPiece(new Position(x, y), p);
    }

    /**
     * Put the given Piece at the given Position on the board.
     *
     * @param pos the position on the board
     * @param p   the piece object to be placed
     */
    public void setPiece(Position pos, Piece p) {
        board[pos.x][pos.y] = p;
        p.setPosition(pos);
        p.setBoard(this);
    }

    /**
     * Get the Piece at the given Position.
     *
     * @param pos the position on the board
     * @return    the Piece at the position
     */
    public Piece getPiece(Position pos) {
        return board[pos.x][pos.y];
    }

    /**
     * Move the piece at the first position to the second position.
     *
     * @param a origin
     * @param b destination
     */
    public void move(Position a, Position b) {
        board[b.x][b.y] = board[a.x][a.y];
        board[a.x][a.y] = null;
        getPiece(b).setPosition(b);
    }

    /**
     * Return true if position has no piece on it.
     *
     * @param pos position to be tested
     * @return    emptiness of position
     */
    public Boolean isEmpty(Position pos) {
        return (getPiece(pos) == null);
    }

    /**
     * Return true if position has no piece of same side on it.
     *
     * @param pos  position to be tested
     * @param side side of the piece wanting to move
     * @return    emptiness of position
     */
    public Boolean isEmpty(Position pos, Piece.Side side) {
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
    public Boolean inRange(Position pos) {
        return
            (pos.x >= 0)    && (pos.y >= 0) &&
            (pos.x < width) && (pos.y < height);
    }

    /**
     * Return true if position is in range <i>and</i> empty.
     *
     * @param pos position to be tested
     * @return    validity of position
     */
    public Boolean isFree(Position pos) {
        return inRange(pos) && isEmpty(pos);
    }

    /**
     * Return true if position is in range and empty of given side.
     *
     * @param pos  position to be tested
     * @param side side of the piece wanting to move
     * @return     validity of position
     */
    public Boolean isFree(Position pos, Piece.Side side) {
        return inRange(pos) && isEmpty(pos, side);
    }
}
