package je.pense.doro.samsara.EMR_OBJ_excute;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.filecontrol.datetime.Date_current;

import java.awt.*;

public class EMR_eGFR extends JFrame {
    private static final String[] LABELS = {
        "Creatinie : ", "eGFR : ", "Albumin / Creatinie ratio : "
    };
    
    private static final String[][] GFR_CATEGORIES = {
        {"90", "G1  : Normal GFR"},
        {"60", "G2  : Mildly decreased GFR"},
        {"45", "G3a : Mildly to moderately decreased GFR"},
        {"30", "G3b : Moderate to severely decreased GFR"},
        {"15", "G4  : Severely decreased GFR"},
        {"0",  "G5  : Kidney failure"}
    };
    
    private final JTextField[] inputs = new JTextField[LABELS.length];

    public EMR_eGFR() {
        setupFrame();
        createUI();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("EMR GFR Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
    }

    private void createUI() {
        add(createInputPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        
        for (int i = 0; i < LABELS.length; i++) {
            panel.add(new JLabel(LABELS[i], JLabel.RIGHT));
            inputs[i] = createInputField(i);
            panel.add(inputs[i]);
        }
        return panel;
    }

    private JTextField createInputField(int index) {
        JTextField field = new JTextField();
        field.setHorizontalAlignment(JTextField.CENTER);
        field.addActionListener(e -> handleEnterKey(index));
        return field;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(createButton("Submit", () -> processInput()));
        panel.add(createButton("Clear", () -> clearInputs()));
        return panel;
    }

    private JButton createButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        return button;
    }

    private void handleEnterKey(int currentField) {
        if (currentField < inputs.length - 1) {
            inputs[currentField + 1].requestFocus();
        } else {
            processInput();
        }
    }

    private void processInput() {
        String cr = inputs[0].getText().trim();
        String egfr = inputs[1].getText().trim();
        String ac = inputs[2].getText().trim();

        StringBuilder result = new StringBuilder("\n");
        result.append("\t").append(cr).append("\tCreatinie (mg/dl)\n");
        
        if (!egfr.isEmpty()) {
            result.append("\t").append(egfr).append("\teGFR ( mL/min/1.73m²)\n")
                  .append("\t").append(ac).append("\t+A/C ratio (mg/g)\n");
            
            String classification = calculateClassification(
                Double.parseDouble(cr),
                Double.parseDouble(egfr),
                Double.parseDouble(ac)
            );
            
            updateEMRFrame(result.toString(), classification);
        }
        
        clearInputs();
        dispose();
    }

    private String calculateClassification(double cr, double egfr, double acRatio) {
        StringBuilder result = new StringBuilder();
        
        // A/C Ratio Classification
        if (acRatio < 30) {
            result.append("\tA1  : Normal to mildly increased A/C_ratio\n");
        } else if (acRatio <= 300) {
            result.append("\tA2  : Moderately increased A/C_ratio\n");
        } else {
            result.append("\tA3  : Severely increased A/C_ratio\n");
        }
        
        // GFR Classification
        for (String[] category : GFR_CATEGORIES) {
            if (egfr >= Double.parseDouble(category[0])) {
                result.append("\t").append(category[1]);
                break;
            }
        }
        
        return result.toString();
    }

    private void updateEMRFrame(String details, String classification) {
        GDSEMR_frame.setTextAreaText(5, "\n\t..........\n" + details + classification);
        
        String[] codes = classification.split("\n");
        String codeString = codes[0].substring(0, codes[0].indexOf(':')) + " " +
                          codes[1].substring(0, codes[1].indexOf(':'));
        
        GDSEMR_frame.setTextAreaText(7, String.format("\n>  CKD [ %s ] %s", 
            codeString.trim(), Date_current.main("m")));
    }

    private void clearInputs() {
        for (JTextField field : inputs) {
            field.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EMR_eGFR::new);
    }
}
