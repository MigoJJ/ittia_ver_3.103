package je.pense.doro.chartplate.filecontrol;

import javax.swing.*;

import je.pense.doro.entry.EntryDir;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;

public class FileGeditToCilpboard extends JFrame {
	
	public static void FileGeditToCilpboard() {
        String fileName = EntryDir.BACKUP_DIR + "/backup";
        try {
            Process p = Runtime.getRuntime().exec("gedit " + fileName);
            Timer timer = new Timer(5000, new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    p.destroy();
                }
            });
            timer.setRepeats(false);
            timer.start();

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection stringSelection = new StringSelection(getFileContents(fileName));
            clipboard.setContents(stringSelection, null);

        } catch (IOException e) {
        }
    }

    private static String getFileContents(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        br.close();
        return sb.toString();
    }

    public static void main(String[] args) {
        new FileGeditToCilpboard();
    }

}
