package com.nullprogram.chess;

import java.io.Serializable;

/**
 * Represents a single moves on a chess board.
 *
 * This is actually a linked list able to describe several movement
 * actions at once that make up a single turn (castling, for example).
 *
 * If destination position is null, it means to remove the piece in
 * the origin position.
 */
public final class Move implements Serializable {

    /** Versioning for object serialization. */
    private static final long serialVersionUID = 276216209410699761L;

    /** Originating position. */
    private final Position origin;

    /** Destination position. */
    private final Position destination;

    /** Linked list entry for next part of this move. */
    private Move next;

    /** Piece that was capture: used for undoing a move.  */
    private Piece captured;

    /** New piece to make. */
    private String replacement;

    /** Side of the new piece to make. */
    private Piece.Side replacementSide;

    /** Score for this move. */
    private double score;

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
     * Create a Move from an existing Move.
     *
     * @param move move to copy
     */
    public Move(final Move move) {
        this(move.getOrigin(), move.getDest());
        captured = move.getCaptured();
        replacement = move.getReplacement();
        replacementSide = move.getReplacementSide();
        if (move.getNext() != null) {
            next = new Move(move.getNext());
        }
    }

    /**
     * Set the next movement action for this move.
     *
     * @param move the next move
     */
    public void setNext(final Move move) {
        next = move;
    }

    /**
     * Get the next movement action for this move.
     *
     * @return the next move
     */
    public Move getNext() {
        return next;
    }

    /**
     * Get the origin position.
     *
     * @return origin position
     */
    public Position getOrigin() {
        return origin;
    }

    /**
     * Get the destination position.
     *
     * @return destination position
     */
    public Position getDest() {
        return destination;
    }

    /**
     * Set the piece that was captured by this move.
     *
     * @param p piece that was captured
     */
    public void setCaptured(final Piece p) {
        captured = p;
    }

    /**
     * Set the piece that was captured by this move.
     *
     * @return piece that was captured
     */
    public Piece getCaptured() {
        return captured;
    }

    /**
     * Set the piece that will be created.
     *
     * @param pieceName piece to be created
     */
    public void setReplacement(final String pieceName) {
        replacement = pieceName;
    }

    /**
     * Get the name of the piece that will be created.
     *
     * @return piece to be created
     */
    public String getReplacement() {
        return replacement;
    }

    /**
     * Set the side of piece that will be created.
     *
     * @param side piece to be created
     */
    public void setReplacementSide(final Piece.Side side) {
        replacementSide = side;
    }

    /**
     * Get the side of the piece that will be created.
     *
     * @return side of piece to be created
     */
    public Piece.Side getReplacementSide() {
        return replacementSide;
    }

    @Override
    public String toString() {
        return "" + origin + destination;
    }

    /**
     * Return this move's set score (AI purposes).
     *
     * @return this move's score
     */
    public double getScore() {
        return score;
    }

    /**
     * Set this move's score (AI purposes).
     *
     * @param newscore  this move's score
     */
    public void setScore(final double newscore) {
        this.score = newscore;
    }
}
