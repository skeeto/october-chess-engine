package com.nullprogram.chess;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.swing.UIManager;

import com.nullprogram.chess.gui.ChessFrame;

/**
 * Main class for the Chess game application.
 */
public final class Chess {
    /** This class's Logger. */
    private static final Logger LOG = Logger.getLogger("Chess");

    /** The program's running title, prefix only. */
    private static final String TITLE_PREFIX = "October Chess";

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

        try {
            String lnf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lnf);
        } catch (Exception e) {
            LOG.warning("Failed to set 'Look and Feel'");
        }
        ChessFrame frame = new ChessFrame();
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
