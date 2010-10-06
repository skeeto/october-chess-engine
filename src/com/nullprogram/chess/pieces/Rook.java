package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.PositionList;

/**
 * The Chess rook.
 *
 * This class describes the movement and capture behavior of the Chess
 * rook.
 */
public class Rook extends Piece {

    public Rook(Side side) {
        super(side);
    }

    public PositionList getMoves() {
        PositionList list = new PositionList(getBoard());
        list = getMoves(this, list);
        return list;
    }

    /**
     * Determine rook moves for given situation.
     *
     * This method is here for the purposes of reuse.
     *
     * @param p     the piece being tested
     * @param list  list to be appended to
     * @return      the modified list
     */
    public static PositionList getMoves(Piece p, PositionList list) {
        // Scan each direction and stop looking when we run into something.
        int x = p.getPosition().x;
        int y = p.getPosition().y;
        while (x >= 0) {
            x--;
            Position pos = new Position(x, y);
            if (!list.addMove(pos, p.getSide())) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = p.getPosition().x;
        y = p.getPosition().y;
        while (x < p.getBoard().getWidth()) {
            x++;
            Position pos = new Position(x, y);
            if (!list.addMove(pos, p.getSide())) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = p.getPosition().x;
        y = p.getPosition().y;
        while (y >= 0) {
            y--;
            Position pos = new Position(x, y);
            if (!list.addMove(pos, p.getSide())) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = p.getPosition().x;
        y = p.getPosition().y;
        while (y < p.getBoard().getHeight()) {
            y++;
            Position pos = new Position(x, y);
            if (!list.addMove(pos, p.getSide())) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        return list;
    }
}
