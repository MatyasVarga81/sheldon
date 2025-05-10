package org.example;

import javax.swing.*;
import java.awt.*;
public class NamePanel extends JPanel {
    public NamePanel(GameFrame f){
        setLayout(new BorderLayout());
        JLabel l=new JLabel("Kérlek, add meg a neved:"); JTextField tf=new JTextField();
        JButton ok=new JButton("OK"); ok.addActionListener(e->{
            String name=tf.getText().trim(); if(!name.isEmpty()){f.setPlayerName(name); f.showOpponentSelection();}
        });
        JPanel p=new JPanel(new BorderLayout(5,5)); p.add(l,BorderLayout.NORTH);
        p.add(tf,BorderLayout.CENTER); p.add(ok,BorderLayout.SOUTH);
        add(p,BorderLayout.CENTER);
    }
}