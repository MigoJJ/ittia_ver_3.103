package je.pense.doro.support.sqlite3_manager.abbreviation.setfindedit;

import java.sql.Connection;			
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import je.pense.doro.entry.EntryDir;

public class AbbreviationExpander {

	  public static String replaceAbbreviations(String text, String dbURL) throws SQLException {
	    String expandedText = text;

	    // Connect to the database
	    try (Connection conn = DriverManager.getConnection(dbURL)) {
	      // Prepare a statement to select full text for each abbreviation
	      String sql = "SELECT full_text FROM Abbreviations WHERE abbreviation = ?";
	      PreparedStatement pstmt = conn.prepareStatement(sql);

	      // Scanner to iterate over words in the text
	      Scanner scanner = new Scanner(text);
	      while (scanner.hasNext()) {
	        String word = scanner.next();

	        // Check if the word is an abbreviation
	        pstmt.setString(1, word);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	          String fullText = rs.getString("full_text");
	          expandedText = expandedText.replace(word, fullText);
	        }
	        rs.close();
	      }
	      scanner.close();    }

	    return expandedText;
	  }

	  public static void main(String args) throws SQLException {
	    String text = args;
	    String dbURL = "jdbc:sqlite:" + EntryDir.HOME_DIR + "/chartplate/filecontrol/database/AbbFullDis.db";
	    String expandedText = replaceAbbreviations(text, dbURL);
	    System.out.println(expandedText); // Output: The meeting is To Be Determined. We expect an estimated time of arrival by noon.

	  }
	}


