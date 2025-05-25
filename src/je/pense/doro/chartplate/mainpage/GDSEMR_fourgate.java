package je.pense.doro.chartplate.mainpage;

import java.io.IOException;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.keybutton.EMR_top_buttons_obj;
import je.pense.doro.soap.assessment.AssessmentSupport;
import je.pense.doro.soap.cc.CCSupport;
import je.pense.doro.soap.pe.EMR_PE_general;
import je.pense.doro.soap.plan.IttiaGDSPlan;
import je.pense.doro.soap.pmh.EMRPMH;
import je.pense.doro.soap.ros.EMR_ROS;
import je.pense.doro.soap.subjective.EMR_symptom_main;

public class GDSEMR_fourgate extends GDSEMR_frame {

	public GDSEMR_fourgate() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void main(String text) throws IOException {
//		System.out.println("Double-clicked on: text >>> " + text);
		if (text.contains("PMH>")) {
			EMRPMH.main(null);
		} else if (text.contains("CC>")) {
			CCSupport.main(null);
		} else if (text.contains("ROS>")) {
				EMR_ROS.main(null);
		} else if (text.contains("S>")) {
			EMR_symptom_main.main(null);
		} else if (text.contains("O>")) {
			EMR_top_buttons_obj.main(text);
		} else if (text.contains("A>")) {
			AssessmentSupport.main(null);
		} else if (text.contains("P>")) {
			IttiaGDSPlan.main(null);
		} else if (text.contains("Physical Exam>")) {
			EMR_PE_general.main(null);
		}
	}
}
