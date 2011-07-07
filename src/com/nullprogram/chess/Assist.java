package com.nullprogram.chess;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.nullprogram.chess.ai.Helper;

public class Assist {
    public static final int ASSIST_PORT = 2000;

    public static void main(String[] args) {
        Chess.init();

        Executor executor = Executors.newCachedThreadPool();
        String address = args[0];
        Helper[] helpers = new Helper[Helper.NTHREADS];
        for (int i = 0; i < Helper.NTHREADS; i++) {
            helpers[i] = new Helper(address, 2000);
            executor.execute(helpers[i]);
        }
    }
}
