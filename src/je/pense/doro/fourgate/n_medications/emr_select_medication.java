package je.pense.doro.fourgate.n_medications;

import javax.swing.*;			
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class emr_select_medication extends JFrame implements ActionListener {

    private static final int FRAME_WIDTH = 200;
    private static final int FRAME_HEIGHT = 600;
    private final JButton[] medicationButtons = new JButton[12];
    private Timer timer;

    public emr_select_medication() {
        setTitle("Select Medication");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel buttonPanel = new JPanel(new GridLayout(12, 1, 5, 5));

        String[] medicationNames = {
                "DM", "Hypertension", "Dyslipidemia",
                "Thyroid", "Osteoporosis", "Medication 6",
                "Medication 7", "Medication 8", "Medication 9",
                "Medication 10", "Medication 11", "Quit"
        };

        for (int i = 0; i < 12; i++) {
            medicationButtons[i] = new JButton(medicationNames[i]);
            medicationButtons[i].addActionListener(this);

            // Customize button appearance
            medicationButtons[i].setBackground(new Color(220, 220, 255)); // Light purple
            medicationButtons[i].setForeground(Color.BLACK);           // Black text
            medicationButtons[i].setFont(new Font("Arial", Font.BOLD, 14)); // Larger, bold font

            buttonPanel.add(medicationButtons[i]);
        }

        add(buttonPanel);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        positionFrameToTopRight(this);
        setVisible(true);

        timer = new Timer(4000, e -> dispose());
        timer.setRepeats(false);
        timer.start();
    }

    private static void positionFrameToTopRight(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - frame.getWidth();
        int y = 0;
        frame.setLocation(x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals("Quit")) {
            timer.stop();  // Stop the timer if Quit is clicked
            dispose();       // Close the frame
            return; //Important: exit the method after handling the Quit button
        }

        timer.stop(); // Stop the timer if a medication button is clicked
        String medicationName = actionCommand; // Use actionCommand now


        // Rest of event handling logic...
        System.out.println("Selected Medication: " + medicationName);

        emr_excute_medication.main(medicationName);

        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new emr_select_medication());
    }
}