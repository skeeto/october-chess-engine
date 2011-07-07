package com.nullprogram.chess;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.nullprogram.chess.ai.Helper;

/**
 * Another main entry to assist a chess engine running on another
 * server by performing board evaluations for it.
 */
public class Assist {
    /** The default assistance port.*/
    public static final int PORT = 2000;

    /**
     * Hidden constructor.
     */
    protected Assist() {
    }

    /**
     * The main method to set up assistance.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        Chess.init();

        Executor executor = Executors.newCachedThreadPool();
        String address = args[0];
        Helper[] helpers = new Helper[Helper.NTHREADS];
        for (int i = 0; i < Helper.NTHREADS; i++) {
            helpers[i] = new Helper(address, PORT);
            executor.execute(helpers[i]);
        }
    }
}
