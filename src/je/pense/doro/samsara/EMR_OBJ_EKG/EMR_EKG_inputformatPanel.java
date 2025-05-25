package je.pense.doro.samsara.EMR_OBJ_EKG;

import javax.swing.*;	
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EMR_EKG_inputformatPanel extends JPanel {
    private List<JTextField> textFields = new ArrayList<>();
    private List<String> fieldLabels = new ArrayList<>();
    private List<JCheckBox> checkBoxes = new ArrayList<>();
    private Map<JTextField, String> textFieldToSection = new HashMap<>();
    private Map<JCheckBox, String> checkBoxToSection = new HashMap<>();
    private List<String> sectionTitles = new ArrayList<>();
    private EMR_EKG_input parentFrame;

    public EMR_EKG_inputformatPanel(EMR_EKG_input parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Section: Patient Information
        addSection(formPanel, gbc, "🩺 Patient Information", row++);
        addField(formPanel, gbc, "Name:", row++, "🩺 Patient Information");
        addField(formPanel, gbc, "Date/Time:", row++, "🩺 Patient Information");
        addField(formPanel, gbc, "Age/Sex:", row++, "🩺 Patient Information");
        addField(formPanel, gbc, "Clinical Context:", row++, "🩺 Patient Information");

        // Section: Rhythm
        addSection(formPanel, gbc, "1. Rhythm", row++);
        addCheckGroup(formPanel, gbc, new String[]{"Regular", "Irregular"}, row++, "1. Rhythm");
        addCheckGroup(formPanel, gbc, new String[]{"Atrial fibrillation", "Atrial Flutter","Ectopy"}, row++, "1. Rhythm");
        addField(formPanel, gbc, "R-R intervals : ", row++, "1. Rhythm");
//        addField(formPanel, gbc, "Irregular pattern noted (e.g., AFib, flutter, ectopy):", row++, "1. Rhythm");

        // Section: Heart Rate
        addSection(formPanel, gbc, "2. Heart Rate (HR)", row++);
        addCheckGroup(formPanel, gbc, new String[]{"1500 Method", "6-second Rule", "Count R-R with ruler"}, row++, "2. Heart Rate (HR)");
        addField(formPanel, gbc, "Rate (bpm) : ", row++, "2. Heart Rate (HR)");

        // Section: P Waves
        addSection(formPanel, gbc, "3. P Waves", row++);
        addCheckGroup(formPanel, gbc, new String[]{"Present before each QRS", "Morphology consistent", "Absent or abnormal"}, row++, "3. P Waves");

        // Section: PR Interval
        addSection(formPanel, gbc, "4. PR Interval", row++);
        addField(formPanel, gbc, "Measured PR Interval (0.12-0.20 sec) : ", row++, "4. PR Interval");
        addCheckGroup(formPanel, gbc, new String[]{"Normal (0.12–0.20 sec)"}, row++, "4. PR Interval");
        addCheckGroup(formPanel, gbc, new String[]{"Prolonged → Suspect AV Block"}, row++, "4. PR Interval");
        addCheckGroup(formPanel, gbc, new String[]{"Shortened → Suspect pre-excitation"}, row++, "4. PR Interval");
        // Section: QRS Duration
        addSection(formPanel, gbc, "5. QRS Duration", row++);
        addField(formPanel, gbc, "Measured QRS (< 0.12 sec) : ", row++, "5. QRS Duration");
        addCheckGroup(formPanel, gbc, new String[]{"Normal (< 0.12 sec)", "Prolonged → Consider BBB or ventricular rhythm"}, row++, "5. QRS Duration");

        // Section: Ectopic or Early Beats
        addSection(formPanel, gbc, "6. Ectopic or Early Beats", row++);
        addCheckGroup(formPanel, gbc, new String[]{"None","PACs", "PJCs", "PVCs"}, row++, "6. Ectopic or Early Beats");
        addCheckGroup(formPanel, gbc, new String[]{"Bigeminy", "Trigeminy", "Couplets"}, row++, "6. Ectopic or Early Beats");

        // Section: R Wave Progression
        addSection(formPanel, gbc, "7. R Wave Progression (V1–V6)", row++);
        addCheckGroup(formPanel, gbc, new String[]{"Normal", "Poor R wave progression"}, row++, "7. R Wave Progression (V1–V6)");
        addField(formPanel, gbc, "Transition zone (e.g., V3):", row++, "7. R Wave Progression (V1–V6)");

        // Section: ST Segment
        addSection(formPanel, gbc, "8. ST Segment", row++);
        addCheckGroup(formPanel, gbc, new String[]{"Normal"}, row++, "8. ST Segment");
        addField(formPanel, gbc, "Elevated (leads)  : ", row++, "8. ST Segment");
        addField(formPanel, gbc, "Depressed (leads)  : ", row++, "8. ST Segment");
        addCheckGroup(formPanel, gbc, new String[]{"Concave", "Convex", "Horizontal"}, row++, "8. ST Segment");

        // Section: Q Waves
        addSection(formPanel, gbc, "9. Q Waves", row++);
        addCheckGroup(formPanel, gbc, new String[]{"Normal", "Pathological"}, row++, "9. Q Waves");
        addField(formPanel, gbc, "Leads Affected:", row++, "9. Q Waves");

        // Section: T Waves
        addSection(formPanel, gbc, "10. T Waves", row++);
        addCheckGroup(formPanel, gbc, new String[]{"Upright in most leads", "Inverted", "Peaked", "Biphasic"}, row++, "10. T Waves");
        addField(formPanel, gbc, "Leads Affected : ", row++, "10. T Waves");

        // Section: U Waves
        addSection(formPanel, gbc, "11. U Waves", row++);
        addCheckGroup(formPanel, gbc, new String[]{"Not visible", "Present", "Prominent in V2–V3"}, row++, "11. U Waves");
        addCheckGroup(formPanel, gbc, new String[]{"Consider: Hypokalemia", "Bradycardia"}, row++, "11. U Waves");

        // Section: Signs of Ischemia or Infarction
        addSection(formPanel, gbc, "12. Signs of Ischemia or Infarction", row++);
        addCheckGroup(formPanel, gbc, new String[]{"Not visible"}, row++, "12. Signs of Ischemia or Infarction");
        addCheckGroup(formPanel, gbc, new String[]{"ST Depression (Ischemia)", "ST Elevation (Infarction)", "Q Waves (Old infarct)"}, row++, "12. Signs of Ischemia or Infarction");
        addCheckGroup(formPanel, gbc, new String[]{"Anterior", "Inferior", "Lateral", "Posterior"}, row++, "12. Signs of Ischemia or Infarction");

        // Section: Final Interpretation / Summary
        addSection(formPanel, gbc, "13. Final Interpretation / Summary", row++);
        addField(formPanel, gbc, "Rhythm:", row++, "13. Final Interpretation / Summary");
        addField(formPanel, gbc, "Rate (bpm):", row++, "13. Final Interpretation / Summary");
        addField(formPanel, gbc, "Axis:", row++, "13. Final Interpretation / Summary");
        addField(formPanel, gbc, "PR:", row++, "13. Final Interpretation / Summary");
        addField(formPanel, gbc, "QRS:", row++, "13. Final Interpretation / Summary");
        addField(formPanel, gbc, "QT/QTc:", row++, "13. Final Interpretation / Summary");
        addField(formPanel, gbc, "Abnormal Findings:", row++, "13. Final Interpretation / Summary");
        addField(formPanel, gbc, "Possible Diagnosis:", row++, "13. Final Interpretation / Summary");
        addField(formPanel, gbc, "Recommendations / Action:", row++, "13. Final Interpretation / Summary");

        // Make the form scrollable
        JScrollPane scrollPane = new JScrollPane(formPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        // Add ItemListener to all checkboxes
        for (JCheckBox cb : checkBoxes) {
            cb.addItemListener(e -> {
                System.out.println("Checkbox " + cb.getText() + " state changed: " + cb.isSelected());
                if (parentFrame != null) {
                    parentFrame.updateSummary();
                }
            });
        }

        // Add DocumentListener to all text fields
        for (JTextField tf : textFields) {
            tf.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateSummary();
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateSummary();
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateSummary();
                }
                private void updateSummary() {
                    System.out.println("Text field changed: " + tf.getText());
                    if (parentFrame != null) {
                        parentFrame.updateSummary();
                    }
                }
            });
        }
    }

    private void addSection(JPanel panel, GridBagConstraints gbc, String section, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JLabel label = new JLabel(section);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 15f));
        panel.add(label, gbc);
        gbc.gridwidth = 1;
        sectionTitles.add(section);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, int row, String section) {
        gbc.gridx = 0;
        gbc.gridy = row;
        JLabel jLabel = new JLabel(label);
        panel.add(jLabel, gbc);
        gbc.gridx = 1;
        JTextField field = new JTextField(25);
        field.setHorizontalAlignment(JTextField.LEFT);
        panel.add(field, gbc);
        textFields.add(field);
        fieldLabels.add(label);
        textFieldToSection.put(field, section);
    }

    private void addCheckGroup(JPanel panel, GridBagConstraints gbc, String[] labels, int row, String section) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        for (String label : labels) {
            JCheckBox cb = new JCheckBox(label);
            groupPanel.add(cb);
            checkBoxes.add(cb);
            checkBoxToSection.put(cb, section);
        }
        panel.add(groupPanel, gbc);
        gbc.gridwidth = 1;
    }

    public Map<String, List<String>> getSummary() {
        Map<String, List<String>> sectionSummary = new LinkedHashMap<>();

        // Initialize lists for each section
        for (String section : sectionTitles) {
            sectionSummary.put(section, new ArrayList<>());
        }

        // Collect checkbox inputs
        for (JCheckBox cb : checkBoxes) {
            if (cb.isSelected()) {
                String section = checkBoxToSection.get(cb);
                sectionSummary.get(section).add(cb.getText());
            }
        }

        // Collect text field inputs
        for (int i = 0; i < textFields.size(); i++) {
            String text = textFields.get(i).getText().trim();
            if (!text.isEmpty()) {
                String section = textFieldToSection.get(textFields.get(i));
                sectionSummary.get(section).add(fieldLabels.get(i) + " " + text);
            }
        }

        // Remove sections with no inputs
        sectionSummary.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        // Debug: Log summary content
        System.out.println("getSummary() returned: " + sectionSummary);
        return sectionSummary;
    }

    public void clearFields() {
        for (JTextField tf : textFields) {
            tf.setText("");
        }
        for (JCheckBox cb : checkBoxes) {
            cb.setSelected(false);
        }
        // Debug: Log clear action
        System.out.println("Cleared panel fields");
    }

    // For demonstration/testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("12-Lead EKG Interpretation Input Format");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new EMR_EKG_inputformatPanel(null));
            frame.setSize(900, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}