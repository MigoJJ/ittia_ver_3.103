package je.pense.doro.soap.plan;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;

public class IttiaGDSPlanPanel_1 extends JPanel {

    private static JTextField textField1;
    private static JTextField textField2;
    private static JTextArea textArea;

    public IttiaGDSPlanPanel_1(IttiaGDSPlan frame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("< Follow up interval > "));
        textField1 = new JTextField();
        textField1.setPreferredSize(new Dimension(20, 30));
        textField1.setHorizontalAlignment(JTextField.CENTER);
        textField1.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                updateTextArea();
            }
            public void removeUpdate(DocumentEvent e) {
                updateTextArea();
            }
            public void insertUpdate(DocumentEvent e) {
//    			String followUpInterval = textField1.getText();
//			    String text = "[" + followUpInterval + "] months later\n";
//   				textArea.setText(text);
                updateTextArea();
            }
        });
        add(textField1);
        add(new JLabel("< Change Prescription >"));
        textField2 = new JTextField();
        textField2.setPreferredSize(new Dimension(20, 30));
        textField2.setHorizontalAlignment(JTextField.CENTER);
        textField2.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                updateTextArea();
            }
            public void removeUpdate(DocumentEvent e) {
                updateTextArea();
            }
            public void insertUpdate(DocumentEvent e) {
                updateTextArea();
            }
        });
        add(textField2);
        
		textField1.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		            textField2.requestFocusInWindow();
		            textField2.setCaretPosition(0);
		        }
		    }
		});

		textField2.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e) {
		        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//		            textArea.append(textField1.getText() + " " + textField2.getText() + "\n");
		            textField1.requestFocusInWindow();
		            textField1.setCaretPosition(0);
		        }
		    }
		});

        add(new JLabel("Output"));
        textArea = new JTextArea();
        textArea.setPreferredSize(new Dimension(20, 500));
        add(textArea);
    }

    private void updateTextArea() {
    	String rtfield2 = returnchangefield2(textField2.getText());
		textArea.setText("\nFollow up interval: [ " + textField1.getText() + " ] months\n"
                + "Prescription :" + rtfield2 + "\n");
    }

private String returnchangefield2(String meds) {
		String[] codes = {"5", "55", "6", "8", "2", "4", "0", "1"};
       String[] messages = {
            " |→  starting new medicine to treat ",
            " →|  discontinue current medication",
            " [ → ] advised the patient to continue with current medication",
            " [ ↗ ] increased the dose of current medication",
            " [ ↘ ] decreased the dose of current medicatio",
            " [ ⭯ ] changed the dose of current medication",
            " Observation & Follow-up without medication",
            " With conservative treatment"
        };

        for (int i = 0; i < codes.length; i++) {
            if (meds.equals(codes[i])) {
                return messages[i];
            }
        }
        return "";
    }

	public static void appendTextArea(String text) {
    	textArea.append(text);    	
	}
	
	public static void clearButton() {
        textField1.setText("");
        textField2.setText("");
        textArea.setText("");	

	}
	public static void saveButton() {
	    String text = textArea.getText();
	    GDSEMR_frame.setTextAreaText(8, text);

	}
}
