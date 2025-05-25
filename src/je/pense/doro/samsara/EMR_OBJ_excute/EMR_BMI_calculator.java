package je.pense.doro.samsara.EMR_OBJ_excute;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.filecontrol.datetime.Date_current;

import java.awt.*;

public class EMR_BMI_calculator extends JFrame {
    private static final String[] FIELDS = {"Height (cm): ", "Weight (kg): ", "Waist (cm or inch): "};
    private static final Dimension FIELD_SIZE = new Dimension(15, 30);
    private final JTextField[] inputs = new JTextField[FIELDS.length];

    public EMR_BMI_calculator() {
        setUndecorated(true); // Remove title bar and border
        setupFrame();
        createUI();
        setVisible(true);
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 160);
        setLocation(
            Toolkit.getDefaultToolkit().getScreenSize().width - 300,
            120 // Moved from 60 to 160 pixels from top
        );
    }

    private void createUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i < FIELDS.length; i++) {
            addInputRow(panel, gbc, i);
        }

        addSaveButton(panel, gbc);
        add(panel);
    }

    private void addInputRow(JPanel panel, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(FIELDS[row]), gbc);

        JTextField field = createTextField();
        inputs[row] = field;
        gbc.gridx = 1;
        panel.add(field, gbc);

        field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    handleEnterKey(row);
                }
            }
        });
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(10);
        field.setPreferredSize(FIELD_SIZE);
        field.setHorizontalAlignment(SwingConstants.CENTER);
        return field;
    }

    private void addSaveButton(JPanel panel, GridBagConstraints gbc) {
        JButton saveButton = new JButton("Save & Quit");
        saveButton.addActionListener(e -> dispose());
        gbc.gridx = 1;
        gbc.gridy = FIELDS.length;
        gbc.gridwidth = 2;
        panel.add(saveButton, gbc);
    }

    private void handleEnterKey(int currentField) {
        if (currentField < inputs.length - 1) {
            inputs[currentField + 1].requestFocusInWindow();
        } else {
            calculateBMI();
        }
    }

    private void calculateBMI() {
        try {
            double height = Double.parseDouble(inputs[0].getText());
            double weight = Double.parseDouble(inputs[1].getText());
            double bmi = weight / Math.pow(height / 100, 2);

            String bmiCategory = getBMICategory(bmi);
            String waistMeasurement = processWaistMeasurement();

            updateEMRFrame(bmi, height, weight, bmiCategory, waistMeasurement);

            dispose();
            EMR_BMI_calculator.main(null);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers");
        }
    }

    private String processWaistMeasurement() {
        String waist = inputs[2].getText().trim();
        if (waist.isEmpty()) return "";

        if (waist.contains("i")) {
            double inches = Double.parseDouble(waist.replace("i", ""));
            return String.format("%.1f", inches * 2.54);
        }
        return waist;
    }

    private String getBMICategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25) return "Healthy weight";
        if (bmi < 30) return "Overweight";
        return "BMI Category";
    }

    private void updateEMRFrame(double bmi, double height, double weight,
                                String category, String waist) {
        String bmiText = String.format("%s : BMI: [ %.2f ]kg/m^2", category, bmi);
        String details = String.format("\n< BMI >\n%s\nHeight : %.1f cm   Weight : %.1f kg%s",
            bmiText, height, weight,
            waist.isEmpty() ? "" : "   Waist: " + waist + " cm");

        GDSEMR_frame.setTextAreaText(5, details);
        GDSEMR_frame.setTextAreaText(7, "\n# " + bmiText + "    " + Date_current.main("m"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EMR_BMI_calculator::new);
    }
}
