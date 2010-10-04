package com.nullprogram.chess;

/**
 * Board data structure.
 */
public class Board {

    private Piece[][] board;
    private int width, height;

    static final int DEFAULT_WIDTH = 8;
    static final int DEFAULT_HEIGHT = 8;

    public Board() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        clear();
    }

    public void clear() {
        board = new Piece[width][height];
    }

    public int getWidth() {
        return width;
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
    }

    public Piece getPiece(Position pos) {
        return board[pos.x][pos.y];
    }

    public void move (Position a, Position b) {
        board[b.x][b.y] = board[a.x][a.y];
        board[a.x][a.y] = null;
        getPiece(b).setPosition(b);
    }
}
