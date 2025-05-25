package je.pense.doro.chartplate.keybutton;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

import je.pense.doro.chartplate.keybutton.EMR_ButtonEntry;

public class GDSEMR_ButtonNorthSouth extends JPanel {
    private JButton[] buttons = new JButton[13]; // Increased from 11 to 13

    public GDSEMR_ButtonNorthSouth(String locations) {
        super();

        // Create the buttons
        for (int i = 0; i < 13; i++) { // Updated to 12 buttons
            buttons[i] = new JButton(locations + (i + 1));

            // Add ActionListener to button
            int buttonNumber = i + 1;
            buttons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        EMR_ButtonEntry.EMR_B_1entryentry(e.getActionCommand(), locations);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    // Handle button click
                    // System.out.println("Button " + e.getActionCommand() + " clicked");
                }
            });
            // Add the button to the panel
            add(buttons[i]);
        }

        if (locations.equals("north")) {
            buttons[0].setText("Rescue");
            buttons[1].setText("Backup");
            buttons[2].setText("Copy");
            buttons[3].setText("Clear");
            buttons[4].setText("CE");
            buttons[5].setText("Exit");
            buttons[6].setText("Abbreviation");
            buttons[7].setText("ICD-11");
            buttons[8].setText("KCD8");
            buttons[9].setText("Lab code");
            buttons[10].setText("Lab sum");
            buttons[11].setText("ittia_support"); // New button 1
            buttons[12].setText("db"); // New button 2
            // Add the button panel to the north of the frame
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setBackground(Color.GRAY);
        } else if (locations.equals("south")) {
            buttons[0].setText("F/U DM");
            buttons[1].setText("F/U HTN");
            buttons[2].setText("F/U Chol");
            buttons[3].setText("F/U Thyroid");
            buttons[4].setText("Osteoporosis");
            buttons[5].setText("URI");
            buttons[6].setText("Allergy");
            buttons[7].setText("Injections");
            buttons[8].setText("GDS RC");
            buttons[9].setText("공단검진");
            buttons[10].setText("F/U Edit");
//            buttons[11].setText("New South 1"); // New button 1
//            buttons[12].setText("New South 2"); // New button 2
            // Add the button panel to the south of the frame
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            setBackground(Color.LIGHT_GRAY);
        }
    }
}