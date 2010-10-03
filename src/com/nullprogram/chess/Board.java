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
        board[x][y] = p;
    }

    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    public void move (int x0, int y0, int x1, int y1) {
        board[x1][y1] = board[x0][y0];
        board[x0][y0] = null;
    }
}
