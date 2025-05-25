package je.pense.doro.chartplate.keybutton.ai;

import java.awt.BorderLayout;	
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Mprompt extends JFrame {

    private JTextArea inputDataArea;
    private JTextArea outputDataArea;

    public Mprompt() {
        setTitle("Mprompt AI Listener");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Central panel with two text areas
        JPanel centralPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        inputDataArea = new JTextArea();
        outputDataArea = new JTextArea();

        inputDataArea.setFont(new Font("Consolas", Font.PLAIN, 10));
        outputDataArea.setFont(new Font("Consolas", Font.PLAIN, 10));

        centralPanel.add(new JScrollPane(inputDataArea));
        centralPanel.add(new JScrollPane(outputDataArea));
        add(centralPanel, BorderLayout.CENTER);

        // West panel with six buttons
        JPanel westPanel = new JPanel(new GridLayout(6, 1, 10, 10));

        JButton rescueButton = createStyledButton("Rescue", new Color(255, 87, 34), Color.WHITE);
        JButton copyToClipboardButton = createStyledButton("Copy to clipboard", new Color(76, 175, 80), Color.WHITE);
        JButton clearInputButton = createStyledButton("Clear input", new Color(33, 150, 243), Color.WHITE);
        JButton clearOutputButton = createStyledButton("Clear output", new Color(255, 193, 7), Color.WHITE);
        JButton clearAllButton = createStyledButton("Clear All", new Color(156, 39, 176), Color.WHITE);
        JButton saveAndQuitButton = createStyledButton("Save and Quit", new Color(244, 67, 54), Color.WHITE);


        westPanel.add(rescueButton);
        westPanel.add(copyToClipboardButton);
        westPanel.add(clearInputButton);
        westPanel.add(clearOutputButton);
        westPanel.add(clearAllButton);
        westPanel.add(saveAndQuitButton);

        add(westPanel, BorderLayout.WEST);

        // South panel with six buttons
        JPanel southPanel = new JPanel(new GridLayout(1, 6, 10, 10));

        JButton aButton = createStyledButton("A>", new Color(63, 81, 181), Color.WHITE);
        JButton labButton = createStyledButton("Lab>", new Color(3, 169, 244), Color.WHITE);
        JButton tLabButton = createStyledButton("T-Lab>", new Color(0, 188, 212), Color.WHITE);
        JButton piButton = createStyledButton("PI>", new Color(0, 150, 136), Color.WHITE);
        JButton pButton = createStyledButton("P>", new Color(139, 195, 74), Color.WHITE);
        JButton etcButton = createStyledButton("Etc>", new Color(255, 152, 0), Color.WHITE);

        southPanel.add(aButton);
        southPanel.add(labButton);
        southPanel.add(tLabButton);
        southPanel.add(piButton);
        southPanel.add(pButton);
        southPanel.add(etcButton);

        add(southPanel, BorderLayout.SOUTH);

        // Button Action Listeners
        copyToClipboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyTextToClipboard(outputDataArea.getText());
            }
        });

        clearInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputDataArea.setText("");
            }
        });

        clearOutputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                outputDataArea.setText("");
            }
        });

        clearAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputDataArea.setText("");
                outputDataArea.setText("");
            }
        });

        saveAndQuitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add save logic here if needed
                dispose();
            }
        });

        // Action listener for the A> button
        aButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formatAndDisplayOutput();
//                copyTextToClipboard(outputDataArea.getText());
//                appendClipboardContentToOutput();
            }
        });

        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }
    
    private JButton createStyledButton(String text, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFont(new Font("Consolas", Font.BOLD, 14));
        return button;
    }

    private void formatAndDisplayOutput() {
        String inputText = inputDataArea.getText();

        String formattedText = "\nI will give you a new problem data set.\n\n"
                + inputText + "\n\n"
                + "Can you sort this result by the table of contents in Harrison's Textbook of Internal Medicine?\n"
                + "For the report, ensure the Category is formatted as FONT Consolas 11, color -> Sky Blue;\n"
                + "Titles FONT Consolas 10, BOLD, Size 10, color -> Deep Blue, BOLD;\n"
                + "The Note or contents as FONT NORMAL, size 9, color -> Black;\n"
                + "With all line spacing set to 1.0.\n"
                + "And modify this result to a more easily visible format.";

        outputDataArea.setFont(new Font("Consolas", Font.PLAIN, 10)); // Set font as specified
        outputDataArea.setForeground(Color.BLACK); // Set text color to black
        outputDataArea.setLineWrap(true); // Set line wrap to true for better readability
        outputDataArea.setText(formattedText); // Set the formatted text to the output area
    }

    private void copyTextToClipboard(String text) {
        StringSelection stringSelection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
    private void appendClipboardContentToOutput() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            String clipboardText = (String) clipboard.getData(DataFlavor.stringFlavor);
            outputDataArea.append("\n" + clipboardText); // Append a new line and clipboard content to output area
        } catch (Exception ex) {
            outputDataArea.append("\n[Error fetching clipboard content]");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Mprompt::new);
    }
}

