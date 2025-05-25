package je.pense.doro.soap.plan;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import je.pense.doro.GDSEMR_frame;

public class IttiaGDSPlan extends JFrame {

    public IttiaGDSPlan() {
        setTitle("My Frame");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel centerPanel = new JPanel(new GridLayout(1, 3));

        IttiaGDSPlanPanel_1 panel1 = new IttiaGDSPlanPanel_1(this);
        centerPanel.add(panel1);
        IttiaGDSPlanPanel_2 panel2 = new IttiaGDSPlanPanel_2(this);
        centerPanel.add(panel2);
        IttiaGDSPlanPanel_3 panel3 = new IttiaGDSPlanPanel_3(this);
        centerPanel.add(panel3);
        
        JButton clearButton = new JButton("Clear");
        JButton saveButton = new JButton("Save");
        JButton quitButton = new JButton("Quit");

        JPanel southPanel = new JPanel();
        southPanel.add(clearButton);
        southPanel.add(saveButton);
        southPanel.add(quitButton);

        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

	        clearButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	IttiaGDSPlanPanel_1.clearButton();
	            	IttiaGDSPlanPanel_2.clearButton();
                    GDSEMR_frame.setTextAreaText(8, "");

	            }
	        });
	        saveButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	IttiaGDSPlanPanel_1.saveButton();
	            	dispose();
	            }
	        });
	        quitButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	IttiaGDSPlanPanel_1.saveButton();
	            	dispose();
	            }
	        });
    	}

    public static void main(String[] args) {
        IttiaGDSPlan frame = new IttiaGDSPlan();
        frame.setVisible(true);
    }
}
