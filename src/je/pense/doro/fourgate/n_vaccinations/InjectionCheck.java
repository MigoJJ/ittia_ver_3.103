package je.pense.doro.fourgate.n_vaccinations;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;

public class InjectionCheck extends JFrame {
    private static final String[] VACCINES = {
        " ➢ Sanofi's VaxigripTetra® Vaccine(4가) [유독]",
        " ➢ Kovax Influ 4ga PF® vaccine [nip]",
        " ➢ Tdap (Tetanus, Diphtheria, Pertussis)",
        " ➢ Td (Tetanus, Diphtheria)",
        " ➢ Shingles Vaccine (Shingrix) #1/2",
        " ➢ HAV vaccination #1/2",
        " ➢ HBV vaccination #1/3",
        " ➢ Prevena 13 (pneumococcal vaccine (PCV13))",
        " ➢ Etc."
    };

    private static final String[] CONTROLS = {
        "All denied", "All other", "Delay", "Clear", "Save", "Quit"
    };

    private static final Object[][] REACTIONS = {
        {"Injection Site", "Pain"},
        {"Injection Site", "Redness"},
        {"Injection Site", "Tenderness or soreness"},
        {"Injection Site", "Swelling"},
        {"Injection Site", "Hardened lump"},
        {"Systemic", "Fever or chilling"},
        {"Systemic", "Headache"},
        {"Systemic", "Muscle Pain"},
        {"Systemic", "Fatigue or tiredness"},
        {"Systemic", "Joint pain"},
        {"Systemic", "Swollen lymph nodes"},
        {"Systemic", "Nausea"}
    };

    private final JTable reactionTable;
    private final JTextArea outputArea;

    public InjectionCheck() {
        super("Injection Check");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        outputArea = createOutputArea();
        reactionTable = createReactionTable();

        setupLayout();
        setVisible(true);
    }

    private JTextArea createOutputArea() {
        JTextArea area = new JTextArea(10, 20);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(new Color(135, 206, 235));
        return area;
    }

    private JTable createReactionTable() {
        JTable table = new JTable(REACTIONS, new String[]{"Reaction Type", "Reaction"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Timer to handle single-click delay
        javax.swing.Timer singleClickTimer = new javax.swing.Timer(200, e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                outputArea.append("\t[ ▹ ] " + table.getValueAt(row, 1) + "\n");
            }
        });
        singleClickTimer.setRepeats(false); // Ensure the timer only fires once

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            private boolean doubleClickFlag = false; // Flag to track double click

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    if (e.getClickCount() == 1) { // Single click
                        doubleClickFlag = false; // Reset flag for single click
                        // Start the timer for single-click action
                        singleClickTimer.restart(); // Reset and start the timer
                    } else if (e.getClickCount() == 2) { // Double click
                        // Set flag to true and cancel single-click action
                        doubleClickFlag = true;
                        singleClickTimer.stop();
                        // Execute double-click action
                        outputArea.append("\t[ ⏵ ] " + table.getValueAt(row, 1) + "\n");
                    }
                }
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                // Cancel the timer if double click detected
                if (doubleClickFlag) {
                    singleClickTimer.stop();
                }
            }
        });

        return table;
    }



    private void setupLayout() {
        setLayout(new BorderLayout());
        add(createVaccinePanel(), BorderLayout.EAST);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);
    }

    private JPanel createVaccinePanel() {
        JPanel panel = new JPanel(new GridLayout(VACCINES.length, 1, 5, 5));
        for (String vaccine : VACCINES) {
            JButton btn = new JButton(vaccine);
            btn.addActionListener(e -> outputArea.append(vaccine + " side effect [ ⏵ ]\n"));
            panel.add(btn);
        }
        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(reactionTable), BorderLayout.CENTER);
        panel.add(new JScrollPane(outputArea), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        for (String control : CONTROLS) {
            JButton btn = new JButton(control);
            btn.addActionListener(e -> handleControlButton(control));
            panel.add(btn);
        }
        return panel;
    }

    private void handleControlButton(String command) {
        switch (command) {
            case "All denied":
                appendAllDenied();
                break;
            case "All other":
                outputArea.append("     [ ✔ ] All other side effects of injections are denied.");
                break;
            case "Delay":
                outputArea.append("     [ ✔ ] Recommendations for delayed immunization.");
                break;
            case "Clear":
                outputArea.setText("");
                break;
            case "Save":
                GDSEMR_frame.setTextAreaText(1, "\n..." + outputArea.getText());
                break;
            case "Quit":
                dispose();
                break;
        }
    }

    private void appendAllDenied() {
        outputArea.append("     [ ✔ ] All side effects of injections are denied.\n");
        for (Object[] reaction : REACTIONS) {
            outputArea.append(String.format("     [-] %-15s : %s\n", 
                reaction[0], reaction[1]));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InjectionCheck::new);
    }
}
