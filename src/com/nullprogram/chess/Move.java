package com.nullprogram.chess;

/**
 * Represents a single moves on a chess board.
 *
 * This is actually a linked list able to describe several movement
 * actions at once that make up a single turn (castling, for example).
 */
public class Move {

    /** Originating position. */
    private Position origin;

    /** Destination position. */
    private Position destination;

    /** Linked list entry for next part of this move. */
    private Move next;

    /**
     * Create a new move to move a piece from one position to another.
     *
     * @param orig origin position
     * @param dest destination position
     */
    public Move(final Position orig, final Position dest) {
        destination = dest;
        origin = orig;
        next = null;
    }

    /**
     * Set the next movement action for this move.
     *
     * @param move the next move
     */
    public final void setNext(final Move move) {
        next = move;
    }

    /**
     * Get the next movement action for this move.
     *
     * @return the next move
     */
    public final Move getNext() {
        return next;
    }

    /**
     * Get the origin position.
     *
     * @return origin position
     */
    public final Position getOrigin() {
        return origin;
    }

    /**
     * Get the destination position.
     *
     * @return destination position
     */
    public final Position getDest() {
        return destination;
    }
}
