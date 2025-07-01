package je.pense.doro.chartplate.keybutton;

import java.awt.Toolkit;		
import java.awt.datatransfer.StringSelection;

import javax.swing.JOptionPane;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.filecontrol.FileGeditToCilpboard;
import je.pense.doro.chartplate.filecontrol.database.Database_Control;
import je.pense.doro.chartplate.keybutton.EMR_Backup_Excute.EMR_B_CopyBackup;
import je.pense.doro.chartplate.keybutton.EMR_Backup_Excute.EMR_B_FileListFrame;
import je.pense.doro.chartplate.keybutton.EMR_Backup_Excute.EMR_InputFrame;
import je.pense.doro.chartplate.mainpage.EMR_Write_To_Chartplate;
import je.pense.doro.chartplate.mainpage.controller.Mainpage_controller;
import je.pense.doro.fourgate.A_editmain.EMR_FU_diabetesEdit;
import je.pense.doro.fourgate.A_editmain.EMR_FU_hypercholesterolemiaEdit;
import je.pense.doro.fourgate.A_editmain.EMR_FU_hypertensionEdit;
import je.pense.doro.fourgate.A_editmain.EMR_FU_mainEdit;
import je.pense.doro.fourgate.A_editmain.EMR_FU_uriEdit;
import je.pense.doro.fourgate.diabetes.EMR_dm_mainentry;
import je.pense.doro.fourgate.hypercholesterolemia.EMR_chol_mainentry;
import je.pense.doro.fourgate.hypertension.EMR_htn_mainentry;
import je.pense.doro.fourgate.n_vaccinations.InjectionApp;
import je.pense.doro.fourgate.osteoporosis.EMR_DEXA;
import je.pense.doro.fourgate.osteoporosis.buttons.EMR_Os_buttons;
import je.pense.doro.fourgate.routinecheck.RoutineCheck;
import je.pense.doro.fourgate.thyroid.entry.EMR_thyroid_mainentry;
import je.pense.doro.fourgate.thyroid.pregnancy.EMR_thyroid_Pregnancyentry;
import je.pense.doro.soap.pmh.EMRPMHAllergy;
import je.pense.doro.support.EMR_ittia_support;
import je.pense.doro.support.sqlite3_manager.abbreviation.MainScreen;
import je.pense.doro.support.sqlite3_manager.icd.icd11.ICD11Manager;
import je.pense.doro.support.sqlite3_manager.icd.icd8.KCDViewer;
import je.pense.doro.support.sqlite3_manager.lab.lablong.LabCodeMainScreen;
import je.pense.doro.support.sqlite3_manager.lab.labshort.JavalabtestsDBManagerGUI;


/**
 * Handles button actions for the EMR interface.
 */
public class EMR_ButtonEntry extends GDSEMR_frame {

    public EMR_ButtonEntry() {
        super();
    }

    /**
     * Processes button actions based on button name and location (north/south).
     * @param btn The button name.
     * @param location The panel location ("north" or "south").
     */
    public static void EMR_B_1entryentry(String btn, String location) {
        if ("north".equals(location)) {
            switch (btn) {
                case "Rescue":
                    EMR_Write_To_Chartplate.copyToClipboard(tempOutputArea);
                    FileGeditToCilpboard.FileGeditToCilpboard();
                    EMR_B_FileListFrame.main(null);
                    break;
                case "Backup":
                    EMR_InputFrame.main(null);
                    new EMR_B_CopyBackup().saveTextToFile(tempOutputArea.getText());
                    EMR_B_FileListFrame.main(null);
                    break;
                case "Copy":
                    try {
                        Toolkit.getDefaultToolkit().getSystemClipboard()
                               .setContents(new StringSelection(tempOutputArea.getText()), null);
                        JOptionPane.showMessageDialog(null, "Text copied!");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Copy failed: " + e.getMessage());
                    }
                    break;
                case "CE":
                    for (int i = 1; i <= 7; i++) {
                        textAreas[i].setText(TEXT_AREA_TITLES[i] + "\t");
                    }
                    break;
                case "Clear":
                    Mainpage_controller.clearAllTextAreas();
                    break;
                case "Exit":
                    System.exit(0);
                    break;
                case "Abbreviation":
                    MainScreen.main(null);
                    break;
                case "ICD-11":
                    ICD11Manager.main(null);
                    break;
                case "KCD8":
                	KCDViewer.main(null);
                    break;
                case "Lab code":
                    LabCodeMainScreen.main(null);
                    break;
                case "Lab sum":
                		JavalabtestsDBManagerGUI.main(null);
                    break;
                case "db":
                		Database_Control.main(null);
                break;
                case "ittia_support":
                    EMR_ittia_support.main(null);
                    break;

            }
        } else if ("south".equals(location)) {
            switch (btn) {
                case "F/U DM":
                    EMR_FU_diabetesEdit.main(null);
                    EMR_dm_mainentry.main(null);
                    break;
                case "F/U HTN":
                    EMR_FU_hypertensionEdit.main(null);
                    EMR_htn_mainentry.main(null);
                    break;
                case "F/U Chol":
                    EMR_FU_hypercholesterolemiaEdit.main(null);
                    EMR_chol_mainentry.main(null);
                    break;
                case "F/U Thyroid":
                    EMR_thyroid_mainentry.main(null);
                    EMR_thyroid_Pregnancyentry.main(null);
                    break;
                case "Osteoporosis":
                    EMR_Os_buttons.main(null);
                    EMR_DEXA.main(null);
                    break;
                case "URI":
                    EMR_FU_uriEdit.main(null);
                    break;
                case "Allergy":
                    EMRPMHAllergy.main(null);
                    break;
                case "Injections":
                    InjectionApp.main(null);
                    break;
                case "GDS RC":
                    RoutineCheck.performGDSRoutineCheck();
                    break;
                case "공단검진":
                    RoutineCheck.performHCRoutineCheck();
                    break;
                case "F/U Edit":
                    EMR_FU_mainEdit.main(null);
                    break;
            }
        }
    }
}