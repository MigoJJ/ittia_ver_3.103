package je.pense.doro.fourgate.diabetes.dmGeneral;

import javax.swing.*;

import je.pense.doro.entry.EntryDir;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class EMR_FU_diabetes extends JFrame {

//    private static final String BASE_PATH = EntryDir.CURRENT_DIR + "/fourgate/diabetes/dmGeneral/textarea";
    private static final String BASE_PATH = EntryDir.CURRENT_DIR + "/je/pense/doro/fourgate/diabetes/dmGeneral/textarea";
    private static final String[] SECTION_TITLES = {
        "CC>", "PI>", "ROS>", "PMH>", "S>", "O>", 
        "Physical Exam>", "A>", "P>", "Comment>"
    };
    
    private final ArrayList<JTextArea> textAreas = new ArrayList<>();

    public EMR_FU_diabetes() {
        setupFrame();
        createUI();
    }

    private void setupFrame() {
        setTitle("Diabetes Mellitus Preform");
        setSize(400, 1000);
        setLocation(200, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createTextAreaPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.NORTH);
        add(mainPanel);
        setVisible(true);
    }

    private JPanel createTextAreaPanel() {
        JPanel panel = new JPanel(new GridLayout(SECTION_TITLES.length, 1));
        
        for (int i = 0; i < SECTION_TITLES.length; i++) {
            JTextArea textArea = createTextArea(i);
            panel.add(new JScrollPane(textArea));
            textAreas.add(textArea);
        }
        
        return panel;
    }

    private JTextArea createTextArea(int index) {
        JTextArea textArea = new JTextArea(loadText(index));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.LIGHT_GRAY);
        return textArea;
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

    private String loadText(int index) {
    	    File file = new File(BASE_PATH + index);
    	    System.out.println("Attempting to load file: " + file.getAbsolutePath());
    	    if (!file.exists()) {
    	        System.out.println("File does NOT exist: " + file.getAbsolutePath());
    	        return SECTION_TITLES[index]; // <--- This is where it happens!
    	    }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return SECTION_TITLES[index];
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
            JOptionPane.showMessageDialog(this, "Text saved successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving files: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EMR_FU_diabetes::new);
    }
}
