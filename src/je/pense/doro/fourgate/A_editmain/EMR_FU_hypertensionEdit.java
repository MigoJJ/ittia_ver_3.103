package je.pense.doro.fourgate.A_editmain;

import java.io.File;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.entry.EntryDir;

/**
 * Handles the editing of hypertension follow-up records in the EMR system.
 * Loads text content from files and updates the UI accordingly.
 */
public class EMR_FU_hypertensionEdit extends JFrame {
    private static final int NUM_TEXT_AREAS = 10;
    private static final String HTN_BASE_PATH = EntryDir.HOME_DIR + "/fourgate/hypertension/htnGeneral/textarea";

    public EMR_FU_hypertensionEdit() {
        initializeTextAreas();
    }

    /**
     * Initializes all text areas by loading content from corresponding files
     */
    private void initializeTextAreas() {
        for (int areaIndex = 0; areaIndex < NUM_TEXT_AREAS; areaIndex++) {
            clearTextArea(areaIndex);
            String filePath = HTN_BASE_PATH + areaIndex;
            loadFileContent(filePath, areaIndex);
        }
    }

    /**
     * Clears the specified text area if it exists
     */
    private void clearTextArea(int index) {
        if (hasValidTextArea(index)) {
            GDSEMR_frame.textAreas[index].setText("");
        }
    }

    private boolean hasValidTextArea(int index) {
        return GDSEMR_frame.textAreas != null && 
               GDSEMR_frame.textAreas[index] != null;
    }

    /**
     * Initiates asynchronous loading of file content
     */
    private void loadFileContent(String filePath, int areaIndex) {
        new FileContentLoader(filePath, areaIndex).execute();
    }

    /**
     * SwingWorker class to handle asynchronous file loading
     */
    private class FileContentLoader extends SwingWorker<String, Void> {
        private final String filePath;
        private final int targetIndex;

        public FileContentLoader(String filePath, int targetIndex) {
            this.filePath = filePath;
            this.targetIndex = targetIndex;
        }

        @Override
        protected String doInBackground() {
            return readFileContent();
        }

        private String readFileContent() {
            StringBuilder content = new StringBuilder();
            try (Scanner fileScanner = new Scanner(new File(filePath))) {
                while (fileScanner.hasNextLine()) {
                    content.append(fileScanner.nextLine()).append("\n");
                }
            } catch (Exception e) {
                logError("File reading failed", e);
            }
            return content.toString();
        }

        @Override
        protected void done() {
            try {
                updateTextArea(get());
            } catch (Exception e) {
                logError("Content loading failed", e);
            }
        }

        private void updateTextArea(String content) {
            if (hasValidTextArea(targetIndex)) {
                GDSEMR_frame.textAreas[targetIndex].setText(content);
                
                // Special handling for BP recommendations
                if (targetIndex == 9) {
                    String bpRecommendation = EMR_FU_Comments.getRandomRecommendation("BP");
                    GDSEMR_frame.setTextAreaText(9, bpRecommendation);
                }
            }
        }

        private void logError(String message, Exception e) {
            System.err.printf("%s for %s: %s%n", message, filePath, e.getMessage());
        }
    }

    public static void main(String[] args) {
        new EMR_FU_hypertensionEdit();
    }
}
