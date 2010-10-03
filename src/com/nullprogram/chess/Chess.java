package com.nullprogram.chess;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;

import com.nullprogram.chess.gui.BoardPanel;

public class Chess {
    public static void main(String[] args) {
        Board board = new Board();
        BoardPanel display = new BoardPanel(board);

        JFrame frame = new JFrame("Chess");
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(display);
        frame.pack();
        frame.setVisible(true);
    }
}
