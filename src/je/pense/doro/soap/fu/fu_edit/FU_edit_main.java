package je.pense.doro.soap.fu.fu_edit;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * FU_edit_main class creates a JFrame with a JTable.
 * The JTable now has 11 columns and predefined rows for various medical conditions.
 * Each cell supports multiple lines of text for display and editing.
 * Clicking a row will trigger the 'fu_precomment' method.
 * Buttons for "Edit Cells", "Save", and "Quit" are added to the south panel.
 * Implements saving and loading of table data to/from a text file.
 */
public class FU_edit_main extends JFrame {

    private JTable table; // The main JTable component
    private DefaultTableModel tableModel; // The table model to manage data
    private String[] columnNames; // Array to hold column headers
    private String[] rowHeaders; // Array to hold row headers (medical conditions)

    // Constants for file saving/loading
    private static final String SAVE_FILE_NAME = "fu_table_data.txt";
    private static final String CELL_DELIMITER = "<CELL_END>"; // Delimiter to separate cells in a row
    private static final String ROW_DELIMITER = "<ROW_END>";   // Delimiter to separate rows in the file

    /**
     * Constructor for FU_edit_main.
     * Initializes the frame, sets up table data, loads existing data, and creates the UI.
     */
    public FU_edit_main() {
        setTitle("FU Edit Main"); // Set the window title
        setSize(1000, 600); // Increased width to better accommodate 11 columns
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define close operation
        setLocationRelativeTo(null); // Center the window on the screen

        setupTableStructure(); // Prepare the column names and row headers
        loadTableData(); // Attempt to load existing data or initialize empty
        createUI(); // Build the user interface
    }

    /**
     * Prepares the column names and row headers for the JTable.
     * This method defines the fixed structure of the table.
     */
    private void setupTableStructure() {
        // Initialize 11 column names
        columnNames = new String[11];
        for (int i = 0; i < 11; i++) {
            columnNames[i] = "Column " + (i + 1);
        }

        // Define the row headers (medical conditions)
        rowHeaders = new String[]{
            "Hypertension",
            "Hypercholesterolemia",
            "Osteoporosis",
            "URI",
            "Diabetes Mellitus Gestational",
            "Diabetes Mellitus Type 1",
            "Diabetes Mellitus Type 2",
            "Thyroid Hyperthyroidism",
            "Thyroid Hypothyroidism",
            "Thyroid Hyperthyroidism with Pregnancy",
            "Thyroid Hypothyroidism with Pregnancy"
        };
    }

    /**
     * Loads table data from a file. If the file does not exist or is empty,
     * it initializes the table with row headers in the first column and empty strings elsewhere.
     */
    private void loadTableData() {
        File file = new File(SAVE_FILE_NAME);
        Object[][] loadedData = new Object[rowHeaders.length][11];

        // Initialize with default values (row headers in first column, empty strings elsewhere)
        for (int i = 0; i < rowHeaders.length; i++) {
            loadedData[i][0] = rowHeaders[i];
            for (int j = 1; j < 11; j++) {
                loadedData[i][j] = "";
            }
        }

        if (file.exists() && file.length() > 0) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                int row = 0;
                while ((line = reader.readLine()) != null && row < rowHeaders.length) {
                    // Split the line by ROW_DELIMITER first, then by CELL_DELIMITER
                    // This handles cases where a cell itself might contain the CELL_DELIMITER string
                    String[] rowParts = line.split(ROW_DELIMITER, -1); // -1 to keep trailing empty strings
                    if (rowParts.length > 0) {
                        String rowContent = rowParts[0]; // The actual content for this row

                        // Split the row content into cells
                        String[] cellContents = rowContent.split(CELL_DELIMITER, -1); // -1 to keep trailing empty strings

                        // Populate the loadedData array, skipping the first column (row header)
                        // and ensuring we don't go out of bounds.
                        for (int col = 1; col < 11 && (col - 1) < cellContents.length; col++) {
                            loadedData[row][col] = cellContents[col - 1];
                        }
                    }
                    row++;
                }
            } catch (IOException e) {
                System.err.println("Error loading table data: " + e.getMessage());
                // Optionally show a message to the user
                JOptionPane.showMessageDialog(this, "Error loading saved data: " + e.getMessage(),
                                              "Load Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        // Create the DefaultTableModel with the loaded or default data
        tableModel = new DefaultTableModel(loadedData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make the first column (row headers) non-editable
                return column != 0;
            }
        };
    }

