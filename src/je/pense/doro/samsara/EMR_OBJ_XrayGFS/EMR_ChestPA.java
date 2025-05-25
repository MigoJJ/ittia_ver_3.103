package je.pense.doro.samsara.EMR_OBJ_XrayGFS;
import java.time.LocalDate;	
import java.time.format.DateTimeFormatter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.filecontrol.datetime.Date_current;

public class EMR_ChestPA {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Chest Pain Checklist");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); // center the frame on the screen
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
	// Create the check boxes and labels
		String[] checklistItems = {
			
				"A normal Chest PA X-ray ",
				"Pneumonia",
				"Pulmonary edema",
				"Chronic obstructive pulmonary disease (COPD)",
				"Lung cancer",
				"Pleural effusion",
				"Tuberculosis",
				"Tuberculosis scar",
				"Atelectasis",
				"Bronchitis",
				"Interstitial lung disease",
				"Pulmonary embolism.",
				"Cardiomegaly"
		
		};
		
		JCheckBox[] checkboxes = new JCheckBox[checklistItems.length];
		for (int i = 0; i < checklistItems.length; i++) {
		checkboxes[i] = new JCheckBox(checklistItems[i]);
		panel.add(checkboxes[i]);
		}
	
		// Create the submit button
		JButton submitButton = new JButton("Submit");
			submitButton.addActionListener(event -> {
			String selectedItems = "\n< Chest PA >";
			for (int i = 0; i < checkboxes.length; i++) {
				if (checkboxes[i].isSelected()) {
				selectedItems += "\t" + checkboxes[i].getText();
				}
			}
			//		JOptionPane.showMessageDialog(frame, selectedItems);
			String cdate = Date_current.defineTime("d");
			GDSEMR_frame.setTextAreaText(5, selectedItems  + "   " + cdate  + "\n");
//			saveComment(selectedItems);
			frame.dispose();
		
		});
		panel.add(submitButton);
		// Add the panel to the frame
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	public static void saveComment(String argscomment) {

		String[] lines = argscomment.split("\n");
		StringBuilder updatedEkg = new StringBuilder();
	
		for (int i = 1; i < lines.length; i++) {
		    if (i == lines.length - 1) {
		        LocalDate currentDate = LocalDate.now();
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		        String formattedDate = currentDate.format(formatter);
		        updatedEkg.append(lines[i]).append(" -> ").append(formattedDate);
		    } else {
		        updatedEkg.append("Ⓟ ").append(lines[i]);
		    }
		    updatedEkg.append("\n");
		}
	
		String result = updatedEkg.toString();
		GDSEMR_frame.setTextAreaText(9,result);
	}
}