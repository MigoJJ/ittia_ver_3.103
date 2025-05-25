package je.pense.doro.fourgate.A_editmain;

import java.io.File;		
import java.io.FileNotFoundException;
import java.util.Random;	
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.mainpage.EMR_resettextAreas10;
import je.pense.doro.entry.EntryDir;

public class EMR_FU_hypercholesterolemiaEdit extends JFrame {
    private static final int NUM_TEXT_AREAS = 10;

    public EMR_FU_hypercholesterolemiaEdit() {
        
        	EMR_resettextAreas10.main("/fourgate/hypercholesterolemia/cholGeneral");
        
    }
    public static void main(String[] args) {
        new EMR_FU_hypercholesterolemiaEdit(); 
    }
}

