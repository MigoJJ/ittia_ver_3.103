
package je.pense.doro.soap.pmh;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import je.pense.doro.GDSEMR_frame;

public class EMRPMH extends JFrame {
    private JTextArea textArea1, textArea2;
    private JButton clearButton, copyButton, saveButton, quitButton;
    private JCheckBox[] checkBoxes;
    private String[] conditions = {
        "Hypertension", "Dyslipidemia", "Diabetes Mellitus",
        "Cancer", "Operation", "Thyroid Disease",
        "Asthma", "Pneumonia", "Tuberculosis",
        "GERD","Hepatitis A / B","Gout",
        "Arthritis", "Hearing Loss", "Parkinson's Disease",
        "CVA", "Depression", "Cognitive Disorder",
        "AMI","Angina Pectoris", "Arrhythmia",
        "Allergy", "Food", "Injection", "Medication","All denied allergies..."
    };

    public EMRPMH() {
        setTitle("EMR PMH");
        setSize(800, 820);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // Text areas initialization
        textArea1 = new JTextArea(3, 10);
        textArea2 = new JTextArea(11, 50);
        setupTextArea(textArea1);
        setupTextArea(textArea2);
        textArea2.setEditable(false);  // textArea2 for display only
        setTextArea2Content();  // Set the predefined text

        // Checkboxes setup
        JPanel checkBoxPanel = new JPanel(new GridLayout(0, 3));  // 3 columns
        checkBoxes = new JCheckBox[conditions.length];
        for (int i = 0; i < conditions.length; i++) {
            checkBoxes[i] = new JCheckBox(conditions[i]);
            checkBoxes[i].addItemListener(new CheckBoxItemListener());
            checkBoxPanel.add(checkBoxes[i]);
        }

        // Buttons setup
        clearButton = new JButton("Clear");
        copyButton = new JButton("Copy");
        saveButton = new JButton("Save");
        quitButton = new JButton("Quit");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(clearButton);
        buttonPanel.add(copyButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(quitButton);

        // Main layout setup
        JPanel textAreaPanel = new JPanel(new GridLayout(2, 1));
        textAreaPanel.add(new JScrollPane(textArea1));
        textAreaPanel.add(new JScrollPane(textArea2));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(textAreaPanel, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(checkBoxPanel), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        configureButtonActions();
    }

    private void setupTextArea(JTextArea textArea) {
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }

    private void setTextArea2Content() {
        textArea2.setFont(new Font("Consolas", Font.PLAIN, 12));  // Use a monospaced font for alignment
        textArea2.setText(
            "    □ Dyslipidemia     \t□ Hypertension     \t□ Diabetes Mellitus\n" +
            "    □ Cancer           \t□ Operation        \t□ Thyroid Disease\n" +
            "    □ Asthma           \t□ Pneumonia        \t□ Tuberculosis\n" +
            "    □ GERD             \t□ Hepatitis A / B  \t□ Gout\n" +
            "    □ Arthritis        \t□ Hearing Loss     \t□ Parkinson's Disease\n" +
            "    □ CVA              \t□ Depression       \t□ Cognitive Disorder\n" +
            "    □ AMI              \t□ Angina Pectoris  \t□ Arrhythmia\n" +
            "    □ Allergy          \t□ All denied allergies...\n" +
            "    □ Food             \t□ Injection        \t□ Medication\n"
        );
    }

    private void configureButtonActions() {
        clearButton.addActionListener(this::clearAction);
        copyButton.addActionListener(this::copyAction);
        saveButton.addActionListener(this::saveAction);
        quitButton.addActionListener(e -> dispose());
    }

    private void clearAction(ActionEvent e) {
        textArea1.setText("");
        textArea2.setText("");  // Clear both text areas
        setTextArea2Content();  // Reset predefined text
        for (JCheckBox checkBox : checkBoxes) {
            checkBox.setSelected(false);
        }
    }

    private void copyAction(ActionEvent e) {
        StringSelection stringSelection = new StringSelection(
            textArea1.getText() + "\n" + textArea2.getText() + "\nSelected Conditions:\n" +
            String.join("\n", getSelectedConditions())
        );
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
    }

    private void saveAction(ActionEvent e) {
        // Get text from text areas and add separators using StringBuilder
        StringBuilder textArea1Content = new StringBuilder();
        textArea1Content.append("-------------------------\n")
                        .append(textArea1.getText())
                        .append("\n--------------------------");
        
        StringBuilder textArea2Content = new StringBuilder();
        textArea2Content.append("-------------------------\n")
                        .append(textArea2.getText())
                        .append("\n-------------------------------------------------");

        // Check if "All denied allergies" exists within the content
        String searchPhrase = "All denied allergies";
        if (textArea1Content.toString().contains(searchPhrase)) {
            // Replace only the specific phrase while keeping other content
            String replacement = "Allergy\n   During the medical check-up, the patient had no known allergies\n   "
                                 + "to food, injections and medications as of " +":cd ";
            int start = textArea1Content.indexOf(searchPhrase);
            int end = start + searchPhrase.length();
            textArea1Content.replace(start, end, replacement);
        }

        // Update GDSEMR_frame text areas on the EDT for thread safety
        SwingUtilities.invokeLater(() -> {
            GDSEMR_frame.setTextAreaText(7, textArea1Content.toString());
            GDSEMR_frame.setTextAreaText(3, textArea2Content.toString());
        });
    }



    private String getCheckedItemsText() {
        StringBuilder sb = new StringBuilder();
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                sb.append("▣ ").append(checkBox.getText()).append("\n");
            }
        }
        return sb.toString();
    }

    private java.util.List<String> getSelectedConditions() {
        java.util.List<String> selectedConditions = new java.util.ArrayList<>();
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                selectedConditions.add(checkBox.getText());
            }
        }
        return selectedConditions;
    }

    // Listener class for checkboxes
    private class CheckBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox source = (JCheckBox) e.getItem();
            if (e.getStateChange() == ItemEvent.SELECTED) {
                textArea1.append("   ▣ " + source.getText() + "\n");
                updateTextArea2(source.getText(), "□ ", "▣ ");
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                textArea1.setText(textArea1.getText().replace("   ▣ " + source.getText() + "\n", ""));
                updateTextArea2(source.getText(), "▣ ", "□ ");
            }
        }

        private void updateTextArea2(String text, String oldPrefix, String newPrefix) {
            String currentText = textArea2.getText();
            String newText = currentText.replace(oldPrefix + text, newPrefix + text);
            textArea2.setText(newText);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EMRPMH frame = new EMRPMH();
            frame.setVisible(true);
        });
    }
}
