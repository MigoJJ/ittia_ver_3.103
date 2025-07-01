package je.pense.doro.support.sqlite3_manager.icd.icd8;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import je.pense.doro.entry.EntryDir;

public class KCDDatabaseEditor {

    private static final String DB_DIR = EntryDir.HOME_DIR + "/soap/assessment/kcd8";
    private static final String DB_URL = "jdbc:sqlite:" + DB_DIR + "/kcd8db.db";

    public void addData(String code, String kDiseaseName, String eDiseaseName) {
        String sql = "INSERT INTO kcd8db(code, korean_name, english_name) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            pstmt.setString(2, kDiseaseName);
            pstmt.setString(3, eDiseaseName);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteData(String code) {
        String sql = "DELETE FROM kcd8db WHERE code = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Data deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "No data found with code: " + code, "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData(String code, String kDiseaseName, String eDiseaseName) {
        String sql = "UPDATE kcd8db SET korean_name = ?, english_name = ? WHERE code = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kDiseaseName);
            pstmt.setString(2, eDiseaseName);
            pstmt.setString(3, code);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Data updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "No data found with code: " + code, "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error updating data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}