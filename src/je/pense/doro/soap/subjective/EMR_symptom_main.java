package je.pense.doro.soap.subjective;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import je.pense.doro.samsara.EMR_PE.PE_Abdominal_pain_diagnosis;
import je.pense.doro.samsara.EMR_PE.PhysicalExaminationAbdomen;

public class EMR_symptom_main {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Select category ...");
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setLocation(0, 610); // Set frame position
        frame.setSize(new Dimension(300, 460)); // Set frame size

        // Define button names
        String[] buttonNames = {
            "Diabetes Mellitus",
            "Hyperthyroidism",
            "Hypothyroidism",
            "URI",
            "UTI",
            "Abdominal pain",
            "Atypical chest pain",
            "Osteoporosis",
            "Hypercholesterolemia",
            "Pituitary disease",
            "Quit"
        };

        // Add buttons to the frame
        for (String name : buttonNames) {
            JButton button = createButton(name, frame);
            frame.add(button); // Add button to the frame
            frame.add(Box.createVerticalStrut(10)); // Add vertical spacing
        }

        // Set frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close application on window close
        frame.setVisible(true); // Make the frame visible
    }

    /**
     * Creates a JButton with the specified name and adds an ActionListener.
     *
     * @param name  The name of the button.
     * @param frame The JFrame to which the button belongs.
     * @return The configured JButton.
     */
    private static JButton createButton(String name, JFrame frame) {
        JButton button = new JButton(name);
        button.setPreferredSize(new Dimension(200, 40)); // Set preferred size
        button.setMaximumSize(new Dimension(200, 40));  // Set maximum size
        button.setAlignmentX(Box.CENTER_ALIGNMENT);     // Center-align the button

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String actionCommand = e.getActionCommand();

                switch (actionCommand) {
                    case "Abdominal pain":
                        if (name.equals("Abdominal pain")) {
                        	   processSymptomList(actionCommand);
                            PE_Abdominal_pain_diagnosis.main(null);
                            PhysicalExaminationAbdomen.main(null);
                        } else {                    
                        }
                        break;
                    case "Quit":
                        frame.dispose();
                        break;
                    default:
                        processSymptomList(actionCommand);
                        break;
                }
            }

            private void processSymptomList(String command) {
                String[] Esrr = EMR_symptom_retStr.returnStr(command);
                if (Esrr != null && Esrr.length > 0) {
                    EMR_symptom_list.main(Esrr);
                } else {
                    System.err.println("Error: No data returned for " + command);
                    JOptionPane.showMessageDialog(frame,
                        "No symptom data available for " + command,
                        "Data Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        return button;
    }

    /**
     * Placeholder method for returning a String array.
     * Replace this with actual logic to populate the array.
     *
     * @param name The input string.
     * @return An empty String array.
     */
    private String[] returnStr(String name) {
        String[] returnargs = new String[]{};
        // Add your logic to populate the returnargs array
        return returnargs;
    }
}