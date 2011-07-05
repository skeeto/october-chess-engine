package com.nullprogram.chess;

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
        for (Handler h : root.getHandlers()) {
            h.setFormatter(new java.util.logging.SimpleFormatter() {
                @Override
                public String format(final LogRecord r) {
                    return r.getLevel() + ": " + r.getMessage() + "\n";
                }
            });
        }
        root.setLevel(Level.WARNING);
        String plevel = System.getProperty(".level");
        if (plevel != null) {
            root.setLevel(Level.parse(plevel));
        }

        try {
            String lnf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lnf);
        } catch (Exception e) {
            LOG.warning("Failed to set 'Look and Feel'");
        }
        ChessFrame frame = new ChessFrame();
    }
}
