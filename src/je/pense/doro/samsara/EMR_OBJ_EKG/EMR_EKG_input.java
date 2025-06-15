package je.pense.doro.samsara.EMR_OBJ_EKG;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.filecontrol.datetime.Date_current;
import je.pense.doro.entry.EntryDir;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EMR_EKG_input extends JFrame {
    private JCheckBox[] leadCheckboxes;
    private JTextArea summaryArea;
    private EMR_EKG_inputformatPanel inputFormatPanel;

    public EMR_EKG_input() {
        setTitle("EMR EKG Analysis");
        setSize(1300, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // South Panel with buttons
        JPanel southPanel = new JPanel();
        JButton clearButton = new JButton("Clear All");
        JButton saveButton = new JButton("Save");
        JButton quitButton = new JButton("Quit");
        JButton refButton = new JButton("EKG reference");
        southPanel.add(clearButton);
        southPanel.add(saveButton);
        southPanel.add(quitButton);
        southPanel.add(refButton);

        // East Panel with vertical checkboxes
        JPanel eastPanel = new JPanel(new BorderLayout());
        String[] leads = {
            "Normal ECG","Sinus Bradycardia","Sinus Tachycardia","Atrial Fibrillation",
            "Premature Ventricular Contraction (PVC)",
            "Premature Atrial Contraction (PAC)",
            "Left Ventricular Hypertrophy (LVH) ECG pattern",
            "Non-specific ST-T changes",
            "Right Bundle Branch Block (RBBB)","Left Bundle Branch Block (LBBB)",
            "Prolonged QT","ST Elevation","ST Depression","Ventricular Tachycardia (VT)",
            "Wolff-Parkinson-White (WPW) syndrome","Supraventricular Tachycardia (SVT: PSVT, etc.)",
            "Anterior Wall Ischemia / STEMI",
            "Atypical T wave changes",
            "LAE","RAE","LVH","RVH","PTFV1",
            "Junctional rhythm",
            "Supraventricular tachycardia (SVT)",
            "Poor R Wave Progression ",
            "Atrial Flutter"
        };
        
        JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new GridLayout(0, 1)); // Vertical layout
        leadCheckboxes = new JCheckBox[leads.length];
        
        for(int i = 0; i < leads.length; i++) {
            leadCheckboxes[i] = new JCheckBox(leads[i]);
            leadCheckboxes[i].setHorizontalAlignment(SwingConstants.LEFT);
            leadCheckboxes[i].addItemListener(e -> updateSummary());
            checkboxPanel.add(leadCheckboxes[i]);
        }
        
        JScrollPane scrollPane = new JScrollPane(checkboxPanel);
        scrollPane.setPreferredSize(new Dimension(330, 0)); // Set to 30% of 1100 (~330 pixels)
        eastPanel.add(scrollPane, BorderLayout.CENTER);

        // West Panel with Summary and Conclusion TextArea
        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel summaryLabel = new JLabel("Summary and Conclusion:");
        summaryArea = new JTextArea(10, 20);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        JScrollPane summaryScroll = new JScrollPane(summaryArea);
        westPanel.add(summaryLabel, BorderLayout.NORTH);
        westPanel.add(summaryScroll, BorderLayout.CENTER);

        // Central Panel
        JPanel centralPanel = new JPanel(new BorderLayout());
        centralPanel.setBorder(BorderFactory.createTitledBorder("EKG Interpretation Input"));
        inputFormatPanel = new EMR_EKG_inputformatPanel(this); // Pass this as parentFrame
        centralPanel.add(inputFormatPanel, BorderLayout.CENTER);

        // Container for West and Central panels with GridBagLayout
        JPanel mainContentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);

        // West panel: 30% width
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.220; // 30/(30+40) ≈ 0.4286 for 30% of total width
        gbc.weighty = 1.0;
        mainContentPanel.add(westPanel, gbc);

        // Central panel: 40% width
        gbc.gridx = 1;
        gbc.weightx = 0.6; // 40/(30+40) ≈ 0.5714 for 40% of total width
        mainContentPanel.add(centralPanel, gbc);

        // Add components to frame
        add(southPanel, BorderLayout.SOUTH);
        add(eastPanel, BorderLayout.EAST);
        add(mainContentPanel, BorderLayout.CENTER);

        // Button actions
        clearButton.addActionListener(e -> clearFields());
        quitButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> saveData());
        refButton.addActionListener(e -> refFile());
    }

    public void updateSummary() {
        StringBuilder sb = new StringBuilder();
        
        // Append selected lead checkboxes from East panel
        for (JCheckBox cb : leadCheckboxes) {
            if (cb.isSelected()) {
                sb.append("# ").append(cb.getText()).append("\n");
            }
        }

        // Append section titles and their inputs from inputFormatPanel
        Map<String, List<String>> sectionSummary = inputFormatPanel.getSummary();
        for (String section : sectionSummary.keySet()) {
            sb.append("# ").append(section).append("\n");
            for (String item : sectionSummary.get(section)) {
                sb.append("     ").append(item).append("\n");
            }
        }

        // Debug: Log the summary content
        System.out.println("Updating summary: \n" + sb.toString());
        summaryArea.setText(sb.toString());
    }

    private void clearFields() {
        summaryArea.setText("");
        for (JCheckBox cb : leadCheckboxes) {
            cb.setSelected(false);
        }
        inputFormatPanel.clearFields();
        // Debug: Log clear action
        System.out.println("Cleared all fields and summary");
    }

    private void saveData() {
        // Implement actual save logic here
        StringBuilder sb = new StringBuilder("Selected Leads and Parameters:\n");
        for (JCheckBox cb : leadCheckboxes) {
            if (cb.isSelected()) {
                sb.append("• ").append(cb.getText()).append("\n");
            }
        }
        sb.append("\nSummary and Conclusion:\n")
          .append(summaryArea.getText());
        
        // Debug: Log save action
        System.out.println("Saving data: \n" + sb.toString());
//        JOptionPane.showMessageDialog(this, sb.toString());
        GDSEMR_frame.setTextAreaText(5, String.format("\n< EKG >" + Date_current.main("m")+ "\n" + summaryArea.getText()));
    }
    
    private void refFile() {
        try {
            if (!Desktop.isDesktopSupported()) {
                throw new IOException("Desktop API unsupported");
            }
            File file = new File(EntryDir.HOME_DIR + "/samsara/EMR_OBJ_EKG/EKG reference.odt");
            System.out.println("Attempting to open: " + file.getAbsolutePath());
            if (!file.exists()) {
                throw new IOException("File not found: " + file.getAbsolutePath());
            }
            Desktop.getDesktop().open(file);
            System.out.println("Opened: " + file.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Cannot open reference: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EMR_EKG_input emrEkg = new EMR_EKG_input();
            emrEkg.setVisible(true);
        });
    }
}