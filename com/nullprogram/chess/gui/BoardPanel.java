package com.nullprogram.chess.gui;

import com.nullprogram.chess.Board;
import com.nullprogram.chess.GameEvent;
import com.nullprogram.chess.GameListener;
import com.nullprogram.chess.Move;
import com.nullprogram.chess.MoveList;
import com.nullprogram.chess.Piece;
import com.nullprogram.chess.Player;
import com.nullprogram.chess.Position;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;
import javax.swing.JComponent;

/**
 * Displays a board and exposes local players.
 *
 * This swing element displays a game board and can also behave as a
 * player as needed.
 */
public class BoardPanel extends JComponent
    implements MouseListener, Player, GameListener {

    /** This class's Logger. */
    private static final Logger LOG =
        Logger.getLogger("com.nullprogram.chess.gui.BoardPanel");

    /** Size of a tile in working coordinates. */
    private static final double TILE_SIZE = 200.0;

    /** Shape provided for drawing background tiles. */
    private static final Shape TILE =
        new Rectangle2D.Double(0, 0, TILE_SIZE, TILE_SIZE);

    /** Padding between the highlight and tile border. */
    static final int HIGHLIGHT_PADDING = 15;

    /** Thickness of highlighting. */
    static final Stroke HIGHLIGHT_STROKE = new BasicStroke(12);

    /** Shape for drawing the highlights. */
    private static final Shape HIGHLIGHT =
        new RoundRectangle2D.Double(HIGHLIGHT_PADDING, HIGHLIGHT_PADDING,
                                    TILE_SIZE - HIGHLIGHT_PADDING * 2,
                                    TILE_SIZE - HIGHLIGHT_PADDING * 2,
                                    HIGHLIGHT_PADDING * 4,
                                    HIGHLIGHT_PADDING * 4);

    /** Version for object serialization. */
    private static final long serialVersionUID = 1L;

    /** The board being displayed. */
    private Board board;

    /** Indicate flipped status. */
    private boolean flipped = true;

    /** The currently selected tile. */
    private Position selected = null;

    /** The list of moves for the selected tile. */
    private MoveList moves = null;

    /** The color for the dark tiles on the board. */
    static final Color DARK = new Color(0xD1, 0x8B, 0x47);

    /** The color for the light tiles on the board. */
    static final Color LIGHT = new Color(0xFF, 0xCE, 0x9E);

    /** Border color for a selected tile. */
    static final Color SELECTED = new Color(0x00, 0xFF, 0xFF);

    /** Border color for a highlighted movement tile. */
    static final Color MOVEMENT = new Color(0x7F, 0x00, 0x00);

    /** Last move highlight color. */
    static final Color LAST = new Color(0x00, 0x7F, 0xFF);

    /** Minimum size of a tile, in pixels. */
    static final int MIN_SIZE = 25;

    /** Preferred size of a tile, in pixels. */
    static final int PREF_SIZE = 75;

    /** The current interaction mode. */
    private Mode mode = Mode.WAIT;

    /** Current player making a move, when interactive. */
    private Piece.Side side;

    /** Latch to hold down the Game thread while the user makes a selection. */
    private CountDownLatch latch;

    /** The move selected by the player. */
    private Move selectedMove;

    /** The interaction modes. */
    private enum Mode {
        /** Don't interact with the player. */
        WAIT,
        /** Interact with the player. */
        PLAYER;
    }

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
        updateSize();
        addMouseListener(this);
    }

    /**
     * Set the preferred board size.
     */
    private void updateSize() {
        setPreferredSize(new Dimension(PREF_SIZE * board.getWidth(),
                                       PREF_SIZE * board.getHeight()));
        setMinimumSize(new Dimension(MIN_SIZE * board.getWidth(),
                                     MIN_SIZE * board.getHeight()));
    }

    @Override
    public final Dimension getPreferredSize() {
        return new Dimension(PREF_SIZE * board.getWidth(),
                             PREF_SIZE * board.getHeight());
    }

    /**
     * Change the board to be displayed.
     *
     * @param b the new board
     */
    public final void setBoard(final Board b) {
        board = b;
        updateSize();
        repaint();
    }

    /**
     * Change the board to be displayed.
     *
     * @return display's board
     */
    public final Board getBoard() {
        return board;
    }

    /**
     * Return the transform between working space and drawing space.
     *
     * @return display transform
     */
    public final AffineTransform getTransform() {
        AffineTransform at = new AffineTransform();
        at.scale(getWidth() / (TILE_SIZE * board.getWidth()),
                 getHeight() / (TILE_SIZE * board.getHeight()));
        return at;
    }

    /**
     * Standard painting method.
     *
     * @param graphics the drawing surface
     */
    public final void paintComponent(final Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        int h = board.getHeight();
        int w = board.getWidth();
        g.transform(getTransform());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                           RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                           RenderingHints.VALUE_RENDER_QUALITY);

        /* Temp AffineTransform for the method */
        AffineTransform at = new AffineTransform();

        /* Draw the background */
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if ((x + y) % 2 == 0) {
                    g.setColor(LIGHT);
                } else {
                    g.setColor(DARK);
                }
                at.setToTranslation(x * TILE_SIZE, y * TILE_SIZE);
                g.fill(at.createTransformedShape(TILE));
            }
        }

        /* Place the pieces */
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Piece p = board.getPiece(new Position(x, y));
                if (p != null) {
                    Image tile = p.getImage();
                    int yy = y;
                    if (flipped) {
                        yy = board.getHeight() - 1 - y;
                    }
                    at.setToTranslation(x * TILE_SIZE, yy * TILE_SIZE);
                    g.drawImage(tile, at, null);
                }
            }
        }

        /* Draw last move */
        Move last = board.last();
        if (last != null) {
            g.setColor(LAST);
            highlight(g, last.getOrigin());
            highlight(g, last.getDest());
        }

        /* Draw selected square */
        if (selected != null) {
            g.setColor(SELECTED);
            highlight(g, selected);

            /* Draw piece moves */
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
    private void highlight(final Graphics2D g, final Position pos) {
        int x = pos.getX();
        int y = pos.getY();
        if (flipped) {
            y = board.getHeight() - 1 - y;
        }
        g.setStroke(HIGHLIGHT_STROKE);
        AffineTransform at = new AffineTransform();
        at.translate(x * TILE_SIZE, y * TILE_SIZE);
        g.draw(at.createTransformedShape(HIGHLIGHT));
    }

    @Override
    public final void mouseReleased(final MouseEvent e) {
        switch (e.getButton()) {
        case MouseEvent.BUTTON1:
            leftClick(e);
            break;
        default:
            /* do nothing */
            break;
        }
        repaint();
    }

    /**
     * Handle the event when the left button is clicked.
     *
     * @param e the mouse event
     */
    private void leftClick(final MouseEvent e) {
        if (mode == Mode.WAIT) {
            return;
        }

        Position pos = getPixelPosition(e.getPoint());
        if (!board.inRange(pos)) {
            /* Click was outside the board, somehow. */
            return;
        }
        if (pos != null) {
            if (pos.equals(selected)) {
                /* Deselect */
                selected = null;
                moves = null;
            } else if (moves != null && moves.containsDest(pos)) {
                /* Move selected piece */
                mode = Mode.WAIT;
                Move move = moves.getMoveByDest(pos);
                selected = null;
                moves = null;
                selectedMove = move;
                latch.countDown();
            } else {
                /* Select this position */
                Piece p = board.getPiece(pos);
                if (p != null && p.getSide() == side) {
                    selected = pos;
                    moves = p.getMoves(true);
                }
            }
        }
    }

    /**
     * Determine which tile a pixel point belongs to.
     *
     * @param p the point
     * @return  the position on the board
     */
    private Position getPixelPosition(final Point2D p) {
        Point2D pout = null;
        try {
            pout = getTransform().inverseTransform(p, null);
        } catch (java.awt.geom.NoninvertibleTransformException t) {
            /* This will never happen. */
            return null;
        }
        int x = (int) (pout.getX() / TILE_SIZE);
        int y = (int) (pout.getY() / TILE_SIZE);
        if (flipped) {
            y = board.getHeight() - 1 - y;
        }
        return new Position(x, y);
    }

    @Override
    public final Move takeTurn(final Board turnBoard,
                               final Piece.Side currentSide) {
        latch = new CountDownLatch(1);
        board = turnBoard;
        side = currentSide;
        repaint();
        mode = Mode.PLAYER;
        try {
            latch.await();
        } catch (InterruptedException e) {
            LOG.warning("BoardPanel interrupted during turn.");
        }
        return selectedMove;
    }

    @Override
    public final void gameEvent(final GameEvent e) {
        board = e.getGame().getBoard();
        if (e.getType() != GameEvent.STATUS) {
            repaint();
        }
    }

    /**
     * Return the desired aspect ratio of the board.
     *
     * @return desired aspect ratio
     */
    public final double getRatio() {
        return board.getWidth() / (1.0 * board.getHeight());
    }

    /**
     * Set whether or not the board should be displayed flipped.
     * @param value  the new flipped state
     */
    public final void setFlipped(final boolean value) {
        flipped = value;
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        /* Do nothing */
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        /* Do nothing */
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        /* Do nothing */
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        /* Do nothing */
    }
}
