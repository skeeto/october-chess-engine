package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Board;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;

/**
 * The Chess pawn.
 *
 * This class describes the movement and capture behavior of the pawn
 * chess piece.
 */
public class Pawn extends Piece {

    /**
     * Create a new pawn on the given side.
     *
     * @param side piece owner
     */
    public Pawn(final Side side) {
        super(side);
    }

    /** {@inheritDoc} */
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        Position pos = getPosition();
        Board board = getBoard();
        int dir = direction();
        Position dest = new Position(pos, 0, 1 * dir);
        Move first = new Move(pos, dest);
        if (dest.getY() == upgradeRow()) {
            first.setNext(new Move(dest, null)); // remove the pawn
            Move upgrade = new Move(null, dest);
            upgrade.setCaptured(new Queen(getSide()));
            first.getNext().setNext(upgrade);     // add a queen
        }
        if (list.addMove(first)) {
            if (!moved()) {
                list.addMove(new Move(pos, new Position(pos, 0, 2 * dir)));
            }
        }
        list.addCaptureOnly(new Move(pos, new Position(pos, -1, 1 * dir)));
        list.addCaptureOnly(new Move(pos, new Position(pos,  1, 1 * dir)));

        /* check for en passant */
        Move last = board.last();
        if (last != null) {
            Position left = new Position(pos, -1, 0);
            Position right = new Position(pos, 1, 0);
            if (left.equals(last.getDest())
                    && (board.getPiece(left) instanceof Pawn)) {
                /* en passant to the left */
                Move passant = new Move(pos, new Position(pos, -1, dir));
                passant.setNext(new Move(left, null));
                list.addMove(passant);
            } else if (right.equals(last.getDest())
                       && (board.getPiece(right) instanceof Pawn)) {
                /* en passant to the right */
                Move passant = new Move(pos, new Position(pos, 1, dir));
                passant.setNext(new Move(right, null));
                list.addMove(passant);
            }
        }
        return list;
    }

    /**
     * Determine direction of this pawn's movement.
     *
     * @return direction for this pawn
     */
    private int direction() {
        if (getSide() == Side.WHITE) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Determine upgrade row.
     *
     * @return the upgrade row index.
     */
    private int upgradeRow() {
        if (getSide() == Side.BLACK) {
            return 0;
        } else {
            return getBoard().getHeight() - 1;
        }
    }
}
