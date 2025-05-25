package je.pense.doro.fourgate.A_editmain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.entry.EntryDir;

public class EMR_FU_diabetesEdit extends JFrame {
    private static final int NUM_TEXT_AREAS = 10;

    public EMR_FU_diabetesEdit() {
        for (int i = 0; i < NUM_TEXT_AREAS; i++) {
            if (GDSEMR_frame.textAreas != null && GDSEMR_frame.textAreas[i] != null) {
                GDSEMR_frame.textAreas[i].setText("");
            }
            String fileName = EntryDir.homeDir + "/fourgate/diabetes/dmGeneral/textarea" + (i);
            new FileLoader(fileName, i).execute();
        }
    }

    private class FileLoader extends SwingWorker<String, Void> {
        private final String fileName;
        private final int index;

        public FileLoader(String fileName, int index) {
            this.fileName = fileName;
            this.index = index;
        }

        @Override
        protected String doInBackground() throws Exception {
            String text = "";
            try (Scanner scanner = new Scanner(new File(fileName))) {
                while (scanner.hasNextLine()) {
                    text += scanner.nextLine() + "\n";
                }
            } catch (FileNotFoundException ex) {
                System.err.println("Failed to read file " + fileName + ": " + ex.getMessage());
            }
            return text;
        }

        @Override
        protected void done() {
            try {
                String text = get();
                if (GDSEMR_frame.textAreas != null && GDSEMR_frame.textAreas[index] != null) {
                    GDSEMR_frame.textAreas[index].setText(text);

                    // Get the recommendation directly:
                    String recommendation = EMR_FU_Comments.getRandomRecommendation("DM");
                    GDSEMR_frame.setTextAreaText(9, recommendation);  
                }
                System.out.println("Loaded text from file: " + fileName); 
            } catch (Exception ex) {
                System.err.println("Failed to load file " + fileName + ": " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new EMR_FU_diabetesEdit(); 
    }
}