package je.pense.doro;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import je.pense.doro.chartplate.keybutton.EMR_top_buttons_obj;
import je.pense.doro.chartplate.keybutton.GDSEMR_ButtonNorthSouth;
import je.pense.doro.chartplate.keybutton.GDSEMR_FunctionKey;
import je.pense.doro.chartplate.mainpage.EMR_BlendColors;
import je.pense.doro.chartplate.mainpage.GDSEMR_DocumentListner;
import je.pense.doro.chartplate.mainpage.GDSEMR_fourgate;
import je.pense.doro.fourgate.n_vaccinations.InjectionApp;
import je.pense.doro.samsara.EMR_OBJ_Vitalsign.Vitalsign;
import je.pense.doro.samsara.EMR_OBJ_excute.EMR_BMI_calculator;
import je.pense.doro.samsara.EMR_OBJ_excute.EMR_HbA1c;
import je.pense.doro.samsara.EMR_OBJ_excute.EMR_TFT;
import je.pense.doro.soap.fu.FUplan;
import je.pense.doro.soap.fu.Followup;
import je.pense.doro.soap.subjective.EMR_symptom_main;

/**
 * Main application window for the GDS EMR interface.
 */
public class GDSEMR_frame {
    private static final int FRAME_WIDTH = 1275;
    private static final int FRAME_HEIGHT = 1020;
    public static JFrame frame;
    public static JTextArea[] textAreas;
    public static JTextArea tempOutputArea;
    public static final String[] TEXT_AREA_TITLES = {
            "CC>", "PI>", "ROS>", "PMH>", "S>",
            "O>", "Physical Exam>", "A>", "P>", "Comment>"
    };

    /**
     * Initializes the EMR frame.
     */
    public GDSEMR_frame() {
        frame = new JFrame("GDS EMR Interface for Physician");
        textAreas = new JTextArea[TEXT_AREA_TITLES.length];
        tempOutputArea = new JTextArea();
    }

    /**
     * Creates and displays the GUI.
     */
    public void createAndShowGUI() {
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLocation(348, 60);
        frame.setUndecorated(true);
        frame.add(createCenterPanel(), BorderLayout.CENTER);
        frame.add(createWestPanel(), BorderLayout.WEST);
        frame.add(new GDSEMR_ButtonNorthSouth("north"), BorderLayout.NORTH);
        frame.add(new GDSEMR_ButtonNorthSouth("south"), BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Creates the center panel containing the main EMR text areas.
     * @return JPanel The center panel.
     */
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(5, 2));
        centerPanel.setPreferredSize(new Dimension(900, 1000));

        for (int i = 0; i < textAreas.length; i++) {
            textAreas[i] = new JTextArea(TEXT_AREA_TITLES[i] + "\t");
            setupTextArea(textAreas[i], i);
            JScrollPane scrollPane = new JScrollPane(textAreas[i]);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            centerPanel.add(scrollPane);
        }
        return centerPanel;
    }

    /**
     * Sets up the properties and listeners for a single text area.
     * @param textArea The JTextArea to configure.
     * @param index The index of the text area.
     */
    private void setupTextArea(JTextArea textArea, int index) {
        textArea.setLineWrap(true);
        textArea.setCaretPosition(0);
        try {
            EMR_BlendColors.blendColors(textArea, tempOutputArea, index);
        } catch (Exception e) {
            System.err.println("Error applying colors: " + e.getMessage());
        }
        textArea.getDocument().addDocumentListener(new GDSEMR_DocumentListner(textAreas, tempOutputArea));
        textArea.addMouseListener(new DoubleClickMouseListener());
        textArea.addKeyListener(new FunctionKeyPress());
    }

    /**
     * Creates the west panel with a gradient background for output.
     * @return JPanel The west panel.
     */
    private JPanel createWestPanel() {
        JPanel westPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(240, 255, 240),
                        0, height, new Color(220, 245, 220)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
            }
        };
        westPanel.setPreferredSize(new Dimension(500, FRAME_HEIGHT));

        JScrollPane outputScrollPane = new JScrollPane(tempOutputArea);
        outputScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        outputScrollPane.setOpaque(false);
        outputScrollPane.getViewport().setOpaque(false);
        tempOutputArea.setOpaque(false);
        tempOutputArea.setBorder(null);
        westPanel.add(outputScrollPane, BorderLayout.CENTER);
        return westPanel;
    }

    /**
     * Updates the text of a specific text area.
     * @param index The index of the text area to update.
     * @param text The text to append.
     */
    public static void setTextAreaText(int index, String text) {
        if (textAreas == null || index < 0 || index >= textAreas.length || textAreas[index] == null) {
            System.err.println("Invalid text area index or text areas not initialized.");
            return;
        }
        textAreas[index].append(text);
    }

    /**
     * Updates the temporary output area.
     * @param text The text to set in the output area.
     */
    public static void updateTempOutputArea(String text) {
        if (tempOutputArea != null) {
            tempOutputArea.setText(text);
        }
    }

    /**
     * Handles function key press events.
     */
    private static class FunctionKeyPress extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode >= KeyEvent.VK_F1 && keyCode <= KeyEvent.VK_F12) {
                String functionKeyMessage = "F" + (keyCode - KeyEvent.VK_F1 + 1) + " key pressed - Action executed.";
                GDSEMR_FunctionKey.handleFunctionKeyAction(1, functionKeyMessage, keyCode);
            }
        }
    }

    /**
     * Handles double-click events on text areas.
     */
    private static class DoubleClickMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                JTextArea source = (JTextArea) e.getSource();
                String text = source.getText();
                try {
                    GDSEMR_fourgate.main(text);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Launches the application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GDSEMR_frame emrFrame = new GDSEMR_frame();
            try {
            	  EMR_top_buttons_obj.main(null);
                Vitalsign.main(args);
                EMR_HbA1c.main(null);
                EMR_symptom_main.main(null);
                EMR_BMI_calculator.main(null);
                EMR_TFT.main(null);
                InjectionApp.main(null);
                emrFrame.createAndShowGUI();
                Followup.main(null);
                FUplan.main(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}