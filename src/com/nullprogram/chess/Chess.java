package com.nullprogram.chess;

import java.net.ServerSocket;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.swing.UIManager;

import com.nullprogram.chess.ai.Minimax;
import com.nullprogram.chess.ai.HelperSocket;
import com.nullprogram.chess.gui.ChessFrame;

/**
 * Main class for the Chess game application.
 */
public final class Chess {
    /** This class's Logger. */
    private static final Logger LOG = Logger.getLogger("Chess");

    /** The program's running title, prefix only. */
    private static final String TITLE_PREFIX = "October Chess";

    /** Option to listen for distributed helpers. */
    private static boolean listen = true;

    /**
     * Hidden constructor.
     */
    protected Chess() {
    }

    /**
     * The main method of the Chess game application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        init();
        try {
            String lnf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lnf);
        } catch (Exception e) {
            LOG.warning("Failed to set 'Look and Feel'");
        }
        ChessFrame frame = new ChessFrame();

        if (listen) {
            new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ServerSocket s = new ServerSocket(Assist.PORT);
                            while (true) {
                                HelperSocket h
                                    = new HelperSocket(s.accept());
                                Minimax.addHelper(h);
                                LOG.info("added new helper");
                            }
                        } catch (Exception e) {
                            LOG.warning("no longer listening for helpers");
                        }
                    }
                }).start();
        }
    }

    /**
     * Perform some general initialization (logging).
     */
    public static void init() {
        /* Set up logger. */
        Logger root = Logger.getLogger("");
        Level level = Level.WARNING;
        String plevel = System.getProperty(".level");
        if (plevel != null) {
            level = Level.parse(plevel);
        }
        root.setLevel(level);
        for (Handler h : root.getHandlers()) {
            h.setFormatter(new java.util.logging.SimpleFormatter() {
                @Override
                public String format(final LogRecord r) {
                    return r.getLevel() + ": " + r.getMessage() + "\n";
                }
            });
            h.setLevel(level);
        }
    }

    /**
     * Returns the full title for the program, including version number.
     *
     * @return the title of the program
     */
    public static String getTitle() {
        String version = "";
        try {
            InputStream s = Chess.class.getResourceAsStream("/version.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(s));
            version = in.readLine();
        } catch (java.io.IOException e) {
            LOG.warning("failed to read version info");
            version = "";
        }
        return TITLE_PREFIX + " " + version;
    }
}
