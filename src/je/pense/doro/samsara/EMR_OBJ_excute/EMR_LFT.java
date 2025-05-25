package je.pense.doro.samsara.EMR_OBJ_excute;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import je.pense.doro.GDSEMR_frame;

public class EMR_LFT implements ActionListener, KeyListener {
	private JFrame frame = new JFrame("EMR LFT");
	private JTextField[] inputFields;
	private JTextArea outputArea;
	private JButton clearButton, saveButton, quitButton;

    public EMR_LFT() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // center the frame on the screen
        frame. setSize(400, 300);
        

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        String[] labels = {"GOT", "GPT", "GGT", "T-bilirubin", "Albumin"};
        inputFields = new JTextField[labels.length];
        for (int i = 0; i < labels.length; ++i) {
            inputFields[i] = new JTextField();
            inputFields[i].setPreferredSize(new Dimension(120, 50)); // Set the width and height here
        }

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setHorizontalAlignment(SwingConstants.RIGHT);
            inputPanel.add(label);
            inputFields[i] = new JTextField();
            inputFields[i].setHorizontalAlignment(JTextField.CENTER);
            inputFields[i].addActionListener(this);
            inputFields[i].addKeyListener(this);
            inputFields[i].setPreferredSize(new Dimension(50, 30));
            inputPanel.add(inputFields[i]);
        }

        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        quitButton = new JButton("Quit");
        quitButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(quitButton);

        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(inputPanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(scrollPane);

        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clearButton) {
            for (int i = 0; i < inputFields.length; i++) {
                inputFields[i].setText("");
            }
            outputArea.setText("");
        } else if (e.getSource() == saveButton) {
            saveResults();
        } else if (e.getSource() == quitButton) {
            frame.dispose();
        }
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER) {
            Object source = e.getSource();
            for (int i = 0; i < inputFields.length; i++) {
                if (source == inputFields[i]) {
                    if (i < inputFields.length - 1) {
                        inputFields[i+1].requestFocusInWindow();
                    } else {
                        saveResults();
                    }
                    break;
                }
            }
        }
    }

    private void saveResults() {
        String[] labels = {"GOT", "GPT", "GGT", "T-bilirubin", "Albumin"};
        String[] sublabels = Arrays.copyOf(labels, inputFields.length);
        List<String> valuesList = new ArrayList<String>();
        for (int i = 0; i < inputFields.length; i++) {
            String input = inputFields[i].getText();
            if (!input.isEmpty()) {
                String value = sublabels[i] + "..." + input;
                valuesList.add(value);
            }
        }
        String[] values = valuesList.toArray(new String[0]);
        String result = Arrays.toString(values);

        // Add the labels to the result
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        int count = 0;
        for (int i = 0; i < inputFields.length; i++) {
            if (!inputFields[i].getText().isEmpty()) {
                if (count > 0) {
                    sb.append(", ");
                }
                sb.append(labels[i]);
                count++;
            }
        }
        sb.append(" ]\n");

        // Add the result to the text area
        outputArea.append(result + "\n");
        GDSEMR_frame.setTextAreaText(5, "\n" + result + "\n");
    }



    public void keyReleased(KeyEvent e) {}


    public void keyTyped(KeyEvent e) {}


    public static void main(String[] args) {
        new EMR_LFT();
    }
}
