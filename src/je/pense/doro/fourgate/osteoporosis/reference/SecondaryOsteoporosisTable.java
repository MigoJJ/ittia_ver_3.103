package je.pense.doro.fourgate.osteoporosis.reference;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SecondaryOsteoporosisTable extends JFrame implements ActionListener {

    private JTable osteoporosisTable;
    private JTextArea outputTextArea;
    private JButton selectListButton, clearButton, saveButton, quitButton;

    public SecondaryOsteoporosisTable() {
        setTitle("Causes of Secondary Osteoporosis");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        createLayout();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** Initializes UI Components */
    private void initComponents() {
        // Create the table model with data
        String[] columnNames = {"Category", "Condition/Medication"};
        Object[][] data = {
                {"CNS Conditions", "Stroke"},
                {"CNS Conditions", "Parkinson’s Disease"},
                {"CNS Conditions", "Multiple Sclerosis"},
                {"Endocrine/Metabolic Disorders", "Type 1 Diabetes"},
                {"Endocrine/Metabolic Disorders", "Hyperparathyroidism"},
                {"Endocrine/Metabolic Disorders", "Hyperthyroidism"},
                {"Endocrine/Metabolic Disorders", "Adrenal Insufficiency"},
                {"Infections", "HIV/AIDS"},
                {"Gastrointestinal Disorders", "Celiac Disease"},
                {"Gastrointestinal Disorders", "Gastric Bypass Surgery"},
                {"Gastrointestinal Disorders", "Inflammatory Bowel Syndrome"},
                {"Autoimmune Diseases", "Rheumatoid Arthritis"},
                {"Autoimmune Diseases", "Systemic Lupus Erythematosus"},
                {"Medications", "Anticonvulsants"},
                {"Medications", "Chemotherapeutics"},
                {"Medications", "Methotrexate"},
                {"Medications", "Heparin"},
                {"Medications", "Corticosteroids"},
        };

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make the table non-editable
                return false;
            }
        };

        osteoporosisTable = new JTable(model);
        osteoporosisTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add a mouse listener to handle row clicks
        osteoporosisTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = osteoporosisTable.getSelectedRow();
                if (selectedRow != -1) {
                    String category = (String) osteoporosisTable.getValueAt(selectedRow, 0);
                    String condition = (String) osteoporosisTable.getValueAt(selectedRow, 1);
                    outputTextArea.append(" [-] denied: " + category + " - " + condition + "\n");
                }
            }
        });

        outputTextArea = new JTextArea(10, 40);
        outputTextArea.setEditable(false);

        // Initialize buttons
        selectListButton = new JButton("Select All");
        clearButton = new JButton("Clear");
        saveButton = new JButton("Save");
        quitButton = new JButton("Quit");

        // Add action listeners to buttons
        selectListButton.addActionListener(this);
        clearButton.addActionListener(this);
        saveButton.addActionListener(this);
        quitButton.addActionListener(this);
    }

    /** Creates UI Layout */
    private void createLayout() {
        JScrollPane tableScrollPane = new JScrollPane(osteoporosisTable);

        // Calculate preferred height with 10% increase
        int preferredHeight = (int) (osteoporosisTable.getPreferredSize().getHeight() * 1.1);
        tableScrollPane.setPreferredSize(new Dimension(osteoporosisTable.getPreferredSize().width, preferredHeight));



        add(tableScrollPane, BorderLayout.CENTER);

        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        add(outputScrollPane, BorderLayout.SOUTH);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectListButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(quitButton);
        add(buttonPanel, BorderLayout.NORTH);
    }

    /** Handles button click events */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "Select All":
                // Get all data from the table model
                DefaultTableModel model = (DefaultTableModel) osteoporosisTable.getModel();
                int rowCount = model.getRowCount();
                StringBuilder allDeniedText = new StringBuilder();

                for (int i = 0; i < rowCount; i++) {
                    String category = (String) model.getValueAt(i, 0);
                    String condition = (String) model.getValueAt(i, 1);
                    allDeniedText.append("   [-] : ").append(category).append(" - ").append(condition).append("\n");
                }
                outputTextArea.append(allDeniedText.toString());
                	break;
            case "Clear":
                outputTextArea.setText(""); // Clear the text area
                break;
            case "Save":
                // Add functionality for "Save" button
                GDSEMR_frame.setTextAreaText(1, "\n" + outputTextArea.getText());
//                JOptionPane.showMessageDialog(this, "Save button clicked!");
                break;
            case "Quit":
                dispose(); // Close the application
                break;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SecondaryOsteoporosisTable::new);
    }
}