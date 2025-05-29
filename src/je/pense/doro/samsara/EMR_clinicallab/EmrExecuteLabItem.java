package je.pense.doro.samsara.EMR_clinicallab;

import je.pense.doro.fourgate.osteoporosis.EMR_DEXA;
import je.pense.doro.samsara.EMR_OBJ_EKG.EMR_EKG_input;
import je.pense.doro.samsara.EMR_OBJ_Vitalsign.Vitalsign;
import je.pense.doro.samsara.EMR_OBJ_XrayGFS.*;
import je.pense.doro.samsara.EMR_OBJ_excute.*;

public class EmrExecuteLabItem {

    public static void main(String labItemName) {
        switch (labItemName) {
            case "BMI"      -> EMR_BMI_calculator.main(null);
            case "BP"       -> Vitalsign.main(null);
            case "HbA1c"    -> EMR_HbA1c.main(null);
            case "TFT"      -> EMR_TFT.main(null);
            case "TFTout"   -> EMR_TFTout.main(null);
            case "LDL"      -> EMR_LDL.main(null);
            case "LFT"      -> EMR_LFT.main(null);
            case "CBC"      -> EMR_CBC.main(null);
            case "eGFR"     -> EMR_eGFR.main(null);
            case "Lp(a)"    -> EMR_LpaApoB.main(null);
            case "Etc."     -> { EMRLabPositive.main(null); EMR_Lab_enterresult.main(null); }
            case "ChestPA"  -> EMR_ChestPA.main(null);
            case "EKG"      -> EMR_EKG_input.main(null);
            case "GFS"      -> EMR_endo_GFS.main(null);
            case "CFS"      -> EMR_endo_CFS.main(null);
            case "DEXA"     -> EMR_DEXA.main(null);
            default         -> System.out.println("Unknown lab item selected.");
        }
    }
}
