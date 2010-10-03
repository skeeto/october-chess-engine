package com.nullprogram.chess.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JPanel;

import com.nullprogram.chess.Board;

public class BoardPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private Board board;

    private Color dark = new Color(0xC0, 0x56, 0x0);
    private Color light = new Color(0xFF, 0xA8, 0x58);
    static final int MIN_SIZE = 25;
    static final int PREF_SIZE = 50;

    public BoardPanel(Board board) {
        this.board = board;
        setPreferredSize(new Dimension(PREF_SIZE * board.getWidth(),
                                       PREF_SIZE * board.getHeight()));
        setMinimumSize(new Dimension(MIN_SIZE * board.getWidth(),
                                     MIN_SIZE * board.getHeight()));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int sizeX = getWidth() / board.getWidth();
        int sizeY = getHeight() / board.getHeight();
        int size = Math.min(sizeX, sizeY);

        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                if ((x + y) % 2 == 0) {
                    g.setColor(light);
                } else {
                    g.setColor(dark);
                }
                g.fillRect(x * size, y * size, size, size);
            }
        }
    }
}
