package je.pense.doro.soap.assessment.kcd8.shortdb;

import je.pense.doro.entry.EntryDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a "short" version of the KCD8 database (kcd8db_short.db).
 * Handles creation, population, and querying of this short database.
 */
public class DatabaseManager_short {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager_short.class);

    private static final String KCD8_BASE_DIR = Paths.get(EntryDir.homeDir, "soap", "assessment", "kcd8").toString();
    private static final String ORIGINAL_DB_FILENAME = "kcd8db.db";
    private static final String ORIGINAL_DB_PATH = Paths.get(KCD8_BASE_DIR, ORIGINAL_DB_FILENAME).toString();
    private static final String TABLE_NAME_ORIGINAL = "kcd8db";
    private static final String SHORT_DB_DIR = Paths.get(KCD8_BASE_DIR, "shortdb").toString();
    private static final String SHORT_DB_FILENAME = "kcd8db_short.db";
    private static final String SHORT_DB_PATH = Paths.get(SHORT_DB_DIR, SHORT_DB_FILENAME).toString();
    private static final String DB_URL_SHORT = "jdbc:sqlite:" + SHORT_DB_PATH;
    private static final String TABLE_NAME_SHORT = "kcd8db_short";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CODE = "code";
    private static final String COLUMN_KOREAN_NAME = "korean_name";
    private static final String COLUMN_ENGLISH_NAME = "english_name";

    public DatabaseManager_short() {
        createShortCodeDatabase();
    }

    private void createShortCodeDatabase() {
        try {
            Files.createDirectories(Paths.get(SHORT_DB_DIR));
        } catch (IOException e) {
            logger.error("FATAL: Could not create directory for short database at {}", SHORT_DB_DIR, e);
            return;
        }
        createTable();
        populateShortCodeDatabase();
    }

    private void createTable() {
        String sql = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT UNIQUE NOT NULL," +
                        "%s TEXT," +
                        "%s TEXT" +
                        ");",
                TABLE_NAME_SHORT, COLUMN_ID, COLUMN_CODE, COLUMN_KOREAN_NAME, COLUMN_ENGLISH_NAME);

        try (Connection conn = DriverManager.getConnection(DB_URL_SHORT);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            logger.info("Short code database table is ready at {}", DB_URL_SHORT);
        } catch (SQLException e) {
            logger.error("Short code table creation error", e);
        }
    }

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
            try {
                int rowsAffected = stmt.executeUpdate(insertSql);
                logger.info("Populated '{}' with {} new rows from 'original_db.{}'.", TABLE_NAME_SHORT, rowsAffected, TABLE_NAME_ORIGINAL);
            } finally {
                stmt.execute(detachSql);
            }
        } catch (SQLException e) {
            logger.error("Short code database population error", e);
        }
    }

    public String getEnglishNameByCode(String code) {
        return getValueByCode(code, COLUMN_ENGLISH_NAME, DB_URL_SHORT);
    }

    public String getKoreanNameByCode(String code) {
        return getValueByCode(code, COLUMN_KOREAN_NAME, DB_URL_SHORT);
    }

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

    public boolean codeExists(String code) {
        return codeExists(code, DB_URL_SHORT);
    }

    public boolean codeExists(String code, String dbURL) {
        String sql = String.format("SELECT 1 FROM %s WHERE %s = ? LIMIT 1", TABLE_NAME_SHORT, COLUMN_CODE);
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            logger.error("Error checking existence for code '{}'", code, e);
        }
        return false;
    }

    // --- Methods for KCDShortViewer ---
    public List<KcdCodeEntry> getAllShortCodes() {
        List<KcdCodeEntry> entries = new ArrayList<>();
        String sql = String.format("SELECT %s, %s, %s, %s FROM %s ORDER BY %s",
                COLUMN_ID, COLUMN_CODE, COLUMN_KOREAN_NAME, COLUMN_ENGLISH_NAME, TABLE_NAME_SHORT, COLUMN_ID);
        try (Connection conn = DriverManager.getConnection(DB_URL_SHORT);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                entries.add(new KcdCodeEntry(
                        rs.getInt(COLUMN_ID),
                        rs.getString(COLUMN_CODE),
                        rs.getString(COLUMN_KOREAN_NAME),
                        rs.getString(COLUMN_ENGLISH_NAME)
                ));
            }
        } catch (SQLException e) {
            logger.error("Error fetching all short codes", e);
        }
        return entries;
    }

    public List<KcdCodeEntry> searchShortCodes(String query) {
        List<KcdCodeEntry> entries = new ArrayList<>();
        String sql = String.format(
                "SELECT %s, %s, %s, %s FROM %s WHERE %s LIKE ? OR %s LIKE ? OR %s LIKE ? ORDER BY %s",
                COLUMN_ID, COLUMN_CODE, COLUMN_KOREAN_NAME, COLUMN_ENGLISH_NAME, TABLE_NAME_SHORT,
                COLUMN_CODE, COLUMN_KOREAN_NAME, COLUMN_ENGLISH_NAME, COLUMN_ID);
        String likeQuery = "%" + query + "%";
        try (Connection conn = DriverManager.getConnection(DB_URL_SHORT);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, likeQuery);
            pstmt.setString(2, likeQuery);
            pstmt.setString(3, likeQuery);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entries.add(new KcdCodeEntry(
                            rs.getInt(COLUMN_ID),
                            rs.getString(COLUMN_CODE),
                            rs.getString(COLUMN_KOREAN_NAME),
                            rs.getString(COLUMN_ENGLISH_NAME)
                    ));
                }
            }
        } catch (SQLException e) {
            logger.error("Error searching short codes for query '{}'", query, e);
        }
        return entries;
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

    public static void main(String[] args) {
        logger.info("Initializing DatabaseManager_short...");
        DatabaseManager_short dbManager = new DatabaseManager_short();
        logger.info("Initialization complete.");
        String testCode = "A01";
        if (dbManager.codeExists(testCode)) {
            logger.info("Code {} exists.", testCode);
            logger.info("  -> Korean Name: {}", dbManager.getKoreanNameByCode(testCode));
            logger.info("  -> English Name: {}", dbManager.getEnglishNameByCode(testCode));
        } else {
            logger.warn("Code {} does not exist in the short database or an error occurred.", testCode);
        }
    }

    // Minimal record for KcdCodeEntry
    public static record KcdCodeEntry(int id, String code, String koreanName, String englishName) {}
}
