package com.nullprogram.chess.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListSelectionModel;

/**
 * Creates a panel for selecting a player type.
 */
public class PlayerSelector extends JPanel {

    /** Version for object serialization. */
    private static final long serialVersionUID = 1L;

    /** JList labels for the user. */
    private static final String[] LABELS_AI = {
        "Fool (2 plies)",
        "Very Easy (3 plies)",
        "Easy (4 plies)",
        "Medium (5 plies)",
        "Hard (6 plies)",
        "Harder (7 plies)",
        "Challenging (8 plies)"
    };

    /** Configuration names corresponding to LABELS_AI. */
    private static final String[] NAMES_AI = {
        "depth2", "depth3", "depth4", "depth5", "depth6", "depth7", "depth8",
    };

    /** The default AI selection in the JList. */
    private static final int DEFAULT_AI = 2;

    /** Selection for a human player. */
    private final JRadioButton human = new JRadioButton("Human");;

    /** Selection for a computer player. */
    private final JRadioButton minimax = new JRadioButton("Computer");

    /** AI selector. */
    private final JList ai = new JList(LABELS_AI);

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

        /* Set up widgets. */
        ButtonGroup group = new ButtonGroup();
        group.add(human);
        group.add(minimax);
        human.setSelected(humanSet);
        minimax.setSelected(!humanSet);
        ai.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ai.setSelectedIndex(DEFAULT_AI);
        ai.setEnabled(!humanSet);

        /* Set up widget alignment. */
        human.setAlignmentX(Component.LEFT_ALIGNMENT);
        minimax.setAlignmentX(Component.LEFT_ALIGNMENT);
        ai.setAlignmentX(Component.LEFT_ALIGNMENT);

        /* Set up list enable/disable. */
        human.addActionListener(new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
                ai.setEnabled(!human.isSelected());
            }
        });
        minimax.addActionListener(new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
                ai.setEnabled(minimax.isSelected());
            }
        });

        add(human);
        add(minimax);
        add(ai);
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
            int i = ai.getSelectedIndex();
            if (i < 0) {
                return "default";
            } else {
                return NAMES_AI[i];
            }
        }
    }
}
