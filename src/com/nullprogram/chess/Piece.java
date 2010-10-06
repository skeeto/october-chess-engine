package com.nullprogram.chess;

import java.util.ArrayList;
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
        WHITE, BLACK;
    }

    private Side side;
    private Position pos;
    private Board board;

    /**
     * When creating a piece, you must always choose a side.
     */
    protected Piece() {
    }

    /**
     * Create a new piece on the given side.
     *
     * @param side the side of the piece
     */
    protected Piece(Side side) {
        this.side = side;
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
     * @param pos new position
     */
    public void setPosition(Position pos) {
        this.pos = pos;
    }

    /**
     * Get the position of this piece on the board.
     *
     * @return the piece position
     */
    public Position getPosition() {
        return pos;
    }

    /**
     * Set the board for the current piece.
     *
     * This is used in determining moves.
     *
     * @param board the current board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Get the board set for the current piece.
     *
     * @return the piece's board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Set the side for this piece.
     *
     * @param side the new side
     */
    public void setSide(Side side) {
        this.side = side;
    }

    /**
     * Get the side for this piece.
     *
     * @return the piece's side
     */
    public Side getSide() {
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
    public BufferedImage getImage(int size) {
        String name = this.getClass().getSimpleName();
        return ImageServer.getTile(name + "-" + side, size);
    }
}
