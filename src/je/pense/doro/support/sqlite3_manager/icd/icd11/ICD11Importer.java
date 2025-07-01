package je.pense.doro.support.sqlite3_manager.icd.icd11;

import java.awt.BorderLayout;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import je.pense.doro.entry.EntryDir;

public class ICD11Importer extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(ICD11Importer.class.getName());
    private static final String DB_URL = "jdbc:sqlite:" +EntryDir.HOME_DIR + "/soap/assessment/icd_11/icd11.db";
    private static final String CSV_PATH = EntryDir.HOME_DIR + "/soap/assessment/icd_11/icdcodes.csv";
    private Connection conn;

    public ICD11Importer() {
        setTitle("ICD-11 Importer");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton importButton = new JButton("Import ICD-11 Data");
        importButton.addActionListener(e -> new Thread(() -> {
            try {
                connectDatabase();
                importCsvData(CSV_PATH);
                JOptionPane.showMessageDialog(this, "Import completed successfully!");
            } catch (Exception ex) {
                LOGGER.severe("Import error: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }).start());

        add(importButton, BorderLayout.CENTER);
        setVisible(true);
    }

    private void connectDatabase() throws SQLException {
        LOGGER.info("Connecting to database: " + DB_URL);
        conn = DriverManager.getConnection(DB_URL);
        try (Statement stmt = conn.createStatement()) { // try-with-resources for Statement
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS icd11 (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "Mark TEXT, " +
                    "Code TEXT NOT NULL, " +
                    "ICD11Name TEXT NOT NULL, " +
                    "Note TEXT)");
        }
    }

    private void importCsvData(String csvPath) throws SQLException, IOException {
        LOGGER.info("Importing CSV data from: " + csvPath);
        try (CSVReader csvReader = new CSVReader(new FileReader(csvPath)); // try-with-resources for CSVReader
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO icd11 (Mark, Code, ICD11Name, Note) VALUES (?, ?, ?, ?)")) {
            conn.setAutoCommit(false); // Start a transaction
            String[] data;
            boolean firstLine = true;
            int rowCount = 0;

            try {
				while ((data = csvReader.readNext()) != null) {
				    if (firstLine) {
				        firstLine = false;
				        continue;
				    }

				    LOGGER.info("Processing row: " + String.join(",", data));

				    if (data.length < 2) {
				        LOGGER.warning("Skipping invalid row with insufficient columns: " + String.join(",", data));
				        continue;
				    }

				    String mark = null;
				    String code = data[0].trim();
				    String icd11Name = data[1].trim();
				    String note = data.length > 2 && !data[2].isEmpty() ? data[2].trim() : null;

				    if (code.isEmpty() || icd11Name.isEmpty()) {
				        LOGGER.warning("Skipping row with empty Code or ICD11Name: " + String.join(",", data));
				        continue;
				    }

				    pstmt.setString(1, mark);
				    pstmt.setString(2, code);
				    pstmt.setString(3, icd11Name);
				    pstmt.setString(4, note);
				    pstmt.executeUpdate();
				    rowCount++;
				}
			} catch (CsvValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            conn.commit(); // Commit the transaction if all rows are processed successfully.
            LOGGER.info("Imported " + rowCount + " rows into icd11 table");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback the transaction in case of an error
                    LOGGER.severe("Transaction rolled back due to error: " + e.getMessage());
                } catch (SQLException ex) {
                    LOGGER.severe("Error rolling back transaction: " + ex.getMessage());
                }
            }
            throw e; // Re-throw the original SQLException to be caught by the caller.
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); //reset auto commit
                    conn.close(); // Close the connection in a finally block
                } catch (SQLException e) {
                    LOGGER.severe("Error closing database connection: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ICD11Importer::new);
    }
}
