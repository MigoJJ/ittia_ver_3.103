package je.pense.doro.fourgate.thyroid.pregnancy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.support.EMR_ittia_support;

public class EMR_thyroid_Pregnancyentry {
    private static final String[] BUTTONS = {
        "New Patient for Pregnancy with Thyroid disease",
        "F/U Pregnancy with Normal Thyroid Function (TAb+)",
        "Infertility and Thyroid Function Evaluation",
        "F/U Pregnancy with Hyperthyroidism",
        "F/U Pregnancy with TSH low (Hyperthyroidism/GTT)",
        "F/U Pregnancy with Hypothyroidism",
        "F/U Pregnancy with TSH elevation (Subclinical Hypothyroidism)",
        "Postpartum Thyroiditis",
        "Support Files",
        "Quit"
    };

    private static final int FRAME_WIDTH = 410;
    private static final int FRAME_HEIGHT = 400;
    private static final int AUTO_CLOSE_DELAY = 300000; // 5 minutes

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EMR_thyroid_Pregnancyentry::createAndShowGUI);
    }

    /**
     * Creates and displays the GUI frame.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Thyroid Pregnancy Management");
        setupFrame(frame);
        addButtons(frame);
        positionFrame(frame);
        setupAutoClose(frame);
        frame.setVisible(true);
    }

    /**
     * Configures the frame properties.
     */
    private static void setupFrame(JFrame frame) {
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(BUTTONS.length, 1));
    }

    /**
     * Adds buttons for different thyroid conditions.
     */
    private static void addButtons(JFrame frame) {
        for (String buttonText : BUTTONS) {
            frame.add(createStyledButton(buttonText, e -> handleButtonClick(frame, buttonText)));
        }
    }

    /**
     * Creates a styled JButton with gradient background.
     */
    private static JButton createStyledButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (g instanceof Graphics2D g2d) {
                    int h = getHeight();
                    GradientPaint gradient = new GradientPaint(
                            0, 0, new Color(240, 230, 210), // Lighter top color
                            0, h, new Color(225, 215, 185)  // Lighter bottom color
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), h);
                }
                super.paintComponent(g);
            }
        };
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.addActionListener(listener);
        return button;
    }

    /**
     * Positions the frame on the screen (bottom-right corner).
     */
    private static void positionFrame(JFrame frame) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(
            screen.width - FRAME_WIDTH,
            screen.height - FRAME_HEIGHT
        );
    }

    /**
     * Sets up automatic frame closure after a delay.
     */
    private static void setupAutoClose(JFrame frame) {
        new Timer(AUTO_CLOSE_DELAY, e -> frame.dispose()).start();
    }

    /**
     * Handles button click events.
     */
    private static void handleButtonClick(JFrame frame, String buttonText) {
        if ("Quit".equals(buttonText)) {
            frame.dispose();
            return;
        }

        // Execute special logic for new patient entry
        else if (buttonText.startsWith("New Patient")) {
            EMR_Preg_CC.main(null); // Assuming EMR_Preg_CC is another class
            return;
        }

        else if ("Support Files".equals(buttonText)) {
        	EMR_ittia_support.thyroid("Thyroid");
        	frame.dispose();
            return;
        }
        // Update EMR with patient condition without opening any file
        updateEMRFrameText(buttonText);
//        JOptionPane.showMessageDialog(frame, 
//            "Selected Condition: " + buttonText + "\nEMR Updated.", 
//            "Notification", 
//            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Updates the EMR frame text area with patient condition details.
     */
    private static void updateEMRFrameText(String condition) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String baseCondition = condition.replace("F/U ", "");

        GDSEMR_frame.setTextAreaText(0, 
            String.format("F/U [   ] weeks    %s%n\t%s", currentDate, baseCondition));
        GDSEMR_frame.setTextAreaText(7, 
            String.format("%n  #  %s  [%s]", condition, currentDate));
        GDSEMR_frame.setTextAreaText(8, 
            String.format("...Plan F/U [   ] weeks%n\t %s", baseCondition));
    }
}
