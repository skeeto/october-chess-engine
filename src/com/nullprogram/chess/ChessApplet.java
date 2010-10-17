/* Copyright (c) 2010 Christopher Wellons <mosquitopsu@gmail.com>
 *
 * Permission to use, copy, modify, and distribute this software for
 * any purpose with or without fee is hereby granted, provided that
 * the above copyright notice and this permission notice appear in all
 * copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL
 * WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE
 * AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS
 * OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 * NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN
 * CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package com.nullprogram.chess;

import javax.swing.UIManager;
import javax.swing.JApplet;

import com.nullprogram.chess.gui.BoardPanel;

import com.nullprogram.chess.boards.StandardBoard;

import com.nullprogram.chess.ai.Minimax;

/**
 * Applet that runs a game versus the computer, no other options.
 */
public final class ChessApplet extends JApplet {

    /** Version for object serialization. */
    private static final long serialVersionUID = 34863129470926196L;

    /** {@inheritDoc} */
    public void init() {
        try {
            String lnf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lnf);
        } catch (Exception e) {
            System.out.println("Failed to set 'Look and Feel'.");
        }

        StandardBoard board = new StandardBoard();
        BoardPanel panel = new BoardPanel(board);
        add(panel);
        Game game = new Game(null, board, panel, new Minimax(null));
        game.begin();
    }
}
