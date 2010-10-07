package com.nullprogram.chess;

public class Game {

    private Board board;
    private Player white;
    private Player black;
    private Piece.Side turn;
    private Boolean done;

    public Game(Board board, Player white, Player black) {
        done = false;
        this.board = board;
        this.white = white;
        this.black = black;
        white.setGame(this);
        black.setGame(this);
        turn = Piece.Side.WHITE;
        white.setActive(turn);
    }

    /**
     * Perform a turn of the game.
     *
     * This should not be called by the same thread that called
     * setActive() in the player.
     *
     * @param orig originating position
     * @param dest destination position
     */
    public void move(Position orig, Position dest) {
        board.move(orig, dest);
        if (board.checkmate() || board.stalemate()) {
            done = true;
            return;
        }

        if (turn == Piece.Side.WHITE) {
            turn = Piece.Side.BLACK;
            black.setActive(turn);
        } else {
            turn = Piece.Side.WHITE;
            white.setActive(turn);
        }
    }
}
