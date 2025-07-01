package je.pense.doro.chartplate.keybutton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;

import je.pense.doro.fourgate.n_medications.emr_select_medication;
import je.pense.doro.fourgate.osteoporosis.EMR_DEXA;
import je.pense.doro.fourgate.osteoporosis.buttons.EMR_Os_buttons;
import je.pense.doro.samsara.EMR_OBJ_EKG.EMR_EKG_input;
import je.pense.doro.samsara.EMR_OBJ_Vitalsign.Vitalsign;
import je.pense.doro.samsara.EMR_OBJ_XrayGFS.EMR_ChestPA;
import je.pense.doro.samsara.EMR_OBJ_XrayGFS.EMR_endo_CFS;
import je.pense.doro.samsara.EMR_OBJ_XrayGFS.EMR_endo_GFS;
import je.pense.doro.samsara.EMR_OBJ_excute.EMRLabPositive;
import je.pense.doro.samsara.EMR_OBJ_excute.EMR_BMI_calculator;
import je.pense.doro.samsara.EMR_OBJ_excute.EMR_CBC;
import je.pense.doro.samsara.EMR_OBJ_excute.EMR_HbA1c;
import je.pense.doro.samsara.EMR_OBJ_excute.EMR_LDL;
import je.pense.doro.samsara.EMR_OBJ_excute.EMR_LFT;
import je.pense.doro.samsara.EMR_OBJ_excute.EMR_Lab_enterresult;
import je.pense.doro.samsara.EMR_OBJ_excute.EMR_LpaApoB;
import je.pense.doro.samsara.EMR_OBJ_excute.EMR_TFT;
import je.pense.doro.samsara.EMR_OBJ_excute.EMR_TFTout;
import je.pense.doro.samsara.EMR_OBJ_excute.EMR_eGFR;
import je.pense.doro.soap.fu.Followup;
import je.pense.doro.soap.pmh.EMRPMHAllergy;
import je.pense.doro.support.sqlite3_manager.lab.lablong.LabCodeMainScreen;

/**
 * Creates a toolbar with buttons for various EMR functionalities.
 */
public class EMR_top_buttons_obj extends JFrame implements ActionListener {
    private ArrayList<JButton> buttons = new ArrayList<>();
    private Map<String, Runnable> buttonActions = new HashMap<>();

    /**
     * Initializes the toolbar with buttons at the specified position and title.
     * @param position The position of the toolbar (e.g., "east").
     * @param title The title of the toolbar window.
     */
    public EMR_top_buttons_obj(String position, String title) {
        setUndecorated(true);
        setSize(new Dimension(1850, 30));
        setTitle(title);
        setLocation(1460, 0);
        setBackground(new Color(240, 240, 240));
        setLayout(new GridLayout(0, 20));

        initializeUI();
        createButtonActionsMap();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Initializes the UI by creating buttons for medical functionalities.
     */
    private void initializeUI() {
        String[] buttonNames = {
            "BMI", "BP", "HbA1c", "TFT", "TFTout",
            "LDL", "LFT", "CBC", "eGFR", "Lp(a)",
            "Etc.", "ChestPA", "EKG", "GFS", "CFS",
            "DEXA", "Lab", "Allergy","Followup", "Medication"
        };

        for (String buttonName : buttonNames) {
            JButton button = new JButton(buttonName);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.addActionListener(this);
            button.setBackground(new Color(135, 206, 235));
            buttons.add(button);
            add(button);
        }
    }

    /**
     * Maps button labels to their corresponding actions.
     */
    private void createButtonActionsMap() {
        buttonActions.put("BMI", () -> EMR_BMI_calculator.main(new String[0]));
        buttonActions.put("BP", () -> Vitalsign.main(new String[0]));
        buttonActions.put("HbA1c", () -> EMR_HbA1c.main(new String[0]));
        buttonActions.put("LDL", () -> EMR_LDL.main(new String[0]));
        buttonActions.put("LFT", () -> EMR_LFT.main(new String[0]));
        buttonActions.put("eGFR", () -> EMR_eGFR.main(new String[0]));
        buttonActions.put("Lp(a)", () -> EMR_LpaApoB.main(new String[0]));
        buttonActions.put("TFT", () -> EMR_TFT.main(new String[0]));
        buttonActions.put("TFTout", () -> EMR_TFTout.main(new String[0]));
        buttonActions.put("CBC", () -> EMR_CBC.main(new String[0]));
        buttonActions.put("Etc.", () -> {
            EMR_Lab_enterresult.main(new String[0]);
            EMRLabPositive.main(new String[0]);
        });
        buttonActions.put("GFS", () -> EMR_endo_GFS.main(new String[0]));
        buttonActions.put("CFS", () -> EMR_endo_CFS.main(new String[0]));
        buttonActions.put("ChestPA", () -> EMR_ChestPA.main(new String[0]));
        buttonActions.put("EKG", () -> EMR_EKG_input.main(new String[0]));
        buttonActions.put("DEXA", () -> {
            EMR_DEXA.main(new String[0]);
            EMR_Os_buttons.main(null);
        });
        buttonActions.put("Allergy", () -> EMRPMHAllergy.main(new String[0]));
        buttonActions.put("Lab", () -> LabCodeMainScreen.main(new String[0]));
        buttonActions.put("Followup", () -> Followup.main(new String[0]));
        buttonActions.put("Medication", () -> emr_select_medication.main(new String[0]));
    }

    /**
     * Handles button click events by executing the mapped action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonText = ((JButton) e.getSource()).getText();
        Runnable action = buttonActions.get(buttonText);
        if (action != null) {
            action.run();
        }
    }

    /**
     * Launches the toolbar.
     */
	public static void main(String text) {
        new EMR_top_buttons_obj("east", "EMR Object Buttons");		
	}
}