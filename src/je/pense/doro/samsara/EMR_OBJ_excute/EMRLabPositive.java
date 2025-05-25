package je.pense.doro.samsara.EMR_OBJ_excute;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;
import java.awt.event.*;

public class EMRLabPositive extends JFrame implements ActionListener {

    private static final String[] TEST_LABELS = {
        "HAV-Ab IgG:    Negative", "HAV-Ab IgG:    Positive", 
        "HBsAg:    Negative", "HBsAg:    Positive", 
        "HBsAb:    Negative", "HBsAb:    Positive", 
        "HCV Ab:    Negative", "HCV Ab:    Positive", 
        "RPR:    Negative", "RPR:    Positive", 
        "Stool Hb:    Negative", "Stool Hb:    Positive",
        "RF:    Negative", "RF:    Positive"
    };

    private JCheckBox[] checkboxes;
    private JTextArea resultsTextArea;

    public EMRLabPositive() {
        initializeFrame();
        createComponents();
    }

    private void initializeFrame() {
        setTitle("EMR Interface");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setLayout(new BorderLayout());
    }

    private void createComponents() {
        add(createLabelPanel(), BorderLayout.NORTH);
        add(createCheckboxesPanel(), BorderLayout.CENTER);
        add(createResultsPanel(), BorderLayout.SOUTH);
    }

    private JPanel createLabelPanel() {
        JLabel label = new JLabel("Select test results:");
        JPanel labelPanel = new JPanel();
        labelPanel.add(label);
        return labelPanel;
    }

    private JPanel createCheckboxesPanel() {
        JPanel checkboxesPanel = new JPanel(new GridLayout(7, 2));
        checkboxes = new JCheckBox[TEST_LABELS.length];

        for (int i = 0; i < TEST_LABELS.length; i++) {
            checkboxes[i] = new JCheckBox(TEST_LABELS[i]);
            checkboxesPanel.add(checkboxes[i]);
        }

        return checkboxesPanel;
    }

    private JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel(new BorderLayout());

        resultsTextArea = new JTextArea(10, 30);
        resultsTextArea.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(resultsTextArea);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        JButton updateResultsButton = new JButton("Update Results");
        updateResultsButton.addActionListener(this);
        resultsPanel.add(updateResultsButton, BorderLayout.SOUTH);

        return resultsPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateResults();
    }

    private void updateResults() {
        StringBuilder results = new StringBuilder("\n");
        for (JCheckBox checkbox : checkboxes) {
            if (checkbox.isSelected()) {
                results.append("\n\t").append(checkbox.getText()).append("\n");
            }
        }
        resultsTextArea.setText(results.toString());
        GDSEMR_frame.setTextAreaText(5, results.toString());
        dispose(); // Consider if this is really needed as it will close the window
    }

    public static void main(String[] args) {
        EMRLabPositive emrInterface = new EMRLabPositive();
        emrInterface.setVisible(true);
    }
}
