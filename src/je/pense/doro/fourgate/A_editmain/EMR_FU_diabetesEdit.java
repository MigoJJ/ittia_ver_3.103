package je.pense.doro.fourgate.A_editmain;

import javax.swing.JFrame;

import je.pense.doro.chartplate.mainpage.EMR_resettextAreas10;

public class EMR_FU_diabetesEdit extends JFrame {

    public EMR_FU_diabetesEdit() {
    	EMR_resettextAreas10.main("/fourgate/diabetes/dmGeneral");
    	}

    public static void main(String[] args) {
        new EMR_FU_diabetesEdit(); 
    }
}