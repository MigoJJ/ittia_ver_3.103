package je.pense.doro.fourgate.hypertension.htnGeneral;

import javax.swing.*;

import je.pense.doro.entry.EntryDir;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class EMR_FU_hypertension extends JFrame {
    private static final String BASE_PATH = EntryDir.HOME_DIR + "/fourgate/hypertension/htnGeneral/textarea";
    private static final String[] SECTIONS = {
        "CC>", "PI>", "ROS>", "PMH>", "S>", "O>", 
        "Physical Exam>", "A>", "P>", "Comment>"
    };
    
    private final ArrayList<JTextArea> textAreas = new ArrayList<>();
    private static final Color[] GRADIENT_COLORS = createGradientColors();

    public EMR_FU_hypertension() {
        setupFrame();
        createUI();
    }

    private void setupFrame() {
        setTitle("Hypertension Preform");
        setSize(400, 1000);
        setLocation(200, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createUI() {
        add(createTextPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.NORTH);
        setVisible(true);
    }

    private JPanel createTextPanel() {
        JPanel panel = new JPanel(new GridLayout(SECTIONS.length, 1));
        
        for (int i = 0; i < SECTIONS.length; i++) {
            JTextArea textArea = createTextArea(i);
            panel.add(new JScrollPane(textArea));
            textAreas.add(textArea);
        }
        
        return panel;
    }

    private JTextArea createTextArea(int index) {
        JTextArea area = new JTextArea(loadText(index));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(GRADIENT_COLORS[index]);
        return area;
    }

    private static Color[] createGradientColors() {
        Color[] colors = new Color[SECTIONS.length];
        float hue = 0.12f;
        float saturation = 0.5f;
        float brightness = 1.0f;
        
        for (int i = 0; i < colors.length; i++) {
            colors[i] = Color.getHSBColor(hue, saturation, brightness);
            brightness -= 0.03f;
        }
        return colors;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveTexts());
        
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> dispose());
        
        panel.add(saveButton);
        panel.add(exitButton);
        return panel;
    }

    private String loadText(int index) {
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

    private void saveTexts() {
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
        SwingUtilities.invokeLater(EMR_FU_hypertension::new);
    }
}
