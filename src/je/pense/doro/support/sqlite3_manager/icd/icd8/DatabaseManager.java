package je.pense.doro.support.sqlite3_manager.icd.icd8;

import java.sql.*;

import je.pense.doro.entry.EntryDir;

public class DatabaseManager {
    private static final String DB_DIR = EntryDir.HOME_DIR + "/chartplate/filecontrol/database";
    private static final String DB_URL = "jdbc:sqlite:" + DB_DIR + "/kcd8db.db";

    public DatabaseManager() {
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS kcd8db ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "code TEXT UNIQUE,"
                + "korean_name TEXT,"
                + "english_name TEXT"
                + ");";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database table ready at " + DB_URL);
        } catch (SQLException e) {
            System.err.println("Table creation error: " + e.getMessage());
        }
    }

    public void insertData(String code, String kDiseaseName, String eDiseaseName) {
        String sql = "INSERT OR IGNORE INTO kcd8db(code, korean_name, english_name) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            pstmt.setString(2, kDiseaseName);
            pstmt.setString(3, eDiseaseName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Insert error for code " + code + ": " + e.getMessage());
        }
    }

    // Maintain compatibility with existing method
    public void insertData(String code, String description) {
        // Map description to english_name, leave korean_name as null
        insertData(code, null, description);
    }

    public ResultSet getAllData() {
        String sql = "SELECT * FROM kcd8db";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.err.println("Fetch error: " + e.getMessage());
            return null;
        }
    }
}