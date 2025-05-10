package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ResultPanel extends JPanel {
    private static final int ICON_SIZE = 80;
    private final GameFrame frame;
    private final JLabel playerLbl = new JLabel();
    private final JLabel aiLbl     = new JLabel();
    private final JTextArea text   = new JTextArea();
    private final ImageIcon sheldonIcon;

    public ResultPanel(GameFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));

        // Top: titles and scaled move icons in 2 rows
        JPanel top = new JPanel(new GridLayout(2, 2, 5, 5));
        JLabel playerTitle = new JLabel("Játékos választása", SwingConstants.CENTER);
        JLabel aiTitle     = new JLabel("Gép választása", SwingConstants.CENTER);
        top.add(playerTitle);
        top.add(aiTitle);
        top.add(playerLbl);
        top.add(aiLbl);
        add(top, BorderLayout.NORTH);

        // Bottom panel: bubble and OK button
        sheldonIcon = new ImageIcon(
                new ImageIcon(getClass().getResource("/images/sheldon.png"))
                        .getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)
        );
        JPanel bottomWrapper = new JPanel(new BorderLayout());

        // Bubble panel
        JPanel bubblePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.drawImage(sheldonIcon.getImage(), 10, 10, this);

                String[] lines = text.getText().split("\\n");
                if (lines.length > 0) {
                    int bubbleX = 10 + ICON_SIZE + 10;
                    int bubbleY = 10;
                    int bubbleW = getWidth() - bubbleX - 10;
                    int bubbleH = lines.length * g2.getFontMetrics().getHeight() + 20;
                    RoundRectangle2D bubble = new RoundRectangle2D.Double(bubbleX, bubbleY, bubbleW, bubbleH, 20, 20);
                    g2.setColor(Color.WHITE);
                    g2.fill(bubble);
                    g2.setColor(Color.BLACK);
                    g2.draw(bubble);

                    int textX = bubbleX + 10;
                    int lineHeight = g2.getFontMetrics().getHeight();
                    int textY = bubbleY + g2.getFontMetrics().getAscent() + 10;
                    for (int i = 0; i < lines.length; i++) {
                        g2.drawString(lines[i], textX, textY + i * lineHeight);
                    }
                }
            }
        };
        bubblePanel.setPreferredSize(new Dimension(0, ICON_SIZE + 20));
        bottomWrapper.add(bubblePanel, BorderLayout.CENTER);

        // OK button
        JButton ok = new JButton("OK");
        ok.addActionListener(e -> frame.showOpponentSelection());
        JPanel btnPanel = new JPanel();
        btnPanel.add(ok);
        bottomWrapper.add(btnPanel, BorderLayout.SOUTH);

        add(bottomWrapper, BorderLayout.SOUTH);
    }

    public void showResult(Move player, Move ai) {
        // Scale and set icons
        ImageIcon pIcon = new ImageIcon(getClass().getResource(player.getImagePath()));
        Image pImg = pIcon.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        playerLbl.setIcon(new ImageIcon(pImg));

        ImageIcon aIcon = new ImageIcon(getClass().getResource(ai.getImagePath()));
        Image aImg = aIcon.getImage().getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
        aiLbl.setIcon(new ImageIcon(aImg));

        // Save to DB
        String outcome = player == ai ? "Döntetlen" : (player.beats(ai) ? "Győzelem" : "Vereség");
        DBHelper.saveResult(
                frame.getPlayerName(),
                frame.getCurrentOpponent().getName(),
                player.getName(), ai.getName(), outcome
        );



        if (player == ai) {
            // Draw
            text.setText("\nDöntetlen!");
        } else {
            GameLogic logic = new GameLogic(frame.getCurrentOpponent());
            String ruleSentence = logic.getRuleSentence(player, ai);
            String finalSentence = player.beats(ai)
                    ? "Mázlid volt. Játszunk még egyet?"
                    : "És végül a gépek győzedelmeskednek";
            text.setText(ruleSentence + "\n" + finalSentence);
        }
        repaint();
    }
}




