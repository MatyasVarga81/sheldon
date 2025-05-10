package org.example;

import java.sql.*; import java.time.Instant;
public class DBHelper {
    private static final String URL="jdbc:sqlite:game.db";
    static {
        try(Connection c=DriverManager.getConnection(URL);
            Statement s=c.createStatement()){
            s.executeUpdate("CREATE TABLE IF NOT EXISTS results("+
                    "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "player_name TEXT,opponent TEXT,player_move TEXT,"+
                    "ai_move TEXT,outcome TEXT,timestamp TEXT)");
        }catch(SQLException e){e.printStackTrace();}
    }
    public static void saveResult(String playerName,String opponent,
                                  String playerMove,String aiMove,String outcome){
        String sql="INSERT INTO results(player_name,opponent,player_move,ai_move,outcome,timestamp) VALUES(?,?,?,?,?,?)";
        try(Connection c=DriverManager.getConnection(URL);
            PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,playerName);p.setString(2,opponent);
            p.setString(3,playerMove);p.setString(4,aiMove);
            p.setString(5,outcome);p.setString(6, Instant.now().toString());
            p.executeUpdate();
        }catch(SQLException e){e.printStackTrace();}
    }
    public static ResultSet fetchAll() throws SQLException {
        Connection c=DriverManager.getConnection(URL);
        Statement s=c.createStatement();
        return s.executeQuery("SELECT * FROM results ORDER BY id DESC");
    }
}