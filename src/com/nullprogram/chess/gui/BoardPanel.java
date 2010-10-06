package com.nullprogram.chess.gui;

import java.util.ArrayList;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

import com.nullprogram.chess.Board;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Position;
import com.nullprogram.chess.PositionList;

public class BoardPanel extends JPanel implements MouseListener {

    private static final long serialVersionUID = 1L;

    private Board board;
    private Position selected = null;
    private PositionList moves = null;

    private Color dark = new Color(0xC0, 0x56, 0x0);
    private Color light = new Color(0xFF, 0xA8, 0x58);
    private Color selColor = new Color(0x00, 0xFF, 0xFF);
    private Color moveColor = new Color(0x7F, 0x00, 0x00);

    static final int MIN_SIZE = 25;
    static final int PREF_SIZE = 50;

    private enum Mode {
        WAIT, PLAYER;
    }

    private Mode mode = Mode.WAIT;
    private Piece.Side side;

    public BoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(PREF_SIZE * board.getWidth(),
                                       PREF_SIZE * board.getHeight()));
        setMinimumSize(new Dimension(MIN_SIZE * board.getWidth(),
                                     MIN_SIZE * board.getHeight()));
        addMouseListener(this);
    }

    /**
     * Get the current pixel size of a tile.
     *
     * @return the current size in pixel of one tile.
     */
    private int getTileSize() {
        int h = board.getHeight();
        int w = board.getWidth();
        int sizeX = getWidth() / w;
        int sizeY = getHeight() / h;
        return Math.min(sizeX, sizeY);
    }

    /**
     * Standard painting method.
     *
     * @param g the drawing surface
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int h = board.getHeight();
        int w = board.getWidth();
        int size = getTileSize();

        // Draw the background
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if ((x + y) % 2 == 0) {
                    g.setColor(light);
                } else {
                    g.setColor(dark);
                }
                g.fillRect(x * size, y * size, size, size);
            }
        }

        // Place the pieces
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Piece p = board.getPiece(new Position(x, y));
                if (p != null) {
                    BufferedImage tile = p.getImage(size);
                    g.drawImage(tile, x * size, (h - y - 1) * size, this);
                }
            }
        }

        // Draw selected square
        if (selected != null) {
            g.setColor(selColor);
            highlight(g, selected);

            // Draw piece moves
            if (moves != null) {
                g.setColor(moveColor);
                for (Position pos : moves) {
                    highlight(g, pos);
                }
            }
        }
    }

    /**
     * Highlight the given tile on the board using the current color.
     *
     * @param g   the drawing surface
     * @param pos position to highlight
     */
    private void highlight(Graphics g, Position pos) {
        int size = getTileSize();
        int x = pos.x * size;
        int y = (board.getHeight() - 1 - pos.y) * size;
        int padding = 2;
        int thickness = 3;
        for (int i = padding; i < thickness + padding; i++) {
            g.drawRect(x + i, y + i,
                       size - 1 - i * 2, size - 1 - i * 2);
        }
    }

    public void mouseReleased(MouseEvent e) {
        Position pos = getPixelPosition(e.getPoint());
        if (pos != null) {
            if (pos.equals(selected)) {
                // Delected
                selected = null;
                moves = null;
            } else if (moves != null && moves.contains(pos)) {
                // Move selected piece
                moves.contains(pos);
                // XXX this is temporary
                board.move(selected, pos);
                // XXX
                selected = null;
                moves = null;
            } else {
                // Select this position
                selected = pos;
                Piece p = board.getPiece(selected);
                if (p != null) {
                    moves = p.getMoves();
                } else {
                    moves = null;
                }
            }
        }
        repaint();
    }

    /**
     * Determine which tile a pixel point belongs to.
     *
     * @param p the point
     * @return  the position on the board
     */
    private Position getPixelPosition(Point p) {
        return new Position((int)(p.getX()) / getTileSize(),
                            board.getWidth() - 1 -
                            (int)(p.getY()) / getTileSize());
    }

    /**
     * Tell the BoardPanel to get a move from the player.
     *
     * @param side the side who is making the move
     */
    public void setActive(Piece.Side side) {
        this.side = side;
        this.mode = Mode.PLAYER;
    }

    public void mouseExited(MouseEvent e) {
        // Do nothing
    }

    public void mouseEntered(MouseEvent e) {
        // Do nothing
    }

    public void mouseClicked(MouseEvent e) {
        // Do nothing
    }

    public void mousePressed(MouseEvent e) {
        // Do nothing
    }
}
