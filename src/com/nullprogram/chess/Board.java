package com.nullprogram.chess;

/**
 * Board data structure.
 */
public abstract class Board {

    private Piece[][] board;
    private int width, height;

    public void clear() {
        board = new Piece[width][height];
    }

    protected void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setPiece(int x, int y, Piece p) {
        setPiece(new Position(x, y), p);
    }

    public void setPiece(Position pos, Piece p) {
        board[pos.x][pos.y] = p;
        p.setPosition(pos);
        p.setBoard(this);
    }

    public Piece getPiece(Position pos) {
        return board[pos.x][pos.y];
    }

    public void move (Position a, Position b) {
        board[b.x][b.y] = board[a.x][a.y];
        board[a.x][a.y] = null;
        getPiece(b).setPosition(b);
    }

    /**
     * Return true if position has no piece on it.
     *
     * @param pos position to be tested
     */
    public Boolean isEmpty(Position pos) {
        return (getPiece(pos) == null);
    }

    /**
     * Return true if position has no piece of same side on it.
     *
     * @param pos  position to be tested
     * @param side side of the piece wanting to move
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
     */
    public Boolean isFree(Position pos) {
        return inRange(pos) && isEmpty(pos);
    }

    /**
     * Return true if position is in range and empty of given side.
     *
     * @param pos  position to be tested
     * @param side side of the piece wanting to move
     */
    public Boolean isFree(Position pos, Piece.Side side) {
        return inRange(pos) && isEmpty(pos, side);
    }
}
