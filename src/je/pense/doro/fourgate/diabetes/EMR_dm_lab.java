package je.pense.doro.fourgate.diabetes;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EMR_dm_lab extends JFrame {

    public EMR_dm_lab() {
        setTitle("DM F/U Laboratory Test");
        setSize(800, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center the frame on the monitor

        // South panel with buttons
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());

        JButton editButton = new JButton("Edit");
        JButton saveButton = new JButton("Save");
        JButton quitButton = new JButton("Quit");

        southPanel.add(editButton);
        southPanel.add(saveButton);
        southPanel.add(quitButton);

        add(southPanel, BorderLayout.SOUTH);

        // Main panel with grid layout 3x2
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 2));

        mainPanel.add(createFrameWithTable("F/U Laboratory Test", new String[]{
                "...FBS / PP2  HbA1c",
                "...FBS / PP2  HbA1c +A/C(eGFR, U-microalbumin)",
                "...FBS / PP2  HbA1c +A/C Electorlyte",
                "...FBS / PP2  HbA1c +A/C Electorlyte Insulin",
                "...FBS / PP2  HbA1c +A/C Electorlyte  Vitamin-D",
        }));

        mainPanel.add(createFrameWithTable("Synthyroxine", new String[]{
                "...EKG  CUS : Carotid artery US  CPA",
                "...EKG  CUS : Carotid artery US",
                "...EKG",
                "...CPA",
                "...Carotid artery US",
                "...",

        }));

        mainPanel.add(createFrameWithTable("Methimazole", new String[]{
                "Januvia [ 50 ] mg 1 tab p.o. q.d.",
                "Januvia [ 100 ] mg 1 tab p.o. q.d.",
                "Janumet [ 50/500 ] mg 1 tab p.o. q.d.",
                "Janumet [ 50/500 ] mg 1 tab p.o. b.i.d.",
        }));

        mainPanel.add(createFrameWithTable("Antiroid", new String[]{
                "Lantus Solosta  [     ] IU SC AM",
                "Ryzodeg FlexTouch [    ] IU SC AM",
                "Tresiba FlexTouch  [     ] IU SC AM",
                "Levemir FlexPen [     ] IU SC AM",
                "Tuojeo Solostar  [     ] IU SC AM",
                "---Rapid acting---",
                "NovoRapid FlexPen 100u/mL [     ] IU SC",
                "NOVOMIX 30 Flexpen 100U/mL  [     ] IU SC",
                "Apidra Inj. SoloStar [     ] IU SC ",
                "Fiasp Flex Touch  [    ] IU SC",
                "Humalog Mix 25 Quick Pen  [    ] IU SC",
                "Humalog Mix 50 Quick Pen  [    ] IU SC",
                "---Mixed---",
                "Soliqua Pen (10-40) [     ] IU SC ",
        }));

        mainPanel.add(createFrameWithTable("Camen", new String[]{
                "Diabex [ 250 ] mg 1 tab p.o. q.d.",
                "Diabex [ 500 ] mg 1 tab p.o. q.d.",
                "Diabex [ 250 ] mg 1 tab p.o. b.i.d.",
                "Diabex [ 500 ] mg 1 tab p.o. b.i.d.",
                "------",
                "Diamicron [ 30 ] mg 1 tab p.o. q.d.",
                "Diamicron [ 30 ] mg 1 tab p.o. b.i.d.",
                "Diamicron [ 60 ] mg 1 tab p.o. q.d.",
        }));
        
        mainPanel.add(createFrameWithTable("Followup", new String[]{
                "[ → ] advised the patient to continue with current medication\n",
                "[ ↘ ] decreased the dose of current medication\n",
                "[ ↗ ] increased the dose of current medication\n",
                "[ ⭯ ] changed the dose of current medication\n",
                " |→   Starting new medication\n",
                "  →|  discontinue current medication\n",
        }));

        add(mainPanel, BorderLayout.CENTER);

        // Add action listeners for buttons
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement edit functionality
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement save functionality
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private JScrollPane createFrameWithTable(String title, String[] data) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setLayout(new BorderLayout());

        String[] columnNames = {"Medication"};
        String[][] rowData = new String[data.length][1];
        for (int i = 0; i < data.length; i++) {
            rowData[i][0] = "   " + data[i]; // Add three spaces as a prefix
        }

        JTable table = new JTable(rowData, columnNames);
        table.setRowHeight(23); // Set row height to 23
        table.setFont(new Font("Consolas", Font.PLAIN, 12)); // Set font to Consolas
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    String cellValue = (String) target.getValueAt(row, column);
                    System.out.println("Plan to perform laboratory tests during the next visit on  :cd \n ..." + cellValue);
                    
                    try {
                        GDSEMR_frame.setTextAreaText(8, "\n...Plan to perform laboratory tests during the next visit on [ :cd ]\n ..." + cellValue);
                    } catch (Exception ex) {
                        System.out.println("Error updating text area: " + ex.getMessage());
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return scrollPane;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EMR_dm_lab().setVisible(true);
            }
        });
    }
}
