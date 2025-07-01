package je.pense.doro.support.sqlite3_manager.lab.lablong;

import java.sql.*;

import javax.swing.table.DefaultTableModel;

import je.pense.doro.entry.EntryDir;

class LabDatabaseModel {
    static final String DB_URL = "jdbc:sqlite:" + EntryDir.HOME_DIR + "/chartplate/filecontrol/database/LabCodeFullDis.db";

    public void createDatabaseTable() {
        String sql = "CREATE TABLE IF NOT EXISTS labcodes (" +
                     "Category TEXT, B_code TEXT, Items TEXT PRIMARY KEY, unit TEXT, comment TEXT)";
        executeSQL(sql);
    }

    public void createIndexOnItems() {
        String sql = "CREATE INDEX IF NOT EXISTS idx_items ON labcodes (Items)";
        executeSQL(sql);
    }

    private void executeSQL(String sql) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    public void insertRecord(String category, String bCode, String items, String unit, String comment) {
        String sql = "INSERT INTO labcodes (Category, B_code, Items, unit, comment) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setString(2, bCode);
            pstmt.setString(3, items);
            pstmt.setString(4, unit);
            pstmt.setString(5, comment);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting record: " + e.getMessage());
        }
    }

    public void deleteRecord(String items) {
        String sql = "DELETE FROM labcodes WHERE Items=?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, items);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting record: " + e.getMessage());
        }
    }

    public ResultSet getRecordsSortedByItems() {
        String sql = "SELECT Category, B_code, Items, unit, comment FROM labcodes ORDER BY Items ASC";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error fetching records: " + e.getMessage());
            return null;
        }
    }

    public void findAndDisplayRecords(String searchText, DefaultTableModel tableModel) {
        String sql = "SELECT Category, B_code, Items, unit, comment FROM labcodes " +
                     "WHERE Category LIKE ? OR B_code LIKE ? OR Items LIKE ? OR unit LIKE ? OR comment LIKE ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + searchText + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            pstmt.setString(5, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                tableModel.setRowCount(0);
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("Category"), rs.getString("B_code"), rs.getString("Items"),
                        rs.getString("unit"), rs.getString("comment")
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding records: " + e.getMessage());
        }
    }

    public void updateRecord(String oldItems, String category, String bCode, String newItems, String unit, String comment) {
        String sql = "UPDATE labcodes SET Category = ?, B_code = ?, Items = ?, unit = ?, comment = ? WHERE Items = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setString(2, bCode);
            pstmt.setString(3, newItems);
            pstmt.setString(4, unit);
            pstmt.setString(5, comment);
            pstmt.setString(6, oldItems);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("No record found with Items: " + oldItems);
            }
        } catch (SQLException e) {
            System.err.println("Error updating record: " + e.getMessage());
        }
    }
}