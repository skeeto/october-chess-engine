package com.nullprogram.chess.gui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Creates a panel for selecting a player type.
 */
public class BoardSelector extends JPanel {

    /** Version for object serialization. */
    private static final long serialVersionUID = 1L;

    /** Selection for a human player. */
    private final JRadioButton standard;

    /** Selection for a computer player. */
    private final JRadioButton gothic;

    /** Vertical padding around this panel. */
    static final int V_PADDING = 15;

    /** Horizontal padding around this panel. */
    static final int H_PADDING = 10;

    /**
     * Creates a player selector panel.
     */
    public BoardSelector() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Game type:");
        add(label);

        standard = new JRadioButton("Standard");
        gothic = new JRadioButton("Gothic");
        ButtonGroup group = new ButtonGroup();
        group.add(standard);
        group.add(gothic);
        standard.setSelected(true);

        add(standard);
        add(gothic);

        setBorder(BorderFactory.createEmptyBorder(H_PADDING, V_PADDING,
                  H_PADDING, V_PADDING));
    }

    /**
     * Get the board selected by this dialog.
     *
     * @return the board type
     */
    public final String getBoard() {
        if (standard.isSelected()) {
            return "chess";
        } else if (gothic.isSelected()) {
            return "gothic";
        } else {
            throw new AssertionError("Unknown board selected!");
        }
    }
}
