package com.nullprogram.chess.boards;

import com.nullprogram.chess.Piece;
import com.nullprogram.chess.pieces.Archbishop;
import com.nullprogram.chess.pieces.Bishop;
import com.nullprogram.chess.pieces.Chancellor;
import com.nullprogram.chess.pieces.King;
import com.nullprogram.chess.pieces.Knight;
import com.nullprogram.chess.pieces.Pawn;
import com.nullprogram.chess.pieces.Queen;
import com.nullprogram.chess.pieces.Rook;

/**
 * Board for the game of Gothic Chess.
 */
public class Gothic extends StandardBoard {

    /** Serialization identifier. */
    private static final long serialVersionUID = 277220873L;

    /** The standard board width. */
    static final int WIDTH = 10;

    /** The standard board height. */
    static final int HEIGHT = 8;

    /** Row of the white pawns. */
    static final int WHITE_PAWN_ROW = 1;

    /** Row of the black pawns. */
    static final int BLACK_PAWN_ROW = 6;

    /** White home row. */
    static final int WHITE_ROW = 0;

    /** Black home row. */
    static final int BLACK_ROW = 7;

    /** Queen side rook column. */
    static final int Q_ROOK = 0;

    /** Queen side knight column. */
    static final int Q_KNIGHT = 1;

    /** Queen side bishop column. */
    static final int Q_BISHOP = 2;

    /** Queen column. */
    static final int QUEEN = 3;

    /** Chancellor column. */
    static final int CHANCELLOR = 4;

    /** King column. */
    static final int KING = 5;

    /** Archbishop column. */
    static final int ARCHBISHOP = 6;

    /** King side bishop column. */
    static final int K_BISHOP = 7;

    /** King side knight column. */
    static final int K_KNIGHT = 8;

    /** King side rook column. */
    static final int K_ROOK = 9;

    /**
     * The Gothic Chess board.
     */
    public Gothic() {
        setWidth(WIDTH);
        setHeight(HEIGHT);
        clear();
        for (int x = 0; x < WIDTH; x++) {
            setPiece(x, WHITE_PAWN_ROW, new Pawn(Piece.Side.WHITE));
            setPiece(x, BLACK_PAWN_ROW, new Pawn(Piece.Side.BLACK));
        }
        setPiece(Q_ROOK, WHITE_ROW, new Rook(Piece.Side.WHITE));
        setPiece(K_ROOK, WHITE_ROW, new Rook(Piece.Side.WHITE));
        setPiece(Q_ROOK, BLACK_ROW, new Rook(Piece.Side.BLACK));
        setPiece(K_ROOK, BLACK_ROW, new Rook(Piece.Side.BLACK));
        setPiece(Q_KNIGHT, WHITE_ROW, new Knight(Piece.Side.WHITE));
        setPiece(K_KNIGHT, WHITE_ROW, new Knight(Piece.Side.WHITE));
        setPiece(Q_KNIGHT, BLACK_ROW, new Knight(Piece.Side.BLACK));
        setPiece(K_KNIGHT, BLACK_ROW, new Knight(Piece.Side.BLACK));
        setPiece(Q_BISHOP, WHITE_ROW, new Bishop(Piece.Side.WHITE));
        setPiece(K_BISHOP, WHITE_ROW, new Bishop(Piece.Side.WHITE));
        setPiece(Q_BISHOP, BLACK_ROW, new Bishop(Piece.Side.BLACK));
        setPiece(K_BISHOP, BLACK_ROW, new Bishop(Piece.Side.BLACK));
        setPiece(QUEEN, WHITE_ROW, new Queen(Piece.Side.WHITE));
        setPiece(QUEEN, BLACK_ROW, new Queen(Piece.Side.BLACK));
        setPiece(KING, WHITE_ROW, new King(Piece.Side.WHITE));
        setPiece(KING, BLACK_ROW, new King(Piece.Side.BLACK));

        setPiece(CHANCELLOR, WHITE_ROW, new Chancellor(Piece.Side.WHITE));
        setPiece(CHANCELLOR, BLACK_ROW, new Chancellor(Piece.Side.BLACK));
        setPiece(ARCHBISHOP, WHITE_ROW, new Archbishop(Piece.Side.WHITE));
        setPiece(ARCHBISHOP, BLACK_ROW, new Archbishop(Piece.Side.BLACK));
    }
}
