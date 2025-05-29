package je.pense.doro.samsara.EMR_clinicallab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Llaboratoyitemslist extends JFrame implements ActionListener {

    private static final int FRAME_WIDTH = 200;
    private static final int FRAME_HEIGHT = 600;

    private final JButton[] labItemButtons = new JButton[17];  // Corrected array size
    private Timer autoCloseTimer;

    public Llaboratoyitemslist() {
        setTitle("Select Laboratory Item");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Define button labels
        String[] labItemNames = {
            "BMI", "BP", "HbA1c", "TFT", "TFTout",
            "LDL", "LFT", "CBC", "eGFR", "Lp(a)",
            "Etc.", "ChestPA", "EKG", "GFS", "CFS",
            "DEXA", "Quit"
        };

        // Create panel with grid layout
        JPanel buttonPanel = new JPanel(new GridLayout(labItemNames.length, 1, 5, 5));

        for (int i = 0; i < labItemNames.length; i++) {
            labItemButtons[i] = new JButton(labItemNames[i]);
            labItemButtons[i].addActionListener(this);

            // Customize appearance
            labItemButtons[i].setBackground(new Color(245, 235, 220));  // Very light brown
            labItemButtons[i].setForeground(Color.BLACK);               // Black text
            labItemButtons[i].setFont(new Font("Arial", Font.BOLD, 14)); // Bold font

            buttonPanel.add(labItemButtons[i]);
        }

        add(buttonPanel);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        positionFrameTopRight(this);
        setVisible(true);

        // Auto-close timer
        autoCloseTimer = new Timer(4000, e -> dispose());
        autoCloseTimer.setRepeats(false);
        autoCloseTimer.start();
    }

    private static void positionFrameTopRight(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - frame.getWidth();
        int y = 0;
        frame.setLocation(x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedItem = e.getActionCommand();

        autoCloseTimer.stop();  // Stop timer on any interaction

        if (selectedItem.equals("Quit")) {
            dispose();          // Close window on Quit
            return;
        }

        System.out.println("Selected Laboratory Item: " + selectedItem);
        
         EmrExecuteLabItem.main(selectedItem); // Uncomment if method exists

        dispose(); // Close window after selection
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Llaboratoyitemslist::new);
    }
}
