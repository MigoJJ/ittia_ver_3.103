package je.pense.doro.fourgate.A_editmain;

import java.io.File;		
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.mainpage.EMR_resettextAreas10;
import je.pense.doro.entry.EntryDir;

public class EMR_FU_uriEdit extends JFrame {
 
    public EMR_FU_uriEdit() {
    	EMR_resettextAreas10.main("/fourgate/uri");
    	}

     public static void main(String[] args) {
        new EMR_FU_uriEdit();
    }
}
