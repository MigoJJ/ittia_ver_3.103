package je.pense.doro.fourgate.osteoporosis.buttons;

import java.awt.BorderLayout;	
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import je.pense.doro.entry.EntryDir;
import je.pense.doro.fourgate.osteoporosis.EMR_Osteoporosis_meds;

public class OS_definition extends JFrame {
    private static final String BASE_PATH = EntryDir.HOME_DIR + "/support/EMR_support_Folder/Osteoporosis/";
    
    private enum OsOption {
        DEFINITION("Osteoporosis Definition", "OsteoporosisDiagnosis.png", true),
        SECONDARY("Secondary Osteoporosis", "Causes of Secondary Osteoporosis.txt", false),
        MEDICATIONS("Medications", "Osteoporosis.jpg", true);
        
        final String label;
        final String filename;
        final boolean isImage;
        
        OsOption(String label, String filename, boolean isImage) {
            this.label = label;
            this.filename = filename;
            this.isImage = isImage;
        }
    }

    public OS_definition(String osargs) {
        setupFrame();
        handleOption(osargs);
    }

    private void setupFrame() {
        setTitle("Osteoporosis Viewer");
    	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }

    private void handleOption(String osargs) {
        for (OsOption option : OsOption.values()) {
            if (option.label.equals(osargs)) {
                if (option == OsOption.MEDICATIONS) {
                    EMR_Osteoporosis_meds.main(null);
                }
                displayContent(option);
                setVisible(true);
                return;
            }
        }
        showError("Invalid argument. Use 'Osteoporosis Definition', 'Secondary Osteoporosis', or 'Medications'.");
    }

    private void displayContent(OsOption option) {
        String filePath = BASE_PATH + option.filename;
        try {
            if (option.isImage) {
                displayImage(filePath);
            } else {
                displayText(filePath);
            }
        } catch (IOException e) {
            showError("Error loading " + (option.isImage ? "image" : "text file") + ": " + e.getMessage());
        }
    }

    private void displayImage(String imagePath) throws IOException {
        ImageIcon icon = new ImageIcon(ImageIO.read(new File(imagePath)));
        JLabel label = new JLabel(icon);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(new JScrollPane(label), BorderLayout.CENTER);
    }

    private void displayText(String txtPath) throws IOException {
        String textContent = new String(java.nio.file.Files.readAllBytes(new File(txtPath).toPath()));
        JTextArea textArea = new JTextArea(textContent);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message);
        dispose();
    }

    public static void main(String osargs) {
        SwingUtilities.invokeLater(() -> new OS_definition(osargs));
    }
}
