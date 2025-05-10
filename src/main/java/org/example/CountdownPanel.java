package org.example;
import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
public class CountdownPanel extends JPanel {
    private JLabel lbl = new JLabel("",SwingConstants.CENTER);
    private String[] seq = {"kő","papír","olló","gyík","spock"};
    private int idx; private GameFrame frame;
    public CountdownPanel(GameFrame frame){ this.frame=frame; setLayout(new BorderLayout()); lbl.setFont(new Font(null,Font.PLAIN,48)); add(lbl,BorderLayout.CENTER); }
    public void startCountdown(){ idx=0; java.util.Timer t = new java.util.Timer(); t.scheduleAtFixedRate(new TimerTask()
    { public void run(){ if(idx<seq.length){ lbl.setText(seq[idx++]); } else { t.cancel(); SwingUtilities.invokeLater(frame::countdownFinished); } } },0,800);} }

