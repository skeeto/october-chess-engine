package com.nullprogram.chess;

/**
 * An object (like the display) that needs to be informed of board
 * changes.
 */
public interface BoardListener {

    /**
     * Called when the board has changed.
     *
     * @param board  the board that changed
     */
    void boardChange(Board board);
}
