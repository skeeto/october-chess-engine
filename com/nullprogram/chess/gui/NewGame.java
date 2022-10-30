package com.nullprogram.chess.gui;

import com.nullprogram.chess.Board;
import com.nullprogram.chess.Game;
import com.nullprogram.chess.Player;
import com.nullprogram.chess.ai.Minimax;
import com.nullprogram.chess.boards.Gothic;
import com.nullprogram.chess.boards.StandardBoard;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Presents the "New Game" dialog to let the user set up a game.
 */
public class NewGame extends JDialog implements ActionListener {

    /** Version for object serialization. */
    private static final long serialVersionUID = 1L;

    /** Parent to this dialog. */
    private final ChessFrame parent;

    /** White player selector. */
    private final PlayerSelector whitePanel;

    /** Black player selector. */
    private final PlayerSelector blackPanel;

    /** Black player selector. */
    private final BoardSelector boardPanel;

    /** Vertical padding around this panel. */
    static final int V_PADDING = 15;

    /** Horizontal padding around this panel. */
    static final int H_PADDING = 10;

    /** True if the dialog was cancelled away. */
    private boolean cancelled = true;

    /**
     * Create a new dialog to ask the user for the game configuration.
     *
     * @param owner parent to this dialog
     */
    public NewGame(final ChessFrame owner) {
        super(owner, "New game", true);
        parent = owner;
        setLayout(new BorderLayout());
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        whitePanel = new PlayerSelector("White:", true);
        blackPanel = new PlayerSelector("Black:", false);
        add(whitePanel, BorderLayout.LINE_START);
        add(blackPanel, BorderLayout.CENTER);
        boardPanel = new BoardSelector();
        add(boardPanel, BorderLayout.LINE_END);

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        ok.addActionListener(this);
        cancel.addActionListener(this);
        getRootPane().setDefaultButton(ok);
        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new BoxLayout(buttonRow, BoxLayout.X_AXIS));
        buttonRow.setBorder(BorderFactory.createEmptyBorder(H_PADDING,
                            V_PADDING,
                            H_PADDING,
                            V_PADDING));
        buttonRow.add(Box.createHorizontalGlue());
        buttonRow.add(ok);
        buttonRow.add(cancel);
        add(buttonRow, BorderLayout.PAGE_END);

        pack();
    }

    @Override
    public final void actionPerformed(final ActionEvent e) {
        if ("OK".equals(e.getActionCommand())) {
            cancelled = false;
        }
        setVisible(false);
        dispose();
    }

    /**
     * Create a new Player instance based on the given string.
     *
     * @param game the game the player will be playing
     * @param name name of type of player
     * @return player of named type
     */
    private Player createPlayer(final Game game, final String name) {
        if ("human".equals(name)) {
            return parent.getPlayer();
        } else {
            return new Minimax(game, name);
        }
    }

    /**
     * Create a new Board instance based on the given string.
     *
     * @param name name of type of board
     * @return board of named type
     */
    private Board createBoard(final String name) {
        if ("chess".equals(name)) {
            return new StandardBoard();
        } else if ("gothic".equals(name)) {
            return new Gothic();
        } else {
            return null;
        }
    }

    /**
     * Get the game selected/created by the user.
     *
     * @return the new game
     */
    public final Game getGame() {
        if (cancelled) {
            return null;
        }
        Game game = new Game(createBoard(boardPanel.getBoard()));
        Player white = createPlayer(game, whitePanel.getPlayer());
        Player black = createPlayer(game, blackPanel.getPlayer());
        game.seat(white, black);
        return game;
    }
}
