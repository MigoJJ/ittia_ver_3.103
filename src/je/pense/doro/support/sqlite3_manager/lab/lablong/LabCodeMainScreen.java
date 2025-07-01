package je.pense.doro.support.sqlite3_manager.lab.lablong;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import je.pense.doro.GDSEMR_frame;

/**
 * A Swing-based GUI for managing laboratory codes in a SQLite database.
 */
public class LabCodeMainScreen extends JFrame implements ActionListener {

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField searchField;
    private JPanel southPanel;
    private LabDatabaseModel model;

    /**
     * Constructs the LabCodeMainScreen, initializes the database, and sets up the UI.
     */
    public LabCodeMainScreen() {
        model = new LabDatabaseModel();
        model.createDatabaseTable();
        model.createIndexOnItems();

        setupFrame();
        setupTable();
        setupButtons();
        loadData();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("Lab Code Database");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
//        setLocationRelativeTo(null);
        setLocation(0, 0); // Coordinates (0, 0) for top-left
    }

    private void setupTable() {
        String[] columnNames = {"Category", "B_code", "Items", "Unit", "Comment"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Consolas", Font.BOLD, 11));

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        table.getColumnModel().getColumn(0).setPreferredWidth(100); // Category
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // B_code
        table.getColumnModel().getColumn(2).setPreferredWidth(500); // Items
        table.getColumnModel().getColumn(3).setPreferredWidth(50); // Unit
        table.getColumnModel().getColumn(4).setPreferredWidth(300); // Comment
        setColumnAlignment();
    
    
    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                int selectedRow = table.getSelectedRow();
                int modelRow = table.convertRowIndexToModel(selectedRow);
                String bCode = (String) tableModel.getValueAt(modelRow, 1); // B_code (index 1)
                String items = (String) tableModel.getValueAt(modelRow, 2); // Items (index 2)
                String output = "\n # " + bCode + "\t:  " + items;
                // Temporary output to console for testing
                System.out.println(output);
                // Send to GDSEMR_frame when available
                GDSEMR_frame.setTextAreaText(9, output);
            }
        }
    });
}

    private void setColumnAlignment() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // B_code
    }

    private void setupButtons() {
        southPanel = new JPanel();

        String[] buttonLabels = {"Add", "Delete", "Edit", "Find", "Exit"};
        for (String label : buttonLabels) {
            southPanel.add(createButton(label));
        }

        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setHorizontalAlignment(JTextField.CENTER);
        searchField.setFont(new Font("Arial", Font.BOLD, 14));

        searchField.addActionListener(e -> {
            findRecords(searchField.getText());
            searchField.setText("");
        });

        southPanel.add(new JLabel("Search:"));
        southPanel.add(searchField);

        add(southPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        return button;
    }

    private void loadData() {
        try (ResultSet rs = model.getRecordsSortedByItems()) {
            if (rs == null) {
                showError("Failed to fetch records.");
                return;
            }
            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("Category"), rs.getString("B_code"), rs.getString("Items"),
                    rs.getString("unit"), rs.getString("comment")
                });
            }
        } catch (SQLException e) {
            showError("Error loading data: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchField) {
            findRecords(searchField.getText());
            searchField.setText("");
        } else {
            String command = e.getActionCommand();
            switch (command) {
                case "Add": showAddDialog(); break;
                case "Delete": deleteRecord(); break;
                case "Edit": editRecord(); break;
                case "Find": findRecords(searchField.getText()); break;
                case "Exit": dispose(); break;
            }
        }
    }

    private void findRecords(String searchText) {
        model.findAndDisplayRecords(searchText, tableModel);
    }

    private void deleteRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = table.convertRowIndexToModel(selectedRow);
            model.deleteRecord((String) tableModel.getValueAt(modelRow, 2)); // Use Items column (index 2)
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }

 // Replace the placeholder editRecord() method
    private void editRecord() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = table.convertRowIndexToModel(selectedRow);

            // Get current values from the selected row
            String currentCategory = (String) tableModel.getValueAt(modelRow, 0);
            String currentBCode = (String) tableModel.getValueAt(modelRow, 1);
            String currentItems = (String) tableModel.getValueAt(modelRow, 2);
            String currentUnit = (String) tableModel.getValueAt(modelRow, 3);
            String currentComment = (String) tableModel.getValueAt(modelRow, 4);

            // Create input fields pre-filled with current values
            JTextField categoryField = new JTextField(currentCategory, 20);
            JTextField bCodeField = new JTextField(currentBCode, 10);
            JTextField itemsField = new JTextField(currentItems, 20);
            JTextField unitField = new JTextField(currentUnit, 10);
            JTextField commentField = new JTextField(currentComment, 30);

            // Create dialog panel
            JPanel panel = new JPanel(new GridLayout(0, 2));
            panel.add(new JLabel("Category:"));
            panel.add(categoryField);
            panel.add(new JLabel("B_code:"));
            panel.add(bCodeField);
            panel.add(new JLabel("Items:"));
            panel.add(itemsField);
            panel.add(new JLabel("Unit:"));
            panel.add(unitField);
            panel.add(new JLabel("Comment:"));
            panel.add(commentField);

            // Show dialog and handle user input
            int result = JOptionPane.showConfirmDialog(this, panel, "Edit Entry", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                // Update the database with new values
            	model.updateRecord(
            		    currentItems, // Use current Items as the key
            		    categoryField.getText(),
            		    bCodeField.getText(),
            		    itemsField.getText(),
            		    unitField.getText(),
            		    commentField.getText()
            		);

                loadData(); // Refresh the table
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showAddDialog() {
        JTextField categoryField = new JTextField(20);
        JTextField bCodeField = new JTextField(10);
        JTextField itemsField = new JTextField(20);
        JTextField unitField = new JTextField(10);
        JTextField commentField = new JTextField(30);

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("B_code:"));
        panel.add(bCodeField);
        panel.add(new JLabel("Items:"));
        panel.add(itemsField);
        panel.add(new JLabel("Unit:"));
        panel.add(unitField);
        panel.add(new JLabel("Comment:"));
        panel.add(commentField);

        if (JOptionPane.showConfirmDialog(null, panel, "Add New Entry", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            model.insertRecord(categoryField.getText(), bCodeField.getText(), itemsField.getText(), 
                              unitField.getText(), commentField.getText());
            loadData();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LabCodeMainScreen::new);
    }
}
