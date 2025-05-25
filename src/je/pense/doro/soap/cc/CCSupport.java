package je.pense.doro.soap.cc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import je.pense.doro.GDSEMR_frame;

public class CCSupport {
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    
    private JTextArea textArea;
    private JPanel centerPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CCSupport::new);
    }

    public CCSupport() {
        JFrame frame = createMainFrame();
        frame.add(createTextArea(), BorderLayout.NORTH);
        frame.add(createComboBoxPanel(), BorderLayout.CENTER);
        frame.add(createButtonPanel(), BorderLayout.SOUTH);
        frame.add(createCheckBoxPanel(), BorderLayout.EAST); // Add the checkbox panel to the EAST
        frame.setVisible(true);
    }

    private JFrame createMainFrame() {
        JFrame frame = new JFrame("Chief Compaint Support");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        return frame;
    }

    private JScrollPane createTextArea() {
        textArea = new JTextArea(10, 40);
        return new JScrollPane(textArea);
    }

    private JPanel createComboBoxPanel() {
        centerPanel = new JPanel(new GridLayout(10, 1));
        String[][] comboBoxOptions = getComboBoxOptions();

        for (String[] comboBoxOption : comboBoxOptions) {
            JComboBox<String> comboBox = new JComboBox<>(comboBoxOption);
            comboBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    textArea.append(e.getItem().toString() + "\n");
                }
            });
            centerPanel.add(comboBox);
        }

        // Fill with generic options if less than 10 arrays
        for (int i = comboBoxOptions.length; i < 10; i++) {
            JComboBox<String> comboBox = new JComboBox<>(new String[]{"Option 1", "Option 2", "Option 3"});
            centerPanel.add(comboBox);
        }
        return centerPanel;
    }

    private JPanel createButtonPanel() {
        JPanel southPanel = new JPanel();

        // Define button properties in a structured manner
        String[] buttonLabels = {"Clear", "Save", "Quit"};
        ActionListener[] actionListeners = {
            this::clearAction, 
            this::saveAction,
            this::quitAction
        };

        // Create buttons using iteration
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.addActionListener(actionListeners[i]);
            southPanel.add(button);
        }

        return southPanel;
    }
    private void clearAction(ActionEvent e) {
        for (Component innerComp : centerPanel.getComponents()) {
            if (innerComp instanceof JComboBox) {
                ((JComboBox<?>) innerComp).setSelectedIndex(0);
            }
        }
        textArea.setText("");
    }

    private void saveAction(ActionEvent e) {
        String assess = textArea.getText();
        if (assess.endsWith("\n")) {
            assess = assess.substring(0, assess.length() - 1);
        }
        GDSEMR_frame.setTextAreaText(0, "   " + assess +"\t:(" );
    }

    private void quitAction(ActionEvent e) {
        JFrame frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, (Component) e.getSource());
        frame.dispose();
    }

    private JPanel createCheckBoxPanel() {
        JPanel eastPanel = new JPanel(new GridLayout(0, 1)); // Use GridLayout with 0 rows to make it dynamic

        // Create checkboxes and add them to the panel
        String[] checkboxLabels = {
            "Cough and Sneezing","Cough and Sore throat",
            "Vertigo", "Atypical chest pain",
            "Abdominal pain",  "Epigastric pain","Diarrhea", "Headache",
            "Skin Eruption and Vesicles", "Neck nodules",".",
            "RC result review", "공단 검진", "지디스 검진",
            "Other clinic Lab check", 
            ".", "Hearing Loss", "s/p CVA",
            ".", "Cognitive Disorder", ".",
            ".", "."
        };

        for (String label : checkboxLabels) {
            JCheckBox checkBox = new JCheckBox(label);
            checkBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Append the checkbox label to the textArea
                    textArea.append("   " + label + "\n");
                }
            });
            eastPanel.add(checkBox);
        }

        return eastPanel;
    }

    private String[][] getComboBoxOptions() {
        return new String[][]  {
			 {       "****  CC  ****",
				 "Fever", "Cough", "Shortness of Breath", "Chest Pain", "Abdominal Pain", "Headache", "Fatigue/Weakness", "Dizziness/Lightheadedness"
	            },
        	
        			 {       "****  DM  ****",
	            "Poorly controlled blood sugar--------------", 
	            "Frequent urination", "Excessive thirst", "Increased hunger", "Unexplained weight loss", "Blurred vision", "Fatigue/Weakness", "Dizziness/Lightheadedness", "Shakiness/Tremors", "Sweating", "Irritability", "Confusion", 
	            "Diabetic complications--------------",
	            "Foot problems","Wound healing issues", "Visual changes", "Numbness/tingling", "Chest pain", "Shortness of breath", "Erectile dysfunction", 
	            "Medication Side Effects--------------",
	            "Nausea", "Vomiting", "Diarrhea", "Constipation", "Headache", 
	            "Infections--------------",
	            "Fever", "Cough", "Sore throat", "Skin infections", "Urinary tract infection"
        	            },
        	            {   "****  Thyroid  ****",
        	            	"Weight changes", "Fatigue/Weakness", "Changes in bowel habits", "Changes in heart rate", "Changes in mood", "Sleep disturbances", "Intolerance to heat or cold", "Skin changes", "Hair changes", "Menstrual irregularities", "Muscle weakness/aches", "Goiter", "Tremors", "Difficulty concentrating/cognitive changes", "Myxedema", "Thyroid storm"
        	            },
        	            {
        	            	"****  GI  ****",
        	            	"Abdominal pain", "Nausea", "Vomiting", "Diarrhea", "Constipation", "Heartburn/Acid reflux", "Indigestion/Dyspepsia", "Bloating", "Flatulence", "Loss of appetite", "Weight loss", "Changes in stool color/consistency", "Rectal bleeding", "Jaundice", "Dysphagia"
        	            },
        	            {
        	            	"****  Liver  ****",
        	            	"Jaundice", "Fatigue/Weakness", "Abdominal pain/discomfort", "Nausea and Vomiting", "Loss of appetite", "Dark urine", "Pale or clay-colored stools", "Itching", "Easy bruising or bleeding", "Swelling in the legs and ankles", "Ascites", "Encephalopathy", "Weight loss", "Right upper quadrant pain"
        	    		},
        	            {
        	            	"****  Cancer  ****",
        	            	"Unexplained Weight Loss", "Fatigue/Weakness", "Pain", "Lump or Thickening", "Changes in Bowel or Bladder Habits", "Persistent Cough or Hoarseness", "Unusual Bleeding or Discharge", "Non-healing Sore or Ulcer", "Changes in Skin", "Indigestion or Difficulty Swallowing", "Night Sweats", "Persistent Low-grade Fever", "Neurological Changes"
        	            },
        	            {
        	            	"****  Benign Operation  ****",
        	            	"Cataract Surgery",
        	            	"Arthroscopic Knee Surgery",
        	            	"Hernia Repair",
        	            	"Hemorrhoidectomy",
        	            	"Cholecystectomy",
        	            	"Tonsillectomy and/or Adenoidectomy",
        	            	"Carpal Tunnel Release",
        	            	"Thyroidectomy (Partial)",
        	            	"Thyroidectomy (Total)",
        	            	"Myomectomy",
        	            	"Dilation and Curettage (D&C)",
        	            	"Appendectomy",
        	            	"Ganglion Cyst Excision",
        	            	"Skin Tag Removal",
        	            	"Lipoma Excision",
        	            	"Excision of Sebaceous Cyst",
        	            	"Dupuytren's Contracture Release",
        	            	"Trigger Finger Release",
        	            	"Bunion Surgery",
        	            	"Varicose Vein Stripping/Ablation",
        	            	"Benign Prostatic Hyperplasia (BPH) Surgery"
        	            },
        	            {   "**** Pulmonary disease  ****",
        	            	"Cough", "Shortness of Breath", "Wheezing", "Chest Pain", "Sputum Production", "Hemoptysis", "Fatigue", "Night Sweats", "Recurrent Respiratory Infections", "Swelling in the Legs or Feet", "Voice Changes", "Changes in Breathing Pattern", "Cyanosis", "Weight Loss"
        	            },
        	            
        	            {   "**** Cardiovascular disease  ****",
        	            	"Chest Pain", "Shortness of Breath", "Palpitations", "Fatigue", "Dizziness or Lightheadedness", "Syncope", "Swelling in the Legs, Ankles, or Feet", "Leg Pain or Cramping with Exercise", "Cyanosis", "Coldness or Numbness in Extremities", "Headache", "Changes in Skin Color", "Cough", "Difficulty Sleeping"
        	            },
        	            {   "**** CVA  ****",
        	            	"Headache", "Dizziness/Vertigo", "Seizures", "Weakness", "Numbness/Tingling", "Tremor", "Changes in Vision", "Speech Difficulties", "Memory Problems", "Changes in Gait/Balance", "Movement Disorders", "Pain", "Changes in Consciousness/Mental Status", "Sleep Disturbances", "Fainting/Syncope", "Muscle Spasms/Cramps"
        	            },
        	            // You can add more options arrays here if needed
        	        };        
    }
}
