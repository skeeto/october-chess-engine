package com.nullprogram.chess.boards;

/**
 * An empty Chess board.
 */
public class EmptyBoard extends StandardBoard {

    /**
     * Default board width.
     */
    static final int DEFAULT_WIDTH = 8;

    /**
     * Default board height.
     */
    static final int DEFAULT_HEIGHT = 8;

    /**
     * Create an empty board with the default dimensions.
     */
    public EmptyBoard() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Create an empty board with specific dimensions.
     *
     * @param width  the board width
     * @param height the board height
     */
    public EmptyBoard(final int width, final int height) {
        setWidth(width);
        setHeight(height);
        clear();
    }
}
