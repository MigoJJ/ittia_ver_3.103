package je.pense.doro.soap.plan;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class IttiaGDSPlanPanel_2 extends JPanel implements ActionListener {
    private static String label;
	private static JTextArea textArea;

    public IttiaGDSPlanPanel_2(IttiaGDSPlan frame) {

        // Other code
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        for (int i = 1; i <= 10; i++) {
        	
        	ittiaGDSPlanPanel_2_String myClass = ittiaGDSPlanPanel_2_String.myMethod(i);
        	String retA = myClass.getRetA();
        	String[] retB = myClass.getRetB();
        	
            JLabel label = new JLabel(retA);
            add(label);
            label.setAlignmentX(Component.LEFT_ALIGNMENT); // Align label to the left

            JComboBox<String> comboBox = new JComboBox<>(myClass.getRetB()); // create combo box with retB values
            comboBox.addActionListener(this); // Add action listener to the combo box
            add(comboBox);
        }
        textArea = new JTextArea();
        add(textArea);
        textArea.setVisible(false);
    }

	@Override
    public void actionPerformed(ActionEvent e) {
        JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
         
        String selectedItem = (String) comboBox.getSelectedItem();
        textArea.setText("");
        textArea.append("\n..."+ selectedItem + "\n"); // Append selected item to the target JTextArea
        IttiaGDSPlanPanel_1.appendTextArea(textArea.getText());
    }
	public static void clearButton() {
        textArea.setText("");	
    }
}
