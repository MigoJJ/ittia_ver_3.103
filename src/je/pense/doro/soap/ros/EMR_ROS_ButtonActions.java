package je.pense.doro.soap.ros;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextArea;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.chartplate.filecontrol.datetime.Date_current;

public class EMR_ROS_ButtonActions  extends JFrame implements ActionListener {
    private JTable dataTable;
    private JTextArea outputArea;
	private String cdate = Date_current.defineTime("d");


    public EMR_ROS_ButtonActions(JTable dataTable, JTextArea outputArea) {
        this.dataTable = dataTable;
        this.outputArea = outputArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
        case "Clear":
        	outputArea.setText("");
           break;
        // handle other button actions here
        case "Save":
			ArrayList<String> selectList = EMR_ROS.selectList;
			ArrayList<String> allList = EMR_ROS.allList;
			String retuenedStr = EMR_ROS_ReplaceStringArray.main(allList, selectList);
			GDSEMR_frame.setTextAreaText(2, retuenedStr);
			GDSEMR_frame.setTextAreaText(9, "\n   [ ▶ ] " + selectList.toString() + "   " + cdate);
           outputArea.setText("");
			EMR_ROS.disposemain(null);
		    break;
        case "Quit":
        	System.out.println(" case quit:");
        	outputArea.setText("");
        	EMR_ROS.disposemain(null);
        	break;
        }
    }
}
