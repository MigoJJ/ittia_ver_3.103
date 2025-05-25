package je.pense.doro.fourgate.diabetes.dmPeripheral;

import javax.swing.JTextArea;    	
import javax.swing.table.DefaultTableModel;

public class PNPdmCheckbox {

    public static void checkboxAction(JTextArea textArea, DefaultTableModel model, int row, int column, boolean isChecked) {
        if (isChecked) {
            // If checkbox is checked, append the corresponding message to JTextArea
            appendCheckboxStatus(textArea, model, row, column);
        } else {
            // If checkbox is unchecked, remove the specific message from JTextArea
            removeCheckboxStatus(textArea, model, row, column);
        }
    }
    
        private static void appendCheckboxStatus(JTextArea textArea, DefaultTableModel model, int row, int column) {
        if (column == 1 || column == 2) {
            Object columnValue = model.getValueAt(row, 3); // Value from column 4
            String status = (column == 1) ? "    [ V ]  " : "    [ - ]  ";
            textArea.append("\n" + status + columnValue + "\n");
        }
    }

    private static void removeCheckboxStatus(JTextArea textArea, DefaultTableModel model, int row, int column) {
        Object columnValue = model.getValueAt(row, 3); // Get the value from column 4
        String statusPrefix = (column == 1) ? "[ V ]  " : "[ - ]  ";
        String targetLine = statusPrefix + columnValue; // The line to be removed
        
        String[] lines = textArea.getText().split("\n");
        StringBuilder newText = new StringBuilder();
        boolean found = false; // Flag to indicate if the target line was found
        
        for (String line : lines) {
            if (!found && line.trim().equals(targetLine.trim())) {
                found = true; // Skip the first occurrence of the target line
            } else {
                newText.append(line).append("\n"); // Append all other lines
            }
        }
        
        textArea.setText(newText.toString().trim()); // Update JTextArea, maintain formatting
    }
}
