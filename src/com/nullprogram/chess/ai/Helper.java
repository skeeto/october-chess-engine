package com.nullprogram.chess.ai;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import java.util.logging.Logger;

import com.nullprogram.chess.Move;
import com.nullprogram.chess.Board;
import com.nullprogram.chess.Piece;

public class Helper extends Minimax implements Runnable {
    /** This class's Logger. */
    private static final Logger LOG = Logger.getLogger("ai.Helper");

    private final String address;
    private final int port;

    public Helper(String server, int serverPort) {
        super();
        address = server;
        port = serverPort;
    }

    public void run() {
        try {
            Socket socket = new Socket(address, port);
            InputStream socketin = socket.getInputStream();
            OutputStream socketout = socket.getOutputStream();
            ObjectInputStream in = new ObjectInputStream(socketin);
            ObjectOutputStream out = new ObjectOutputStream(socketout);
            while (socket.isConnected()) {
                Board board = (Board) in.readObject();
                Move move = (Move) in.readObject();
                Piece.Side side = (Piece.Side) in.readObject();
                setSide(side);
                board.move(move);
                LOG.info("performing a board evaluation");
                double v = search(board, getMaxDepth() - 1,
                                  Piece.opposite(side),
                                  Double.NEGATIVE_INFINITY,
                                  Double.POSITIVE_INFINITY);
                move.setScore(-v);
                out.writeObject(move);
                out.flush();
            }
        } catch (Exception e) {
            LOG.severe("helper failed: " + e);
        }
    }
}
