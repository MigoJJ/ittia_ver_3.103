package je.pense.doro.chartplate.mainpage;

import javax.swing.JTextArea;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.entry.EntryDir;

public class EMR_resettextAreas10 {

    private static final String[] TITLES = {
        "CC>", "PI>", "ROS>", "PMH>", "S>", "O>",
        "Physical Exam>", "A>", "P>", "Comment>"
    };

    public static final int NUM_TEXT_AREAS = TITLES.length;

    public EMR_resettextAreas10(String caseFolder) {
        for (int i = 0; i < NUM_TEXT_AREAS; i++) {
            if (GDSEMR_frame.textAreas[i] != null) {
                GDSEMR_frame.textAreas[i].setText(TITLES[i] + "\t");
            }

            String fileName = EntryDir.HOME_DIR + caseFolder + "/textarea" + i;
            new FileLoader(fileName, i).execute();
        }
    }

    static class FileLoader {
        private final String fileName;
        private final int textAreaIndex;

        public FileLoader(String fileName, int textAreaIndex) {
            this.fileName = fileName;
            this.textAreaIndex = textAreaIndex;
        }

        public void execute() {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    GDSEMR_frame.textAreas[textAreaIndex].append(line + "\n");
                }
                System.out.println("Loaded content into textArea[" + textAreaIndex + "] from " + fileName);
            } catch (IOException e) {
                System.err.println("Error reading file " + fileName + ": " + e.getMessage());
            }
        }
    }

    public static void main(String casePath) {
        new EMR_resettextAreas10(casePath);
    }
}
