package je.pense.doro.support.sqlite3_manager.icd.icd11;

// Import Color and Font
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.filecontrol.datetime.Date_current;
import je.pense.doro.entry.EntryDir;

// Assuming GDSEMR_frame and Date_current placeholders or actual classes exist
// import static je.panse.doro.soap.assessment.icd_11.ICD11Manager.GDSEMR_frame;
// import static je.panse.doro.soap.assessment.icd_11.ICD11Manager.Date_current;


public class ICD11Manager extends JFrame implements ActionListener {

    private static final Logger LOGGER = Logger.getLogger(ICD11Manager.class.getName());
//    private static final String DB_URL = "jdbc:sqlite:" + EntryDir.homeDir + "/soap/assessment/icd_11/icd11.db"; // Adjust path if needed
    private static final String DB_URL = "jdbc:sqlite:" + EntryDir.HOME_DIR + "/chartplate/filecontrol/database/icd11.db"; // Adjust path if needed
    private Connection conn;
    
    // UI Components
    private JTextField idField, markField, codeField, nameField, noteField, searchField;
    private JButton addButton, editButton, deleteButton, searchButton, clearButton, saveButton, quitButton, loadAllButton, appendIttiaButton; // Added appendIttiaButton
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private boolean isEditing = false; // Flag to indicate if we are adding or editing

