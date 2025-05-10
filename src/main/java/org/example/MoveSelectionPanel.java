package org.example;

import javax.swing.*;
import java.awt.*;
public class MoveSelectionPanel extends JPanel {
    private static final int ICON_SIZE = 80;  // Fixed icon size

    public MoveSelectionPanel(GameFrame frame) {
        setLayout(new GridLayout(1, 5, 5, 5));
        for (Move m : Move.values()) {
            ImageIcon orig = new ImageIcon(getClass().getResource(m.getImagePath()));
            Image scaled = orig.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
            JButton btn = new JButton(new ImageIcon(scaled));
            btn.setToolTipText(m.getName());
            btn.addActionListener(e -> frame.onPlayerMoveSelected(m));
            add(btn);
        }
    }
}