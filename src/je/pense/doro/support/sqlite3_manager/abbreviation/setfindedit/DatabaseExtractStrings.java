package je.pense.doro.support.sqlite3_manager.abbreviation.setfindedit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import je.pense.doro.entry.EntryDir;

public class DatabaseExtractStrings {

    private static final String DB_URL = "jdbc:sqlite:" + EntryDir.HOME_DIR + "/chartplate/filecontrol/database/AbbFullDis.db";
    private static final String TABLE_NAME = "Abbreviations";
    private static final String COLUMN_TO_SORT = "abbreviation";

    public static void main(String[] args) throws IOException {
        DatabaseExtractStrings extractor = new DatabaseExtractStrings();
        extractor.extractAndSaveData();
        extractor.sortAlphabetically();
    }

    public void extractAndSaveData() throws IOException {
        String[][] data = extractData();
        saveToFile(data, "extracteddata.txt");
        System.out.println("Data saved to extracteddata.txt");
    }

    private void saveToFile(String[][] data, String filename) throws IOException {
        String filePath = EntryDir.HOME_DIR + "/chartplate/filecontrol/database/" + filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] entry : data) {
                writer.write("replacements.put(\"" + entry[0] + "\", \"" + entry[1] + "\");\n");
            }
        }
    }

    private String[][] extractData() {
        List<String[]> dataList = new ArrayList<>();
        String sql = "SELECT abbreviation, full_text FROM " + TABLE_NAME + " ORDER BY " + COLUMN_TO_SORT + " ASC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String abbreviation = rs.getString("abbreviation");
                String fullText = rs.getString("full_text");
                dataList.add(new String[]{abbreviation, fullText});
            }

        } catch (SQLException e) {
            System.err.println("Error extracting data: " + e.getMessage());
        }

        return dataList.toArray(new String[0][]);
    }

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
}