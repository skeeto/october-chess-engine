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

public class BoardPanel extends JPanel implements MouseListener {

    private static final long serialVersionUID = 1L;

    private Board board;
    private Position selected = null;

    private Color dark = new Color(0xC0, 0x56, 0x0);
    private Color light = new Color(0xFF, 0xA8, 0x58);
    private Color selColor = new Color(0x00, 0xFF, 0xFF);
    private Color moveColor = new Color(0x7F, 0x00, 0x00);

    static final int MIN_SIZE = 25;
    static final int PREF_SIZE = 50;

    public BoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(PREF_SIZE * board.getWidth(),
                                       PREF_SIZE * board.getHeight()));
        setMinimumSize(new Dimension(MIN_SIZE * board.getWidth(),
                                     MIN_SIZE * board.getHeight()));
        addMouseListener(this);
    }

    private int getTileSize() {
        int h = board.getHeight();
        int w = board.getWidth();
        int sizeX = getWidth() / w;
        int sizeY = getHeight() / h;
        return Math.min(sizeX, sizeY);
    }

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
            Piece p = board.getPiece(selected);
            if (p != null) {
                ArrayList<Position> moves = p.getMoves();
                g.setColor(moveColor);
                for (Position pos : moves) {
                    highlight(g, pos);
                }
            }
        }
    }

    private void highlight(Graphics g, Position pos) {
        int size = getTileSize();
        int x = pos.x * size;
        int y = (board.getHeight() - 1 - pos.y) * size;
        int thickness = 3;
        for (int i = 0; i < thickness; i++) {
            g.drawRect(x + i, y + i,
                       size - 1 - i * 2, size - 1 - i * 2);
        }
    }

    public void mouseReleased(MouseEvent e) {
        selected = getPixelPosition(e.getPoint());
        repaint();
    }

    private Position getPixelPosition(Point p) {
        return new Position((int)(p.getX()) / getTileSize(),
                            board.getWidth() - 1 -
                            (int)(p.getY()) / getTileSize());
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
