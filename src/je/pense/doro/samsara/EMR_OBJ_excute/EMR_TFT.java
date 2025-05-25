package je.pense.doro.samsara.EMR_OBJ_excute;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.filecontrol.datetime.Date_current;

public class EMR_TFT extends JFrame {
    private JTextField[] textFields;
    private JTextArea inputTextArea;
    private String[] labels = {
        "T3 (pg/mL):", "Free T4 (ng/dL):", "TSH (mIU/L):",
        "Anti-TSH-Receptor Antibodies (IU/L <1.75):",
        "Anti-Thyroglobulin Antibodies (IU/mL <115):",
        "Anti-Microsomal Antibodies (IU/mL <34):"
    };
    private double[] ranges = {
        0.9, 2.5, 10.6, 19.4, 0.25, 5.0,
        Double.NEGATIVE_INFINITY, 1.75,
        Double.NEGATIVE_INFINITY, 115,
        Double.NEGATIVE_INFINITY, 34
    };
    
    public EMR_TFT() {
        setupFrame();
        JPanel mainPanel = createMainPanel();
        add(mainPanel);
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("GDS TFT Input Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 360);
        setLocation(1750, 310);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(createInputPanel(), BorderLayout.NORTH);
        panel.add(createInputTextArea(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(labels.length, 2, 10, 10));
        textFields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i], SwingConstants.RIGHT);
            JTextField textField = createTextField(i);
            inputPanel.add(label);
            inputPanel.add(textField);
            textFields[i] = textField;
        }

        return inputPanel;
    }

    private JTextField createTextField(int index) {
        JTextField textField = new JTextField();
        textField.setName(labels[index]);
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 30));
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.addKeyListener(new TextFieldKeyListener(index));
        return textField;
    }

    private JTextArea createInputTextArea() {
        inputTextArea = new JTextArea();
        inputTextArea.setBackground(Color.YELLOW);
        inputTextArea.setEditable(true);
        inputTextArea.setPreferredSize(new Dimension(150, 100));
        return inputTextArea;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(createButton("Clear", e -> clearFields()));
        buttonPanel.add(createButton("Save", e -> saveData()));
        buttonPanel.add(createButton("Quit", e -> dispose()));
        return buttonPanel;
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }

    private void clearFields() {
        for (JTextField textField : textFields) {
            textField.setText("");
        }
    }

    private void saveData() {
        StringBuilder outputText = new StringBuilder("\n");
        for (int i = 0; i < labels.length; i++) {
            String value = textFields[i].getText();
            if (!value.isEmpty()) {
                double numericValue = Double.parseDouble(value);
                double rangeMin = ranges[i * 2];
                double rangeMax = ranges[i * 2 + 1];
                String indicator = (numericValue < rangeMin) ? "▼" : (numericValue > rangeMax) ? "▲" : " ";
                outputText.append(String.format("%s\t%s\t%s\n", indicator, value, labels[i]));
            }
        }
        inputTextArea.setText(outputText.toString());
        GDSEMR_frame.setTextAreaText(5, outputText.toString());
        String cdate = Date_current.main("d");
        GDSEMR_frame.setTextAreaText(7, "\n# TFT    " + cdate);
        inputTextArea.setText("");
        clearFields();
    }

    private class TextFieldKeyListener extends KeyAdapter {
        private int index;

        TextFieldKeyListener(int index) {
            this.index = index;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                String value = textFields[index].getText().trim(); // Trim whitespace

                if (index == 3) { // Special behavior for textFields[3]
                    if (value.isEmpty()) { 
                        // If empty, save data and clear fields
                        saveData();
                        clearFields();
                    } else {
                        // If not empty, move focus to textFields[4]
                        textFields[4].requestFocus();
                    }
                    return; // Exit after handling textFields[3]
                }

                // Default behavior for other fields
                if (index < textFields.length - 1) {
                    textFields[index + 1].requestFocus();
                } else {
                    saveData();
                    clearFields();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EMR_TFT::new);
    }
}