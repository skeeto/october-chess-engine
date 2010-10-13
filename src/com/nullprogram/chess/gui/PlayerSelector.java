package com.nullprogram.chess.gui;

import javax.swing.ButtonGroup;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

/**
 * Creates a panel for selecting a player type.
 */
public class PlayerSelector extends JPanel {

    /** Version for object serialization. */
    private static final long serialVersionUID = 1L;

    /** Selection for a human player. */
    private JRadioButton human;

    /** Selection for a computer player. */
    private JRadioButton minimax;

    /** Vertical padding around this panel. */
    static final int V_PADDING = 15;

    /** Horizontal padding around this panel. */
    static final int H_PADDING = 10;

    /**
     * Creates a player selector panel.
     *
     * @param title title for this selector
     * @param humanSet select the human player by default
     */
    public PlayerSelector(final String title, final boolean humanSet) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(title);
        add(label);

        human = new JRadioButton("Human");
        minimax = new JRadioButton("Computer");
        ButtonGroup group = new ButtonGroup();
        group.add(human);
        group.add(minimax);
        human.setSelected(humanSet);
        minimax.setSelected(!humanSet);

        add(human);
        add(minimax);

        setBorder(BorderFactory.createEmptyBorder(H_PADDING, V_PADDING,
                                                  H_PADDING, V_PADDING));
    }

    /**
     * Get the player selected by this dialog.
     *
     * @return the player type
     */
    public final String getPlayer() {
        if (human.isSelected()) {
            return "human";
        } else {
            return "computer";
        }
    }
}
