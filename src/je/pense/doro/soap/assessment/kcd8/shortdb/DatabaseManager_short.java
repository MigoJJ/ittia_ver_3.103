package je.pense.doro.soap.assessment.kcd8.shortdb;

import je.pense.doro.entry.EntryDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * NOTE on SLF4J Multiple Bindings Warning:
 * The warning "SLF4J: Class path contains multiple SLF4J bindings" indicates a project
 * dependency issue, likely in your pom.xml or build.gradle file. You have both
 * 'logback-classic' and 'slf4j-log4j12' on the classpath. To fix this, you should
 * choose one logging implementation and exclude the other from your project's dependencies.
 */

/**
 * Manages a "short" version of the KCD8 database (kcd8db_short.db).
 * This short version contains entries from an original KCD8 database (kcd8db.db)
 * where the classification code length is less than 6 characters.
 * This class handles the creation, population, and querying of this short database.
 */
public class DatabaseManager_short {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager_short.class);

    // --- Path and DB Configuration ---
    // The base directory for all KCD8-related database files.
    private static final String KCD8_BASE_DIR = Paths.get(EntryDir.homeDir, "soap", "assessment", "kcd8").toString();

    // Constants for the original database
    private static final String ORIGINAL_DB_FILENAME = "kcd8db.db";
    private static final String ORIGINAL_DB_PATH = Paths.get(KCD8_BASE_DIR, ORIGINAL_DB_FILENAME).toString();
    private static final String TABLE_NAME_ORIGINAL = "kcd8db";

    // Constants for the "short" database
    private static final String SHORT_DB_DIR = Paths.get(KCD8_BASE_DIR, "shortdb").toString();
    private static final String SHORT_DB_FILENAME = "kcd8db_short.db";
    private static final String SHORT_DB_PATH = Paths.get(SHORT_DB_DIR, SHORT_DB_FILENAME).toString();
    private static final String DB_URL_SHORT = "jdbc:sqlite:" + SHORT_DB_PATH;
    private static final String TABLE_NAME_SHORT = "kcd8db_short";

    // Column Name Constants
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CODE = "code";
    private static final String COLUMN_KOREAN_NAME = "korean_name";
    private static final String COLUMN_ENGLISH_NAME = "english_name";

    /**
     * Constructs a DatabaseManager_short instance.
     * Initializes the short code database by ensuring its directory exists,
     * creating its table (if needed), and populating it from the original KCD8 database.
     */
    public DatabaseManager_short() {
        createShortCodeDatabase();
    }

    /**
     * Orchestrates the creation and population of the short code database.
     */
    private void createShortCodeDatabase() {
        // **FIX:** Ensure the directory for the short database exists before connecting.
        try {
            Files.createDirectories(Paths.get(SHORT_DB_DIR));
        } catch (IOException e) {
            logger.error("FATAL: Could not create directory for short database at {}", SHORT_DB_DIR, e);
            return; // Cannot proceed if directory creation fails.
        }

        createTable();
        populateShortCodeDatabase();
    }

    /**
     * Creates the kcd8db_short table in the short database if it does not already exist.
     */
    private void createTable() {
        String sql = String.format(
                "CREATE TABLE IF NOT EXISTS %s ("
                + "%s INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "%s TEXT UNIQUE NOT NULL,"
                + "%s TEXT,"
                + "%s TEXT"
                + ");",
                TABLE_NAME_SHORT, COLUMN_ID, COLUMN_CODE, COLUMN_KOREAN_NAME, COLUMN_ENGLISH_NAME);

        try (Connection conn = DriverManager.getConnection(DB_URL_SHORT);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            logger.info("Short code database table is ready at {}", DB_URL_SHORT);
        } catch (SQLException e) {
            logger.error("Short code table creation error", e);
        }
    }

    /**
     * Populates the kcd8db_short table with data from the original kcd8db table
     * using SQLite's ATTACH DATABASE command. It only copies entries where the
     * code length is less than 6.
     */
    private void populateShortCodeDatabase() {
        if (!Files.exists(Paths.get(ORIGINAL_DB_PATH))) {
            logger.error("Cannot populate short DB: Original database not found at {}", ORIGINAL_DB_PATH);
            return;
        }

        String attachSql = "ATTACH DATABASE '" + ORIGINAL_DB_PATH.replace("'", "''") + "' AS original_db;";
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
            logger.debug("Attached original database successfully.");

            try {
                int rowsAffected = stmt.executeUpdate(insertSql);
                logger.info("Populated '{}' with {} new rows from 'original_db.{}'.", TABLE_NAME_SHORT, rowsAffected, TABLE_NAME_ORIGINAL);
            } finally {
                // Ensure DETACH happens even if INSERT fails
                stmt.execute(detachSql);
                logger.debug("Detached original database successfully.");
            }
        } catch (SQLException e) {
            logger.error("Short code database population error", e);
        }
    }

    /**
     * Retrieves the English name for a given code from the short database.
     */
    public String getEnglishNameByCode(String code) {
        return getValueByCode(code, COLUMN_ENGLISH_NAME, DB_URL_SHORT);
    }

    /**
     * Retrieves the Korean name for a given code from the short database.
     */
    public String getKoreanNameByCode(String code) {
        return getValueByCode(code, COLUMN_KOREAN_NAME, DB_URL_SHORT);
    }

    /**
     * Generic helper to retrieve a single string value from a specific column based on a code.
     *
     * @param code       The classification code to look up.
     * @param columnName The name of the column to retrieve the value from.
     * @param dbURL      The JDBC URL of the database to query.
     * @return The string value, or null if not found or an error occurs.
     */
    private String getValueByCode(String code, String columnName, String dbURL) {
        String sql = String.format("SELECT %s FROM %s WHERE %s = ?", columnName, TABLE_NAME_SHORT, COLUMN_CODE);
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(columnName);
                }
            }
        } catch (SQLException e) {
            logger.error("Error fetching '{}' for code '{}'", columnName, code, e);
        }
        return null;
    }

    /**
     * Checks if a given code exists in the short database.
     */
    public boolean codeExists(String code) {
        return codeExists(code, DB_URL_SHORT);
    }

    /**
     * Checks if a given code exists in the specified database.
     */
    public boolean codeExists(String code, String dbURL) {
        String sql = String.format("SELECT 1 FROM %s WHERE %s = ? LIMIT 1", TABLE_NAME_SHORT, COLUMN_CODE);
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if a row was found
            }
        } catch (SQLException e) {
            logger.error("Error checking existence for code '{}'", code, e);
        }
        return false;
    }

    // --- Disabled Operations ---
    public void insertData(String code, String kDiseaseName, String eDiseaseName) {
        logger.warn("Insert operation is not supported for the read-only short database.");
    }
    public void updateData(String code, String kDiseaseName, String eDiseaseName) {
        logger.warn("Update operation is not supported for the read-only short database.");
    }
    public void deleteData(String code) {
        logger.warn("Delete operation is not supported for the read-only short database.");
    }

    /**
     * Main method for basic testing or initialization.
     */
    public static void main(String[] args) {
        logger.info("Initializing DatabaseManager_short...");
        DatabaseManager_short dbManager = new DatabaseManager_short();
        logger.info("Initialization complete.");

        // Example usage for testing:
        String testCode = "A01"; // An example of a short code
        if (dbManager.codeExists(testCode)) {
            logger.info("Code {} exists.", testCode);
            logger.info("  -> Korean Name: {}", dbManager.getKoreanNameByCode(testCode));
            logger.info("  -> English Name: {}", dbManager.getEnglishNameByCode(testCode));
        } else {
            logger.warn("Code {} does not exist in the short database or an error occurred.", testCode);
        }
    }
}