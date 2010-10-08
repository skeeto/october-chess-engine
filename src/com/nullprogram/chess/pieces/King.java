package com.nullprogram.chess.pieces;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Board;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;

/**
 * The Chess king.
 *
 * This class describes the movement and capture behavior of the Chess
 * king.
 *
 * TODO: check
 */
public class King extends Piece {

    /** Queen side knight column. */
    static final int Q_KNIGHT = 1;

    /** Queen side bishop column. */
    static final int Q_BISHOP = 2;

    /** Queen column. */
    static final int QUEEN = 3;

    /** King side bishop column. */
    static final int K_BISHOP = 5;

    /** King side knight column. */
    static final int K_KNIGHT = 6;

    /**
     * Create a new king on the given side.
     *
     * @param side piece owner
     */
    public King(final Side side) {
        super(side);
    }

    /** {@inheritDoc} */
    public final MoveList getMoves() {
        MoveList list = new MoveList(getBoard());
        Position pos = getPosition();
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (x != 0 || y != 0) {
                    list.addCapture(new Move(pos, new Position(pos, x, y)));
                }
            }
        }

        /* check for castling */
        if (!moved()) {
            Board board = getBoard();
            int home;
            if (getSide() == Side.WHITE) {
                home = 0;
            } else {
                home = board.getHeight() - 1;
            }
            Position qRookPos = new Position(0, home);
            Piece qRook = board.getPiece(qRookPos);
            if (qRook != null
                    && board.getPiece(qRookPos) instanceof Rook
                    && !board.getPiece(qRookPos).moved()
                    && (board.getPiece(new Position(Q_KNIGHT, home)) == null)
                    && (board.getPiece(new Position(Q_BISHOP, home)) == null)
                    && (board.getPiece(new Position(QUEEN, home)) == null)) {
                /* castle queen-side */
                Move king = new Move(pos, new Position(pos, -1 * 2, 0));
                king.setNext(new Move(qRookPos, new Position(pos, -1, 0)));
                list.add(king);
            }
            Position kRookPos = new Position(board.getWidth() - 1, home);
            Piece kRook = board.getPiece(kRookPos);
            if (kRook != null
                    && board.getPiece(kRookPos) instanceof Rook
                    && !board.getPiece(kRookPos).moved()
                    && (board.getPiece(new Position(K_KNIGHT, home)) == null)
                    && (board.getPiece(new Position(K_BISHOP, home)) == null)) {
                /* castle king-side */
                Move king = new Move(pos, new Position(pos, 2, 0));
                king.setNext(new Move(kRookPos, new Position(pos, 1, 0)));
                list.add(king);
            }
        }
        return list;
    }
}
