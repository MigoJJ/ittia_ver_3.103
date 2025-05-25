package je.pense.doro.soap.subjective;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import je.pense.doro.GDSEMR_frame;

public class EMR_symptom_list extends JPanel {
    private JFrame frame;
    private JTextField[] textFieldLabels = new JTextField[20];
    private JTextField[] textFields = new JTextField[20];
    private JCheckBox[] checkBoxes = new JCheckBox[20];
    private JTextArea textArea = new JTextArea(10, 30);
    private static String[] retString = {};
    private static int retStringlen = 0;

    public EMR_symptom_list(JFrame frame) {
        this.frame = frame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (int i = 0; i < retStringlen; i++) {
            checkBoxes[i] = new JCheckBox(retString[i]);
            checkBoxes[i].setHorizontalAlignment(JTextField.RIGHT);

            textFieldLabels[i] = new JTextField(50);
            textFieldLabels[i].setHorizontalAlignment(JTextField.RIGHT);

            textFields[i] = new JTextField();
            int textFieldWidth = 260;
            int textFieldHeight = textFields[i].getPreferredSize().height;
            Dimension textFieldSize = new Dimension(textFieldWidth, textFieldHeight);
            textFields[i].setPreferredSize(textFieldSize);
            textFields[i].setMinimumSize(textFieldSize);
            textFields[i].setMaximumSize(textFieldSize);
            textFields[i].setSize(textFieldSize);
            textFields[i].setHorizontalAlignment(JTextField.CENTER);
            textFields[i].setText("□  ");

            final int index = i;
            checkBoxes[i].addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    JCheckBox checkBox = (JCheckBox) e.getSource();
                    if (checkBoxes[index].isSelected()) {
                        textFields[index].setText("▣  ");
                    } else {
                        textFields[index].setText("□  ");
                    }
                }
            });

            JPanel checkBoxPanel = new JPanel();
            checkBoxPanel.setLayout(new BorderLayout());
            checkBoxPanel.add(checkBoxes[i], BorderLayout.WEST);
            checkBoxPanel.add(textFields[i], BorderLayout.EAST);

            add(checkBoxPanel);
        }

        JButton button1 = new JButton("Show Text Field Content");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < retStringlen; i++) {
                    String symptom = retString[i];
                    String textFieldContent = textFields[i].getText();
                    result.append("\t").append(textFieldContent).append("    ").append(symptom).append("\n");
                }
                textArea.setText("The Patient has suffered from : ▣ \n");
                textArea.append(result.toString());
            }
        });

        JButton button2 = new JButton("Clear");
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });

        JButton button3 = new JButton("Save");
        button3.addActionListener(e -> {
            // First execute the show text field content logic
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < retStringlen; i++) {
                String symptom = retString[i];
                String textFieldContent = textFields[i].getText();
                result.append("\t").append(textFieldContent).append("    ").append(symptom).append("\n");
            }
            textArea.setText("The Patient has suffered from : ▣ \n");
            textArea.append(result.toString());
            
            // Then execute the save logic
            String text = textArea.getText();
            GDSEMR_frame.setTextAreaText(4, text);
            textArea.setText("");
            frame.dispose();
        });


        JButton button4 = new JButton("DDx");
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get text from the text area and trim leading/trailing spaces
                String text = textArea.getText().trim();
                // Save the text in GDSEMR_frame and clear the text area
                GDSEMR_frame.setTextAreaText(4, text); // Save the input text
                textArea.setText(""); // Clear the text area

                // Close the frame after saving
                frame.dispose();
            }
        });


        JButton button5 = new JButton("Quit");
        button5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        // Create a panel for the buttons and set the layout to horizontal
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(button4);
        buttonPanel.add(button5);

        // Add components to the main panel
        add(new JScrollPane(textArea));
        add(buttonPanel);
        
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {  // Check for double click
                    String text = textArea.getText();
                    GDSEMR_frame.setTextAreaText(4, text);
                    textArea.setText("");
                    frame.dispose();
                }
            }
        });
    }

    public static void main(String[] argsStr) {
        retString = argsStr;
        retStringlen = argsStr.length;

        JFrame frame = new JFrame("EMR Symptom List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        EMR_symptom_list panel = new EMR_symptom_list(frame);
        frame.getContentPane().add(panel);
        frame.setSize(500, 600);
        frame.setVisible(true);
    }
}
