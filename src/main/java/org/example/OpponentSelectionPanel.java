package org.example;

// OpponentSelectionPanel.java
import javax.swing.*;
        import java.awt.*;
public class OpponentSelectionPanel extends JPanel {
    public OpponentSelectionPanel(GameFrame frame) {
        setLayout(new GridLayout(0, 1, 5, 5));
        Opponent[] ops = {
                new Opponent("Sheldon",2.0),
                new Opponent("Penny",0.7),
                new Opponent("Raj",1.0),
                new Opponent("Howard",1.0),
                new Opponent("Leonard",1.0)
        };
        for (Opponent o : ops) {
            JButton b = new JButton(o.getName());
            b.addActionListener(e -> {
                if (o.getName().equals("Sheldon")) {
                    int res = JOptionPane.showConfirmDialog(this,
                            "Biztos vagy benne, hogy Sheldonnal akarsz játszani?",
                            "Megerősítés", JOptionPane.YES_NO_OPTION);
                    if (res != JOptionPane.YES_OPTION) return;
                    JOptionPane.showMessageDialog(this, "Hát legyen");
                }
                frame.startGameWithOpponent(o);
            });
            add(b);
        }
        JButton hist = new JButton("Eredmények lekérése");
        hist.addActionListener(e -> frame.showHistory());
        add(hist);
    }
}