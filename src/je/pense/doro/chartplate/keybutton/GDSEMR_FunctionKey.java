package je.pense.doro.chartplate.keybutton;

import java.awt.event.KeyEvent;

import je.pense.doro.chartplate.mainpage.controller.Mainpage_controller;
import je.pense.doro.fourgate.A_editmain.EMR_FU_diabetesEdit;
import je.pense.doro.fourgate.A_editmain.EMR_FU_hypercholesterolemiaEdit;
import je.pense.doro.fourgate.A_editmain.EMR_FU_hypertensionEdit;
import je.pense.doro.fourgate.diabetes.EMR_dm_mainentry;
import je.pense.doro.fourgate.hypertension.EMR_htn_mainentry;
import je.pense.doro.fourgate.osteoporosis.EMR_DEXA;
import je.pense.doro.fourgate.osteoporosis.buttons.EMR_Os_buttons;
import je.pense.doro.fourgate.thyroid.entry.EMR_thyroid_mainentry;
import je.pense.doro.fourgate.thyroid.pregnancy.EMR_thyroid_Pregnancyentry;
import je.pense.doro.soap.plan.ittiaGDS_FUplan;

public class GDSEMR_FunctionKey {

    // Static method to handle function key action
    public static void handleFunctionKeyAction(int textAreaIndex, String functionKeyMessage, int keyCode) {
        // Print the function key action to the console
        System.out.println(functionKeyMessage);

        // Check if the key code corresponds to F1, F2, F3, or F4
        if (keyCode == KeyEvent.VK_F1) {
            // Set the specific text in the text area at index 8 if F1, F2, F3, or F4 is pressed
            EMR_FU_diabetesEdit.main(null);
            EMR_dm_mainentry.main(null);
        } else if (keyCode == KeyEvent.VK_F2) {
        	EMR_FU_hypertensionEdit.main(null);
        	EMR_htn_mainentry.main(null);
        } else if (keyCode == KeyEvent.VK_F3) {
        	EMR_FU_hypercholesterolemiaEdit.main(null);
        } else if (keyCode == KeyEvent.VK_F4) {
        	EMR_thyroid_mainentry.main(null);
        	EMR_thyroid_Pregnancyentry.main(null);
        } else if (keyCode == KeyEvent.VK_F5) {
        	EMR_DEXA.main(null);
        	EMR_Os_buttons.main(null);
        } else if (keyCode == KeyEvent.VK_F8) {
            Mainpage_controller.main(new String[]{"f8"}); 
               	        	
        } else if (keyCode == KeyEvent.VK_F9) {
            Mainpage_controller.main(new String[]{"f9"}); 
//            je.panse.doro.GDSEMR_frame.setTextAreaText(8, "\n...follow - up [  1  ] month later [ :cd ] \n...");
        } else if (keyCode == KeyEvent.VK_F10) {
            Mainpage_controller.main(new String[]{"f10"});
        } else if (keyCode == KeyEvent.VK_F11) {
            Mainpage_controller.main(new String[]{"f11"});
        } else if (keyCode == KeyEvent.VK_F12) {
            je.pense.doro.GDSEMR_frame.setTextAreaText(8 , "\n...followup without meds [ :cd ]\n...");    
        } else {
            // Set the text in the specified text area based on the passed index and message
            je.pense.doro.GDSEMR_frame.setTextAreaText(keyCode, functionKeyMessage);
        }
    }
}
