package je.pense.doro.chartplate.keybutton.EMR_Backup_Excute;


import java.io.BufferedWriter;	
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.entry.EntryDir;

public class EMR_B_CopyBackup {

	    public void saveTextToFile(String patientId) {
	        try {
	            String content = GDSEMR_frame.tempOutputArea.getText();
	            String directoryPath = EntryDir.HOME_DIR + "/tripikata/rescue/rescuefolder/";
	            File directory = new File(directoryPath);
	            if (!directory.exists()) {
	                directory.mkdirs();  // Create the directory if it does not exist
	            }
	            File file = new File(directoryPath + patientId + ".txt");
	            FileWriter fw = new FileWriter(file);
	            BufferedWriter bw = new BufferedWriter(fw);
	            bw.write(content);
	            bw.close();
	            JOptionPane.showMessageDialog(null, "Backup saved successfully!");
	        } catch (IOException ex) {
	            JOptionPane.showMessageDialog(null, "Error saving backup: " + ex.getMessage());
	        }
	    }
	    public void clearDirectory() {
	        String directoryPath = EntryDir.HOME_DIR + "/tripikata/rescue/rescuefolder/";
	        File directory = new File(directoryPath);
	        if (directory.exists()) {
	            File[] files = directory.listFiles(); // Get all files in the directory
	            if (files != null) { // Make sure the list files operation doesn't return null
	                for (File file : files) {
	                    if (!file.delete()) { // Attempt to delete each file
	                        JOptionPane.showMessageDialog(null, "Failed to delete " + file.getName(), "Error", JOptionPane.ERROR_MESSAGE);
	                        return; // Exit if a deletion fails
	                    }
	                }
	                JOptionPane.showMessageDialog(null, "All files have been deleted successfully!");
	            }
	        } else {
	            JOptionPane.showMessageDialog(null, "Directory does not exist or is not accessible", "Error", JOptionPane.WARNING_MESSAGE);
	        }
	    }

}

	