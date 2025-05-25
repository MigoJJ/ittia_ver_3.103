package je.pense.doro.soap.fu;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;
import java.awt.event.ActionEvent;

public class FUplan extends JFrame {
    private JTextField fuField;
    private JTextField medsCodeField;
    private Font fieldFont = new Font("Arial", Font.BOLD, 12); // Keep the font

    public FUplan() {
        super("FU Plan");
        setUndecorated(true);
        setLayout(new GridLayout(2, 2, 5, 5)); // Use GridLayout for the basic structure

        // FU Label
        JLabel fuLabel = createRightAlignedLabel("FU:");
        add(fuLabel);
        fuField = createCenteredTextField(7);
        increaseTextFieldHeight(fuField, 1.5); // Increase height by 50%
        add(fuField);

        // Meds Code Label
        JLabel medsCodeLabel = createRightAlignedLabel("Meds Code:");
        add(medsCodeLabel);
        medsCodeField = createCenteredTextField(7);
        increaseTextFieldHeight(medsCodeField, 1.5); // Increase height by 50%
        add(medsCodeField);

        // Action when Enter is pressed in FU field
        fuField.addActionListener((ActionEvent e) -> medsCodeField.requestFocusInWindow());

        // Action when Enter is pressed in Meds Code field
        medsCodeField.addActionListener((ActionEvent e) -> {
            String fu = parseFU(fuField.getText().trim());
            String meds = medsCodeField.getText().trim();
            String medsMessage = returnchangefield2(meds);

            System.out.printf("Saved: FU = %s, Meds Code = %s%n", fu, meds);
            System.out.println("***: " + fu);
            System.out.println("***: " + medsMessage);
            GDSEMR_frame.setTextAreaText(8, "\n***: " + fu + "\n***: " + medsMessage);

            // Clear text fields
            fuField.setText("");
            medsCodeField.setText("");
            fuField.requestFocusInWindow();
        });

        setSize(300, 55);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width - getWidth(), 60);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private JLabel createRightAlignedLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private JTextField createCenteredTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(fieldFont);
        textField.setHorizontalAlignment(JTextField.CENTER);
        return textField;
    }

    private void increaseTextFieldHeight(JTextField textField, double scaleFactor) {
        Dimension preferredSize = textField.getPreferredSize();
        int newHeight = (int) (preferredSize.height * scaleFactor);
        textField.setPreferredSize(new Dimension(preferredSize.width, newHeight));
    }

    private String parseFU(String input) {
        if (input == null || input.isEmpty()) return "";
        String num = input.replaceAll("[^0-9]", "");
        if (input.toLowerCase().contains("w")) {
            return String.format("follow-up [ %s ] weeks later", num);
        } else {
            return String.format("follow-up [ %s ] months later", num);
        }
    }

    private String returnchangefield2(String meds) {
        String[] codes = {"5", "55", "6", "8", "2", "4", "0", "1"};
        String[] messages = {
            " |→\u00A0 starting new medicine to treat ",
            " →|\u00A0 discontinue current medication",
            " [ → ] advised the patient to continue with current medication",
            " [ ↗ ] increased the dose of current medication",
            " [ ↘ ] decreased the dose of current medication",
            " [ ⭯ ] changed the dose of current medication",
            " Observation & Follow-up without medication",
            " With conservative treatment"
        };

        for (int i = 0; i < codes.length; i++) {
            if (meds.equals(codes[i])) return messages[i];
        }
        return "(unknown code)";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FUplan::new);
    }
}