package je.pense.doro.support.sqlite3_manager.lab.lablong;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import je.pense.doro.entry.EntryDir;

/**
 * A utility class to remove rows from the labcodes table where B_code is empty.
 */
public class CleanEmptyBCodeRows {
    private static final String DB_URL = "jdbc:sqlite:" + EntryDir.HOME_DIR + "/chartplate/filecontrol/database/LabCodeFullDis.db";
    private static final String TABLE_NAME = "labcodes"; // Matches the table name in LabDatabaseModel

    /**
     * Deletes rows from the labcodes table where B_code is NULL or an empty string.
     */
    public static void cleanEmptyBCodes() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Check if the table exists (optional, for robustness)
            if (!tableExists(conn)) {
                System.err.println("Error: Table '" + TABLE_NAME + "' does not exist in the database.");
                return;
            }

            // Prepare and execute the deletion query
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE B_code IS NULL OR B_code = ''";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Deleted " + rowsAffected + " rows with empty B_code from table '" + TABLE_NAME + "'.");
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to the database or executing the query: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
    }

    /**
     * Checks if the labcodes table exists in the database.
     * @param conn The database connection
     * @return true if the table exists, false otherwise
     * @throws SQLException if a database error occurs
     */
    private static boolean tableExists(Connection conn) throws SQLException {
        String checkTableSQL = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkTableSQL)) {
            pstmt.setString(1, TABLE_NAME);
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Main method to run the cleaning operation standalone.
     */
    public static void main(String[] args) {
        cleanEmptyBCodes();
    }
}