package je.pense.doro.support.sqlite3_manager.abbreviation;

import java.awt.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

import je.pense.doro.entry.EntryDir;
import je.pense.doro.support.sqlite3_manager.abbreviation.setfindedit.DatabaseExtractStrings;
import je.pense.doro.support.sqlite3_manager.abbreviation.setfindedit.DatabaseSort;

public class MainScreen extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private static final String DB_URL = "jdbc:sqlite:" + EntryDir.HOME_DIR + "/chartplate/filecontrol/database/AbbFullDis.db";

    public MainScreen() {
        setupFrame();
        setupTable();
        setupButtons();
        createDatabaseTable();
        loadData();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("Database Interaction Screen");
        setSize(750, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
    }

    private void setupTable() {
        String[] columnNames = {"Abbreviation", "Full Text"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        Font customFont = new Font("Consolas", Font.BOLD, 11);
        table.setFont(customFont);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        setColumnWidths();
        setColumnAlignment();
        setColumnIndentation(table, 6);
    }

    private void setColumnWidths() {
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(500);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    private void setColumnAlignment() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private void setColumnIndentation(JTable table, int indentSpaces) {
        int indentPixels = indentSpaces * table.getFontMetrics(table.getFont()).charWidth('W');
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JLabel) {
                    JLabel label = (JLabel) c;
                    label.setBorder(BorderFactory.createEmptyBorder(0, indentPixels, 0, 0));
                }
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private void setupButtons() {
        JPanel southPanel = new JPanel();
        String[] buttonLabels = {"Add", "Delete", "Edit", "Find", "Exit", "Extract"};
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(e -> handleButtonClick(label));
            southPanel.add(button);
        }
        add(southPanel, BorderLayout.SOUTH);
    }

    private void handleButtonClick(String label) {
        switch (label) {
            case "Add": showAddDialog(); break;
            case "Delete": deleteRecord(); break;
            case "Edit": editRecord(); break;
            case "Find": showFindDialog(); break;
            case "Exit": dispose(); break;
            case "Extract":
                try {
                    DatabaseExtractStrings.main(null);
                } catch (IOException e) {
                    showError("Error extracting data: " + e.getMessage());
                }
                break;
        }
    }

    private void loadData() {
        String sql = "SELECT abbreviation, full_text FROM Abbreviations";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getString("abbreviation"), rs.getString("full_text")});
            }
        } catch (SQLException e) {
            showError("Error loading data: " + e.getMessage());
        }
    }

    private void createDatabaseTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Abbreviations (" +
                     "abbreviation TEXT PRIMARY KEY, " +
                     "full_text TEXT NOT NULL)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            showError("Error creating table: " + e.getMessage());
        }
    }

    private void showAddDialog() {
        JTextField abbreviationField = new JTextField(10);
        JTextField fullTextField = new JTextField(30);
        JPanel panel = createInputPanel(abbreviationField, fullTextField);
        if (JOptionPane.showConfirmDialog(null, panel, "Add New Entry", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            insertRecord(abbreviationField.getText(), fullTextField.getText());
        }
    }

    private void insertRecord(String abbreviation, String fullText) {
        if (abbreviation.isEmpty() || fullText.isEmpty()) {
            showError("Abbreviation and Full Text cannot be empty.");
            return;
        }
        String sql = "INSERT INTO Abbreviations (abbreviation, full_text) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, abbreviation);
            pstmt.setString(2, fullText);
            pstmt.executeUpdate();
            tableModel.addRow(new Object[]{abbreviation, fullText});
            JOptionPane.showMessageDialog(this, "Record added successfully!");
        } catch (SQLException e) {
            showError("Error inserting record: " + e.getMessage());
        }
    }

    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow >= 0) {
            // Convert view index to model index (necessary if sorting is applied)
            int modelRow = table.convertRowIndexToModel(selectedRow);
            String abbreviation = (String) tableModel.getValueAt(modelRow, 0);

            String sql = "DELETE FROM Abbreviations WHERE abbreviation = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, abbreviation);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Remove row from the table model
                    tableModel.removeRow(modelRow);
                    JOptionPane.showMessageDialog(this, "Record deleted successfully!");
                } else {
                    showError("No record found to delete. Please refresh the table.");
                }
            } catch (SQLException e) {
                showError("Error deleting record: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }


    private void editRecord() {
        int selectedRow = table.convertRowIndexToModel(table.getSelectedRow());
        if (selectedRow >= 0) {
            String abbreviation = (String) tableModel.getValueAt(selectedRow, 0);
            JTextField abbreviationField = new JTextField(abbreviation, 10);
            JTextField fullTextField = new JTextField((String) tableModel.getValueAt(selectedRow, 1), 30);
            JPanel panel = createInputPanel(abbreviationField, fullTextField);

            if (JOptionPane.showConfirmDialog(null, panel, "Edit Entry", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                String newAbbreviation = abbreviationField.getText().trim();
                String newFullText = fullTextField.getText().trim();
                updateRecord(selectedRow, abbreviation, newAbbreviation, newFullText);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
        }
    }

    private void updateRecord(int row, String oldAbbreviation, String newAbbreviation, String newFullText) {
        String sql = "UPDATE Abbreviations SET abbreviation = ?, full_text = ? WHERE abbreviation = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newAbbreviation);
            pstmt.setString(2, newFullText);
            pstmt.setString(3, oldAbbreviation);
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                tableModel.setValueAt(newAbbreviation, row, 0);
                tableModel.setValueAt(newFullText, row, 1);
                JOptionPane.showMessageDialog(this, "Record updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found with the given abbreviation.");
            }
        } catch (SQLException e) {
            showError("Error updating record: " + e.getMessage());
        }
    }


    private void showFindDialog() {
        JTextField searchText = new JTextField(30);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Search Text:"));
        panel.add(searchText);
        if (JOptionPane.showConfirmDialog(null, panel, "Find Record", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            findRecords(searchText.getText());
        }
    }

    private void findRecords(String searchText) {
        String sql = "SELECT abbreviation, full_text FROM Abbreviations WHERE abbreviation LIKE ? OR full_text LIKE ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchText + "%");
            pstmt.setString(2, "%" + searchText + "%");
            ResultSet rs = pstmt.executeQuery();
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{rs.getString("abbreviation"), rs.getString("full_text")});
            }
        } catch (SQLException e) {
            showError("Error finding records: " + e.getMessage());
        }
    }

    private JPanel createInputPanel(JTextField abbreviationField, JTextField fullTextField) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Abbreviation:"));
        panel.add(abbreviationField);
        panel.add(new JLabel("Full Text:"));
        panel.add(fullTextField);
        return panel;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainScreen::new);
    }
}