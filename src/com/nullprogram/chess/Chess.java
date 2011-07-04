package com.nullprogram.chess;

import java.util.logging.Logger;

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
        try {
            String lnf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lnf);
        } catch (Exception e) {
            LOG.warning("Failed to set 'Look and Feel'");
        }
        ChessFrame frame = new ChessFrame();
    }
}
