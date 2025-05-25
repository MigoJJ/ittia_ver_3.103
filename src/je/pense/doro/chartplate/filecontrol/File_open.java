package je.pense.doro.chartplate.filecontrol;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;

public class File_open {

    public static void openWordFile(String filePath) {
        try {
            File file = new File(filePath);
            
            if (!file.exists()) {
                JOptionPane.showMessageDialog(null, "File does not exist: " + filePath, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Desktop.isDesktopSupported()) {
                JOptionPane.showMessageDialog(null, "Desktop is not supported", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(file);
            } else {
                JOptionPane.showMessageDialog(null, "Opening files is not supported on this platform", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error opening file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String wordFilePath) {
        // Example usage
//        String wordFilePath = "/path/to/your/word/file.docx";
        openWordFile(wordFilePath);
    }
}