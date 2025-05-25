package je.pense.doro.samsara.EMR_OBJ_excute;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import je.pense.doro.chartplate.filecontrol.align.String_ConvertToArray;

public class EMR_TFTout extends JFrame {
    private JTextField[] textFields;
    private JTextArea inputTextArea;

    // Labels for the form fields
    private final String[] labels = {
        "T3 (pg/mL):", "Free T3 (pg/mL):", "Free T4 (ng/dL):", "TSH (mIU/L):",
        "Anti-TSH-Receptor Antibodies (IU/L):", "Anti-Thyroglobulin Antibodies (IU/mL):",
        "Anti-Microsomal Antibodies (IU/mL):"
    };

    // Reference ranges (min and max values) for each test
    private final double[] ranges = {
        80, 200, 2.3, 4.2, 0.8, 1.8, 0.4, 4.0,
        Double.NEGATIVE_INFINITY, 1.75, Double.NEGATIVE_INFINITY, 115, Double.NEGATIVE_INFINITY, 34
    };

    public EMR_TFTout() {
        setupFrame();
        add(createMainPanel());
        setVisible(true);
    }

    /**
     * Sets up the JFrame properties.
     */
    private void setupFrame() {
        setTitle("Outside Clinic TFT Input Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
    }

    /**
     * Creates the main panel containing input fields, textarea, and buttons.
     */
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(createInputPanel(), BorderLayout.NORTH);
        panel.add(createInputTextArea(), BorderLayout.CENTER);
        panel.add(createButtonPanel(), BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Creates the panel containing input fields for each test label.
     */
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

    /**
     * Creates a single JTextField with a KeyListener for navigation.
     */
    private JTextField createTextField(int index) {
        JTextField textField = new JTextField();
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 30));
        textField.addKeyListener(new TextFieldKeyListener(index));
        return textField;
    }

    /**
     * Creates a non-editable JTextArea for displaying results.
     */
    private JScrollPane createInputTextArea() {
        inputTextArea = new JTextArea();
        inputTextArea.setEditable(false);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(inputTextArea);
        scrollPane.setPreferredSize(new Dimension(150, 100));
        return scrollPane;
    }

    /**
     * Creates the button panel with Clear, Save, and Quit buttons.
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        buttonPanel.add(createButton("Clear", e -> clearFields()));
        buttonPanel.add(createButton("Save", e -> saveData()));
        buttonPanel.add(createButton("Quit", e -> dispose()));

        return buttonPanel;
    }

    /**
     * Creates a JButton with the given label and action listener.
     */
    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }

    /**
     * Clears all text fields.
     */
    private void clearFields() {
        for (JTextField textField : textFields) {
            textField.setText("");
        }
        inputTextArea.setText("");
    }

    /**
     * Saves the data, checks value ranges, and displays results.
     */
    private void saveData() {
        StringBuilder outputText = new StringBuilder("\n");
        
        for (int i = 0; i < labels.length; i++) {
            String value = textFields[i].getText();
            if (!value.isEmpty()) {
                try {
                    double numericValue = Double.parseDouble(value);
                    double rangeMin = ranges[i * 2];
                    double rangeMax = ranges[i * 2 + 1];
                    String indicator = (numericValue < rangeMin) ? "▼" :
                                       (numericValue > rangeMax) ? "▲" : " ";
                    outputText.append(String.format("%s\t%s\t%s\n", indicator, value, labels[i]));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid input in: " + labels[i], 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        inputTextArea.setText(outputText.toString());
        String_ConvertToArray.main(outputText.toString());
    }

    /**
     * Handles Enter key navigation between fields.
     */
    private class TextFieldKeyListener extends KeyAdapter {
        private final int index;

        TextFieldKeyListener(int index) {
            this.index = index;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (index < textFields.length - 1) {
                    textFields[index + 1].requestFocus();
                }
            }
        }
    }

    /**
     * Main method to launch the application.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(EMR_TFTout::new);
    }
}
