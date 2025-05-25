
package je.pense.doro.samsara.EMR_OBJ_XrayGFS;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.filecontrol.datetime.Date_current;

public class EMR_endo_GFS {
    public static void main(String[] args) {
        JFrame frame = new JFrame("GFS CFS Checklist");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 2)); // set layout to one row, two columns

        // Create the buttons and add them to a panel
        JButton clearButton = new JButton("Clear");
        JButton saveButton = new JButton("Save");
        JButton quitButton = new JButton("Quit");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(quitButton);

        // Create the check boxes and labels
        String[] checklistItems = EMR_endo_GFS.esophagusArray();
        String[] checklistItems1 = EMR_endo_GFS.stomachArray();

        // Create the labels and set their properties
        JLabel[] labels = new JLabel[2];
        labels[0] = new JLabel("Esophagus:");
        labels[1] = new JLabel("Stomach and Duodenum:");
        for (JLabel label : labels) {
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // Create the checkboxes and add them to the appropriate panel
        JPanel[] panels = new JPanel[2];
        JCheckBox[][] checkboxes = new JCheckBox[2][];
        checkboxes[0] = new JCheckBox[checklistItems.length];
        checkboxes[1] = new JCheckBox[checklistItems1.length];
        for (int i = 0; i < panels.length; i++) {
            panels[i] = new JPanel();
            panels[i].setLayout(new BoxLayout(panels[i], BoxLayout.Y_AXIS));
            panels[i].add(labels[i]);
            for (int j = 0; j < checkboxes[i].length; j++) {
                if (i == 0) {
                    checkboxes[i][j] = new JCheckBox(checklistItems[j]);
                } else if (i == 1) {
                    checkboxes[i][j] = new JCheckBox(checklistItems1[j]);
                }
                panels[i].add(checkboxes[i][j]);
            }
        }

        // Create a new panel for the buttons and text area
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);

        // Create the text area
		String cdate = Date_current.defineTime("d");
        JTextArea textArea = new JTextArea("\n< GFS >   " + cdate + "\n");
        textArea.setEditable(true);
        textArea.setWrapStyleWord(true);
        bottomPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Keep track of selected checkboxes
        Set<JCheckBox> selectedCheckboxes = new HashSet<>();

        // Add an action listener to the checkboxes to update the text area
        for (JCheckBox[] checkboxeslist : new JCheckBox[][] { checkboxes[0], checkboxes[1] }) {
            for (JCheckBox checkbox : checkboxeslist) {
                checkbox.addChangeListener(e -> {
                    if (checkbox.isSelected()) {
                        if (!selectedCheckboxes.contains(checkbox)) {
                            textArea.append("\t" + checkbox.getText() + "\n");
                            selectedCheckboxes.add(checkbox);
                        }
                    } else {
                        if (selectedCheckboxes.contains(checkbox)) {
                            String checkboxText = checkbox.getText() + "\n";
                            int index = textArea.getText().indexOf(checkboxText);
                            if (index >= 0) {
                                textArea.replaceRange("", index, index + checkboxText.length());
                            }
                            selectedCheckboxes.remove(checkbox);
                        }
                    }
                });
            }
        }
		
        saveButton.addActionListener(event -> {
            String selectedItems = textArea.getText();
            GDSEMR_frame.setTextAreaText(5, selectedItems + "  " + cdate);
            frame.dispose();
        });
        quitButton.addActionListener(event -> {
            frame.dispose();
        });
        clearButton.addActionListener(event -> {
            textArea.setText(" < GFS : CFS : > \n");
        });

        // Add the bottom panel to the bottom of the main frame
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(panels[0]);
        frame.add(panels[1]);
        frame.setSize(400, 400);
        frame.pack(); // resize frame to fit its contents
        frame.setLocationRelativeTo(null); // center the frame on the screen
        frame.setVisible(true);
    }

	
	public static String[] esophagusArray() {
	    String[] stringArray = {
	    		"Gastroesophageal reflux disease (GERD)",
	    		"Esophageal spasm",
	    		"Achalasia",
	    		"Hiatal hernia",
	    		"Esophageal strictures",
	    		"Barrett's esophagus",
	    		"Eosinophilic esophagitis",
	    		"Infectious esophagitis",
	    		"Esophageal varices",
	    		"Plummer-Vinson syndrome",
	    		"Reflux esophagitis",
	    		"Zenker's diverticulum"
	    		};
	    return stringArray;
	}
	public static String[] stomachArray() {
	    String[] stringArray = {"Gastritis",
		    	"Acute gastritis",
		    	"Chronic superficial gastritis",
		    	"Chronic erosive gastritis",
		    	"Chronic atrophic gastritis",
		    	"Gastric polyp",
		    	"Gastric submucosal tumor",
		    	"Helicobacter pylori infection",
		    	"Helicobacter pylori infection with +Hp4 meds",

		    	"Peptic ulcer disease",
		    	"Gastroesophageal reflux disease (GERD)",
		    	"Zollinger-Ellison syndrome",
		    	"Helicobacter pylori infection",
		    	"Gastric volvulus",
		    	"Ménétrier's disease",
		    	"Acute gastric dilatation",
		    	"Gastrointestinal stromal tumor (GIST)",
		    	"r/o Gastric cancer",
		    	"      Linitis plastica",
		    	"Gastric lymphoma",
		    	"Eosinophilic gastroenteritis",
		    	"Duodenitis",
		    	"Duodenal ulcer"
	    		};
	    return stringArray;
	}
}
