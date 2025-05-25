//package je.panse.doro.fourgate.routinecheck;
//
//import je.panse.doro.GDSEMR_frame;	
//import je.panse.doro.samsara.comm.datetime.Date_current;
//
//public class routinecheck {
//    
//    public static void GDSRC(String[] args) {
//    	String cdate = Date_current.main("d");
//            GDSEMR_frame.setTextAreaText(0, "The patient was seen at the GDS clinic \n\tfor a routine checkup on ." + cdate);
//            GDSEMR_frame.setTextAreaText(8, "\n  #  GDS clinic Routine check  [ " + cdate + " ]");
//    }
//    public static void HCRC(String[] args) {
//    	String cdate = Date_current.main("d");
//            GDSEMR_frame.setTextAreaText(0, "The patient was seen at the GDS clinic \n\tfor a 공단검진 on " + cdate);
//            GDSEMR_frame.setTextAreaText(8, "\n  #  공단검진 at GDS clinic  [ " + cdate + " ]");
//    }
//}
package je.pense.doro.fourgate.routinecheck;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.filecontrol.datetime.Date_current;

public class RoutineCheck {

    // Method to handle General Duty Surgery Routine Check
    public static void performGDSRoutineCheck() {
        String currentDate = getCurrentDate();
        setRoutineCheckText("The patient was seen at the GDS clinic \n\tfor a routine checkup on ", currentDate, "GDS clinic Routine check");
    }

    // Method to handle Health Center Routine Check
    public static void performHCRoutineCheck() {
        String currentDate = getCurrentDate();
        setRoutineCheckText("The patient was seen at the GDS clinic \n\tfor a 공단검진 on ", currentDate, "공단검진 at GDS clinic");
    }

    // Helper method to get current date in the desired format
    private static String getCurrentDate() {
        return Date_current.main("d");
    }

    // Helper method to set text in the GDSEMR_frame TextArea
    private static void setRoutineCheckText(String prefixText, String date, String suffixText) {
        GDSEMR_frame.setTextAreaText(0, prefixText + date);
        GDSEMR_frame.setTextAreaText(8, "\n  #  " + suffixText + "  [ " + date + " ]");
    }
}
