package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Board;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

/**
 * The Chess pawn.
 *
 * This class describes the movement and capture behavior of the pawn
 * chess piece.
 */
public class Pawn extends Piece {

    /** Serialization identifier. */
    private static final long serialVersionUID = 456095470L;

    /**
     * Create a new pawn on the given side.
     *
     * @param side piece owner
     */
    public Pawn(final Side side) {
        super(side, "Pawn");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        Position pos = getPosition();
        Board board = getBoard();
        int dir = direction();
        Position dest = new Position(pos, 0, 1 * dir);
        Move first = new Move(pos, dest);
        addUpgrade(first);
        if (list.addMove(first) && !moved()) {
            list.addMove(new Move(pos, new Position(pos, 0, 2 * dir)));
        }
        Move captureLeft = new Move(pos, new Position(pos, -1, 1 * dir));
        addUpgrade(captureLeft);
        list.addCaptureOnly(captureLeft);
        Move captureRight = new Move(pos, new Position(pos,  1, 1 * dir));
        addUpgrade(captureRight);
        list.addCaptureOnly(captureRight);

        /* check for en passant */
        Move last = board.last();
        if (last != null) {
            Position left = new Position(pos, -1, 0);
            Position right = new Position(pos, 1, 0);
            Position lOrigin = last.getOrigin();
            Position lDest = last.getDest();
            if (left.equals(lDest) &&
                (lOrigin.getX() == lDest.getX()) &&
                (lOrigin.getY() == lDest.getY() + dir * 2) &&
                (board.getPiece(left) instanceof Pawn)) {

                /* en passant to the left */
                Move passant = new Move(pos, new Position(pos, -1, dir));
                passant.setNext(new Move(left, null));
                list.addMove(passant);
            } else if (right.equals(lDest) &&
                       (lOrigin.getX() == lDest.getX()) &&
                       (lOrigin.getY() == lDest.getY() + dir * 2) &&
                       (board.getPiece(right) instanceof Pawn)) {

                /* en passant to the right */
                Move passant = new Move(pos, new Position(pos, 1, dir));
                passant.setNext(new Move(right, null));
                list.addMove(passant);
            }
        }
        return list;
    }

    /**
     * Add the upgrade actions to the given move if needed.
     *
     * @param move the move to be modified
     */
    private void addUpgrade(final Move move) {
        if (move.getDest().getY() != upgradeRow()) {
            return;
        }
        move.setNext(new Move(move.getDest(), null)); // remove the pawn
        Move upgrade = new Move(null, move.getDest());
        upgrade.setReplacement("Queen");
        upgrade.setReplacementSide(getSide());
        move.getNext().setNext(upgrade);              // add a queen
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
