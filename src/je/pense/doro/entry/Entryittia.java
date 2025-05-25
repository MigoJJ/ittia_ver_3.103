package je.pense.doro.entry;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class Entryittia extends JFrame implements ActionListener {
    private final ArrayList<JButton> buttons;

    // Constructor
    public Entryittia() {
        super("Entryittia Interface");
        buttons = new ArrayList<>();
        initializeFrame();
        createButtons();
        setVisible(true);
    }

    // Initialize JFrame settings
    private void initializeFrame() {
        setSize(300, 300);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 1)); // Use a grid layout with 6 rows and 1 column
    }

    // Create and add buttons to the frame
    private void createButtons() {
        String[] buttonNames = {"Log In", "Prologue", "Version Information", "Rescue", "Ittia Start", "Quit"};
        
        for (String name : buttonNames) {
            JButton button = new JButton(name);
            button.addActionListener(this);
            button.setBackground(Color.ORANGE); // Set the background color to ORANGE
            buttons.add(button);
            add(button); // Add button to the frame's layout
        }
    }

    // Handle button press actions
    private void handleButtonPress(String buttonText) throws IOException {
        switch (buttonText) {
            case "Prologue":
                openFileInEditor("Prologue");
                break;
            case "Version Information":
                openFileInEditor("Version_Information");
                break;
            case "Ittia Start":
                startIttiaFrame();
                break;
            case "Rescue":
                // Placeholder for Rescue functionality
//                System.out.println("Rescue action triggered.");
                break;
            case "Quit":
                dispose(); // Close the frame when "Quit" is pressed
                break;
            default:
//                System.err.println("Unrecognized action for button: " + buttonText);
        }
    }



    // Open file in editor using Desktop class (cross-platform)
    private void openFileInEditor(String fileName) throws IOException {
        String filePath = EntryDir.homeDir + "/tripikata/entry/" + fileName;
        File file = new File(filePath);

        // Check if Desktop is supported and if the file exists
        if (Desktop.isDesktopSupported() && file.exists()) {
            Desktop.getDesktop().open(file); // Opens with default system editor
        } else {
//            System.err.println("Desktop is not supported or file does not exist: " + filePath);
        }
    }

    // Launch the GDSEMR_frame
    private void startIttiaFrame() {
        // Placeholder for starting GDSEMR_frame
        GDSEMR_frame.main(null);
    }

    // Event handling for button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String buttonText = button.getText();
//        System.out.println("Button \"" + buttonText + "\" was pressed.");
        
        try {
            handleButtonPress(buttonText);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Entryittia::new); // Launch GUI on the Event Dispatch Thread
    }
}
