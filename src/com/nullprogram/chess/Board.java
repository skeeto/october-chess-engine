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

    public Boolean isEmpty(Position pos) {
        return (getPiece(pos) == null);
    }

    public Boolean inRange(Position pos) {
        return
            (pos.x >= 0)    && (pos.y >= 0) &&
            (pos.x < width) && (pos.y < height);
    }
}
