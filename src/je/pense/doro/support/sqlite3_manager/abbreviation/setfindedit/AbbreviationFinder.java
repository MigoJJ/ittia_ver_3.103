package je.pense.doro.support.sqlite3_manager.abbreviation.setfindedit;

import java.sql.*;

import je.pense.doro.entry.EntryDir;

public class AbbreviationFinder {
//    private String dbURL = "jdbc:sqlite:/home/migowj/git/ittia_ver_4.01/src/je/panse/doro/support/sqlite3/abbreviation/AbbFullDis.db";
    private static String dbURL = "jdbc:sqlite:" + EntryDir.HOME_DIR + "/chartplate/filecontrol/database/AbbFullDis.db";

    public String findFullTextByAbbreviation(String abbreviation) {
        String fullText = null;
        String sql = "SELECT full_text FROM Abbreviations WHERE abbreviation = ?";
        
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, abbreviation);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                fullText = rs.getString("full_text");
            } else {
                fullText = "No match found"; // or return null, based on how you want to handle no matches
            }
        } catch (SQLException e) {
            System.err.println("Error finding abbreviation: " + e.getMessage());
            return "Database error"; // or rethrow, or handle differently
        }
        
        return fullText;
    }

    public static void main(String[] args) {
        AbbreviationFinder finder = new AbbreviationFinder();
        String abbreviation = ":dr"; // Example abbreviation to search
        String fullText = finder.findFullTextByAbbreviation(abbreviation);
        System.out.println("Full Text: " + fullText);
    }
}
