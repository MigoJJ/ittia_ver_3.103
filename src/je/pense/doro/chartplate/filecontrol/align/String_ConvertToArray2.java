package je.pense.doro.chartplate.filecontrol.align;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import je.pense.doro.GDSEMR_frame;

public class String_ConvertToArray2 extends JFrame {
    private JTextArea outputTextArea = new JTextArea(25, 60);

    private void createAndShowGUI(String inputString) {
        // Create a new JFrame and set its properties
        JFrame frame = new JFrame("JTable Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Split the input string by newline to get rows
        String[] rows = inputString.split("\n");

        // Create a new 2D array to store the table data
        Object[][] data = new Object[rows.length][3];

     // Iterate over the rows
        int rowIndex = 0;
        for (String row : rows) {
            // Split the row by tabs to get columns
            String[] columns = row.split("\t");

            // Check if the row is not empty and the columns array has the required length
            if (row != null && !row.trim().isEmpty() && columns.length >= 3) {
                // Assign the values to specific columns
                data[rowIndex][0] = columns[0].trim();
                data[rowIndex][1] = columns[1].trim();
                data[rowIndex][2] = columns[2].trim();
                rowIndex++;
            }
        }

        // Create a new Object array for column names
        Object[] columnNames = new Object[]{"Decision", "Value", "Label"};

        // Create a new DefaultTableModel using the table data and column names
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // Only allow editing the "Value" column
            }
        };

        // Create a new JTable using the DefaultTableModel
        JTable table = new JTable(model);

        // Override the cell editor to move to the next row when Enter is pressed in the "Value" column
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JTextField()) {
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                JTextField textField = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
                textField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            if (row < table.getRowCount() - 1) {
                                table.setRowSelectionInterval(row + 1, row + 1);
                                table.setColumnSelectionInterval(column, column);
                            }
                        }
                    }
                });
                return textField;
            }
        });

        // Add a TableModelListener to the table to listen for changes in the "Value" column
        table.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 1) { // Check if the "Value" column has changed
                int row = e.getFirstRow();
                int column = e.getColumn();
                String valueStr = (String) table.getValueAt(row, column);

                try {
                    double value = Double.parseDouble(valueStr);
                    if (value > 5) {
                        table.setValueAt("high", row, 0); // Set "high" in the "Decision" column
                    }
                } catch (NumberFormatException ex) {
                    // Handle the case where the value is not a valid number
                    System.out.println("Invalid value entered.");
                }
            }
        });

        // Create a JScrollPane and add the JTable to it
        JScrollPane scrollPane = new JScrollPane(table);

        // Create a JScrollPane to contain the JTextArea
        JScrollPane textAreaScrollPane = new JScrollPane(outputTextArea);

        frame.setVisible(false);
        copyRowsToTextArea(table);
    }

    private void copyRowsToTextArea(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        StringBuilder rowsText = new StringBuilder();

        // Append each row to the StringBuilder, excluding the last row
        for (int row = 0; row < model.getRowCount() - 1; row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                rowsText.append(model.getValueAt(row, col)).append("\t");
            }
            rowsText.append("\n");
        }

        // Set the text of the JTextArea
        outputTextArea.setText(rowsText.toString());
        System.out.println(rowsText.toString());
        GDSEMR_frame.setTextAreaText(5, "\n"+ rowsText.toString());

    }

    public static void main(String inputargs) {
        SwingUtilities.invokeLater(() -> {
            new String_ConvertToArray2().createAndShowGUI(inputargs);
        });
    }
}


