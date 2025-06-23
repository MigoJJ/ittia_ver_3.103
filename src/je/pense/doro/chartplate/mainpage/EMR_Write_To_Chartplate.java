package je.pense.doro.chartplate.mainpage;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringJoiner;

import javax.swing.JTextArea;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.filecontrol.File_copy;
import je.pense.doro.entry.EntryDir;

public class EMR_Write_To_Chartplate extends GDSEMR_frame {

    public EMR_Write_To_Chartplate() throws Exception {
        super();
        // TODO Auto-generated constructor stub
    }

    public static void textAreaAppend(JTextArea tempOutputArea) throws IOException {
        String originalText = tempOutputArea.getText();
        // Split the text into lines
        String[] lines = originalText.split("\n");
        Set<String> seenLines = new LinkedHashSet<>();
        StringJoiner resultText = new StringJoiner("\n");

        // Process lines
        for (String line : lines) {
            if (!seenLines.contains(line)) {
                if (line.contains(":")) {
                    line = EMR_ChangeString.EMR_ChangeString(line);
                }
                seenLines.add(line);
                resultText.add(line);
            }
        }

        // Set the text of the JTextArea to the filtered text
        tempOutputArea.setText(resultText.toString());
//        System.out.println("Processed text: " + resultText.toString());

        // Copy updated text to clipboard
        copyToClipboard(tempOutputArea);
        // Save updated text to file
        saveTextToFile(tempOutputArea);
    }

    public static void copyToClipboard(JTextArea textArea) {
        StringSelection stringSelection = new StringSelection(textArea.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    public static void saveTextToFile(JTextArea textArea) {
        String textToSave = textArea.getText();
        String filePath = EntryDir.HOME_DIR + File.separator + "tripikata" + File.separator + "rescue" + File.separator + "backup";
        String newFilePath = EntryDir.HOME_DIR + File.separator + "tripikata" + File.separator + "rescue" + File.separator + "backuptemp";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(textToSave);
        } catch (IOException e) {
            e.printStackTrace();
        }

        File_copy.main(filePath, newFilePath);
    }
}
