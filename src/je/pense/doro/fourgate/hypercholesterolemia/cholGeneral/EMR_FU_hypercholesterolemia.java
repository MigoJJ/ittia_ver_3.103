package je.pense.doro.fourgate.hypercholesterolemia.cholGeneral;

import javax.swing.*;

import je.pense.doro.entry.EntryDir;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class EMR_FU_hypercholesterolemia extends JFrame {
    private static final String BASE_PATH = EntryDir.HOME_DIR + "/fourgate/hypercholesterolemia/cholGeneral/textarea";
    private static final String[] SECTIONS = {
        "CC>", "PI>", "ROS>", "PMH>", "S>", "O>", 
        "Physical Exam>", "A>", "P>", "Comment>"
    };
    
    private final ArrayList<JTextArea> textAreas = new ArrayList<>();

    public EMR_FU_hypercholesterolemia() {
        initializeFrame();
        createAndShowGUI();
    }

    private void initializeFrame() {
        setTitle("Hypercholesterolemia Preform");
        setSize(400, 1000);
        setLocation(100, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createAndShowGUI() {
        add(createTextAreaPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.NORTH);
        setVisible(true);
    }

    private JPanel createTextAreaPanel() {
        JPanel panel = new JPanel(new GridLayout(SECTIONS.length, 1));
        float brightness = 1.0f;
        
        for (int i = 0; i < SECTIONS.length; i++) {
            JTextArea textArea = createTextArea(i);
            textArea.setBackground(createGradientColor(brightness));
            brightness -= 0.02f;
            
            panel.add(new JScrollPane(textArea));
            textAreas.add(textArea);
        }
        
        return panel;
    }

    private JTextArea createTextArea(int index) {
        JTextArea textArea = new JTextArea(loadTextFromFile(index));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }

    private Color createGradientColor(float brightness) {
        return Color.getHSBColor(0.06f, 0.5f, brightness);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveAllTexts());
        
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> dispose());
        
        panel.add(saveButton);
        panel.add(exitButton);
        return panel;
    }

    private String loadTextFromFile(int index) {
        File file = new File(BASE_PATH + index);
        if (!file.exists()) return SECTIONS[index];

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return SECTIONS[index];
        }
    }

    private void saveAllTexts() {
        try {
            for (int i = 0; i < textAreas.size(); i++) {
                try (BufferedWriter writer = new BufferedWriter(
                        new FileWriter(BASE_PATH + i))) {
                    writer.write(textAreas.get(i).getText());
                }
            }
            JOptionPane.showMessageDialog(this, "Files saved successfully");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving files: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EMR_FU_hypercholesterolemia::new);
    }
}
