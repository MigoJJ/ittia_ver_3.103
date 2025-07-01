package je.pense.doro.soap.fu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.fourgate.n_medications.emr_select_medication;
import je.pense.doro.support.sqlite3_manager.lab.labshort.*;
import je.pense.doro.support.sqlite3_manager.lab.labshort.JavalabtestsDBManagerGUI;

/**
 * Manages follow-up injections with a GUI for selecting vaccines.
 */
public class Followup {
    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 800;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String[] BUTTON_LABELS = {
    	"MEDICATION",
    	"...Next Lab F/U with NPO",
		"...Coservative symptomatic treatment",
		"...F/U without medications",
		"...Continie current medications or supplements",
		"...D/C all kinds of dietary[food, health, nutritional] supplements",
		"...The patient Refused dose-adjustment", 
		"...Ophthalmologist consultation[+]", 
		"...Plan to review of other clinic RC result",
		"HISTORY",
		"...History of surgeries or hospitalizations",
		"...Check for Family medical history",
		"...to refer patients to receive additional health care services.",
		"LAB",
		"LAB other",
		"...Gastroenterology consult in GDS clinic",
		"...Pulmonolgy consult in GDS clinic",
		"...Cardiology consult in Other clinic",
        "Quit"
    };

    /**
     * Launches the injection selection frame.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = createMainFrame();
            addButtonsToFrame(frame);
            positionFrameToBottomRight(frame);
            frame.setVisible(true);
        });
    }

    /**
     * Creates the main frame for the injection selection GUI.
     */
    private static JFrame createMainFrame() {
        JFrame frame = new JFrame("Follow-up");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(BUTTON_LABELS.length, 1, 0, 5)); // Added spacing between buttons
        frame.getContentPane().setBackground(new Color(245, 245, 245)); // Light background for frame
        return frame;
    }

    /**
     * Adds buttons for each vaccine to the frame.
     */
    private static void addButtonsToFrame(JFrame frame) {
        ActionListener buttonClickListener = e -> {
            String clickedButtonText = ((JButton) e.getSource()).getText();
            updateDetails(frame, clickedButtonText);
        };

        for (String label : BUTTON_LABELS) {
            FancyButton button = new FancyButton(label);
            button.addActionListener(buttonClickListener);
            frame.add(button);
        }
    }

    /**
     * Positions the frame at the bottom-right of the screen.
     */
    private static void positionFrameToBottomRight(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int xPosition = (int) (screenSize.getWidth() - frame.getWidth());
        int yPosition = (int) (screenSize.getHeight() - frame.getHeight());
        frame.setLocation(xPosition, yPosition);
    }

    /**
     * Updates the EMR text area with the selected vaccine and date.
     */
    private static void updateDetails(JFrame frame, String clickedButtonText) {
        if ("MEDICATION".equals(clickedButtonText)) {
        	  emr_select_medication.main(null);
            return;
        }
        else if ("LAB".equals(clickedButtonText)) {
//        	n_laboratoytest.main(null);
            return;
        }
        else if ("LAB other".equals(clickedButtonText)) {
        	JavalabtestsDBManagerGUI.main(null);
            return;
        }
        else if ("Quit".equals(clickedButtonText)) {
            frame.dispose();
            return;
        }

        String currentDate = DATE_FORMAT.format(new Date());
        try {
            GDSEMR_frame.setTextAreaText(9, "\n  #  " + clickedButtonText + "  [" + currentDate + "]");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error updating text areas: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Custom button with gradient background, rounded corners, and hover effect.
     */
    static class FancyButton extends JButton {
        private boolean isHovered = false;
        private final Color gradientStart = new Color(100, 149, 237); // Cornflower Blue
        private final Color gradientEnd = new Color(30, 144, 255);   // Dodger Blue
        private final Color hoverStart = new Color(135, 206, 250);   // Sky Blue
        private final Color hoverEnd = new Color(70, 130, 180);      // Steel Blue

        public FancyButton(String text) {
            super(text);
            setContentAreaFilled(false); // Disable default background
            setFocusPainted(false);      // Remove focus border
            setBorderPainted(false);     // Remove default border
            setForeground(Color.WHITE);  // White text for contrast
            setFont(new Font("Arial", Font.BOLD, 12)); // Modern font
            setMargin(new Insets(10, 10, 10, 10)); // Padding

            // Add hover effect
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int arc = 15; // Corner radius for rounded rectangle

            // Create gradient paint
            Color startColor = isHovered ? hoverStart : gradientStart;
            Color endColor = isHovered ? hoverEnd : gradientEnd;
            GradientPaint gradient = new GradientPaint(
                0, 0, startColor,
                0, height, endColor
            );

            // Draw rounded rectangle with gradient
            RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                0, 0, width - 1, height - 1, arc, arc
            );
            g2d.setPaint(gradient);
            g2d.fill(roundedRect);

            // Draw subtle border
            g2d.setColor(new Color(255, 255, 255, 100)); // Semi-transparent white
            g2d.draw(roundedRect);

            super.paintComponent(g); // Paint text and other components
            g2d.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            size.height = 40; // Consistent button height
            return size;
        }
    }
}