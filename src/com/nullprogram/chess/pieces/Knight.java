package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;

/**
 * The Chess knight.
 *
 * This class describes the movement and capture behavior of the Chess
 * knight.
 */
public class Knight extends Piece {

    /**
     * Short segment of movement.
     */
    static final int NEAR = 1;

    /**
     * Long segment of movement.
     */
    static final int FAR = 2;

    /**
     * Create a new knight on the given side.
     *
     * @param side piece owner
     */
    public Knight(final Side side) {
        super(side);
    }

    /** {@inheritDoc} */
    public final MoveList getMoves() {
        MoveList list = new MoveList(getBoard());
        Position pos = getPosition();
        list.addCapture(new Move(pos, new Position(pos,  NEAR,  FAR)));
        list.addCapture(new Move(pos, new Position(pos,  FAR,  NEAR)));
        list.addCapture(new Move(pos, new Position(pos, -FAR,  NEAR)));
        list.addCapture(new Move(pos, new Position(pos, -FAR, -NEAR)));
        list.addCapture(new Move(pos, new Position(pos,  FAR, -NEAR)));
        list.addCapture(new Move(pos, new Position(pos,  NEAR, -FAR)));
        list.addCapture(new Move(pos, new Position(pos, -NEAR, -FAR)));
        list.addCapture(new Move(pos, new Position(pos, -NEAR,  FAR)));
        return list;
    }
}