    /**
     * Creates and configures the JTable and adds it to the JFrame.
     * Sets up custom cell renderers and editors for multi-line text.
     * Adds a ListSelectionListener to handle row clicks.
     * Now includes buttons in the south panel.
     */
    private void createUI() {
        // Create the JTable with the custom model
        table = new JTable(tableModel);
        table.setRowHeight(40); // Set an initial generous row height for better visibility

        // Create instances of the custom multi-line renderer and editor
        MultiLineCellRenderer multiLineRenderer = new MultiLineCellRenderer();
        MultiLineCellEditor multiLineEditor = new MultiLineCellEditor();

        // Apply the custom renderer and editor to all columns in the table
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(multiLineRenderer);
            // Only apply editor to editable columns (columns 1 to 10)
            if (i != 0) {
                table.getColumnModel().getColumn(i).setCellEditor(multiLineEditor);
            }
        }

        // Add a ListSelectionListener to the table's selection model.
        // This listener will detect when a row is clicked/selected.
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Check if the event is not still adjusting (to avoid duplicate calls)
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow(); // Get the index of the selected row
                    if (selectedRow != -1) { // Ensure a row is actually selected
                        fu_precomment(selectedRow); // Call the custom method for the clicked row
                    }
                }
            }
        });

        // Wrap the table in a JScrollPane to allow scrolling if content exceeds visible area
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center of the frame

        // Create a panel for buttons in the SOUTH region
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center buttons with spacing

        // Create "Edit Cells" button
        JButton editCellsButton = new JButton("Edit Cells");
        editCellsButton.addActionListener(e -> {
            // This action can be used to programmatically start editing a cell,
            // or simply ensure the table has focus for immediate manual editing.
            table.requestFocusInWindow();
            JOptionPane.showMessageDialog(this, "Click on any editable cell (Column 2 onwards) to start editing.", "Edit Mode", JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(editCellsButton);

        // Create "Save" button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveTableData()); // Call the new save method
        buttonPanel.add(saveButton);

        // Create "Quit" button
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> dispose()); // Close the JFrame
        buttonPanel.add(quitButton);

        add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the south of the frame

        setVisible(true); // Make the JFrame visible
    }

    /**
     * Saves the current content of the JTable to a text file.
     * Each cell's content is separated by CELL_DELIMITER, and each row by ROW_DELIMITER.
     */
    private void saveTableData() {
        // Stop any ongoing cell editing before saving to ensure latest data is captured
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE_NAME))) {
            for (int r = 0; r < tableModel.getRowCount(); r++) {
                StringBuilder rowContent = new StringBuilder();
                // Start from column 1 as column 0 contains row headers (not user-editable data)
                for (int c = 1; c < tableModel.getColumnCount(); c++) {
                    Object cellValue = tableModel.getValueAt(r, c);
                    // Append cell value, handling nulls, and then the cell delimiter
                    rowContent.append(cellValue != null ? cellValue.toString() : "").append(CELL_DELIMITER);
                }
                // After processing all cells in a row, append the row content and the row delimiter
                writer.write(rowContent.toString());
                writer.write(ROW_DELIMITER);
                writer.newLine(); // Add a new line for readability in the file
            }
            JOptionPane.showMessageDialog(this, "Table data saved successfully to " + SAVE_FILE_NAME,
                                          "Save Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving table data: " + e.getMessage(),
                                          "Save Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error saving table data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This method is executed when a row in the JTable is clicked.
     * It displays a simple message dialog indicating which row was clicked.
     * In a real application, this method would contain the specific logic
     * to be performed upon a row click (e.g., opening a new dialog, loading data).
     *
     * @param rowIndex The index of the clicked row.
     */
    private void fu_precomment(int rowIndex) {
        // Retrieve the row header (medical condition) from the first column of the clicked row
        String rowHeader = (String) tableModel.getValueAt(rowIndex, 0);
        // Display a message dialog
        JOptionPane.showMessageDialog(this,
                                      "Row clicked: " + rowHeader + " (Index: " + rowIndex + ")",
                                      "Row Clicked Event",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Main method to run the FU_edit_main application.
     * Ensures that the Swing UI is created and updated on the Event Dispatch Thread (EDT).
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FU_edit_main::new);
    }
}

/**
 * Custom TableCellRenderer for JTable cells to display multi-line text.
 * It uses a JTextArea internally to render the cell content.
 */
class MultiLineCellRenderer extends JTextArea implements TableCellRenderer {

    /**
     * Constructor for MultiLineCellRenderer.
     * Configures the JTextArea for line wrapping and basic styling.
     */
    public MultiLineCellRenderer() {
        setLineWrap(true); // Enable line wrapping
        setWrapStyleWord(true); // Wrap at word boundaries
        setOpaque(true); // Must be opaque for background colors to show correctly
        setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5)); // Add some internal padding
    }

    /**
     * Returns the component used for drawing the cell.
     * This method is called by the JTable to get the renderer component for each cell.
     *
     * @param table The JTable that is asking the renderer to draw.
     * @param value The value of the cell to be rendered.
     * @param isSelected True if the cell is selected.
     * @param hasFocus True if the cell has focus.
     * @param row The row index of the cell being rendered.
     * @param column The column index of the cell being rendered.
     * @return The component (this JTextArea) used for rendering the cell.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        setText(value != null ? value.toString() : ""); // Set the text of the JTextArea

        // Set background and foreground colors based on cell selection state
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        // Dynamically adjust row height based on content.
        // This is a common technique, though it can be performance-intensive for very large tables.
        // It ensures that the row height is sufficient to display all content without truncation.
        int textWidth = table.getColumnModel().getColumn(column).getWidth();
        if (textWidth > 0) { // Ensure column width is not zero to prevent issues
            // Set the width of the JTextArea to the column width, allowing height to be calculated
            setSize(textWidth, Integer.MAX_VALUE);
            // Calculate the preferred height required to display all text
            int preferredHeight = getPreferredSize().height;
            // If the calculated preferred height is greater than the current row height,
            // update the row height to accommodate the content.
            if (table.getRowHeight(row) < preferredHeight) {
                table.setRowHeight(row, preferredHeight);
            }
        }

        return this; // Return the JTextArea itself as the renderer component
    }
}

/**
 * Custom TableCellEditor for JTable cells to allow multi-line text input.
 * It uses a JTextArea wrapped in a JScrollPane as the editing component.
 */
class MultiLineCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JTextArea editorComponent; // The JTextArea used for editing
    private JScrollPane scrollPane; // JScrollPane to allow scrolling within the editor

    /**
     * Constructor for MultiLineCellEditor.
     * Initializes the JTextArea and wraps it in a JScrollPane.
     */
    public MultiLineCellEditor() {
        editorComponent = new JTextArea();
        editorComponent.setLineWrap(true); // Enable line wrapping
        editorComponent.setWrapStyleWord(true); // Wrap at word boundaries
        editorComponent.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5)); // Add padding
        scrollPane = new JScrollPane(editorComponent); // Wrap the text area in a scroll pane
    }

    /**
     * Returns the component that should be added to the client's JTable
     * for the purpose of editing the cell value.
     *
     * @param table The JTable that is asking the editor to edit.
     * @param value The value of the cell to be edited.
     * @param isSelected True if the cell is selected.
     * @param row The row index of the cell being edited.
     * @param column The column index of the cell being edited.
     * @return The component (this JScrollPane containing the JTextArea) used for editing.
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        editorComponent.setText(value != null ? value.toString() : ""); // Set the text for editing
        // Set background and foreground colors for the editor based on selection
        editorComponent.setBackground(table.getSelectionBackground());
        editorComponent.setForeground(table.getSelectionForeground());
        return scrollPane; // Return the scroll pane containing the editor
    }

    /**
     * Returns the value contained in the editor.
     * This method is called when the editing session stops.
     *
     * @return The current text entered in the JTextArea editor.
     */
    @Override
    public Object getCellEditorValue() {
        return editorComponent.getText(); // Return the text from the editor
    }
}
