package je.pense.doro.samsara.EMR_OBJ_excute;

import java.awt.BorderLayout;	
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.filecontrol.datetime.Date_current;

public class EMR_Lab_enterresult extends JFrame {

    private JTextField[] textBoxes;
    private int numFields = 10;
    private String[] nameFields = {"Vitamin-D  (20-50 ng/mL)", "Insulin (2-25 μU/mL)", "C-peptide (1-4.5 ng/mL)","CRP (<3 mg/L) ", "ESR  ", "Uric Acid (Men: 4-7 mg/dL; Women: 3-6 mg/dL) ", "AFP", "CA-19-9", "CEA", "PSA", "CA125", "Cyfra-21"};
	private String cdate = Date_current.defineTime("m");

    public EMR_Lab_enterresult() {
        super("Name Prompt Program");

        // Create the labels and text fields
        JLabel[] labels = new JLabel[numFields];
        textBoxes = new JTextField[numFields];

        for (int i = 0; i < numFields; i++) {
            labels[i] = new JLabel(nameFields[i]);
            labels[i].setHorizontalAlignment(SwingConstants.RIGHT);
            textBoxes[i] = new JTextField(21);
            textBoxes[i].setHorizontalAlignment(JTextField.CENTER);
            textBoxes[i].setPreferredSize(new Dimension(10, 30));
        }

        // Create the panel for the labels and text fields
        JPanel panelTop = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panelTop.add(labels[i], gbc);

            gbc.gridx = 1;
            gbc.gridy = i;
            gbc.anchor = GridBagConstraints.LINE_START;
            panelTop.add(textBoxes[i], gbc);

            // Add key listener to each text field
            final int index = i;
            textBoxes[i].addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (index < textBoxes.length - 1 && index < 9) {  // Perform action if index is within the range 0 to 8
                            textBoxes[index + 1].requestFocus();
                        } else if (index == 9) {  // Perform action if index is 9 (last text field)
                            // Replace the following line with your own code to save the text field values
                            System.out.println("Save button clicked");
                            String outputText = textBoxes[index].getText();
//                            GDSEMR_frame.setTextAreaText(5, outputText);
                        }
                    }
                }
            });

        }

        // Create the buttons and their action listeners
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            for (JTextField textField : textBoxes) {
                textField.setText("");
            }
        });
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            System.out.println("Save button clicked");
            for (int i = 0; i < textBoxes.length; i++) {
                String outputText = textBoxes[i].getText();
                String labelText = labels[i].getText();
                if (!outputText.isEmpty()) {
                    GDSEMR_frame.setTextAreaText(5, "\n\t" + outputText + "\t[ " + labelText + " ]\n");
                    GDSEMR_frame.setTextAreaText(7, "\n\t" + outputText + "\t[ " + labelText + " ]   " + cdate);

                    dispose();
                }
            }
        });
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> {
            dispose();
        });

        // Create the panel for the buttons
        JPanel panelBottom = new JPanel();
        panelBottom.add(clearButton);
        panelBottom.add(saveButton);
        panelBottom.add(quitButton);

        // Add the panels to the frame
        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);

        // Set up the frame
        setTitle("EMR Laboratory Enter Result.");
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EMR_Lab_enterresult promptForName = new EMR_Lab_enterresult();
        promptForName.setVisible(true);
    }
}
