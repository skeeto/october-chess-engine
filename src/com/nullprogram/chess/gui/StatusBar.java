package com.nullprogram.chess.gui;

import com.nullprogram.chess.Game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;

/**
 * Progress bar and status bar combined as one.
 */
public class StatusBar extends JComponent {

    /** Version for object serialization. */
    private static final long serialVersionUID = 1L;

    /** Milliseconds to wait between display updates. */
    private static final long REPAINT_DELAY = 1000L;

    /** Seconds in a minute. */
    private static final int MIN = 60;

    /** The current game being observed. */
    private Game game;

    /** Panel's background color. */
    static final Color BACKGROUND = new Color(0xAA, 0xAA, 0xAA);

    /** Color of the progress bar. */
    static final Color BAR_COLOR = new Color(0x40, 0x00, 0xFF);

    /** Color of the status text. */
    static final Color STATUS_COLOR = new Color(0x00, 0x00, 0x00);

    /** Height of the progress bar. */
    static final int BAR_HEIGHT = 3;

    /** Vertical padding around the progress bar. */
    static final int BAR_PADDING_Y = 3;

    /** Horizontal padding around the progress bar. */
    static final int BAR_PADDING_X = 10;

    /** Display update timer. */
    private final Timer timer = new Timer(true);

    /**
     * Create a new status bar.
     *
     * @param observed  the game to be observed by this component
     */
    public StatusBar(final Game observed) {
        super();
        setBackground(BACKGROUND);
        setPreferredSize(null);
        setMinimumSize(null);
        setMaximumSize(null);
        game = observed;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0L, REPAINT_DELAY);
    }

    /**
     * Set the game to be observed by this component.
     *
     * @param observed  game to be observed
     */
    public final void setGame(final Game observed) {
        game = observed;
    }

    @Override
    public final Dimension getPreferredSize() {
        Graphics g = getGraphics();
        if (g != null) {
            FontMetrics fm = g.getFontMetrics();
            int bar = BAR_PADDING_Y * 2 + BAR_HEIGHT;
            return new Dimension(0, fm.getAscent() + bar);
        }
        return null;
    }

    @Override
    public final Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public final Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE,
                             (int) getPreferredSize().getHeight());
    }

    @Override
    public final void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (game == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fm = g.getFontMetrics();
        int fh = fm.getAscent();

        /* Draw the progress bar. */
        g.setColor(BAR_COLOR);
        int barWidth = getWidth() - BAR_PADDING_X * 2;
        int progress = (int) (game.getProgress() * barWidth);
        g.fillRect(BAR_PADDING_X, fh + BAR_PADDING_Y,
                   progress, BAR_HEIGHT);

        /* Create ETA string. */
        double secs = game.getETA();
        String eta = " ";
        if (secs < Double.POSITIVE_INFINITY) {
            int min = (int) (secs / MIN);
            int sec = (int) (secs - min * MIN);
            eta = String.format(" (est %d:%02d)", min, sec);
        }

        /* Draw the string. */
        String status = game.getStatus() + eta;
        g.setColor(STATUS_COLOR);
        int width = fm.stringWidth(status);
        int height = fm.getHeight();
        Font f = fm.getFont();
        g.setFont(f.deriveFont(Font.BOLD));
        g.drawString(status, getWidth() / 2 - width / 2, height);
    }
}
