package je.pense.doro.fourgate.hypercholesterolemia;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.Timer;

import je.pense.doro.soap.subjective.EMR_symptom_list;
import je.pense.doro.soap.subjective.EMR_symptom_retStr;

public class EMR_chol_mainentry {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Select category ...");
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setLocation(0, 610);
        frame.setSize(new Dimension(300, 460));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] buttonNames = {
                "Chol General symptoms",
                "Chol ",
                "Chol ",
                "Chol Statin side-effect",
                "Medications",
                "Chol F/U Laboratory Test",
                "Hypercholesterolemia",
                "Quit"
        };

        for (String name : buttonNames) {
            JButton button = createButton(name, frame);
            frame.add(button);
            frame.add(Box.createVerticalStrut(10));
        }

        // Create a Timer to close the frame after 5 minutes (300,000 milliseconds)
        Timer timer = new Timer(500000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the frame
            }
        });
        timer.setRepeats(false); // Ensure the timer only runs once
        timer.start(); // Start the timer

        frame.setVisible(true);
    }

    private static JButton createButton(String name, JFrame frame) {
        JButton button = new JButton(name) {
            @Override
            protected void paintComponent(Graphics g) {
                if (g instanceof Graphics2D) {
                    Graphics2D g2d = (Graphics2D) g;
                    int width = getWidth();
                    int height = getHeight();

                    // Create a gradient from top to bottom
                    GradientPaint gradient = new GradientPaint(
                            0, 0, Color.decode("#c7fdb1"),
                            0, height, Color.decode("#7ca26d"));

                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, width, height);
                }
                super.paintComponent(g);
            }
        };

        button.setPreferredSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Box.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	switch (name) {
                case "Quit":
                    frame.dispose();
                    break;
                case "Chol General symptoms":
                    String[] Esrr = EMR_symptom_retStr.returnStr("Hypercholesterolemia"); // Corrected typo
                    EMR_symptom_list.main(Esrr);
                    break;
                case "Chol":
                    // Add Chol functionality here
                    break;
                case "Chol Statin side-effect":
                	EMR_chol_StatinSideEffectCheck.main(null);
                    break;
                case "Medications":
                	EMR_chol_meds.main(null);
                    break;
                    // ... other cases
                case "Hypercholesterolemia":
                    // Add Hypercholesterolemia functionality here
                    break;
                default:
                    // Handle other cases or display a default message
                    break;
            	}
            }
        });

        return button;
    }

    // Placeholder method for returnStr
    private static String[] returnStr(String name) {
        // Add your logic to populate the returnargs array
        return new String[]{};
    }
}
