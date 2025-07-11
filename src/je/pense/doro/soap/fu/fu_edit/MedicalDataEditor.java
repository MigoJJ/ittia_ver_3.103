package je.pense.doro.soap.fu.fu_edit;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * A generic editor for medical SOAP note data.
 * The original Table_Hypertension class has been modified to support any given medical condition.
 */
public class MedicalDataEditor extends JFrame {
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private final String[] rowLabels = {"CC>", "PI>", "ROS>", "PMH>", "S>", "O>", "Physical Exam>", "A>", "P>", "Comment>"};
    
    // File path is now set dynamically in the constructor for each condition.
    private final String filePath;

    /**
     * Constructor for the data editor window.
     * @param conditionName The name of the medical condition to edit (e.g., "Hypertension").
     */
    public MedicalDataEditor(String conditionName) {
        // MODIFICATION: Title is now dynamic based on the condition name.
        setTitle(conditionName + " Data Entry");

        // MODIFICATION: Close only this window, not the entire application.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center on screen

        // MODIFICATION: File path is constructed dynamically based on the condition name.
        // Example: "Diabetes Mellitus Type 2" -> "diabetes_mellitus_type_2_data.txt"
        String basePath = "/home/migowj/git/ittia_ver_3.103/src/src/je/pense/doro/soap/fu/fu_edit/";
        String fileName = conditionName.toLowerCase().replace(" ", "_") + "_data.txt";
        this.filePath = basePath + fileName;

        // --- The rest of the UI setup is largely unchanged ---
        tableModel = new DefaultTableModel(new Vector<>(java.util.Arrays.asList("Category", "Details")), 0) {
            @Override public boolean isCellEditable(int row, int column) { return column != 0; }
            @Override public Class<?> getColumnClass(int columnIndex) { return String.class; }
        };

        for (String label : rowLabels) tableModel.addRow(new Object[]{label, ""});

        dataTable = new JTable(tableModel);
        dataTable.setRowHeight(80);
        dataTable.setFont(new Font("Arial", Font.PLAIN, 14));
        dataTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        dataTable.setDefaultRenderer(String.class, new JTextAreaCellRenderer());
        dataTable.setDefaultEditor(String.class, new JTextAreaCellEditor());
        add(new JScrollPane(dataTable), BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        southPanel.add(createButton("Save", e -> saveData()));
        // MODIFICATION: Changed "Quit" to "Close" and its action to dispose() the current window.
        southPanel.add(createButton("Close", e -> dispose()));
        add(southPanel, BorderLayout.SOUTH);

        loadDataFromFile();
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.addActionListener(listener);
        return button;
    }

    // This method now works for any condition because `filePath` is dynamic.
    private void saveData() {
        File file = new File(filePath);
        // Ensure parent directory exists
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            showError("Failed to create directory: " + file.getParent());
            return;
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Category,Details\n");
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String category = (String) tableModel.getValueAt(i, 0);
                String details = ((String) tableModel.getValueAt(i, 1)).replace("\n", "\\n");
                writer.write(category + "," + details + "\n");
            }
            showMessage("Data saved to: " + file.getAbsolutePath());
        } catch (IOException ex) {
            showError("Error saving data: " + ex.getMessage());
        }
    }

    // This method now works for any condition because `filePath` is dynamic.
    private void loadDataFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return; // No data file exists yet for this condition, which is fine.
        }

        Map<String, String> data = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                int commaIndex = line.indexOf(',');
                if (commaIndex != -1) {
                    String category = line.substring(0, commaIndex);
                    String details = line.substring(commaIndex + 1).replace("\\n", "\n");
                    data.put(category, details);
                }
            }
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String category = (String) tableModel.getValueAt(i, 0);
                if (data.containsKey(category)) {
                    tableModel.setValueAt(data.get(category), i, 1);
                }
            }
        } catch (IOException ex) {
            showError("Error loading data: " + ex.getMessage());
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Status", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Inner class for rendering JTextArea in a cell - no changes needed.
    static class JTextAreaCellRenderer extends JTextArea implements TableCellRenderer {
        public JTextAreaCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                    boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            setFont(table.getFont());
            return this;
        }
    }

    // Inner class for editing JTextArea in a cell - no changes needed.
    static class JTextAreaCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JTextArea editor = new JTextArea();
        private final JScrollPane scrollPane = new JScrollPane(editor);

        public JTextAreaCellEditor() {
            editor.setLineWrap(true);
            editor.setWrapStyleWord(true);
            editor.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            scrollPane.setBorder(null);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                   int row, int column) {
            editor.setText(value == null ? "" : value.toString());
            editor.selectAll();
            return scrollPane;
        }

        @Override
        public Object getCellEditorValue() {
            return editor.getText();
        }
    }

    /**
     * MODIFICATION: The main method now acts as a launcher. It creates a selection
     * window that allows the user to choose which condition to edit.
     */
    public static void main(String[] args) {
        // The list of medical conditions to iterate through.
        String[] conditions = {
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

        // Use SwingUtilities.invokeLater to ensure thread safety for the GUI.
        SwingUtilities.invokeLater(() -> createAndShowSelector(conditions));
    }

    /**
     * Creates and displays the main window for selecting a condition.
     * @param conditions The list of medical conditions to display in the dropdown.
     */
    private static void createAndShowSelector(String[] conditions) {
        JFrame selectorFrame = new JFrame("Select Condition to Edit");
        selectorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        selectorFrame.setSize(450, 150);
        selectorFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Please select a medical condition to edit:", SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);

        JComboBox<String> conditionComboBox = new JComboBox<>(conditions);
        panel.add(conditionComboBox, BorderLayout.CENTER);

        JButton editButton = new JButton("Open Editor");
        editButton.setFont(new Font("Arial", Font.BOLD, 14));
        editButton.addActionListener(e -> {
            String selectedCondition = (String) conditionComboBox.getSelectedItem();
            if (selectedCondition != null) {
                // For the selected item, create and show a new editor instance.
                new MedicalDataEditor(selectedCondition).setVisible(true);
            }
        });
        panel.add(editButton, BorderLayout.SOUTH);

        selectorFrame.add(panel);
        selectorFrame.setVisible(true);
    }
}