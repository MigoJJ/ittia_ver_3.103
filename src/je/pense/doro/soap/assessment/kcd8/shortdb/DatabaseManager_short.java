package je.pense.doro.soap.assessment.kcd8.shortdb; // << CHANGED PACKAGE DECLARATION

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import je.pense.doro.entry.EntryDir;

/**
 * Manages a "short" version of the KCD8 database (kcd8db_short.db).
 * This short version contains entries from an original KCD8 database (kcd8db.db)
 * where the classification code length is less than 6 characters.
 * This class handles the creation, population, and querying of this short database.
 * Data modification operations (insert, update, delete) are intentionally disabled
 * for this short database through this manager.
 */
public class DatabaseManager_short {

    // Note: DB_SUBDIR now reflects the path relative to EntryDir.homeDir,
    // not directly related to the new package structure of this class itself,
    // unless EntryDir.homeDir structure is also changing. Assuming it's a fixed data path.
    private static final String DB_DATA_SUBDIR = "/soap/assessment/kcd8"; // Path for database files
    private static final String DB_DIR = EntryDir.homeDir + DB_DATA_SUBDIR;

    // Constants for the "short" database
    private static final String SHORT_DB_FILENAME = "kcd8db_short.db";
    private static final String DB_URL_SHORT = "jdbc:sqlite:" + DB_DIR + "/shortdb/" + SHORT_DB_FILENAME;
    private static final String TABLE_NAME_SHORT = "kcd8db_short";

    // Constants for the original database
    private static final String ORIGINAL_DB_FILENAME = "kcd8db.db";
    private static final String DB_URL_ORIGINAL = "jdbc:sqlite:" + DB_DIR + "/" + ORIGINAL_DB_FILENAME;
    private static final String TABLE_NAME_ORIGINAL = "kcd8db"; // Assuming table name in original DB

    // Column Name Constants
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CODE = "code";
    private static final String COLUMN_KOREAN_NAME = "korean_name";
    private static final String COLUMN_ENGLISH_NAME = "english_name";

    /**
     * Constructs a DatabaseManager_short instance.
     * Initializes the short code database by creating its table (if it doesn't exist)
     * and populating it with data from the original KCD8 database.
     */
    public DatabaseManager_short() {
        createShortCodeDatabase();
    }

    /**
     * Orchestrates the creation and population of the short code database.
     * Calls methods to create the table and then populate it from the original database.
     */
    private void createShortCodeDatabase() {
        createTable();
        populateShortCodeDatabase();
    }

    /**
     * Creates the kcd8db_short table in the short database if it does not already exist.
     * The table stores classification codes, Korean names, and English names.
     */
    private void createTable() {
        String sql = String.format(
                "CREATE TABLE IF NOT EXISTS %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s TEXT UNIQUE,"
                + "%s TEXT,"
                + "%s TEXT"
                + ");",
                TABLE_NAME_SHORT, COLUMN_ID, COLUMN_CODE, COLUMN_KOREAN_NAME, COLUMN_ENGLISH_NAME);

        try (Connection conn = DriverManager.getConnection(DB_URL_SHORT);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Short code database table ready at " + DB_URL_SHORT);
        } catch (SQLException e) {
            System.err.println("Short code table creation error: " + e.getMessage());
        }
    }

