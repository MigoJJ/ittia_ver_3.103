package je.pense.doro.fourgate.diabetes.dmAutonomic;

import java.awt.Component;		
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableModel;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.fourgate.diabetes.dmAutonomic.ANPdm;

public class ANPdmButtonWest {

    public static Component createButton(String name, JTextArea textArea, JTable table) {
    	
        JButton button = new JButton(name);
        button.addActionListener(e -> performAction(name, textArea, table.getModel(), ANPdm.frame));
        return button;
    }

    public static void performAction(String buttonName, JTextArea textArea, TableModel model, JFrame frame) {
        switch (buttonName) {
            case "Save":
                saveAction(textArea);
                break;
            case "Clear":
                clearAction(textArea, model);
                break;
            case "Copy":
                copyAction(textArea);
                break;
            case "Quit":
            	clearAction(textArea, model);
            	ANPdm.exitFrame(buttonName); // This will close the frame when the button is clicked
            	break;
            case "SelectAll":
                selectAllAction(model);
                break;
            case "ClearAll":
                clearAllAction(model);
                break;
            default:
                System.out.println("Unknown button clicked: " + buttonName);
        }
    }

    private static void saveAction(JTextArea textArea) {
//        System.out.println("Save action performed");
        GDSEMR_frame.setTextAreaText(1, textArea.getText());
    }

    private static void clearAction(JTextArea textArea, TableModel model) {
        textArea.setText("\n  < Autonomic Neuropathy :cd > \n");
        updateCheckboxes(model, false);
    }

    private static void copyAction(JTextArea textArea) {
        String text = textArea.getText();
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

//    public static void exitFrame(JFrame frame) { // Specify ANPdm.JFrame type for clarity
//        int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
//        if (response == JOptionPane.YES_OPTION) {
//            System.out.println("Exit action performed");
//            frame.dispose(); // Close the ANPdm frame
//        }
//    }

    private static void selectAllAction(TableModel model) {
        updateCheckboxesInColumn(model, true, 2);
    }

    private static void clearAllAction(TableModel model) {
        updateCheckboxes(model, false);
    }

    private static void updateCheckboxes(TableModel model, boolean state) {
        if (model != null) {
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    if (model.getColumnClass(j) == Boolean.class) {
                        model.setValueAt(state, i, j);
                    }
                }
            }
        }
    }

    private static void updateCheckboxesInColumn(TableModel model, boolean state, int targetColumn) {
        if (model != null && targetColumn >= 0 && targetColumn < model.getColumnCount()) {
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getColumnClass(targetColumn) == Boolean.class) {
                    model.setValueAt(state, i, targetColumn);
                }
            }
        }
    }
}
