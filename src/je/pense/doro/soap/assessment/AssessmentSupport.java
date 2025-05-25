package je.pense.doro.soap.assessment;

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

public class AssessmentSupport {
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 600;
    
    private JTextArea textArea;
    private JPanel centerPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AssessmentSupport::new);
    }

    public AssessmentSupport() {
        JFrame frame = createMainFrame();
        frame.add(createTextArea(), BorderLayout.NORTH);
        frame.add(createComboBoxPanel(), BorderLayout.CENTER);
        frame.add(createButtonPanel(), BorderLayout.SOUTH);
        frame.add(createCheckBoxPanel(), BorderLayout.EAST); // Add the checkbox panel to the EAST
        frame.setVisible(true);
    }

    private JFrame createMainFrame() {
        JFrame frame = new JFrame("Assessment Support");
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
                    textArea.append("   #  " + e.getItem().toString() + "\n");
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
        GDSEMR_frame.setTextAreaText(7, "\n");
        GDSEMR_frame.setTextAreaText(7, assess);
    }

    private void quitAction(ActionEvent e) {
        JFrame frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, (Component) e.getSource());
        frame.dispose();
    }

    private JPanel createCheckBoxPanel() {
        JPanel eastPanel = new JPanel(new GridLayout(0, 1)); // Use GridLayout with 0 rows to make it dynamic

        // Create checkboxes and add them to the panel
        String[] checkboxLabels = {
            "Diabetes Mellitus", "HTN", "Dyslipidemia",
            "Cancer", "Operation", "Thyroid Disease",
            "Asthma", "Tuberculosis", "Pneumonia",
            "Chronic/Acute Hepatitis", "GERD", "Gout",
            "Arthritis", "Hearing Loss", "CVA",
            "Depression", "Cognitive Disorder", "Allergy",
            "Food", "Injection", "Medication"
        };

        for (String label : checkboxLabels) {
            JCheckBox checkBox = new JCheckBox(label);
            checkBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    // Append the checkbox label to the textArea
                    textArea.append("   #  " + label + "\n");
                }
            });
            eastPanel.add(checkBox);
        }

        return eastPanel;
    }

    private String[][] getComboBoxOptions() {
        return new String[][]  {
        			 {       "****  DM  ****",
							 "diabetes mellitus",
							 "Prediabetes",
							 "Gestational Diabetes Mellitus",
							 "Diabetes mellitus F/U",
							 
							 "DM without complications",
							 "DM with Retinopathy",
							 "DM with Retinopathy : Non-proliferative diabetic retinopathy",
							 "DM with Retinopathy : Proliferative diabetic retinopathy",
							 "DM with Retinopathy : Macular edema",
							 "DM with Nephropathy",
							 "DM with Nephropathy with microalbuminuria",
							 "DM with Nephropathy with CRF",
							 "DM with Peripheral Neuropathy",
							 "DM with Autonomic Neuropathy",
        	            },
        	            {   "****  Thyroid  ****",
        	            	"Hyperthyroidism",
        	            	"Hyperthyroidism : Graves' disease",
        	            	"Hyperthyroidism F/U",
        	            	"Hyperthyroidism with Pregnancy [ ] weeks F/U",
        	            	"Hypothyroidism",
        	            	"Hypothyroidism : Hashimoto's thyroiditis",
        	            	"Hypothyroidism F/U",
        	            	"Hypothyroidism with Pregnancy [ ] weeks F/U",
        	            	
        	            	"Thyroid nodule",
        	            	"Thyroid cyst",
        	            	"Simple Goiter",
        	            	"Subacute Thyroiditis",
        	            	"c/w Chronic Thyroiditis on USG",
        	            	"Non-Thyroidal Illness F/U",
        	            	
        	            	"Papillary Thyroid Cancer OP(+) with tHypothyroidism",
        	            	"Papillary Thyroid Cancer OP(+) RAI Tx(+) with hypothyroidism",
        	            },
        	            {
        	            	"****  GI  ****",
        	            	"GERD",
        	            	"Chronic Atrophic Gastritis",
        	            	"Chronic Superficial Gastritis",
        	            	"r/o Erosive Gastritis",
        	            	"r/o Irritable Bowel Syndrome",
        	            	"Severe Constipation",
        	            	"s/p Gastric Polyp   GFS : ",
        	            	"s/p Duodenal Polyp   GFS : ",
        	            	"s/p Duodenal Ulcer   GFS : ",
        	            	"s/p CLO[ + ]   GFS : ",
        	            	"s/p Colonic Polyp  CFS : ",
        	            	"s/p Colonic diverticulum   CFS : ",
        	            },
        	            {
        	            	"****  Liver  ****",
        	               	"Gilbert's syndrome",
        	            	"s/p Hepatitis A infection",
        	            	"HBsAg(+) Carrier",
        	            	"Hepatitis C virus (HCV) chronic infection",
        	            	"HCV-Ab(Positive) --> PCR(Negative) confirmed",
        	            	"Hepatic Hemangioma",
        	            	"Hepatic Cyst",
        	            	"Hepatic Nodule",
        	            	"Hepatic higher echoic nodule",
        	            	"Fatty Liver",
        	            	"Mild Fatty Liver",
        	            	"Moderate Fatty Liver",
        	            	"Severe Fatty Liver",
        	            	"GB polyp",
        	            	"GB stone" , 
        	    		},

        	            {
        	            	"****  Cancer  ****",
        	            	"Stomach Cancer (Gastric Cancer)",
        	            	"Liver Cancer",
        	            	"Lung Cancer",
        	            	"Colorectal Cancer",
        	            	"Prostate Cancer",
        	            	"Thyroid Cancer",
        	            	"    s/p Papillary Thyroid Cancer OP(+) with tHypothyroidism",
        	            	"    s/p Papillary Thyroid Cancer OP(+) RAI Tx(+) with hypothyroidism",
        	            	"Breast Cancer",
        	            	"    s/p Breast Cancer Operation",
        	            	"    s/p Breast Cancer Operation+ ChemoTx(+)",        
        	            	"    s/p Breast Cancer Operation + RT(+)",        
        	            	"    s/p Breast Cancer Operation + ChemoTx(+) + RT(+)", 
        	            	"Esophageal Cancer",
        	            	"Pancreatic Cancer",
        	            	"Bladder Cancer",
        	            	"Cervical Cancer",
        	            	"Ovarian Cancer",
        	            	"Non-Hodgkin Lymphoma",
        	            	"Leukemia",
        	            	"Oral Cancer",
        	            },
        	            {
        	            	"****  Benign Operation  ****",
        	            	"Laparoscopic cholecystectomy (d/t GB stone)",
        	            	"Endoscopic mucosal resection (EMR) for colorectal polyps",
        	            	"Laparoscopic appendectomy",
        	            	"Laparoscopic myomectomy ",
        	            	"Laparoscopic hysterectomy for benign gynecological conditions",
        	            	"    s/p TAH with BSO", 
        	            	"Cataract surgery",
        	            	"Tonsillectomy",
        	            	"Thyroidectomy (thyroid nodule)",
        	            	"Adenoidectomy (d/t PTH)",
        	            	"Carpal tunnel release surgery",
        	            	"Trigger finger release surgery",
        	            	"Hernia repair",
        	            	"Hemorrhoidectomy ",
        	            	"Mastectomy for benign breast disease",
        	            	"Septoplasty (nasal septum correction surgery)",
        	            },
        	            {   "**** Pulmonary disease  ****",
        	            	"Chronic obstructive pulmonary disease (COPD)",
        	            	"Asthma",
        	            	"    s/p Asthma currently no significant probelm",
        	            	"Bronchitis",
        	            	"Pneumonia",
        	            	"Bronchiectasis",
        	            	"s/p  Tuberculosis , pulmnary",
        	            	"s/p  Pneumothorax",
        	            	"s/p  Pleural effusion",
        	            	"Interstitial lung disease",
        	            	"Sleep apnea",
        	            	"Pulmonary embolism",
        	            	"Cystic fibrosis",
        	            	"Occupational lung diseases",
        	            	"Lung infections",
        	            },
        	            
        	            {   "**** Cardiovascular disease  ****",
        	            	"Hypertension ",
        	            	"Aortic aneurysm",
        	            	"Deep vein thrombosis (DVT) and pulmonary embolism (PE)",
        	            	"    s/p Deep vein thrombosis (DVT)",
        	            	"Coronary artery disease (CAD)",
        	            	"   s/p Angina Pectoris",
        	            	"   s/p Myocardiac Infaction",
        	            	"   s/p Coronary Spasm",
        	            	"Arrhythmia",
        	            	"    s/p Atrial fibrillation (AFib)",
        	            	"    s/p Atrial flutter",
        	            	"    s/p Supraventricular tachycardia (SVT)",
        	            	"    s/p Ventricular fibrillation (VFib)",
        	            	"    s/p Sinus bradycardia",
        	            	"    s/p Sinus tachycardia",
        	            	"    s/p Sick sinus syndrome",
        	            	"    s/p Long QT syndrome",
        	            	"    s/p Brugada syndrome",
        	            	"    s/p Wolff-Parkinson-White syndrome",
        	            	"    s/p Catecholaminergic polymorphic ventricular tachycardia (CPVT)",
        	            	"Heart failure",
        	            	"Stroke",
        	            	"Valvular heart disease",
        	            	"Peripheral artery disease (PAD)",
        	            	"Cardiomyopathy",

        	            	"Congenital heart disease",
        	            	"Endocarditis (inflammation of the inner lining of the heart)",
        	            	"Pericarditis (inflammation of the lining around the heart)",
        	            	"Myocarditis (inflammation of the heart muscle)",
        	            	"Cardiovascular disease due to diabetes",
        	            },
        	            {   "**** CVA  ****",
        	            	"s/p Stroke  CVA",
        	            	"Dementia (Cognitive Disorder)",
        	            	"Alzheimer's disease",
        	            	"Parkinson's disease",
        	            	"Cerebral palsy",
        	            	".",
        	            	"Migraine",
        	            	"Brain tumor",
        	            	"    s/p Brain tumor : mningioma",
        	            	"Hydrocephalus",
        	            	"Traumatic brain injury",
        	            	".",
        	            	"Epilepsy",
        	            	"Multiple sclerosis",
        	            	"Meningitis",
        	            	"Encephalitis",
        	            	"Hydrocephalus",
        	            	"Huntington's disease",
        	            	"Amyotrophic lateral sclerosis (ALS)",
        	            },
        	            // You can add more options arrays here if needed
        	        };        
    }
}
