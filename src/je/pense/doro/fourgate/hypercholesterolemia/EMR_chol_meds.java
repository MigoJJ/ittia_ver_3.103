package je.pense.doro.fourgate.hypercholesterolemia;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EMR_chol_meds extends JFrame {

    public EMR_chol_meds() {
        setTitle("DM Medication Management");
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

        mainPanel.add(createFrameWithTable("Synthyroid", new String[]{
                "Livalo [ 1 ] mg 1 tab p.o. q.d.",
                "Livalo [ 2 ] mg 1 tab p.o. q.d.",
                "Livalo [ 3 ] mg 1 tab p.o. q.d.",
                "Livalo [ 4 ] mg 1 tab p.o. q.d.",

        }));

        mainPanel.add(createFrameWithTable("Synthyroxine", new String[]{
                "Vytorin [ 10/10 ]  mg  1 tab p.o. q.d.",
                "Vytorin [ 10/10 ]  mg  1 tab p.o. q.o.d.",
                "Vytorin [ 10/20 ]  mg  1 tab p.o. q.d.",
                "Vytorin [ 10/40 ]  mg  1 tab p.o. q.d.",
                
                "...",
                
        }));

        mainPanel.add(createFrameWithTable("Methimazole", new String[]{
                "Crestor [ 5 ] mg 1 tab p.o. q.d.",
                "Crestor [ 5 ] mg 1 tab p.o. q.o.d.",
                "Crestor [ 10 ] mg 1 tab p.o. q.d.",
                "Crestor [ 20 ] mg 1 tab p.o. q.d.",
                
        }));

        mainPanel.add(createFrameWithTable("Antiroid", new String[]{
                "Lantus Solosta  [     ] IU SC AM",
                "Ryzodeg FlexTouch [    ] IU SC AM",
                "Tresiba FlexTouch  [     ] IU SC AM",
                "Levemir FlexPen [     ] IU SC AM",
                "Tuojeo Solostar  [     ] IU SC AM",
        }));

        mainPanel.add(createFrameWithTable("Camen", new String[]{
                "Lipitor [ 10 ] mg 1 tab p.o. q.d.",
                "Lipitor [ 10 ] mg 1 tab p.o. q.o.d.",
                "Lipitor [ 20 ] mg 1 tab p.o. q.d.",
                "Lipitor [ 40 ] mg 1 tab p.o. q.d.",
                "Lipitor plus [ 10/10 ] mg 1 tab p.o. q.d.",
                
        }));
        
        mainPanel.add(createFrameWithTable("Followup", new String[]{
                "...Plan to FBS, HbA1c \n",
                "...Plan to FBS, HbA1c, +A/C \n",
                "...Obtain CUS : [ Carotid artery Ultrasonography ]\n",
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
//                    System.out.println("Current meds  :cd \n ..." + cellValue);
                    
                    try {
                        GDSEMR_frame.setTextAreaText(8, "\n...Current meds [ :cd ]\n ..." + cellValue);
                    } catch (Exception ex) {
//                        System.out.println("Error updating text area: " + ex.getMessage());
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
                new EMR_chol_meds().setVisible(true);
            }
        });
    }
}
