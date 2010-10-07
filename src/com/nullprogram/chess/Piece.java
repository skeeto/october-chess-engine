package com.nullprogram.chess;

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
        WHITE,
        /**
         * The darker colored side of the board.
         */
        BLACK;
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
    }

    /**
     * Get the moves for this piece.
     *
     * @return list of moves
     */
    public abstract PositionList getMoves();

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
}
