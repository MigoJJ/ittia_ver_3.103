package je.pense.doro.support.sqlite3_manager.lab.labshort;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

import je.pense.doro.entry.EntryDir;

public class JavalabtestsDBManager {
//    private static final String DB_URL = "jdbc:sqlite:" + EntryDir.homeDir + "/fourgate/n_laboratorytest/frequent/javalabtests.db";
    private static final String DB_URL = "jdbc:sqlite:" + EntryDir.HOME_DIR + "/chartplate/filecontrol/database/javalabtests.db";


    public static void main(String[] args) {
        // Initialize database
        createDatabaseTable();
        
        // Create manager instance
        JavalabtestsDBManager dbManager = new JavalabtestsDBManager();
        
        // Test operations
        dbManager.testDatabaseOperations();
    }
    
    public void testDatabaseOperations() {
        // Insert test records
        insertRecord("Hematology", "CBC", "Complete Blood Count");
        insertRecord("Hematology", "HGB", "Hemoglobin");
        insertRecord("Chemistry", "GLU", "Glucose");
        
        // Display all records
        System.out.println("All records after insertion:");
        displayAllRecords();
        
        // Update a record
        updateRecord("Hematology", "HGB", "Hematology", "HGB", "Hemoglobin Test");
        System.out.println("\nAfter updating HGB:");
        displayAllRecords();
        
        // Search records
        System.out.println("\nSearch results for 'Hem':");
        searchAndDisplay("Hem", "description");
        
        // Delete a record
        deleteRecord("Chemistry", "GLU");
        System.out.println("\nAfter deleting GLU:");
        displayAllRecords();
        
        // Clear table (optional)
        // clearTable();
    }
    
    public static void createDatabaseTable() {
        String sql = "CREATE TABLE IF NOT EXISTS lab_tests (" +
                     "category TEXT, " +
                     "code TEXT, " +
                     "description TEXT, " +
                     "PRIMARY KEY (category, code))";
        executeSQL(sql);
    }

    private static void executeSQL(String sql) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    public void insertRecord(String category, String code, String description) {
        String sql = "INSERT INTO lab_tests (category, code, description) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setString(2, code);
            pstmt.setString(3, description);
            pstmt.executeUpdate();
            System.out.println("Inserted record: " + category + ", " + code);
        } catch (SQLException e) {
            System.err.println("Error inserting record: " + e.getMessage());
        }
    }

    public void deleteRecord(String category, String code) {
        String sql = "DELETE FROM lab_tests WHERE category=? AND code=?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setString(2, code);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Deleted record: " + category + ", " + code);
            } else {
                System.out.println("No record found to delete: " + category + ", " + code);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting record: " + e.getMessage());
        }
    }

    public void updateRecord(String oldCategory, String oldCode, 
                            String newCategory, String newCode, String description) {
        String sql = "UPDATE lab_tests SET category=?, code=?, description=? " +
                     "WHERE category=? AND code=?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newCategory);
            pstmt.setString(2, newCode);
            pstmt.setString(3, description);
            pstmt.setString(4, oldCategory);
            pstmt.setString(5, oldCode);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Updated record from " + oldCategory + "/" + oldCode + 
                                 " to " + newCategory + "/" + newCode);
            } else {
                System.out.println("No record found to update: " + oldCategory + "/" + oldCode);
            }
        } catch (SQLException e) {
            System.err.println("Error updating record: " + e.getMessage());
        }
    }

    public ResultSet getRecords() {
        String sql = "SELECT category, code, description FROM lab_tests";
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error fetching records: " + e.getMessage());
            return null;
        }
    }

    public void findAndDisplayRecords(String searchText, String column, DefaultTableModel tableModel) {
        String sql = "SELECT category, code, description FROM lab_tests WHERE ";
        switch (column) {
            case "category":
                sql += "category LIKE ?";
                break;
            case "code":
                sql += "code LIKE ?";
                break;
            case "description":
                sql += "description LIKE ?";
                break;
            default:
                sql += "category LIKE ? OR code LIKE ? OR description LIKE ?";
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + searchText + "%";
            if (column.equals("ALL")) {
                pstmt.setString(1, searchPattern);
                pstmt.setString(2, searchPattern);
                pstmt.setString(3, searchPattern);
            } else {
                pstmt.setString(1, searchPattern);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                tableModel.setRowCount(0);
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("category"),
                        rs.getString("code"),
                        rs.getString("description")
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding records: " + e.getMessage());
        }
    }
    
    public void searchAndDisplay(String searchText, String column) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Category");
        model.addColumn("Code");
        model.addColumn("Description");
        
        findAndDisplayRecords(searchText, column, model);
        
        for (int i = 0; i < model.getRowCount(); i++) {
            System.out.println(model.getValueAt(i, 0) + "\t" + 
                              model.getValueAt(i, 1) + "\t" + 
                              model.getValueAt(i, 2));
        }
    }
    
    public void displayAllRecords() {
        try (ResultSet rs = getRecords()) {
            System.out.println("Category\tCode\tDescription");
            System.out.println("----------------------------------");
            while (rs != null && rs.next()) {
                System.out.println(rs.getString("category") + "\t" + 
                                  rs.getString("code") + "\t" + 
                                  rs.getString("description"));
            }
        } catch (SQLException e) {
            System.err.println("Error displaying records: " + e.getMessage());
        }
    }

    public void clearTable() {
        String sql = "DELETE FROM lab_tests";
        executeSQL(sql);
        System.out.println("Cleared all records from the table");
    }
}