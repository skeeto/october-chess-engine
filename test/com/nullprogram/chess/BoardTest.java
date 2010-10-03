package com.nullprogram.chess;

import junit.framework.TestCase;

public class BoardTest extends TestCase {

    Board board;
    static final int SIZE = 8;

    public BoardTest(String name) {
        super(name);
    }

    public void setUp() {
        board = new Board(SIZE, SIZE);
    }

    public void testGetWidth() {
        assertEquals(SIZE, board.getWidth());
    }

    public void testGetHeight() {
        assertEquals(SIZE, board.getWidth());
    }
}