    public ICD11Manager() {
        setTitle("ICD-11 Code Manager");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Handle close via Quit button

        // Add a window listener to confirm exit
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                quitApplication();
            }
        });

        // Initialize components
        initComponents(); // Styles will be applied here
        layoutComponents();

        // Database Connection
        try {
            connectDatabase();
            loadInitialData(); // Load data when the app starts
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failed", e);
            JOptionPane.showMessageDialog(this, "Error connecting to database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Exit if DB connection fails
        }

        setVisible(true);
    }

    // --- connectDatabase, layoutComponents remain the same ---
    private void connectDatabase() throws SQLException {
        LOGGER.info("Connecting to database: " + DB_URL);
        conn = DriverManager.getConnection(DB_URL);
        LOGGER.info("Database connection successful.");
        // Ensure table exists (optional, importer should have done this)
        try (Statement stmt = conn.createStatement()) {
             stmt.executeUpdate("CREATE TABLE IF NOT EXISTS icd11 (" +
                                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "Mark TEXT, " +
                                "Code TEXT NOT NULL UNIQUE, " + // Added UNIQUE constraint for code
                                "ICD11Name TEXT NOT NULL, " +
                                "Note TEXT)");
             LOGGER.info("Checked/Ensured table 'icd11' exists.");
         } catch (SQLException e) {
            // Log if UNIQUE constraint already exists or other table creation issues
            LOGGER.log(Level.WARNING, "Could not ensure UNIQUE constraint or table: " + e.getMessage());
         }
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5));

        // --- Input Panel ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Record Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 5, 3, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0
        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; inputPanel.add(idField, gbc);
        gbc.gridx = 2; gbc.gridy = 0; inputPanel.add(new JLabel("Mark:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; inputPanel.add(markField, gbc);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Code*:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; inputPanel.add(codeField, gbc);
        gbc.gridx = 2; gbc.gridy = 1; inputPanel.add(new JLabel("Name*:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL; inputPanel.add(nameField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(new JLabel("Note:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 5; gbc.fill = GridBagConstraints.HORIZONTAL; inputPanel.add(noteField, gbc);

        gbc.gridwidth = 1; // Reset gridwidth
        gbc.fill = GridBagConstraints.NONE; // Reset fill

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(appendIttiaButton); // Added
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);

        // --- Search Panel ---
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        searchPanel.add(new JLabel("Search (Code/Name/Note):"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(loadAllButton);

        // --- Top Panel (Input + Buttons) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.SOUTH);


        // Add panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Initial field state
        setFieldsEditable(false);
    }


    private void initComponents() {
        Color lightYellow = new Color(255, 255, 224); // Light yellow background

        JButton[] buttons = {
            addButton, editButton, deleteButton, searchButton, clearButton,
            saveButton, quitButton, loadAllButton, appendIttiaButton
        };

        for (JButton button : buttons) {
            if (button != null) {
                Font currentFont = button.getFont();
                button.setFont(new Font(currentFont.getName(), Font.BOLD, currentFont.getSize()));

                // Set light yellow background correctly
                button.setBackground(lightYellow);
                button.setOpaque(true); // Must be true to paint background
                button.setContentAreaFilled(true); // Must be true to fill button area
            }
        }
    


        // Text Fields
        idField = new JTextField(5);
        idField.setEditable(false); // ID is usually auto-generated and read-only
        idField.setBackground(lightYellow); // Apply background

        markField = new JTextField(10);
        markField.setBackground(lightYellow); // Apply background

        codeField = new JTextField(10);
        codeField.setBackground(lightYellow); // Apply background

        nameField = new JTextField(30);
        nameField.setBackground(lightYellow); // Apply background

        noteField = new JTextField(30);
        noteField.setBackground(lightYellow); // Apply background

        searchField = new JTextField(20);
        searchField.setBackground(lightYellow); // Apply background


        // Buttons
        addButton = new JButton("Add New");
        editButton = new JButton("Edit Selected");
        deleteButton = new JButton("Delete Selected");
        searchButton = new JButton("Search");
        clearButton = new JButton("Clear Fields");
        saveButton = new JButton("Save");
        quitButton = new JButton("Quit");
        loadAllButton = new JButton("Load All");
        appendIttiaButton = new JButton("Append Ittia");

        // Apply Bold Font to all buttons


        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Mark", "Code", "ICD11 Name", "Note"}, 0) {
             // ... (isCellEditable override) ...
        };
        dataTable = new JTable(tableModel);

        // ************************************************
        // ***** ADD THIS SECTION TO SET COLUMN WIDTHS ****
        // ************************************************
        javax.swing.table.TableColumnModel columnModel = dataTable.getColumnModel();

        // Set preferred widths in pixels (adjust these values to your liking)
        columnModel.getColumn(0).setPreferredWidth(40);   // ID column
        columnModel.getColumn(1).setPreferredWidth(50);   // Mark column
        columnModel.getColumn(2).setPreferredWidth(100);  // Code column
        columnModel.getColumn(3).setPreferredWidth(350);  // ICD11 Name column (needs more space)
        columnModel.getColumn(4).setPreferredWidth(200);  // Note column

        // Optional: To prevent users from resizing a column too small/large
        // columnModel.getColumn(0).setMinWidth(30);
        // columnModel.getColumn(0).setMaxWidth(60);
        // columnModel.getColumn(3).setMinWidth(150); // Ensure Name is reasonably wide

        // Optional: Control how columns resize when the table/window size changes
        // dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Use horizontal scrollbar if needed
        // dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN); // Only the last column adjusts
        // dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // All columns adjust proportionally
        // Default is often AUTO_RESIZE_SUBSEQUENT_COLUMNS

        // ************************************************
        // ************* END OF COLUMN WIDTH SECTION ************
        // ************************************************
    
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only one row selection
        // Add listener to load selected row data into fields when a row is clicked
        dataTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && dataTable.getSelectedRow() != -1) {
                loadSelectedRowToFields();
                // Disable editing fields until "Edit Selected" is clicked
                setFieldsEditable(false);
                saveButton.setEnabled(false);
                isEditing = false; // Reset state
            }
        });
        scrollPane = new JScrollPane(dataTable);
        // Enable sorting
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        dataTable.setRowSorter(sorter);


        // Add Action Listeners
        addButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        searchButton.addActionListener(this);
        clearButton.addActionListener(this);
        saveButton.addActionListener(this);
        quitButton.addActionListener(this);
        loadAllButton.addActionListener(this);
        appendIttiaButton.addActionListener(this);
        searchField.addActionListener(this); // Allow search on Enter key in search field

        // Initially disable Save button until Add or Edit is clicked
        saveButton.setEnabled(false);
        // Initially disable Edit/Delete until a row is selected
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

    // --- Action Handling (actionPerformed) - No changes needed here for styling ---
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == addButton) {
            prepareForAdd();
        } else if (source == editButton) {
            prepareForEdit();
        } else if (source == saveButton) {
            saveRecord();
        } else if (source == deleteButton) {
            deleteRecord();
        } else if (source == clearButton) {
            clearInputFields();
            dataTable.clearSelection();
            setFieldsEditable(false);
            saveButton.setEnabled(false);
            isEditing = false;
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
        } else if (source == searchButton || source == searchField) {
            searchRecords(searchField.getText().trim());
        } else if (source == loadAllButton) {
            loadInitialData();
            searchField.setText(""); // Clear search field when loading all
        } else if (source == appendIttiaButton) { // Added
            appendIttiaData();                  // Added
        } else if (source == quitButton) {
            quitApplication();
        }
    }


    // --- Helper Methods (setFieldsEditable, clearInputFields, etc.) - No changes needed ---
    private void setFieldsEditable(boolean editable) {
        // idField is never editable
        markField.setEditable(editable);
        codeField.setEditable(editable);
        nameField.setEditable(editable);
        noteField.setEditable(editable);
    }

    private void clearInputFields() {
        idField.setText("");
        markField.setText("");
        codeField.setText("");
        nameField.setText("");
        noteField.setText("");
        searchField.setText(""); // Also clear search field if desired
        isEditing = false; // Reset state
        saveButton.setEnabled(false); // Disable save after clearing
    }

    private void loadSelectedRowToFields() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow != -1) {
            // Get data from the *model* row, accounting for sorting/filtering
            int modelRow = dataTable.convertRowIndexToModel(selectedRow);
            idField.setText(tableModel.getValueAt(modelRow, 0).toString());
            markField.setText(tableModel.getValueAt(modelRow, 1) != null ? tableModel.getValueAt(modelRow, 1).toString() : "");
            codeField.setText(tableModel.getValueAt(modelRow, 2).toString());
            nameField.setText(tableModel.getValueAt(modelRow, 3).toString());
            noteField.setText(tableModel.getValueAt(modelRow, 4) != null ? tableModel.getValueAt(modelRow, 4).toString() : "");

            setFieldsEditable(false); // Fields loaded are initially read-only
            saveButton.setEnabled(false); // Disable save until 'Edit' is clicked
            editButton.setEnabled(true); // Enable edit for the selected row
            deleteButton.setEnabled(true); // Enable delete for the selected row
            isEditing = false; // Not in editing mode yet
        } else {
             clearInputFields(); // Clear fields if selection is cleared
             editButton.setEnabled(false);
             deleteButton.setEnabled(false);
        }
    }

    private void prepareForAdd() {
        clearInputFields();
        setFieldsEditable(true);
        codeField.requestFocus(); // Focus on the first editable field
        isEditing = false; // Indicates we are adding a new record
        saveButton.setEnabled(true); // Enable save button
        dataTable.clearSelection(); // Deselect any table row
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
    }

     private void prepareForEdit() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }
        setFieldsEditable(true);
        codeField.setEditable(false); // Usually, the primary code/key is not editable
        nameField.requestFocus();
        isEditing = true; // Indicates we are editing an existing record
        saveButton.setEnabled(true); // Enable save button
        // Edit and Delete remain enabled as a row is selected
    }

    private void loadInitialData() {
        searchRecords(null); // Pass null or empty string to load all
    }

    private void refreshTable() {
        // Re-load data based on the current search term, or load all if empty
        String currentSearch = searchField.getText().trim();
         if (currentSearch.isEmpty()) {
             loadInitialData();
         } else {
             searchRecords(currentSearch);
         }
         clearInputFields(); // Clear fields after refresh
         setFieldsEditable(false);
         saveButton.setEnabled(false);
         editButton.setEnabled(false);
         deleteButton.setEnabled(false);
    }

    // --- Database Operations (searchRecords, saveRecord, deleteRecord) - No changes needed ---
    private void searchRecords(String searchTerm) {
        LOGGER.info("Searching records with term: " + (searchTerm == null ? "ALL" : searchTerm));
        tableModel.setRowCount(0); // Clear existing table data

        String sql = "SELECT ID, Mark, Code, ICD11Name, Note FROM icd11";
        List<Object[]> results = new ArrayList<>();

        boolean hasSearchTerm = searchTerm != null && !searchTerm.isEmpty();
        if (hasSearchTerm) {
             sql += " WHERE Code LIKE ? OR ICD11Name LIKE ? OR Note LIKE ?";
        }
        sql += " ORDER BY Code"; // Optional: Order results

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (hasSearchTerm) {
                String likeTerm = "%" + searchTerm + "%";
                pstmt.setString(1, likeTerm);
                pstmt.setString(2, likeTerm);
                pstmt.setString(3, likeTerm);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(new Object[]{
                        rs.getInt("ID"),
                        rs.getString("Mark"),
                        rs.getString("Code"),
                        rs.getString("ICD11Name"),
                        rs.getString("Note")
                });
            }
            LOGGER.info("Found " + results.size() + " records.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching records", e);
            JOptionPane.showMessageDialog(this, "Error searching database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // Populate table (on EDT)
        SwingUtilities.invokeLater(() -> {
             for (Object[] row : results) {
                 tableModel.addRow(row);
             }
             if (results.isEmpty() && hasSearchTerm) {
                 // Optionally show message
                 // JOptionPane.showMessageDialog(this, "No records found matching '" + searchTerm + "'.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
             } else if (results.isEmpty() && !hasSearchTerm){
                 LOGGER.info("Database table might be empty.");
             }
             // After loading data, clear selection and disable edit/delete buttons initially
             dataTable.clearSelection();
             editButton.setEnabled(false);
             deleteButton.setEnabled(false);
        });
    }

    private void saveRecord() {
        String mark = markField.getText().trim();
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String note = noteField.getText().trim();

        // Basic Validation
        if (code.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Code and ICD11 Name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Use null for empty optional fields if desired
        String finalMark = mark.isEmpty() ? null : mark;
        String finalNote = note.isEmpty() ? null : note;


        if (isEditing) { // --- UPDATE ---
            String idStr = idField.getText();
             if (idStr.isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Cannot update record without an ID. Select a record and click 'Edit'.", "Update Error", JOptionPane.ERROR_MESSAGE);
                 return;
             }
            int id = Integer.parseInt(idStr); // ID field is not editable, so parsing should be safe
            LOGGER.info("Attempting to update record ID: " + id);
            String sql = "UPDATE icd11 SET Mark = ?, ICD11Name = ?, Note = ? WHERE ID = ?";
            // Note: We are *not* updating the Code as it's often treated as a key. If it needs to be updatable, adjust UI/SQL.

             try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setString(1, finalMark);
                 pstmt.setString(2, name); // Name is required
                 pstmt.setString(3, finalNote);
                 pstmt.setInt(4, id);

                 int affectedRows = pstmt.executeUpdate();
                 if (affectedRows > 0) {
                     JOptionPane.showMessageDialog(this, "Record updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                     refreshTable(); // Refresh to show changes
                 } else {
                     JOptionPane.showMessageDialog(this, "Record could not be updated (maybe it was deleted?).", "Update Failed", JOptionPane.WARNING_MESSAGE);
                 }
             } catch (SQLException e) {
                 LOGGER.log(Level.SEVERE, "Error updating record ID: " + id, e);
                 JOptionPane.showMessageDialog(this, "Error updating database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
             }

        } else { // --- INSERT ---
            LOGGER.info("Attempting to insert new record with Code: " + code);
            String sql = "INSERT INTO icd11 (Mark, Code, ICD11Name, Note) VALUES (?, ?, ?, ?)";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, finalMark);
                pstmt.setString(2, code); // Code is required
                pstmt.setString(3, name); // Name is required
                pstmt.setString(4, finalNote);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Record added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshTable(); // Refresh to show the new record
                } else {
                    JOptionPane.showMessageDialog(this, "Record could not be added.", "Insert Failed", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error inserting record with Code: " + code, e);
                 // Check for unique constraint violation (SQLite error code 19)
                 if (e.getErrorCode() == 19 && e.getMessage() != null && e.getMessage().contains("UNIQUE constraint failed: icd11.Code")) {
                     JOptionPane.showMessageDialog(this, "Error: Code '" + code + "' already exists.", "Duplicate Code", JOptionPane.ERROR_MESSAGE);
                 } else {
                     JOptionPane.showMessageDialog(this, "Error inserting into database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                 }
            }
        }
        // After save, reset state
        isEditing = false;
        setFieldsEditable(false);
        saveButton.setEnabled(false);
    }

    private void deleteRecord() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = dataTable.convertRowIndexToModel(selectedRow);
        String idStr = tableModel.getValueAt(modelRow, 0).toString();
        String codeToDelete = tableModel.getValueAt(modelRow, 2).toString(); // Get code for confirmation message
        int id = Integer.parseInt(idStr);

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete record with Code '" + codeToDelete + "' (ID: " + id + ")?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            LOGGER.info("Attempting to delete record ID: " + id);
            String sql = "DELETE FROM icd11 WHERE ID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Record deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshTable(); // Refresh table after deletion
                } else {
                    JOptionPane.showMessageDialog(this, "Record could not be deleted (maybe it was already removed?).", "Deletion Failed", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error deleting record ID: " + id, e);
                JOptionPane.showMessageDialog(this, "Error deleting from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void closeDatabaseConnection() {
        if (conn != null) {
            try {
                conn.close();
                LOGGER.info("Database connection closed.");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error closing database connection", e);
            }
        }
    }

    private void quitApplication() {
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to quit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmation == JOptionPane.YES_OPTION) {
            closeDatabaseConnection();
            dispose(); // Close the window
//            System.exit(0); // Terminate the application
        }
    }


    // --- appendIttiaData Method ---
    private void appendIttiaData() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();

        if (code.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a record or ensure 'Code' and 'Name' fields are populated.",
                    "Data Missing", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
//            String textToAppend = String.format("\n # %s : %s (%s)", code, name, Date_current.main("m"));
            String textToAppend = String.format("\n # %s :\t%s ", code, name);
            GDSEMR_frame.setTextAreaText(7, textToAppend);
            LOGGER.info("Appended to Ittia: " + textToAppend.trim());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error appending data via GDSEMR_frame", e);
            JOptionPane.showMessageDialog(this, "Error appending data: " + e.getMessage(),
                    "Append Error", JOptionPane.ERROR_MESSAGE);
        }
    }


     // --- Placeholder Classes (REMOVE or REPLACE with your actual implementations) ---

     // Example placeholder for Date_current
     static class Date_current {
         static String main(String format) {
             if ("m".equals(format)) {
                  return String.valueOf(java.time.LocalDate.now().getMonthValue());
             }
             return java.time.LocalDate.now().toString();
         }
     }

     // --- End Placeholder Classes ---


    // --- Main Method ---
    public static void main(String[] args) {
        // Set Look and Feel (Optional, makes it look more modern)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Could not set system look and feel", e);
        }

        // Run the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(ICD11Manager::new);
    }
} // End of ICD11Manager class