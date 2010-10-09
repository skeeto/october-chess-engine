package com.nullprogram.chess.gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import com.nullprogram.chess.Board;
import com.nullprogram.chess.Player;

/**
 * The JFrame that contains all GUI elements.
 */
public class ChessFrame extends JFrame {

    /** Version for object serialization. */
    private static final long serialVersionUID = 1L;

    /** The board display. */
    private BoardPanel display;

    /** The progress bar on the display. */
    private JProgressBar progress;

    /**
     * Create a new ChessFrame for the given board.
     *
     * @param board the game board to display
     */
    public ChessFrame(final Board board) {
        super("Chess");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        display = new BoardPanel(board);
        progress = new JProgressBar();
        progress.setStringPainted(true);
        setStatus("Initializing.");
        add(display);
        add(progress);
        pack();

        setVisible(true);
    }

    /**
     * Return the GUI (human) play handler.
     *
     * @return the player
     */
    public final Player getPlayer() {
        return display;
    }

    /**
     * Return the progress bar in the display.
     *
     * @return the progress bar
     */
    public final JProgressBar getProgress() {
        return progress;
    }

    /**
     * Set the display's status string to the human.
     *
     * @param status string to display
     */
    public final void setStatus(final String status) {
        progress.setString(status);
    }
}
