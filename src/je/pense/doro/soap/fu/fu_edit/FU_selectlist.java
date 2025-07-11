package je.pense.doro.soap.fu.fu_edit; // Package as requested

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FU_selectlist extends JFrame {

    private final String[] medicalConditions = {
            "Hypertension",
            "Hypercholesterolemia",
            "Osteoporosis",
            "URI",
            "Diabetes Mellitus Gestational",
            "Diabetes Mellitus Type 1",
            "Diabetes Mellitus Type 2",
            "Thyroid Hyperthyroidism",
            "Thyroid Hypothyroidism",
            "Thyroid Hyperthyroidism with Pregnancy",
            "Thyroid Hypothyroidism with Pregnancy"
    };

    public FU_selectlist() {
        setTitle("FU_selectlist - Medical Conditions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600); // Increased height to accommodate more buttons
        setLocationRelativeTo(null); // Center the window

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(medicalConditions.length, 1, 10, 10)); // Rows, Cols, Hgap, Vgap
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add some padding

        addButtons(panel);

        // Add the panel to a scroll pane in case there are many buttons
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addButtons(JPanel panel) {
        for (String condition : medicalConditions) {
            JButton button = new JButton(condition);
            button.setFont(new Font("Arial", Font.PLAIN, 16)); // Optional: Set font for better readability
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // This is where you would typically interact with your
                    // 'je.pense.doro.soap.fu.fu_edit' package.
                    // For this example, we'll just print to the console.
                    System.out.println("Button clicked: " + button.getText());
                    // Example: You might want to call a method in your other classes like:
                    // SomeClassInFuEdit.handleSelection(button.getText());
                }
            });
            panel.add(button);
        }
    }

    public static void main(String[] args) {
        // Run the Swing GUI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FU_selectlist().setVisible(true);
            }
        });
    }
}