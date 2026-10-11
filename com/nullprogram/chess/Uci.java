package com.nullprogram.chess;

import com.nullprogram.chess.ai.Minimax;
import com.nullprogram.chess.boards.StandardBoard;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Minimal UCI front end for the Minimax AI, standard chess only.
 *
 * Positions are only accepted as "startpos" plus a move list, since
 * castling and en passant rights live in the board's move history.
 */
public final class Uci {

    /** Default search depth, matching the GUI's default AI. */
    private static final int DEFAULT_DEPTH = 4;

    /** Centipawns per unit of material. */
    private static final double CENTI = 100.0;

    /** Current board. */
    private Board board = new StandardBoard();

    /** Side to move on the current board. */
    private Piece.Side side = Piece.Side.WHITE;

    /** Configured search depth. */
    private int depth = DEFAULT_DEPTH;

    /** The AI, created lazily so its thread pool is reused. */
    private Minimax ai;

    /**
     * Hidden constructor.
     */
    private Uci() {
    }

    /**
     * Run the UCI loop on standard input.
     *
     * @param args unused
     * @throws IOException on input failure
     */
    public static void main(final String[] args) throws IOException {
        Logger.getLogger("com.nullprogram.chess").setLevel(Level.WARNING);
        BufferedReader in =
            new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        Uci uci = new Uci();
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            if (!uci.command(line.trim().split("\\s+"))) {
                break;
            }
        }
        System.exit(0);
    }

    /**
     * Handle a single command.
     *
     * @param tok the command's tokens
     * @return false when the program should exit
     */
    private boolean command(final String[] tok) {
        String cmd = tok[0];
        if ("uci".equals(cmd)) {
            say("id name October");
            say("id author Christopher Wellons");
            say("option name Depth type spin default " + DEFAULT_DEPTH
                + " min 1 max 8");
            say("uciok");
        } else if ("isready".equals(cmd)) {
            say("readyok");
        } else if ("setoption".equals(cmd)) {
            if (tok.length >= 5 && "Depth".equalsIgnoreCase(tok[2])) {
                depth = Integer.parseInt(tok[4]);
                ai = null;
            }
        } else if ("position".equals(cmd)) {
            position(tok);
        } else if ("go".equals(cmd)) {
            go();
        } else if ("moves".equals(cmd)) {
            say(legalMoves());
        } else if ("quit".equals(cmd)) {
            return false;
        }
        return true;
    }

    /**
     * Set up the board from a "position startpos moves ..." command.
     *
     * @param tok the command's tokens
     */
    private void position(final String[] tok) {
        board = new StandardBoard();
        side = Piece.Side.WHITE;
        if (tok.length < 2 || !"startpos".equals(tok[1])) {
            say("info string only startpos is supported");
            return;
        }
        for (int i = 3; i < tok.length; i++) {
            Move move = find(tok[i]);
            if (move == null) {
                say("info string illegal move " + tok[i]);
                return;
            }
            board.move(move);
            side = Piece.opposite(side);
        }
    }

    /**
     * Find the legal move matching a UCI move string.
     *
     * @param uci the move in UCI notation, e.g. "e7e8n"
     * @return the matching move, or null if not legal
     */
    private Move find(final String uci) {
        for (Move move : board.allMoves(side, true)) {
            String name = move.toString();
            if (uci.startsWith(name)) {
                if (uci.length() > name.length()) {
                    Move upgrade = promotion(move);
                    if (upgrade == null) {
                        return null;
                    }
                    upgrade.setReplacement(pieceName(uci.charAt(4)));
                }
                return move;
            }
        }
        return null;
    }

    /**
     * Run the AI and report its move.
     */
    private void go() {
        if (ai == null) {
            Properties props = new Properties(Minimax.getConfig("default"));
            props.setProperty("depth", Integer.toString(depth));
            ai = new Minimax(null, props);
        }
        Move best = ai.takeTurn(board, side);
        if (best == null) {
            say("bestmove 0000");
            return;
        }
        long cp = Math.round(best.getScore() * CENTI);
        cp = Math.max(-100000, Math.min(100000, cp));
        say("info depth " + depth + " score cp " + cp);
        say("bestmove " + uciName(best));
    }

    /**
     * List all legal moves, sorted, for debugging and validation.
     *
     * @return the space-separated moves
     */
    private String legalMoves() {
        List<String> names = new ArrayList<String>();
        for (Move move : board.allMoves(side, true)) {
            names.add(uciName(move));
        }
        Collections.sort(names);
        StringBuilder str = new StringBuilder();
        for (String name : names) {
            str.append(name).append(' ');
        }
        return str.toString().trim();
    }

    /**
     * Return the piece-creating part of a promotion move, if any.
     *
     * @param move the move to inspect
     * @return the chained move that creates a piece, or null
     */
    private static Move promotion(final Move move) {
        for (Move m = move.getNext(); m != null; m = m.getNext()) {
            if (m.getReplacement() != null) {
                return m;
            }
        }
        return null;
    }

    /**
     * Name a move in UCI notation.
     *
     * @param move the move to name
     * @return the UCI move string
     */
    private static String uciName(final Move move) {
        Move upgrade = promotion(move);
        if (upgrade == null) {
            return move.toString();
        }
        String r = upgrade.getReplacement();
        char c = "Knight".equals(r) ? 'n' : Character.toLowerCase(r.charAt(0));
        return move.toString() + c;
    }

    /**
     * Map a UCI promotion letter to a piece name.
     *
     * @param c the promotion letter
     * @return the piece name for PieceFactory
     */
    private static String pieceName(final char c) {
        switch (c) {
        case 'n':
            return "Knight";
        case 'b':
            return "Bishop";
        case 'r':
            return "Rook";
        default:
            return "Queen";
        }
    }

    /**
     * Print a line to the GUI.
     *
     * @param line the line to print
     */
    private static void say(final String line) {
        System.out.println(line);
        System.out.flush();
    }
}
