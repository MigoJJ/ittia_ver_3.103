package je.pense.doro.support.sqlite3_manager.abbreviation.setfindedit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import je.pense.doro.entry.EntryDir;

public class DatabaseSort {
    private static final String DB_URL = "jdbc:sqlite:" + EntryDir.HOME_DIR + "/chartplate/filecontrol/database/AbbFullDis.db";
    private static final String TABLE_NAME = "Abbreviations";
    private static final String COLUMN_TO_SORT = "abbreviation";

    public void sortAlphabetically() {
        String sql = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_TO_SORT + " ASC";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String abbreviation = rs.getString("abbreviation");
                String fullText = rs.getString("full_text");
                System.out.println(abbreviation + ": " + fullText);
            }
        } catch (SQLException e) {
            System.err.println("Error sorting data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DatabaseSort sorter = new DatabaseSort();
        sorter.sortAlphabetically();
    }
}