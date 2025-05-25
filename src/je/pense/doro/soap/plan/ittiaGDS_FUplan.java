package je.pense.doro.soap.plan;

import java.awt.BorderLayout;	
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import je.pense.doro.chartplate.mainpage.controller.Mainpage_controller;

public class ittiaGDS_FUplan extends JFrame {
    private JTextArea viewTextArea;
    private JTable medicalTable;
    private JButton saveButton, exitButton, clearButton;

    private final String[] columnNames = {"Category", "Option"};
    private final Object[][] tableData = {
        {"Medication", "...|→ starting new medicine to treat"},
        {"Medication", "...→| discontinue current medication"},
        {"Medication", "...[→] advised the patient to continue with current medication"},
        {"Medication", "...[↗] increased the dose of current medication"},
        {"Medication", "...[↘] decreased the dose of current medication"},
        {"Medication", "...[⭯] changed the dose of current medication"},
        {"Medication", "...Observation & Follow-up without medication"},
        {"Medication", "...With conservative treatment"},
        {"Medication", "----------------------------------------"},
        {"FU plan", "...Next Lab F/U with NPO"},
        {"FU plan", "...Conservative symptomatic treatment"},
        {"FU plan", "...F/U without medications"},
        {"FU plan", "...Continue current medications or supplements"},
        {"FU plan", "...D/C all kinds of dietary[food, health, nutritional] supplements"},
        {"FU plan", "...The patient Refused dose-adjustment"},
        {"FU plan", "--------------------------------------------"},
        {"Check and Consult", "...Ophthalmologist consultation[+]"},
        {"Check and Consult", "...Plan to review of other clinic RC result"},
        {"Check and Consult", "...Plan of surgeries"},
        {"Check and Consult", "...Check for Family medical history"},
        {"Check and Consult", "...to refer patients to receive additional health care services"},
        {"Check and Consult", "...Gastroenterology consult in GDS clinic"},
        {"Check and Consult", "...Pulmonology consult in GDS clinic"}
    };

    public ittiaGDS_FUplan() {
        super("Medical Chart Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        initializeComponents();

        setLayout(new BorderLayout(10, 10));
        add(new JScrollPane(viewTextArea), BorderLayout.WEST);
        add(new JScrollPane(medicalTable), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        setupAutoClose();
        setVisible(true);
    }

    private void initializeComponents() {
        // West Panel: TextArea with 20% increased width (20 columns -> 24 columns)
        viewTextArea = new JTextArea(20, 24); // Original 20 + 20% (4) = 24
        viewTextArea.setEditable(false);
        viewTextArea.setLineWrap(true);
        viewTextArea.setWrapStyleWord(true);
        viewTextArea.setBorder(BorderFactory.createTitledBorder("View Contents"));

        // Center Panel: JTable with adjusted column widths
        DefaultTableModel tableModel = new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        medicalTable = new JTable(tableModel);
        medicalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        medicalTable.getTableHeader().setReorderingAllowed(false);
      
        // Increase row height by 15%
        int defaultRowHeight = medicalTable.getRowHeight(); // Default is typically 16-20 pixels
        int newRowHeight = (int) (defaultRowHeight * 1.33); // Increase by 15%
        medicalTable.setRowHeight(newRowHeight);
        
        // Adjust column widths: Decrease "Category" by 20%
        TableColumn categoryColumn = medicalTable.getColumnModel().getColumn(0);
        int defaultWidth = categoryColumn.getPreferredWidth(); // Default is typically 75 pixels
        categoryColumn.setPreferredWidth((int) (defaultWidth * 0.1)); // 20% decrease
        TableColumn optionColumn = medicalTable.getColumnModel().getColumn(1);
        optionColumn.setPreferredWidth((int) (defaultWidth * 1.9)); // Let "Option" expand

        medicalTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = medicalTable.getSelectedRow();
                    if (row >= 0) {
                        String option = (String) medicalTable.getValueAt(row, 1);
                        viewTextArea.append(option + "\n");
                    }
                }
            }
        });
    }

    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveAction());
        exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> dispose());
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> viewTextArea.setText(""));
        southPanel.add(saveButton);
        southPanel.add(exitButton);
        southPanel.add(clearButton);
        return southPanel;
    }

    private void saveAction() {
        String content = viewTextArea.getText().trim();
        if (!content.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Saved:\n" + content, "Save", JOptionPane.INFORMATION_MESSAGE);
        	Mainpage_controller.saveTextArea(8, (viewTextArea.getText())); // Assuming this method exists
        } else {
            JOptionPane.showMessageDialog(this, "Nothing to save!", "Save", JOptionPane.WARNING_MESSAGE);
        }
        dispose();
    }

    private void setupAutoClose() {
        Timer timer = new Timer(10000, e -> {
            saveAction();  // Execute save operation before closing
            dispose();     // Close the frame
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ittiaGDS_FUplan::new);
    }
}