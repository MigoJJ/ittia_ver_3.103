package je.pense.doro.chartplate.mainpage.controller;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.soap.fu.Followup;

/**
 * Controller class for managing the main page of the chart plate application.
 * Extends GDSEMR_frame to inherit its UI components and functionality.
 */
public class Mainpage_controller extends GDSEMR_frame {

    /**
     * Constructor that initializes the main page controller.
     */
    public Mainpage_controller() {
        super(); // Initialize GDSEMR_frame
    }

    /**
     * Saves content to a specific text area identified by index.
     * @param index The index of the text area to update
     * @param content The content to set in the text area
     */
    public static void saveTextArea(int index, String content) {
        if (textAreas == null || index < 0 || index >= textAreas.length || textAreas[index] == null) {
            JOptionPane.showMessageDialog(frame, "Invalid text area index: " + index, 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        setTextAreaText(index, content);
    }

    /**
     * Clears all text areas and reinitializes them with their titles.
     */
    public static void clearAllTextAreas() {
        if (textAreas == null) {
            JOptionPane.showMessageDialog(frame, "Text areas not initialized", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < textAreas.length; i++) {
            if (textAreas[i] != null) {
                textAreas[i].setText(TEXT_AREA_TITLES[i] + "\t");
            }
        }
    }

    /**
     * Clears and initializes the plan section with a follow-up message.
     * @param monthno Number of months for follow-up period (must be positive)
     */
    public static void clearPlanSection(int monthno) {
        if (monthno <= 0) {
            JOptionPane.showMessageDialog(frame, "Invalid follow-up period: " + monthno, 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int planIndex = -1;
        for (int i = 0; i < TEXT_AREA_TITLES.length; i++) {
            if ("P>".equals(TEXT_AREA_TITLES[i])) {
                planIndex = i;
                break;
            }
        }
        if (textAreas == null || planIndex == -1 || textAreas[planIndex] == null) {
            JOptionPane.showMessageDialog(frame, "Plan section not found or not initialized", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        textAreas[planIndex].setText("P>\n...follow - up [  " + monthno + "  ] month later[ :cd ]\n");
        Followup.main(null);
    }

    /**
     * Main entry point of the application.
     * Handles command-line arguments or initializes the GUI if none provided.
     * @param args Command-line arguments ("f8" for clear all, "f9"/"f10"/"f11" for plan section)
     */
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            SwingUtilities.invokeLater(() -> {
                Mainpage_controller controller = new Mainpage_controller();
                try {
                    controller.createAndShowGUI();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error initializing GUI: " + e.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            return;
        }

        switch (args[0].toLowerCase()) {
            case "f8":
                SwingUtilities.invokeLater(Mainpage_controller::clearAllTextAreas);
                break;
            case "f9":
                SwingUtilities.invokeLater(() -> clearPlanSection(1));
                break;
            case "f10":
                SwingUtilities.invokeLater(() -> clearPlanSection(2));
                break;
            case "f11":
                SwingUtilities.invokeLater(() -> clearPlanSection(6));
                break;
            default:
                JOptionPane.showMessageDialog(null, "Unrecognized argument: " + args[0], 
                    "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }
}