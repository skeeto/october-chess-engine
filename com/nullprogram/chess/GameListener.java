package com.nullprogram.chess;

/**
 * An object (like the display) that needs to be informed of game
 * events.
 */
public interface GameListener {

    /**
     * Called when a game event has occured.
     *
     * @param event  object describing the event
     */
    void gameEvent(GameEvent event);
}
