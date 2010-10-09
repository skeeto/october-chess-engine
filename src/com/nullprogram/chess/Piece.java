package com.nullprogram.chess;

import java.util.WeakHashMap;
import java.awt.image.BufferedImage;

import com.nullprogram.chess.pieces.ImageServer;

/**
 * An abstract Chess piece.
 */
public abstract class Piece {

    /**
     * The side of the piece: white or black.
     */
    public enum Side {
        /**
         * The lighter colored side of the board.
         */
        WHITE (1),
        /**
         * The darker colored side of the board.
         */
        BLACK (-1);

        private final int val;
        Side(int value) {
            this.val = value;
        }
        public int value() { return val; }
    }

    /**
     * The side this piece belongs to.
     */
    private Side side;

    /**
     * The position of this piece.
     */
    private Position pos;

    /**
     * The board this piece is on.
     */
    private Board board;

    /** Movement counter. */
    private int moved = 0;

    /**
     * Hash of check-checked moves.
     */
    private WeakHashMap<Integer, MoveList> checkCache;

    /**
     * Hash of check-unchecked moves.
     */
    private WeakHashMap<Integer, MoveList> noCheckCache;

    /**
     * When creating a piece, you must always choose a side.
     */
    protected Piece() {
    }

    /**
     * Create a new piece on the given side.
     *
     * @param owner the side of the piece
     */
    protected Piece(final Side owner) {
        side = owner;
        checkCache = new WeakHashMap<Integer, MoveList>();
        noCheckCache = new WeakHashMap<Integer, MoveList>();
    }

    /**
     * Get the (possibly cached) moves for this piece.
     *
     * @param checkCheck check for check
     * @return           list of moves
     */
    public MoveList getMoves(boolean checkCheck) {
        if (false)
            return genMoves(checkCheck);
        WeakHashMap<Integer, MoveList> cache;
        if (checkCheck) {
            cache = checkCache;
        } else {
            cache = noCheckCache;
        }
        MoveList moves = cache.get(board.getID());
        if (moves != null) {
            //System.out.println("cached " + this.getClass().getSimpleName());
            return moves;
        } else {
            moves = genMoves(checkCheck);
            cache.put(board.getID(), moves);
            return moves;
        }
    }

    /**
     * Generate the moves for this piece.
     *
     * @param checkCheck check for check
     * @return           list of moves
     */
    protected abstract MoveList genMoves(boolean checkCheck);

    /**
     * Update the piece's current position on the board.
     *
     * @param position new position
     */
    public final void setPosition(final Position position) {
        pos = position;
    }

    /**
     * Get the position of this piece on the board.
     *
     * @return the piece position
     */
    public final Position getPosition() {
        return pos;
    }

    /**
     * Set the board for the current piece.
     *
     * This is used in determining moves.
     *
     * @param surface the current board
     */
    public final void setBoard(final Board surface) {
        board = surface;
    }

    /**
     * Get the board set for the current piece.
     *
     * @return the piece's board
     */
    public final Board getBoard() {
        return board;
    }

    /**
     * Set the side for this piece.
     *
     * @param owner side the new side
     */
    public final void setSide(final Side owner) {
        side = owner;
    }

    /**
     * Get the side for this piece.
     *
     * @return the piece's side
     */
    public final Side getSide() {
        return side;
    }

    /**
     * Get the image that represents this piece.
     *
     * This method currently uses reflection.
     *
     * @param size the square size of the image to return
     * @return     image for this piece
     */
    public final BufferedImage getImage(final int size) {
        String name = this.getClass().getSimpleName();
        return ImageServer.getTile(name + "-" + side, size);
    }

    /**
     * Return true if piece has moved.
     *
     * @return true if piece has moved
     */
    public final Boolean moved() {
        return moved != 0;
    }

    /**
     * Increase piece movement counter.
     */
    public final void incMoved() {
        moved++;
    }

    /**
     * Decrease piece movement counter.
     */
    public final void decMoved() {
        moved--;
    }

    /**
     * Return the opposing side.
     *
     * @param s the side to be opposed
     * @return  the opposing side
     */
    public static Side opposite(final Side s) {
        if (s == Side.BLACK) {
            return Side.WHITE;
        } else {
            return Side.BLACK;
        }
    }

    public int hashCode() {
        return getPosition().hashCode()
            ^ moved
            ^ getSide().value()
            ^ this.getClass().hashCode();
    }
}
