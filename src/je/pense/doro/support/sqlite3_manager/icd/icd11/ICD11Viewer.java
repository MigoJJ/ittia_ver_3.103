package je.pense.doro.support.sqlite3_manager.icd.icd11;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import je.pense.doro.entry.EntryDir;

import java.awt.*;
import java.sql.*;
import java.util.logging.Logger;

public class ICD11Viewer extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(ICD11Viewer.class.getName());
    private static final String DB_URL = "jdbc:sqlite:" + EntryDir.HOME_DIR + "/chartplate/filecontrol/database/icd11.db";
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public ICD11Viewer() {
        setTitle("ICD-11 Database Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Initialize table
        tableModel = new DefaultTableModel(new String[]{"ID", "Mark", "Code", "ICD11Name", "Note"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        setColumnWidths();

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        // Add components to frame
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Load initial data
        loadTableData("");

        // Search button action
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            LOGGER.info("Searching for: " + searchText);
            loadTableData(searchText);
        });

        // Search on Enter key in search field
        searchField.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            LOGGER.info("Enter pressed, searching for: " + searchText);
            loadTableData(searchText);
        });

        // Refresh button action
        refreshButton.addActionListener(e -> {
            LOGGER.info("Refreshing table data");
            searchField.setText("");
            loadTableData("");
        });

        setVisible(true);
    }

    private void setColumnWidths() {
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(50);  // Mark
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Code
        table.getColumnModel().getColumn(3).setPreferredWidth(300); // ICD11Name
        table.getColumnModel().getColumn(4).setPreferredWidth(200); // Note
    }

    private void loadTableData(String searchText) {
        tableModel.setRowCount(0); // Clear existing data
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM icd11";
            if (!searchText.isEmpty()) {
                query += " WHERE LOWER(Code) LIKE ? OR LOWER(ICD11Name) LIKE ?";
            }
            PreparedStatement pstmt = conn.prepareStatement(query);
            if (!searchText.isEmpty()) {
                String searchPattern = "%" + searchText.toLowerCase() + "%";
                pstmt.setString(1, searchPattern);
                pstmt.setString(2, searchPattern);
            }
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("ID"),
                    rs.getString("Mark"),
                    rs.getString("Code"),
                    rs.getString("ICD11Name"),
                    rs.getString("Note")
                });
            }
            LOGGER.info("Loaded " + tableModel.getRowCount() + " rows");
        } catch (SQLException ex) {
            LOGGER.severe("Error loading data: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ICD11Viewer::new);
    }
}