    /**
     * Populates the kcd8db_short table with data from the original kcd8db table.
     * It copies entries where the length of the 'code' is less than 6.
     * This method uses SQLite's ATTACH DATABASE command to access the original database.
     */
    private void populateShortCodeDatabase() {
        String originalDbFilePath = DB_DIR + "/" + ORIGINAL_DB_FILENAME;
        // Basic escaping for path in SQL literal, though often handled by JDBC for standard paths
        String escapedOriginalDbFilePath = originalDbFilePath.replace("'", "''");

        String attachSql = "ATTACH DATABASE '" + escapedOriginalDbFilePath + "' AS original_db;";
        String insertSql = String.format(
            "INSERT OR IGNORE INTO %s (%s, %s, %s) " +
            "SELECT %s, %s, %s FROM original_db.%s WHERE LENGTH(%s) < 6;",
            TABLE_NAME_SHORT, COLUMN_CODE, COLUMN_KOREAN_NAME, COLUMN_ENGLISH_NAME,
            COLUMN_CODE, COLUMN_KOREAN_NAME, COLUMN_ENGLISH_NAME, TABLE_NAME_ORIGINAL, COLUMN_CODE
        );
        String detachSql = "DETACH DATABASE original_db;";

        try (Connection conn = DriverManager.getConnection(DB_URL_SHORT);
             Statement stmt = conn.createStatement()) {

            stmt.execute(attachSql);
            System.out.println("Original database ('" + originalDbFilePath + "') attached successfully as 'original_db'.");

            try {
                stmt.execute(insertSql);
                System.out.println("'" + TABLE_NAME_SHORT + "' populated successfully from 'original_db." + TABLE_NAME_ORIGINAL + "'.");
            } finally {
                // Ensure DETACH happens even if INSERT fails but ATTACH succeeded
                stmt.execute(detachSql);
                System.out.println("'original_db' detached successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Short code database population/attachment error: " + e.getMessage());
            // For more detailed debugging, consider: e.printStackTrace();
        }
    }

    /**
     * Retrieves the English name for a given code from the short database.
     *
     * @param code The classification code.
     * @return The English name, or null if not found or an error occurs.
     */
    public String getEnglishNameByCode(String code) {
        return getEnglishNameByCode(code, DB_URL_SHORT);
    }

    /**
     * Retrieves the English name for a given code from a specified database URL.
     * This method assumes the target database at dbURL has a table named {@value #TABLE_NAME_SHORT}
     * with the expected schema.
     *
     * @param code The classification code.
     * @param dbURL The JDBC URL of the SQLite database to query.
     * @return The English name, or null if not found or an error occurs.
     */
    public String getEnglishNameByCode(String code, String dbURL) {
        String sql = String.format("SELECT %s FROM %s WHERE %s = ?", COLUMN_ENGLISH_NAME, TABLE_NAME_SHORT, COLUMN_CODE);
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(COLUMN_ENGLISH_NAME);
                }
            }
        } catch (SQLException e) {
            System.err.println("Fetch error for English name of code " + code + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves the Korean name for a given code from the short database.
     *
     * @param code The classification code.
     * @return The Korean name, or null if not found or an error occurs.
     */
    public String getKoreanNameByCode(String code) {
        return getKoreanNameByCode(code, DB_URL_SHORT);
    }

    /**
     * Retrieves the Korean name for a given code from a specified database URL.
     * This method assumes the target database at dbURL has a table named {@value #TABLE_NAME_SHORT}
     * with the expected schema.
     *
     * @param code The classification code.
     * @param dbURL The JDBC URL of the SQLite database to query.
     * @return The Korean name, or null if not found or an error occurs.
     */
    public String getKoreanNameByCode(String code, String dbURL) {
        String sql = String.format("SELECT %s FROM %s WHERE %s = ?", COLUMN_KOREAN_NAME, TABLE_NAME_SHORT, COLUMN_CODE);
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(COLUMN_KOREAN_NAME);
                }
            }
        } catch (SQLException e) {
            System.err.println("Fetch error for Korean name of code " + code + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Checks if a given code exists in the short database.
     *
     * @param code The classification code to check.
     * @return True if the code exists, false otherwise.
     */
    public boolean codeExists(String code) {
        return codeExists(code, DB_URL_SHORT);
    }

    /**
     * Checks if a given code exists in a table named {@value #TABLE_NAME_SHORT}
     * within the database specified by dbURL.
     *
     * @param code The classification code to check.
     * @param dbURL The JDBC URL of the SQLite database to query.
     * @return True if the code exists, false otherwise.
     */
	public boolean codeExists(String code, String dbURL) {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = ?", TABLE_NAME_SHORT, COLUMN_CODE);
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Check code existence error for code " + code + ": " + e.getMessage());
        }
        return false;
    }

    /**
     * This operation is not supported for the short database.
     * Prints an error message to System.err.
     */
    public void insertData(String code, String kDiseaseName, String eDiseaseName) {
		System.err.println("Insert Data (3 params) method not available for short db. Please use original kcd8db manager.");
    }

    /**
     * This operation is not supported for the short database.
     * Prints an error message to System.err.
     */
    public void insertData(String code, String description) {
		System.err.println("Insert Data (2 params) method not available for short db. Please use original kcd8db manager.");
    }

    /**
     * This operation is not supported for the short database.
     * Prints an error message to System.err.
     */
    public void updateData(String code, String kDiseaseName, String eDiseaseName) {
		System.err.println("Update Data method not available for short db. Please use original kcd8db manager.");
    }

    /**
     * This operation is not supported for the short database.
     * Prints an error message to System.err.
     */
    public void deleteData(String code) {
		System.err.println("Delete Data method not available for short db. Please use original kcd8db manager.");
    }

    /**
     * Main method for basic testing or initialization.
     * Creates an instance of DatabaseManager_short, which triggers the
     * creation and population of the short database.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Instantiate the manager to trigger database setup
        new DatabaseManager_short();
        System.out.println("DatabaseManager_short initialized. Check console for messages.");

        // Example usage (optional, for testing):
        // DatabaseManager_short dbManager = new DatabaseManager_short();
        // String testCode = "A01"; // Example code that might be short
        // if (dbManager.codeExists(testCode)) {
        //     System.out.println("Code " + testCode + " exists.");
        //     System.out.println("Korean Name: " + dbManager.getKoreanNameByCode(testCode));
        //     System.out.println("English Name: " + dbManager.getEnglishNameByCode(testCode));
        // } else {
        //     System.out.println("Code " + testCode + " does not exist in the short database or an error occurred.");
        // }
    }
}