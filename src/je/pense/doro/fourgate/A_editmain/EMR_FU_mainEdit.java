package je.pense.doro.fourgate.A_editmain;

import javax.swing.JFrame;

import je.pense.doro.fourgate.diabetes.dmGeneral.EMR_FU_diabetes;
import je.pense.doro.fourgate.hypercholesterolemia.cholGeneral.EMR_FU_hypercholesterolemia;
import je.pense.doro.fourgate.hypertension.htnGeneral.EMR_FU_hypertension;
import je.pense.doro.fourgate.uri.EMR_FU_uri;

public class EMR_FU_mainEdit extends JFrame {

	public EMR_FU_mainEdit() {
		EMR_FU_uri.main(null);
		EMR_FU_hypercholesterolemia.main(null);
		EMR_FU_hypertension.main(null);
		EMR_FU_diabetes.main(null);
	}
	
	public static void main(String[] args) {
	    new EMR_FU_mainEdit();
	}
}