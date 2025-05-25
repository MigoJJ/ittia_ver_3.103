package je.pense.doro.fourgate.diabetes;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.Color;

public class EMR_FU_retinopathy {
    private static JCheckBox[] checkboxes;
    private static JTextArea textArea;

    public static void main(String[] args) {
        // Create frame
        JFrame frame = new JFrame("Diabetic Retinopathy Stages and Other Eye Conditions");
        frame.setSize(550, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create textArea at the north
        textArea = new JTextArea(7, 40);
        textArea.setEditable(true);
        textArea.setForeground(Color.BLUE); 
        
     // Set the current date in the format "yy-MM"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate currentDate = LocalDate.now();
        textArea.setText("< Diabetic Retinopathy > " + currentDate.format(formatter) + "\n");

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane, BorderLayout.NORTH);
        
        // Create panel to hold checkboxes and descriptions
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 4 * 10, 0, 0));  // Indent checkboxes by 40 pixels

        // Checkboxes and their descriptions
        String[] descriptions = {
            "The Patient has not visited an eye doctor yet.",
            " ----------------- ",
        	"No apparent retinopathy : no NPDR",
            "Non-proliferative diabetic retinopathy (NPDR)",
            "    - Mild NPDR",
            "    - Moderate NPDR",
            "    - Severe NPDR",
            "Proliferative diabetic retinopathy (PDR)",
            " ----------------- ",
            "Cataract [+] ",
            "Cataract [+] Cataract Surgery:[+]",
            "Glaucoma [+]",
            "Retinal Detachment [+]",
            "Macular Degeration[+]",
            " ----------------- ",
            "Cataract [ denied ]",
            "Glaucoma [ denied ]",
            "Retinal Detachment [ denied ]",
            "Macular Degeration[ denied ]",
            " ----------------- ",
            "Laser Treatment (Photocoagulation) :",
            "Vitrectomy:",
            "Anti-VEGF Therapy :",
            "Intravitreal Triamcinolone Acetonide (IVTA):",
            "Steroid Injections:",
                       
        };

        checkboxes = new JCheckBox[descriptions.length];
        for (int i = 0; i < descriptions.length; i++) {
            checkboxes[i] = new JCheckBox(descriptions[i]);
            checkboxes[i].addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    textArea.append("\t"+ checkboxes[((JCheckBox) e.getItem()).getMnemonic()].getText() + "\n");
                } else {
                    textArea.setText(textArea.getText().replace(checkboxes[((JCheckBox) e.getItem()).getMnemonic()].getText() + "\n", ""));
                }
            });
            checkboxes[i].setMnemonic(i);  // Using the mnemonic to store the index
            panel.add(checkboxes[i]);
        }
        
        frame.add(panel, BorderLayout.CENTER);

        // Create a south panel for buttons
        JPanel southPanel = new JPanel();
        
     // Retinppathy button
        JButton RetinppathyButton = new JButton("Retinppathy");
        RetinppathyButton.addActionListener((ActionEvent e) -> {
            for (JCheckBox checkbox : checkboxes) {
                checkbox.setSelected(false);
            }
            textArea.setText(currentDate.format(formatter) + """
            < Diabetic Retinopathy > ****************
				\tNo apparent retinopathy : no NPDR
				\t\tCataract [-] Cataract Surgery:[-]
				\t\tGlaucoma [-]
				\t\tRetinal Detachment [-]
				\t\tMacular Degeration[-]
                *************************************************
                """);
        });
        
        // Clear button
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener((ActionEvent e) -> {
            for (JCheckBox checkbox : checkboxes) {
                checkbox.setSelected(false);
            }
            textArea.setText("");
        });
        
        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener((ActionEvent e) -> {
            // Placeholder for save action
//            System.out.println("Save button clicked. Implement saving logic here.");
            GDSEMR_frame.setTextAreaText(5, "\n"+ textArea.getText());
            GDSEMR_frame.setTextAreaText(5, "\n"+ "*************************************************");
            textArea.setText("");
            frame.dispose();
        });

        // Quit button
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener((ActionEvent e) -> {
            frame.dispose();
        });

        // Add buttons to southPanel
        southPanel.add(RetinppathyButton);
        southPanel.add(clearButton);
        southPanel.add(saveButton);
        southPanel.add(quitButton);

        // Add southPanel to frame
        frame.add(southPanel, BorderLayout.SOUTH);
        
        // Make frame visible
        frame.setVisible(true);
    }
}
