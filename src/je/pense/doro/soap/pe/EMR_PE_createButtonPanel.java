package je.pense.doro.soap.pe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import je.pense.doro.GDSEMR_frame;

public class EMR_PE_createButtonPanel extends EMR_PE_general {

    public static JPanel createButtonPanel(final JTextArea outputTextArea, final JCheckBox clearCheckbox, final JCheckBox[] checkboxes, JCheckBox[][] subcategories) {
        JPanel buttonPanel = new JPanel();

        String[] buttonNames = {"Clear", "Save", "Save and Quit"};

        for (int i = 0; i < buttonNames.length; i++) {
            JButton button = new JButton(buttonNames[i]);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String buttonText = button.getText();
                    if (buttonText.equals("Clear")) {
                        clearButtonClicked(outputTextArea, clearCheckbox, checkboxes, subcategories);
                    } else if (buttonText.equals("Save")) {
                        GDSEMR_frame.setTextAreaText(6, "\n" + outputTextArea.getText());
                        clearButtonClicked(outputTextArea, clearCheckbox, checkboxes, subcategories);
                    } else if (buttonText.equals("Save and Quit")) {
                        GDSEMR_frame.setTextAreaText(6, "\n" + outputTextArea.getText());
                        clearButtonClicked(outputTextArea, clearCheckbox, checkboxes, subcategories);
                        EMR_PE_general emrPeGeneral = (EMR_PE_general) SwingUtilities.getWindowAncestor(outputTextArea);
                        emrPeGeneral.dispose();
                    } else {
                        System.out.println(buttonText + " button clicked");
                    }
                }
            });
            buttonPanel.add(button);
        }

        return buttonPanel;
    }

    private static void clearButtonClicked(JTextArea outputTextArea, JCheckBox clearCheckbox, JCheckBox[] checkboxes, JCheckBox[][] subcategories) {
        outputTextArea.setText(""); // Clear the outputTextArea
        clearCheckbox.setSelected(false); // Uncheck the clearCheckbox
        for (JCheckBox checkbox : checkboxes) {
            checkbox.setSelected(false); // Uncheck all checkboxes
        }
        for (JCheckBox[] subcategoryArray : subcategories) {
            for (JCheckBox subcategory : subcategoryArray) {
                subcategory.setSelected(false); // Uncheck all subcategory checkboxes
            }
        }
    }
}
