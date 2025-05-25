package je.pense.doro.soap.pe;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class EMR_PE_general extends JFrame {
    public static JTextArea outputTextArea;
    private JCheckBox[] checkboxes;
    private JCheckBox[][] subcategories;
    private String[] checkboxLabels = {
            "< Inspect the eyes >",
            "< Palpation of the neck >",
            "< Auscultation of the Chest >",
            "< Auscultation of the Heart >",
            "< Assessment of Peripheral Edema >",
            "< Inspect the skin for any abnormalities >",
    };

    private String[][] subcategoryLabels = {
            {"not anemic / not icteric", "slightly anemic / not icteric",                   	
                    "No redness, swelling, discharge, abnormal eye movements, or changes in vision.",
                    "No abnormal findings were observed during the assessment of pupil size, shape, and reactivity to light."},
            {"No masses, lymph node enlargement, or tenderness. Check for range of motion and stiffness.",
                    "masses", "lymph node enlargement", "tenderness","abnormal range of motion and stiffness"},
            {"The presence of normal vesicular breath sounds, as well as any abnormal sounds such as crackles, wheezes, or decreased breath sounds.",
                    "crackles", "wheezes", "decreased breath sounds"},
            {"No additional heart sounds, such as murmurs or extra heart sounds.",
                    "additional heart sounds", "murmurs","skipped beats or abnormal rhythms"},
            {"No signs of peripheral edema in the ankles and legs.","peripheral edema in ankle and foot","peripheral edema in legs"},
    		{
    		    "No abnormalities of significant skin lesions.",
    		    "macules", "papules", "nodules", 
    		    "plaques", 
    		    "vesicles", 
    		    "bullae", 
    		    "pustules", 
    		    "wheals", 
    		    "cysts", 
    		    "ulcers", 
    		    "fissures", 
    		    "crusts", 
    		    "scales", 
    		    "erosions", 
    		    "telangiectasias"
    		},

    };

    public EMR_PE_general() {
        setTitle("Weight Loss Checklist");
        setSize(1250, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel checkboxesPanel = new JPanel();
        checkboxesPanel.setLayout(new BoxLayout(checkboxesPanel, BoxLayout.Y_AXIS));
        mainPanel.add(checkboxesPanel, BorderLayout.CENTER);

        checkboxes = new JCheckBox[checkboxLabels.length];
        subcategories = new JCheckBox[checkboxLabels.length][];

        // Create checkboxes and subcategories using array iteration
        for (int i = 0; i < checkboxLabels.length; i++) {
            checkboxes[i] = new JCheckBox(checkboxLabels[i]);
            checkboxesPanel.add(checkboxes[i]);

            subcategories[i] = new JCheckBox[subcategoryLabels[i].length];
            JPanel subcategoryPanel = new JPanel();
            subcategoryPanel.setLayout(new BoxLayout(subcategoryPanel, BoxLayout.Y_AXIS));
            subcategoryPanel.setBorder(new EmptyBorder(0, 40, 0, 0)); // Add left margin
            
            for (int j = 0; j < subcategoryLabels[i].length; j++) {
                subcategories[i][j] = new JCheckBox(subcategoryLabels[i][j]);
                subcategories[i][j].setEnabled(false); // Disable subcategories by default
                subcategories[i][j].addActionListener(e -> {
                    updateOutputText(); // Update output when subcategory checkbox is clicked
                });
                subcategoryPanel.add(subcategories[i][j]);
            }
            checkboxesPanel.add(subcategoryPanel);

            // Disable subcategories by default
            disableSubcategories(i);
        }

        // Add action listeners to enable/disable subcategories and update output
        for (int i = 0; i < checkboxLabels.length; i++) {
            int index = i;
            checkboxes[i].addActionListener(e -> {
                boolean isSelected = checkboxes[index].isSelected();
                if (isSelected) {
                    enableSubcategories(index);
                } else {
                    disableSubcategories(index);
                }
                updateOutputText();
            });
        }

        JScrollPane checkboxesScrollPane = new JScrollPane(checkboxesPanel);
        mainPanel.add(checkboxesScrollPane, BorderLayout.CENTER);

        // Add output text area
        outputTextArea = new JTextArea();
        outputTextArea.setEditable(true);
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        outputScrollPane.setPreferredSize(new Dimension(350, checkboxesPanel.getPreferredSize().height));
        mainPanel.add(outputScrollPane, BorderLayout.WEST);

        // Add button panel
        JCheckBox clearCheckbox = new JCheckBox();
        
        // Call the createButtonPanel method with the JTextArea and JCheckBox objects
        JPanel buttonPanel = EMR_PE_createButtonPanel.createButtonPanel(outputTextArea, clearCheckbox, checkboxes, subcategories);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void enableSubcategories(int index) {
        for (JCheckBox subcategory : subcategories[index]) {
            subcategory.setEnabled(true);
        }
    }

    private void disableSubcategories(int index) {
        for (JCheckBox subcategory : subcategories[index]) {
            subcategory.setEnabled(false);
        }
    }

    private void updateOutputText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < checkboxes.length; i++) {
            if (checkboxes[i].isSelected()) {
                sb.append("   ").append(checkboxLabels[i]).append("\n");
                for (int j = 0; j < subcategories[i].length; j++) {
                	
                	String subcategory = subcategoryLabels[i][j];

                    if (subcategories[i][j].isSelected()) {
                        if (!subcategory.startsWith("No") && !subcategory.startsWith("The")) {
                            sb.append("      [ ✔ ] ").append(subcategory).append("\n");
                        }else {
                        		sb.append("       ").append(subcategory).append("\n");
                        }
                        }
                }
            }
        }
        outputTextArea.setText(sb.toString());
        System.out.println("outputTextArea.setText(sb.toString());\n" + sb.toString());
    }

    public void dispoaePE() {
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EMR_PE_general();
        });
    }
}
