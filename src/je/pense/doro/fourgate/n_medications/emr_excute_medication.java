package je.pense.doro.fourgate.n_medications;

import je.pense.doro.fourgate.diabetes.EMR_dm_meds;
import je.pense.doro.fourgate.hypercholesterolemia.EMR_chol_meds;
import je.pense.doro.fourgate.hypertension.htnMedication.EMR_htn_meds;
import je.pense.doro.fourgate.osteoporosis.EMR_Osteoporosis_meds;
import je.pense.doro.fourgate.thyroid.medication.EMR_thyroid_meds;

public class emr_excute_medication {

    public static void main(String medicationName) {
        // Use a switch statement to determine which medication was selected
        switch (medicationName) {
        	case "DM":
        		executeMedication1();
            	break;
            case "Hypertension":
                executeMedication2();
                break;
            case "Dyslipidemia":
                executeMedication3();
                break;
        	case "Thyroid":
        		executeMedication4();
            	break;
            case "Osteoporosis":
                executeMedication5();
                break;
            case "Medication 6":
//                executeMedication6();
                break;
                // ... cases for other medications
            default:
                System.out.println("Unknown medication selected.");

        }
    }

    private static void executeMedication1() {
    	EMR_dm_meds.main(null);
    }

    private static void executeMedication2() {
    	EMR_htn_meds.main(null);
    }

    private static void executeMedication3() {
    	EMR_chol_meds.main(null);
    }
    
    private static void executeMedication4() {
    	EMR_thyroid_meds.main(null);
    }
    
    private static void executeMedication5() {
    	EMR_Osteoporosis_meds.main(null);
    }

    // ... methods for other medications
}