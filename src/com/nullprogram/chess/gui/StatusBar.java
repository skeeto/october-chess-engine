package com.nullprogram.chess.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.Color;
import java.awt.RenderingHints;

import javax.swing.JPanel;

/**
 * Progress bar and status bar combined as one.
 */
public class StatusBar extends JPanel {

    /** Version for object serialization. */
    private static final long serialVersionUID = 1L;

    /** Maximum value of progress bar. */
    private int maximum = 1;

    /** Current value of progress bar. */
    private int value = 0;

    /** Status string to be displayed. */
    private String status;

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

    /**
     * Create a new status bar.
     */
    public StatusBar() {
        super();
        setBackground(BACKGROUND);
        setPreferredSize(null);
        setMinimumSize(null);
        setMaximumSize(null);
    }

    /** {@inheritDoc} */
    public final Dimension getPreferredSize() {
        Graphics g = getGraphics();
        if (g != null) {
            FontMetrics fm = g.getFontMetrics();
            int bar = BAR_PADDING_Y * 2 + BAR_HEIGHT;
            return new Dimension(0, fm.getAscent() + bar);
        }
        return null;
    }

    /** {@inheritDoc} */
    public final Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /** {@inheritDoc} */
    @Override
    public final Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE,
                             (int) getPreferredSize().getHeight());
    }

    /**
     * Return the maximum value of the progress bar.
     *
     * @return maximum value
     */
    public final int getMaximum() {
        return maximum;
    }

    /**
     * Set the maximum value of the progress bar.
     *
     * @param val new maximum value
     */
    public final void setMaximum(final int val) {
        maximum = Math.max(val, 1);
    }

    /**
     * Get the current value of the progress bar.
     *
     * @return current progress bar value
     */
    public final int getValue() {
        return value;
    }

    /**
     * Set the value of the progress bar.
     *
     * @param val the new value
     */
    public final void setValue(final int val) {
        value = Math.min(maximum, val);
        repaint();
    }

    /**
     * Set the status string of the status bar.
     *
     * @param message the new status string
     */
    public final void setStatus(final String message) {
        status = message;
        repaint();
    }

    /** {@inheritDoc} */
    public final void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        FontMetrics fm = g.getFontMetrics();
        int fh = fm.getAscent();

        /* Draw the progress bar. */
        g.setColor(BAR_COLOR);
        int barWidth = getWidth() - BAR_PADDING_X * 2;
        int progress = (barWidth * value) / maximum;
        g.fillRect(BAR_PADDING_X, fh + BAR_PADDING_Y,
                   progress, BAR_HEIGHT);

        /* Draw the string. */
        g.setColor(STATUS_COLOR);
        int width = fm.stringWidth(status);
        int height = fm.getHeight();
        Font f = fm.getFont();
        g.setFont(f.deriveFont(Font.BOLD));
        g.drawString(status, getWidth() / 2 - width / 2, height);
    }
}
