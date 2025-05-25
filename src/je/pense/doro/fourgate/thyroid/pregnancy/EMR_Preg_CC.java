package je.pense.doro.fourgate.thyroid.pregnancy;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;
import java.awt.event.KeyEvent;

public class EMR_Preg_CC extends JFrame {
    private static final String[] FIELDS = {
        "Pregnancy #:", "Weeks:", "Due Date:", "Diagnosis:", "transferred from GY:"
    };
    
    private static final String[][] CONVERSION_MAP = {
        // Diagnosis codes
        {"o", "Hypothyroidism Diagnosed"},
        {"e", "Hyperthyroidism diagnosed"},
        {"n", "TFT abnormality"},
        // Hospital codes
        {"c", "청담마리 산부인과"},
        {"d", "도곡함춘 산부인과"},
        {"o", "기타 산부인과"}
    };

    private final JTextField[] inputs = new JTextField[FIELDS.length];

    public EMR_Preg_CC() {
        setupFrame();
        createUI();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("Thyroid Pregnancy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new GridLayout(0, 1));
        
        // Create input fields
        for (int i = 0; i < FIELDS.length; i++) {
            mainPanel.add(createInputRow(i));
        }
        
        // Add submit button
        JButton submitBtn = new JButton("Add Pregnancy");
        submitBtn.addActionListener(e -> {
            processAndSaveData();
            dispose();
        });
        mainPanel.add(submitBtn);

        add(mainPanel);
        pack();
    }

    private JPanel createInputRow(int index) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField field = new JTextField(10);
        
        field.setHorizontalAlignment(SwingConstants.CENTER);
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && index < FIELDS.length - 1) {
                    inputs[index + 1].requestFocus();
                }
            }
        });
        
        inputs[index] = field;
        row.add(new JLabel(FIELDS[index]));
        row.add(field);
        return row;
    }

    private void processAndSaveData() {
        String[] values = new String[inputs.length];
        for (int i = 0; i < inputs.length; i++) {
            values[i] = inputs[i].getText().trim();
        }

        String formattedResult = formatPregnancyData(values);
        updateEMRFrame(formattedResult);
    }

    private String formatPregnancyData(String[] data) {
        return String.format("# %s pregnancy  %s weeks  Due-date %s \n\t%s at %s%n",
            data[0],
            data[1],
            data[2],
            convertCode(data[3], 0, 2),    // Diagnosis conversion
            convertCode(data[4], 3, 5));    // Hospital conversion
    }

    private String convertCode(String code, int startIndex, int endIndex) {
        for (int i = startIndex; i <= endIndex; i++) {
            if (CONVERSION_MAP[i][0].equals(code)) {
                return CONVERSION_MAP[i][1];
            }
        }
        return "Unknown code: " + code;
    }

    private void updateEMRFrame(String result) {
        System.out.println(result);  // For debugging
        GDSEMR_frame.setTextAreaText(0, result);
        GDSEMR_frame.setTextAreaText(7, result);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EMR_Preg_CC::new);
    }
}
