    package PaooGame.Managers;
import java.sql.*;
import PaooGame.Managers.Data.SaveData;

public class DatabaseManager {
    public static final String URL = "jdbc:sqlite:joc_dinu.db";

    public static void Initialize() {
        try{
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e)
        {
            System.err.println("Eroare la gasirea driverului");
            return;
        }
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                String sql = "CREATE TABLE IF NOT EXISTS SaveGame (" +
                        "slot_id INTEGER PRIMARY KEY, " +
                        "hp INTEGER, " +
                        "x REAL," +
                        "y REAL," +
                        "level INTEGER, " +
                        "room INTEGER, " +
                        "score INTEGER" +
                        ");";
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.err.println("Eroare la init DB: " + e.getMessage());
        }
    }

    public static void SaveProgress(int slot, int hp, float x, float y, int level, int room, int score) {
        String sql = "INSERT OR REPLACE INTO SaveGame(slot_id, hp, x, y, level,room, score) VALUES(?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int i=0;
            pstmt.setInt(++i,slot);
            pstmt.setInt(++i, hp);
            pstmt.setFloat(++i, x);
            pstmt.setFloat(++i, y);
            pstmt.setInt(++i, level);
            pstmt.setInt(++i, room);
            pstmt.setInt(++i,score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SaveData LoadProgress(int slot)
    {
        String sql = "SELECT hp, x, y, level, room, score FROM SaveGame WHERE slot_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, slot);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // daca gasim salvarea, creeam obliectul cu date
                return new SaveData(
                        rs.getInt("hp"),
                        rs.getFloat("x"),
                        rs.getFloat("y"),
                        rs.getInt("level"),
                        rs.getInt("room"),
                        rs.getInt("score")
                );
            }
        } catch (SQLException e) {
            System.err.println("Eroare la incarcare: " + e.getMessage());
        }
        return null;
    }

    public static void DeleteProgress(int slot)
    {
        String sql = "DELETE FROM SaveGame WHERE slot_id = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, slot);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Eroare la ștergerea salvării: " + e.getMessage());
        }
    }
}
