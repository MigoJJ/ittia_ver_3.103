package je.pense.doro.samsara.EMR_OBJ_Vitalsign;
import javax.swing.*;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class Vitalsign extends JFrame {
    private JTextField inputField;
    private JTextArea outputArea;
    private JTextArea descriptionArea;
    private JButton clearButton;
    private JButton saveButton;
    private JButton quitButton;
    private Set<String> validInputs;
    private Integer sbp = null;
    private Integer dbp = null;
    private Integer pulseRate = null;
    private Double bodyTemperature = null;
    private Integer respirationRate = null;

    public Vitalsign() {
        initializeValidInputs();
        createView();
        setTitle("Vital Sign Tracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 300);
        setLocation(0, 300);
//        setLocationRelativeTo(null); // Centers the window on the screen
        setResizable(true);
    }

    // Initialize the set of valid single character inputs
    private void initializeValidInputs() {
        validInputs = new HashSet<>();
        validInputs.add("h"); // Home
        validInputs.add("o"); // Other clinic
        validInputs.add("g"); // GDS
        validInputs.add("l"); // Left position
        validInputs.add("r"); // Right position
        validInputs.add("i"); // Irregular pulse
        validInputs.add("t"); // Temperature input prefix
    }

    // Set up the main view with all UI components
    private void createView() {
        JPanel panel = new JPanel();
        getContentPane().add(panel);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        createInputField(panel);
        createDescriptionArea(panel);
        createOutputArea(panel);
        addKeyListenerToInputField();
        createButtons(panel);
    }

    // Create and configure the input field for user input
    private void createInputField(JPanel panel) {
//        inputField = new JTextField(20);
        inputField = new GradientTextField(20);
        inputField.setHorizontalAlignment(JTextField.CENTER);
        Dimension preferredSize = inputField.getPreferredSize();
        preferredSize.height = 30;
        inputField.setPreferredSize(preferredSize);
        inputField.setMaximumSize(inputField.getPreferredSize());
        inputField.setOpaque(false);  // Ensure visibility in all environments
        panel.add(inputField);
    }

    // Create and configure the description area for fixed messages
    private void createDescriptionArea(JPanel panel) {
        descriptionArea = new JTextArea(1, 20);
        descriptionArea.setText(" at GDS : Regular pulse, Right Seated Position");
        descriptionArea.setBorder(BorderFactory.createTitledBorder("Description"));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);  // Ensure it's read-only
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        panel.add(descriptionScrollPane);
    }

    // Create and configure the output area for displaying results and messages
    private void createOutputArea(JPanel panel) {
        outputArea = new JTextArea(5, 20);
        outputArea.setBorder(BorderFactory.createTitledBorder("Output"));
        outputArea.setEditable(false);  // Results are not editable by the user
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        panel.add(outputScrollPane);
    }

    // Add a key listener to process input when the user presses Enter
    private void addKeyListenerToInputField() {
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleInput();
                    inputField.setText("");
                }
            }
        });
    }

    // Handle user input from the input field
    private void handleInput() {
        String input = inputField.getText().trim();
        if (validInputs.contains(input)) {
            updateDescriptionArea(input);
        } else {
            createBPBTRR(input);
        }
    }

    // Update the description area based on the specific single character inputs
    private void updateDescriptionArea(String input) {
        String datext = descriptionArea.getText();
        
        switch (input) {
            case "h":
                descriptionArea.setText("   at home by self");
                break;
            case "o":
                descriptionArea.setText("   at Other clinic");
                break;
            case "g":
                descriptionArea.setText(" at GDS : Regular pulse, Right Seated Position");
                break;
            case "l":
                datext = datext.replace("Right", "Left");
                descriptionArea.setText(datext);
                break;
            case "r":
                datext = datext.replace("Left", "Right");
                descriptionArea.setText(datext);
                break;
            case "i":
                datext = datext.replace("Regular", "Irregular");
                descriptionArea.setText(datext);
                break;
            default:
                outputArea.setText("Unrecognized input. Try again.");
                break;
        }
    }

    // Create and configure the buttons for user actions
    private void createButtons(JPanel panel) {
        JPanel buttonPanel = new JPanel();
        clearButton = new JButton("Clear");
        saveButton = new JButton("Save");
        quitButton = new JButton("Quit");

        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(quitButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        clearButton.addActionListener(e -> resetAll());
        saveButton.addActionListener(e -> saveData());
        quitButton.addActionListener(e -> dispose());
    }

    // Handle non-standard inputs for BP, Body Temperature, and Respiration Rate
    private void createBPBTRR(String input) {
        if (input.toLowerCase().startsWith("t")) {
            handleTemperatureInput(input);
        } else {
            handleNumericInput(input);
        }
    }

    // Process and display the body temperature
    private void handleTemperatureInput(String input) {
        try {
            double temperatureValue = Double.parseDouble(input.substring(1));
            descriptionArea.setText(" at GDS : Forehead (Temporal Artery) Thermometers:");
            outputArea.setText("Body Temperature [ " + temperatureValue + " ] ℃");
        } catch (NumberFormatException e) {
            outputArea.setText("Invalid temperature input. Please enter a valid number.");
        }
    }

    // Process numeric inputs for BP, Pulse, and Respiration Rate
    private void handleNumericInput(String input) {
        try {
            double value = Double.parseDouble(input);
            processMeasurement(value);
        } catch (NumberFormatException e) {
            outputArea.setText("Invalid input. Please enter a number.");
        }
    }

    // Update output based on measurements and reset if complete
    private void processMeasurement(double value) {
        if (sbp == null) {
            sbp = (int) value;
            outputArea.setText("\tSBP [" + sbp + "] mmHg    ");
        } else if (dbp == null) {
            dbp = (int) value;
            outputArea.setText("BP [" + sbp + " / " + dbp + "] mmHg    ");
        } else if (pulseRate == null) {
            pulseRate = (int) value;
            outputArea.append("PR [" + pulseRate + "]/minute    ");
        } else if (bodyTemperature == null) {
            bodyTemperature = value;
            outputArea.append("\n\tBody Temperature [" + bodyTemperature + "]℃");
        } else if (respirationRate == null) {
            respirationRate = (int) value;
            outputArea.append("\n\tRespiration Rate [" + respirationRate + "]/minute");
            resetMeasurements();
        } else {
            outputArea.setText("Input exceeded expected data fields.");
        }
    }

    // Reset all vital signs measurements
    private void resetMeasurements() {
        sbp = null;
        dbp = null;
        pulseRate = null;
        bodyTemperature = null;
        respirationRate = null;
    }

    // Reset the interface
    private void resetAll() {
        inputField.setText("");
        outputArea.setText("");
        descriptionArea.setText(" at GDS : Regular pulse, Right Seated Position");
    }

    // Save the current state of description and output areas and restart the application
    private void saveData() {
        GDSEMR_frame.setTextAreaText(5, "\n" + descriptionArea.getText());
        GDSEMR_frame.setTextAreaText(5, "\n\t" + outputArea.getText());
        // Restart the Vitalsign class by disposing of the current instance and creating a new one
        SwingUtilities.invokeLater(() -> {
            dispose();  // Close the current window
            new Vitalsign().setVisible(true);  // Open a new window
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Vitalsign().setVisible(true));
    }
}
