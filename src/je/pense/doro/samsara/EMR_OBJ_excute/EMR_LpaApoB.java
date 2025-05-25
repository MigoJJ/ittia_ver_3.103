package je.pense.doro.samsara.EMR_OBJ_excute;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;

public class EMR_LpaApoB extends JFrame {
    private static final String[] FIELDS = {
        "Lipoprotein(a)", "ApoproteinB"
    };
    
    private final JTextField[] inputs = new JTextField[FIELDS.length];
    private int currentField = 0;

    public EMR_LpaApoB() {
        setupFrame();
        createUI();
        inputs[0].requestFocus();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("Lp(a) ApoB Profile Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
    }

    private void createUI() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        
        for (int i = 0; i < FIELDS.length; i++) {
            panel.add(createLabel(FIELDS[i]));
            panel.add(createInputField(i));
        }
        
        add(panel);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text + ": ");
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private JTextField createInputField(int index) {
        JTextField field = new JTextField(10);
        field.setMargin(new Insets(0, 10, 0, 0));
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
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
            saveResults();
            dispose();
        }
    }

    private void saveResults() {
        try {
            double lpa = Double.parseDouble(inputs[0].getText());
            double apoB = Double.parseDouble(inputs[1].getText());
            
            String result = String.format(
                "\nLiporpotein(a)  [ %.1f ] ≤ 30.0 mg/dL\n" +
                "ApoLiporpotein(B) [ %.1f ] M:46-174 F:46-142 mg/dL",
                lpa, apoB);
                
            GDSEMR_frame.setTextAreaText(5, result);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EMR_LpaApoB::new);
    }
}
