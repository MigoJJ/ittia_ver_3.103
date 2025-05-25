package je.pense.doro.soap.plan;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class IttiaGDSPlanPanel_3 extends JPanel implements ActionListener {
    private String[] plancombo = {"Option 1", "Option 2", "Option 3"};
    private JTextArea textArea;

    public IttiaGDSPlanPanel_3(IttiaGDSPlan frame) {
        setLayout(new GridLayout(28, 1));

        String[] checkboxLabels = ittiaGDSPlanPanel_3_String.getCheckboxLabels();
        for (String label : checkboxLabels) {
            JCheckBox checkBox = new JCheckBox(label);

            checkBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JCheckBox source = (JCheckBox) e.getSource();
                    String labelText = source.getText();
                    IttiaGDSPlanPanel_1.appendTextArea(labelText+"\n");
                }
            });
            add(checkBox);
        }
        
        // Create 5 combo boxes
        for (int i = 1; i <= 8; i++) {
            String[] comboboxes = ittiaGDSPlanPanel_3_String.getboxs(i);
            JComboBox<String> comboBox = new JComboBox<>(comboboxes);
            comboBox.addActionListener(this);
            add(comboBox);
        }

        textArea = new JTextArea();
        add(textArea);
        textArea.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
        textArea.setText("");
        String selectedItem = (String) comboBox.getSelectedItem();
//        System.out.println("WJ ~!!!" +selectedItem);
        textArea.append("..."+ selectedItem + "\n"); // Append selected item to the target JTextArea
        IttiaGDSPlanPanel_1.appendTextArea("\t" + selectedItem+"\n");
    }
}
