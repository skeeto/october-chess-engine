package com.nullprogram.chess.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

import com.nullprogram.chess.Game;
import com.nullprogram.chess.Board;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Player;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Position;

/**
 * Displays a board and exposes local players.
 *
 * This swing element displays a game board and can also behave as a
 * player as needed.
 */
public class BoardPanel extends JPanel implements MouseListener, Player {

    /**
     * Version for object serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The board being displayed.
     */
    private Board board;

    /**
     * The game engine used when the board is behaving as a player.
     */
    private Game game;

    /**
     * The currently selected tile.
     */
    private Position selected = null;

    /**
     * The list of moves for the selected tile.
     */
    private MoveList moves = null;

    /**
     * The color for the dark tiles on the board.
     */
    static final Color DARK = new Color(0xD1, 0x8B, 0x47);

    /**
     * The color for the light tiles on the board.
     */
    static final Color LIGHT = new Color(0xFF, 0xCE, 0x9E);

    /**
     * Border color for a selected tile.
     */
    static final Color SELECTED = new Color(0x00, 0xFF, 0xFF);

    /**
     * Border color for a highlighted movement tile.
     */
    static final Color MOVEMENT = new Color(0x7F, 0x00, 0x00);

    /**
     * Padding between the highlight and tile border.
     */
    static final int PADDING = 2;

    /**
     * Thickness of highlighting.
     */
    static final int THICKNESS = 3;

    /**
     * Minimum size of a tile, in pixels.
     */
    static final int MIN_SIZE = 25;

    /**
     * Preferred size of a tile, in pixels.
     */
    static final int PREF_SIZE = 50;

    /**
     * The interaction modes.
     */
    private enum Mode {
        /**
         * Don't interact with the player.
         */
        WAIT,
        /**
         * Interact with the player.
         */
        PLAYER;
    }

    /**
     * The current interaction mode.
     */
    private Mode mode = Mode.WAIT;

    /**
     * Current player making a move, when interactive.
     */
    private Piece.Side side;

    /**
     * Hidden constructor.
     */
    protected BoardPanel() {
    }

    /**
     * Create a new display for given board.
     *
     * @param displayBoard the board to be displayed
     */
    public BoardPanel(final Board displayBoard) {
        board = displayBoard;
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
    public final void paintComponent(final Graphics g) {
        super.paintComponent(g);
        int h = board.getHeight();
        int w = board.getWidth();
        int size = getTileSize();

        // Draw the background
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if ((x + y) % 2 == 0) {
                    g.setColor(LIGHT);
                } else {
                    g.setColor(DARK);
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
            g.setColor(SELECTED);
            highlight(g, selected);

            // Draw piece moves
            if (moves != null) {
                g.setColor(MOVEMENT);
                for (Move move : moves) {
                    highlight(g, move.getDest());
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
    private void highlight(final Graphics g, final Position pos) {
        int size = getTileSize();
        int x = pos.getX() * size;
        int y = (board.getHeight() - 1 - pos.getY()) * size;
        for (int i = PADDING; i < THICKNESS + PADDING; i++) {
            g.drawRect(x + i, y + i,
                       size - 1 - i * 2, size - 1 - i * 2);
        }
    }

    /** {@inheritDoc} */
    public final void mouseReleased(final MouseEvent e) {
        if (mode == Mode.WAIT) {
            return;
        }

        Position pos = getPixelPosition(e.getPoint());
        if (pos != null) {
            if (pos.equals(selected)) {
                // Delected
                selected = null;
                moves = null;
            } else if (moves != null && moves.containsDest(pos)) {
                // Move selected piece
                mode = Mode.WAIT;
                game.move(moves.getMoveByDest(pos));
                selected = null;
                moves = null;
            } else {
                // Select this position
                Piece p = board.getPiece(pos);
                if (p != null && p.getSide() == side) {
                    selected = pos;
                    moves = p.getMoves(true);
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
    private Position getPixelPosition(final Point p) {
        return new Position((int) (p.getX()) / getTileSize(),
                            board.getWidth() - 1
                            - (int) (p.getY()) / getTileSize());
    }

    /**
     * Tell the BoardPanel to get a move from the player.
     *
     * @param currentSide the side who is making the move
     */
    public final void setActive(final Piece.Side currentSide) {
        side = currentSide;
        mode = Mode.PLAYER;
    }

    /**
     * Set the current game for this player.
     *
     * @param currentGame the game for this player
     */
    public final void setGame(final Game currentGame) {
        game = currentGame;
    }

    /** {@inheritDoc} */
    public void mouseExited(final MouseEvent e) {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void mouseEntered(final MouseEvent e) {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void mouseClicked(final MouseEvent e) {
        // Do nothing
    }

    /** {@inheritDoc} */
    public void mousePressed(final MouseEvent e) {
        // Do nothing
    }
}
