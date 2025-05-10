package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
public class HistoryPanel {
    public static void show(JFrame parent){
        try(ResultSet rs=DBHelper.fetchAll()){
            String[] cols={"ID","Játékos","Ellenfél","Lépés","AI","Eredmény","Dátum"};
            DefaultTableModel m=new DefaultTableModel(cols,0);
            while(rs.next()) m.addRow(new Object[]{rs.getInt("id"),rs.getString("player_name"),
                    rs.getString("opponent"),rs.getString("player_move"),rs.getString("ai_move"),
                    rs.getString("outcome"),rs.getString("timestamp")});
            JTable tbl=new JTable(m);
            JOptionPane.showMessageDialog(parent,new JScrollPane(tbl),"Játékok története",JOptionPane.PLAIN_MESSAGE);
        }catch(SQLException e){e.printStackTrace();}
    }
}