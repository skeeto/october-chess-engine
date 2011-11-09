package com.nullprogram.chess;

import com.nullprogram.chess.boards.EmptyBoard;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BoardTest {

    private static final int SIZE = 8;
    private static Board board = new EmptyBoard(SIZE, SIZE);

    @Test
    public void testGetWidth() {
        assertEquals(SIZE, board.getWidth());
    }

    @Test
    public void testGetHeight() {
        assertEquals(SIZE, board.getWidth());
    }
}
