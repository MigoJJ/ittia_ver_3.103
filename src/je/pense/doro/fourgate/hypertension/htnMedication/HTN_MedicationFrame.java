package je.pense.doro.fourgate.hypertension.htnMedication;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public class HTN_MedicationFrame extends JPanel {

    private JTable sideEffectsTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private final MedicationType medicationType;
    private final JTextArea notesTextArea; // Reference to the shared notesTextArea

    // Enum to represent different medication types
    public enum MedicationType {
        DIURETICS("Diuretics",
                new Object[][]{
                        {false, "Increased Urination"},
                        {false, "Dehydration"},
                        {false, "Electrolyte Imbalances (Low Potassium, Sodium, Magnesium)"},
                        {false, "Dizziness or Lightheadedness"},
                        {false, "Fatigue"},
                        {false, "Muscle Cramps"},
                        {false, "Gout"}
                }),
        BETA_BLOCKERS("Beta-Blockers",
                new Object[][]{
                        {false, "Fatigue"},
                        {false, "Slow Heart Rate"},
                        {false, "Cold Extremities"},
                        {false, "Dizziness or Lightheadedness"},
                        {false, "Nausea"},
                        {false, "Diarrhea or Constipation"},
                        {false, "Depression"},
                        {false, "Insomnia"},
                        {false, "Erectile Dysfunction"}
                }),
        CALCIUM_CHANNEL_BLOCKERS("Calcium Channel Blockers",
                new Object[][]{
                        {false, "Headache"},
                        {false, "Dizziness or Lightheadedness"},
                        {false, "Fatigue"},
                        {false, "Swelling in the Ankles and Feet"},
                        {false, "Constipation"},
                        {false, "Palpitations"}
                }),
        ARBS("ARBs",
                new Object[][]{
                        {false, "Dizziness or Lightheadedness"},
                        {false, "Fatigue"},
                        {false, "Headache"},
                        {false, "Nausea"},
                        {false, "Diarrhea"},
                        {false, "Angioedema (Swelling of Face, Lips, Tongue, or Throat)"}
                }),
        ALPHA_BLOCKERS("Alpha-Blockers",
                new Object[][]{
                        {false, "Dizziness or Lightheadedness, especially when standing up"},
                        {false, "Headache"},
                        {false, "Fatigue"},
                        {false, "Palpitations"},
                        {false, "Nasal Congestion"}
                }),
        ACE_INHIBITORS("ACE Inhibitors",
                new Object[][]{
                        {false, "Dry Cough"},
                        {false, "Dizziness or Lightheadedness"},
                        {false, "Fatigue"},
                        {false, "Headache"},
                        {false, "Nausea"},
                        {false, "Loss of Taste"},
                        {false, "Rash"},
                        {false, "Angioedema (Swelling of Face, Lips, Tongue, Throat)"}
                }),
        ALPHA2_RECEPTOR_AGONISTS("Alpha-2 Receptor Agonists",
                new Object[][]{
                        {false, "Drowsiness"},
                        {false, "Dry Mouth"},
                        {false, "Dizziness or Lightheadedness"},
                        {false, "Fatigue"},
                        {false, "Constipation"},
                        {false, "Erectile Dysfunction"}
                }),
        VASODILATORS("Vasodilators",
                new Object[][]{
                        {false, "Headache"},
                        {false, "Flushing"},
                        {false, "Dizziness or Lightheadedness"},
                        {false, "Palpitations"},
                        {false, "Increased Heart Rate"}
                });

        private final String displayName;
        private final Object[][] sideEffectsData;

        MedicationType(String displayName, Object[][] sideEffectsData) {
            this.displayName = displayName;
            this.sideEffectsData = sideEffectsData;
        }

        public String getDisplayName() {
            return displayName;
        }

        public Object[][] getSideEffectsData() {
            return sideEffectsData;
        }
    }

    public HTN_MedicationFrame(MedicationType medicationType, JTextArea notesTextArea) {
        this.medicationType = medicationType;
        this.notesTextArea = notesTextArea;
        setPreferredSize(new Dimension(500, 250));
        setLayout(new BorderLayout());

        // Create title label
        JLabel titleLabel = new JLabel(medicationType.getDisplayName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // Table Columns
        String[] columns = {"Select", "Side Effect"};

        // Initialize Table Model
        tableModel = new DefaultTableModel(medicationType.getSideEffectsData(), columns) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 0) ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Only "Select" column is editable
            }
        };

        // Create JTable
        sideEffectsTable = new JTable(tableModel);
        sideEffectsTable.setRowHeight(20);

        // Minimize the height of the column headers
        JTableHeader header = sideEffectsTable.getTableHeader();
        Font headerFont = header.getFont();
        header.setFont(new Font(headerFont.getName(), Font.PLAIN, 12)); // Adjust font size as needed

        // Minimize the width of the "Select" column
        TableColumn column = sideEffectsTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(50); // Set a preferred width of 50 pixels. Adjust as needed.
        column.setMaxWidth(50);
        column.setMinWidth(50);

        // Enable sorting
        sorter = new TableRowSorter<>(tableModel);
        sideEffectsTable.setRowSorter(sorter);


        // Add action listener to append text to the shared notesTextArea - CHANGE IS HERE
        sideEffectsTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 0) { // "Select" column
                int row = e.getFirstRow();
                boolean isSelected = (Boolean) tableModel.getValueAt(row, 0);
                String sideEffect = (String) tableModel.getValueAt(row, 1);

                if (isSelected) {
                    notesTextArea.append("   [ + ] " + medicationType.getDisplayName() + ": " + sideEffect + "\n");
                }
            }
        });

        // Scroll Pane for better visibility
        JScrollPane scrollPane = new JScrollPane(sideEffectsTable);

        // Submit Button
        JButton submitButton = new JButton("Submit Selected");
        submitButton.addActionListener(e -> showSelectedSideEffects(medicationType,notesTextArea));

        // Add Components
        add(scrollPane, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);

    }

    // Method to Display Selected Side Effects
    private void showSelectedSideEffects(MedicationType medicationType, JTextArea notesTextArea) {
        System.out.println("Showing Side Effect has been clicked");
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (!((Boolean) tableModel.getValueAt(i, 0))) {
                String sideEffect = (String) tableModel.getValueAt(i, 1);
                        notesTextArea.append("   [ - ]  " + medicationType.getDisplayName() + ": " + sideEffect + "\n");
                    }
                
             }
             JOptionPane.showMessageDialog(this, "Side effects status has been submitted", "Selection Summary", JOptionPane.INFORMATION_MESSAGE);
      }

}