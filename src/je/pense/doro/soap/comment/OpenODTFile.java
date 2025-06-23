package je.pense.doro.soap.comment;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import je.pense.doro.entry.EntryDir;

public class OpenODTFile {
    public static void main(String[] args) {
        File file = new File(EntryDir.HOME_DIR + "/soap/comment/retinopathy.odt");
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            } else {
                System.out.println("Desktop not supported");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
