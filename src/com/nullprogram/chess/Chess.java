package com.nullprogram.chess;

import com.nullprogram.chess.gui.ChessFrame;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import javax.swing.UIManager;

/**
 * Main class for the Chess game application.
 */
public final class Chess {

    /** This class's Logger. */
    private static final Logger LOG =
        Logger.getLogger("com.nullprogram.chess.Chess");

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
            in.close();
        } catch (java.io.IOException e) {
            LOG.warning("failed to read version info");
            version = "";
        }
        return TITLE_PREFIX + " " + version;
    }
}
