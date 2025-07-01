package je.pense.doro.support.sqlite3_manager.lab.labshort;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JavalabtestsDBManagerGUI extends JFrame {
    private JavalabtestsDBManager dbManager;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JTextField categoryField, codeField, descriptionField, searchField;
    private JComboBox<String> searchColumnCombo;

    public JavalabtestsDBManagerGUI() {
        dbManager = new JavalabtestsDBManager();
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setTitle("Laboratory Tests Database Manager");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Create components
        JPanel inputPanel = createInputPanel();
        JPanel buttonPanel = createButtonPanel();
        JPanel searchPanel = createSearchPanel();
        JScrollPane tableScrollPane = createTable();

        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Test Information"));

        panel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        panel.add(categoryField);

        panel.add(new JLabel("Code:"));
        codeField = new JTextField();
        panel.add(codeField);

        panel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        panel.add(descriptionField);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addRecord());

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> updateRecord());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteRecord());

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> dispose());

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(quitButton);

        return panel;
    }
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Search"));

        searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchRecords());

        String[] columns = {"ALL", "category", "code", "description"};
        searchColumnCombo = new JComboBox<>(columns);

        JPanel searchInputPanel = new JPanel(new BorderLayout(5, 5));
        searchInputPanel.add(searchField, BorderLayout.CENTER);
        searchInputPanel.add(searchButton, BorderLayout.EAST);

        panel.add(searchColumnCombo, BorderLayout.WEST);
        panel.add(searchInputPanel, BorderLayout.CENTER);

        return panel;
    }

    private JScrollPane createTable() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Category");
        tableModel.addColumn("Code");
        tableModel.addColumn("Description");

        dataTable = new JTable(tableModel);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add TableRowSorter for sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        dataTable.setRowSorter(sorter);

        // Set default sort by Category (ascending)
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING)); // Category column
        sorter.setSortKeys(sortKeys);

        dataTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = dataTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int modelRow = dataTable.convertRowIndexToModel(selectedRow);
                    String category = tableModel.getValueAt(modelRow, 0).toString();
                    String code = tableModel.getValueAt(modelRow, 1).toString();
                    String description = tableModel.getValueAt(modelRow, 2).toString();
                    // Populate input fields
                    categoryField.setText(category);
                    codeField.setText(code);
                    descriptionField.setText(description);
                    // Send to GDSEMR_frame
                    try {
                        String cellValue = code + "\t" + description;
                        GDSEMR_frame.setTextAreaText(8, "\n... " + cellValue + "[ :cd ]\n ...");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error sending data to GDSEMR_frame: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        return new JScrollPane(dataTable);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        try (ResultSet rs = dbManager.getRecords()) {
            while (rs != null && rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("category"),
                    rs.getString("code"),
                    rs.getString("description")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void addRecord() {
        String category = categoryField.getText().trim();
        String code = codeField.getText().trim();
        String description = descriptionField.getText().trim();

        if (category.isEmpty() || code.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category and Code are required fields",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        dbManager.insertRecord(category, code, description);
        loadData();
        clearFields();
    }

    private void updateRecord() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a record to update",
                    "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = dataTable.convertRowIndexToModel(selectedRow);
        String oldCategory = tableModel.getValueAt(modelRow, 0).toString();
        String oldCode = tableModel.getValueAt(modelRow, 1).toString();
        String newCategory = categoryField.getText().trim();
        String newCode = codeField.getText().trim();
        String description = descriptionField.getText().trim();

        if (newCategory.isEmpty() || newCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Category and Code are required fields",
                    "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        dbManager.updateRecord(oldCategory, oldCode, newCategory, newCode, description);
        loadData();
        clearFields();
    }

    private void deleteRecord() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete",
                    "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this record?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int modelRow = dataTable.convertRowIndexToModel(selectedRow);
            String category = tableModel.getValueAt(modelRow, 0).toString();
            String code = tableModel.getValueAt(modelRow, 1).toString();
            dbManager.deleteRecord(category, code);
            loadData();
            clearFields();
        }
    }
    private void searchRecords() {
        String searchText = searchField.getText().trim();
        String column = (String) searchColumnCombo.getSelectedItem();

        if (searchText.isEmpty()) {
            loadData();
            return;
        }

        dbManager.findAndDisplayRecords(searchText, column, tableModel);
    }

    private void clearFields() {
        categoryField.setText("");
        codeField.setText("");
        descriptionField.setText("");
        dataTable.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new JavalabtestsDBManagerGUI().setVisible(true);
        });
    }
}



