package je.pense.doro.support.sqlite3_manager.lab.labshort;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import je.pense.doro.entry.EntryDir;

public class InsertDataToSQLite {

    private static final String DB_URL = "jdbc:sqlite:" + EntryDir.HOME_DIR +"/fourgate/n_laboratorytest/frequent/javalabtests.db";
    private static final String[][] TEST_DATA = {
            {"Blood Type", "B2001, D1502003", "ABO blood type test"},
            {"Blood Type", "B2021, D1512003", "Rh type test"},
            {"Liver Function", "D1820003", "Direct bilirubin test"},
            {"Pancreatic Function", "C3430", "C-peptide test"},
            {"Pancreatic Function", "3410", "Insulin test"},
            {"Hormones", "C3480", "LH test"},
            {"Hormones", "C3500", "FSH test"},
            {"Hormones", "C3260", "E2 test"},
            {"Hormones", "D1640", "Progesterone test"},
            {"Hormones", "D3420005", "GH test"},
            {"Hormones", "C7351", "Prolactin test"},
            {"Electrolytes", "C3796", "Ionized-calcium test"},
            {"Hormones", "Cx232", "Free-testosterone test"},
            {"Hormones", "D3710050", "Free-testosterone test"},
            {"Hormones", "D3502143", "Free-Cortisol test"},
            {"Liver Function", "C4872", "Anti HCV AB test"},
            {"Liver Function", "C4854006", "HBV DNA test"},
            {"Liver Function", "+hp4", "H. Pylori 4-regimen test"},
            {"Infectious Diseases", "052400041", "Prolia"},
            {"Infectious Diseases", "C6095006", "Influenza test"},
            {"Infectious Diseases", "D6620", "COVID-19 test"},
            {"Vaccines", "Td", "Tetanus-diphtheria vaccine"},
            {"Vaccines", "Menactra", "Meningococcal conjugate vaccine"},
            {"Vaccines", "650003220", "Shingrix vaccine"},
            {"Infectious Diseases", "14912", "HIV Ag/Ab (combo) test"},
            {"Infectious Diseases", "C4854006, B1810", "HBV DNA test [ B1810 ]"},
            {"Diseases", "N94.6", "Dysmenorrhea 월경통"},
            {"Diseases", "O224", "Gestational diabetes 임신성 당뇨병"},
            {"Diseases", "FZ811", "Libre 연속혈당측정기"},
            {"Diseases", "D126", "Colonic polyp 대장 용종"},
            {"Diseases", "E041", "Benign thyroid nodule"},
            {"Diseases", "B1810", "HBV infection"},
    };

    public static void main(String[] args) {
        insertData();
    }

    public static void insertData() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("Connected to the database.");
                createTable(conn); // Create table if it doesn't exist
                insertTestData(conn);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS lab_tests ("
                + "category TEXT NOT NULL,"
                + "code TEXT NOT NULL,"
                + "description TEXT NOT NULL"
                + ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created or already exists.");
        } catch (SQLException e) {
             System.err.println("Table Creation Error: " + e.getMessage());
             throw e; // re-throw the exception to be caught by the caller.
        }

    }

    private static void insertTestData(Connection conn) {
        String sql = "INSERT INTO lab_tests (category, code, description) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (String[] row : TEST_DATA) {
                pstmt.setString(1, row[0]);
                pstmt.setString(2, row[1]);
                pstmt.setString(3, row[2]);
                pstmt.executeUpdate();
            }
            System.out.println("Data inserted successfully.");
        } catch (SQLException e) {
            System.err.println("Data Insertion Error: " + e.getMessage());
        }
    }
}
