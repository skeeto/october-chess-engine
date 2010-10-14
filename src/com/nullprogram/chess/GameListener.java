package com.nullprogram.chess;

/**
 * An object (like the display) that needs to be informed of game
 * events.
 */
public interface GameListener {

    /**
     * Called when a game event has occured.
     *
     * @param game game where event occurred
     */
    void gameEvent(Game game);
}
