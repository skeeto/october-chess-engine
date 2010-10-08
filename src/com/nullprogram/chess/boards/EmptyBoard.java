package com.nullprogram.chess.boards;

import com.nullprogram.chess.Board;
import com.nullprogram.chess.Piece;

/**
 * An empty Chess board.
 */
public class EmptyBoard extends Board {

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

    /** {@inheritDoc} */
    public final Boolean checkmate() {
        return false;
    }

    /** {@inheritDoc} */
    public final Boolean stalemate() {
        return false;
    }

    /** {@inheritDoc} */
    public final Boolean check(final Piece.Side side) {
        return false;
    }
}
