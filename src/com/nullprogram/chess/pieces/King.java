package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;

/**
 * The Chess king.
 *
 * This class describes the movement and capture behavior of the Chess
 * king.
 */
public class King extends Piece {

    /** Serialization identifier. */
    private static final long serialVersionUID = 450219131L;

    /** List of enemy moves (cahced). */
    private MoveList enemy;

    /** Cache the check check. */
    private Boolean inCheck;

    /**
     * Create a new king on the given side.
     *
     * @param side piece owner
     */
    public King(final Side side) {
        super(side, "King");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        Position pos = getPosition();
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (x != 0 || y != 0) {
                    list.addCapture(new Move(pos, new Position(pos, x, y)));
                }
            }
        }

        /* check for castling */
        enemy = null;
        inCheck = null;
        if (check && !moved()) {
            Move left = castle(-1);
            if (left != null) {
                list.add(left);
            }
            Move right = castle(1);
            if (right != null) {
                list.add(right);
            }
        }
        return list;
    }

    /**
     * Try to create a castle move in the given direction.
     *
     * @param dir direction to check
     * @return the move, or null
     */
    private Move castle(final int dir) {
        int dist = getBoard().getWidth() / 2 - 2;
        Position pos = getPosition();

        int max;
        if (dir < 0) {
            max = 0;
        } else {
            max = getBoard().getWidth() - 1;
        }

        Position rookPos = new Position(max, pos.getY());
        Piece rook = getBoard().getPiece(rookPos);
        if (rook == null || rook.moved()) {
            return null;
        }

        if (emptyRow(getPosition(), dir, max) && !inCheck()) {
            /* generate the move */
            Position kpos = new Position(pos, dir * dist, 0);
            Move kingDest = new Move(pos, kpos);
            Position rpos = new Position(pos, dir * dist - dir, 0);
            Move rookDest = new Move(rookPos, rpos);
            kingDest.setNext(rookDest);
            return kingDest;
        }
        return null;
    }

    /**
     * Check for an empty, unthreatened castling row.
     *
     * @param start the starting position
     * @param dir direction to check
     * @param max maximum column for the board
     * @return true if row is safe
     */
    private boolean emptyRow(final Position start, final int dir,
                             final int max) {
        for (int i = start.getX() + dir; i != max; i += dir) {
            Position pos = new Position(i, start.getY());
            if (getBoard().getPiece(pos) != null ||
                enemyMoves().containsDest(pos)) {

                return false;
            }
        }
        return true;
    }

    /**
     * Cache of enemy moves.
     *
     * @return list of enemy moves
     */
    private MoveList enemyMoves() {
        if (enemy != null) {
            return enemy;
        }
        enemy = getBoard().allMoves(opposite(getSide()), false);
        return enemy;
    }

    /**
     * Cache of check check.
     *
     * @return true if king is in check
     */
    private boolean inCheck() {
        if (inCheck != null) {
            return inCheck;
        }
        inCheck = getBoard().check(getSide());
        return inCheck;
    }
}
