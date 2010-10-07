package com.nullprogram.chess;

/**
 * A Player is a class that can take a turn.
 *
 * The game driver will call setActive() on a player when it is the
 * player's turn. The player performs a move by calling move() on the
 * game engine, which will inform the next player with setActive().
 *
 * Players should execute their moves from a different thread than the
 * thread that called setActive(). For example, in an AI player's
 * setActive() it would fire off a new thread to begin calculating the
 * move and <i>that</i> thread calls move() on the game.
 */
public interface Player {

    /**
     * Inform the player it is time to take its turn.
     *
     * @param side the player's side
     */
    void setActive(Piece.Side side);

    /**
     * Set the game the player belongs to.
     *
     * @param game the player's game
     */
    void setGame(Game game);
}
