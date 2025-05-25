package je.pense.doro.chartplate.mainpage;

import java.io.IOException;	
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class GDSEMR_DocumentListner implements DocumentListener {
    private JTextArea[] textAreas;
    private JTextArea tempOutputArea;

    public GDSEMR_DocumentListner(JTextArea[] textAreas, JTextArea tempOutputArea) {
        this.textAreas = textAreas;
        this.tempOutputArea = tempOutputArea;
    }

    public void changedUpdate(DocumentEvent e) {
        try {
            updateOutputArea();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void removeUpdate(DocumentEvent e) {
        try {
            updateOutputArea();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void insertUpdate(DocumentEvent e) {
        try {
            updateOutputArea();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    public synchronized void updateOutputArea() throws IOException {
        // The rest of the code remains the same

        StringBuilder outputData = new StringBuilder();
        for (int i = 0; i < textAreas.length; i++) {
            if (textAreas[i] != null) {
                String text = textAreas[i].getText();
                if (text != null && !text.isEmpty()) {
                    try {
                        System.out.println("Processing text area " + i + ": " + text);
                        text = EMR_ChangeString.EMR_ChangeString(text);
                        System.out.println("After EMR_ChangeString: " + text);
                        text = EMR_organize_titles.EMR_organize_titles(text);
                        System.out.println("After EMR_organize_titles: " + text);
                    } catch (Exception e) {
                        System.err.println("Error processing text area " + i + ": " + e.getMessage());
                        e.printStackTrace();
                        continue; // Skip to the next text area
                    }
                    outputData.append("\n").append(text);
                }
            }
        }
        tempOutputArea.setText(outputData.toString());
        System.out.println("Final Output Data: " + outputData);

        try {
            EMR_Write_To_Chartplate.textAreaAppend(tempOutputArea);
        } catch (Exception e) {
            System.err.println("Error updating chart plate: " + e.getMessage());
            e.printStackTrace();
        }
    }


}