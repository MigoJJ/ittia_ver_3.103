package je.pense.doro.support.sqlite3_manager.lab.lablong;

import java.sql.*;

import je.pense.doro.entry.EntryDir;

/**
 * A class to create and populate a SQLite database for laboratory codes.
 */
public class CreateLabCodeDatabase {

    private static final String DB_URL = "jdbc:sqlite:" + EntryDir.HOME_DIR + "/chartplate/filecontrol/database/LabCodeFullDis.db";

    /**
     * Creates the 'labcodes' table in the database if it doesn't already exist.
     */
    public static void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS labcodes (" +
                "Category TEXT, " +
                "B_code TEXT, " +
                "Items TEXT, " +
                "unit TEXT, " +
                "comment TEXT" +
                ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Table 'labcodes' created successfully or already exists.");
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }

    /**
     * Adds sample laboratory data to the 'labcodes' table.
     */
    public static void addData() {
        String insertSQL = "INSERT INTO labcodes (Category, B_code, Items, unit, comment) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            addLabData(pstmt);
            System.out.println("Data added successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding data: " + e.getMessage());
        }
    }

    /**
     * Adds predefined sample lab data to the database.
     *
     * @param pstmt The PreparedStatement to execute the insertions.
     * @throws SQLException If an error occurs during data insertion.
     */
    private static void addLabData(PreparedStatement pstmt) throws SQLException {
        String[][] labData = {
            {"CBC", "D123", "Hemoglobin", "g/dL", "+alab"},
            {"CBC", "D124", "Hematocrit", "%", "+alab"},
            {"CBC", "D124", "White Blood Cells", "x10³/µL", "+alab"},
            {"CBC", "D124", "Platelets", "x10³/µL", "+alab"},
            {"Chemistry", "D124", "Glucose", "mg/dL", "+alab"},
            {"Chemistry", "D124", "Creatinine", "mg/dL", "+alab"},
            {"Lipid Panel", "D124", "Cholesterol", "mg/dL", "+alab"},
            {"Lipid Panel", "D124", "Triglycerides", "mg/dL", "+alab"}
        };

        for (String[] row : labData) {
            addRow(pstmt, row[0], row[1], row[2], row[3], row[4]);
        }
    }

    /**
     * Inserts a single row into the 'labcodes' table.
     *
     * @param pstmt    The PreparedStatement to execute the insertion.
     * @param category The category of the lab test (e.g., "CBC").
     * @param bcode    The B_code associated with the test (e.g., "D123").
     * @param items    The specific item being tested (e.g., "Hemoglobin").
     * @param unit     The unit of measurement (e.g., "g/dL").
     * @param comment  Additional comments (e.g., "+alab").
     * @throws SQLException If an error occurs during insertion.
     */
    private static void addRow(PreparedStatement pstmt, String category, String bcode, String items, String unit, String comment) throws SQLException {
        pstmt.setString(1, category);
        pstmt.setString(2, bcode);
        pstmt.setString(3, items);
        pstmt.setString(4, unit);
        pstmt.setString(5, comment);
        pstmt.executeUpdate();
    }

    /**
     * Removes duplicate entries from the 'labcodes' table based on the 'Items' column.
     * Keeps the first occurrence of each unique 'Items' value.
     */
    public static void removeDuplicates() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Create a temporary table for unique records
            stmt.execute("CREATE TABLE temp_labcodes (" +
                    "Category TEXT, " +
                    "B_code TEXT, " +
                    "Items TEXT, " +
                    "unit TEXT, " +
                    "comment TEXT" +
                    ");");

            // Insert unique records into the temporary table
            stmt.execute("INSERT INTO temp_labcodes (Category, B_code, Items, unit, comment) " +
                    "SELECT Category, B_code, Items, unit, comment " +
                    "FROM labcodes " +
                    "WHERE rowid IN (" +
                    "    SELECT MIN(rowid) " +
                    "    FROM labcodes " +
                    "    GROUP BY Items" +
                    ");");

            // Drop the original table and rename the temporary table
            stmt.execute("DROP TABLE IF EXISTS labcodes;");
            stmt.execute("ALTER TABLE temp_labcodes RENAME TO labcodes;");

            System.out.println("Duplicates removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error removing duplicates: " + e.getMessage());
        }
    }

    /**
     * Main method to initialize the database, add data, and remove duplicates.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        createTable();
        addData();
        removeDuplicates();
    }
}