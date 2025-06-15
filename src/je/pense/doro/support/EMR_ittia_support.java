package je.pense.doro.support;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import je.pense.doro.entry.EntryDir;

public class EMR_ittia_support {

    public static void main(String[] args) {
        openDirectory("EMR_support_Folder");
    }

    public static void thyroid(String folderName) {
        openDirectory("EMR_support_Folder" + File.separator + folderName);
    }
    private static void openDirectory(String relativePath) {
        String directoryPath = EntryDir.HOME_DIR + File.separator + "support" + File.separator + relativePath;
        File directory = new File(directoryPath);

        try {
            if (directory.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(directory);
                    System.out.println("Directory opened: " + directoryPath);
                } else {
                    System.err.println("Desktop is not supported on this platform.");
                }
            } else {
                System.err.println("Directory does not exist: " + directoryPath);
            }
        } catch (IOException e) {
            System.err.println("Error opening directory: " + e.getMessage());
        }
    }
}
