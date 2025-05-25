package je.pense.doro.samsara.EMR_OBJ_excute;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.filecontrol.datetime.Date_current;

import java.awt.*;
import java.awt.event.KeyEvent;

public class EMR_LDL extends JFrame {
    private static final String[] LIPID_FIELDS = {
        "Total Cholesterol", "HDL-C", "Triglyceride", "LDL-C"
    };
    
    private final JTextField[] inputs = new JTextField[LIPID_FIELDS.length];
    private int currentField = 0;

    public EMR_LDL() {
        setupFrame();
        createUI();
        inputs[0].requestFocus();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("Lipid Profile Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
    }

    private void createUI() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        for (int i = 0; i < LIPID_FIELDS.length; i++) {
            panel.add(new JLabel(LIPID_FIELDS[i] + ": ", SwingConstants.RIGHT));
            panel.add(createInputField(i));
        }
        
        add(panel);
    }

    private JTextField createInputField(int index) {
        JTextField field = new JTextField(10);
        field.setMargin(new Insets(0, 10, 0, 0));
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleEnterKey(index);
                }
            }
        });
        inputs[index] = field;
        return field;
    }

    private void handleEnterKey(int index) {
        if (index < inputs.length - 1) {
            inputs[index + 1].requestFocus();
        } else {
            saveLipidProfile();
            dispose();
        }
    }

    private void saveLipidProfile() {
        String result = formatLipidResult();
        updateEMRFrame(result);
    }

    private String formatLipidResult() {
        return String.format("TC-HDL-Tg-LDL [ %s - %s - %s - %s ] mg/dL",
            inputs[0].getText(), inputs[1].getText(),
            inputs[2].getText(), inputs[3].getText());
    }

    private void updateEMRFrame(String result) {
        String currentDate = Date_current.main("d");
        GDSEMR_frame.setTextAreaText(5, "\n" + result);
        GDSEMR_frame.setTextAreaText(7, "\n>  " + result + "   " + currentDate);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EMR_LDL::new);
    }
}
