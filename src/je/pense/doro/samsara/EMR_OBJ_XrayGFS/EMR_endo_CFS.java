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

public class EMR_endo_CFS {
    public static void main(String[] args) {
        JFrame frame = new JFrame("GFS CFS Checklist");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 1)); // set layout to one row, one column

        // Create the buttons and add them to a panel
        JButton clearButton = new JButton("Clear");
        JButton saveButton = new JButton("Save");
        JButton quitButton = new JButton("Quit");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(quitButton);

        // Create the check boxes and labels
        String[] checklistItems2 = EMR_endo_CFS.colonArray();

        // Create the labels and set their properties
        JLabel[] labels = new JLabel[1];
        labels[0] = new JLabel("Colon:");
        for (JLabel label : labels) {
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        // Create the checkboxes and add them to the appropriate panel
        JPanel[] panels = new JPanel[1];
        JCheckBox[][] checkboxes = new JCheckBox[1][];
        checkboxes[0] = new JCheckBox[checklistItems2.length];
        for (int i = 0; i < panels.length; i++) {
            panels[i] = new JPanel();
            panels[i].setLayout(new BoxLayout(panels[i], BoxLayout.Y_AXIS));
            panels[i].add(labels[i]);
            for (int j = 0; j < checkboxes[i].length; j++) {
                checkboxes[i][j] = new JCheckBox(checklistItems2[j]);
                panels[i].add(checkboxes[i][j]);
            }
        }

        // Create a new panel for the buttons and text area
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);

        // Create the text area
		String cdate = Date_current.defineTime("d");
        JTextArea textArea = new JTextArea("\n< CFS >   " + cdate + "\n");
        textArea.setEditable(true);
        textArea.setWrapStyleWord(true);
        bottomPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        // Keep track of selected checkboxes
        Set<JCheckBox> selectedCheckboxes = new HashSet<>();

        // Add an action listener to the checkboxes to update the text area
        for (JCheckBox[] checkboxeslist : new JCheckBox[][] { checkboxes[0] }) {
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
            GDSEMR_frame.setTextAreaText(5, selectedItems );
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
        frame.setSize(400, 400);
        frame.pack(); // resize frame to fit its contents
        frame.setLocationRelativeTo(null); // center the frame on the screen
        frame.setVisible(true);
    }

	public static String[] colonArray() {
	    String[] stringArray = {"The colon mucosa appeared normal and No polyps ",
	    		"Colonic polyps",
	    		"    polyp #1",
	    		"    polyp #2-5",
	    		"    polyp #5 or more",
	    		"Inflammatory bowel disease (IBD)",
	    		"Ulcerative colitis",
	    		"Crohn's disease",
	    		"Diverticulitis",
	    		"Diverticulosis",
	    		"Colorectal cancer",

	    		"Irritable bowel syndrome (IBS)",
	    		"     Ischemic colitis",
	    		"     Pseudomembranous colitis",
	    		"     Infectious colitis",
	    		"     Lymphocytic colitis",
	    		"     Collagenous colitis",
	    		"     Microscopic colitis",
	    		"Familial adenomatous polyposis (FAP)"
	    		};
	    return stringArray;
	}
}
