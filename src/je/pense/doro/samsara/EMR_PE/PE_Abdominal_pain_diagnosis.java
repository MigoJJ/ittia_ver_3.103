package je.pense.doro.samsara.EMR_PE;
import javax.swing.*;		
import javax.swing.table.*;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PE_Abdominal_pain_diagnosis extends JFrame {
    private final JTable table;
    private final JTextArea notesArea;

    public PE_Abdominal_pain_diagnosis() {
        setTitle("Medical Conditions Checklist");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize components
        notesArea = new JTextArea(5, 40);
        notesArea.setEditable(true);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setBackground(new Color(245, 222, 179)); // Very light brown (Wheat color)

        table = createTable();
        table.setRowHeight(130); // Adjust row height for readability
        table.setPreferredScrollableViewportSize(new Dimension(900, 700)); // Preferred table size
        // EAST Panel for Buttons
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons
        JButton clearButton = new JButton("Clear");
        JButton saveButton = new JButton("Save");
        JButton dateButton = new JButton("Date");
        JButton lessButton = new JButton("Less Likely");
        JButton moreButton = new JButton("More Likely");
        JButton quitButton = new JButton("Quit");

        clearButton.addActionListener(e -> {
            // Clear text area
            notesArea.setText("");
            
            // Clear all checkboxes in the table
            ClickableTableModel model = (ClickableTableModel) table.getModel();
            for (int row = 0; row < model.getRowCount(); row++) {
                Object[][] conditions = (Object[][]) model.getValueAt(row, 1);
                for (Object[] condition : conditions) {
                    condition[0] = false;
                }
            }
            table.repaint();
        });

        dateButton.addActionListener(e -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formattedDate = dateFormat.format(new Date());
            notesArea.append("Date: " + formattedDate + "\n");
        });
        lessButton.addActionListener(e -> notesArea.append("* Less likely\n"));
        moreButton.addActionListener(e -> notesArea.append("* More likely\n"));
        saveButton.addActionListener(e -> {
        	    String textContent = notesArea.getText();
        	    GDSEMR_frame.setTextAreaText(7, textContent);
        	    dispose();
        	});

        quitButton.addActionListener(e -> dispose());

        // Add buttons to the EAST panel
        eastPanel.add(clearButton);
        eastPanel.add(Box.createVerticalStrut(10)); // Add spacing between buttons
        eastPanel.add(saveButton);
        eastPanel.add(Box.createVerticalStrut(10));
        eastPanel.add(dateButton);
        eastPanel.add(Box.createVerticalStrut(10));
        eastPanel.add(lessButton);
        eastPanel.add(Box.createVerticalStrut(10));
        eastPanel.add(moreButton);
        eastPanel.add(Box.createVerticalStrut(10));
        eastPanel.add(quitButton);
        // Layout setup
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            new JScrollPane(table),
            new JScrollPane(notesArea)
        );
        splitPane.setDividerLocation(675);
        splitPane.setResizeWeight(0.7); // Allocate space (70% for table, 30% for notes)

        add(splitPane, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST); // Add EAST panel

        pack(); // Compute optimal size
//        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize frame
        setLocationRelativeTo(null); // Center frame on screen
    }



    private JTable createTable() {
        ClickableTableModel model = new ClickableTableModel();
        JTable table = new JTable(model);
        
        // Configure table properties
        table.setRowHeight(100);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(400);
        
        // Set custom cell renderer and editor
        table.getColumnModel().getColumn(1).setCellRenderer(new CheckboxListRenderer());
        table.getColumnModel().getColumn(1).setCellEditor(new CheckboxListEditor(notesArea));

        return table;
    }

    static class MedicalCondition {
        String category;
        List<ConditionItem> items;

        MedicalCondition(String category) {
            this.category = category;
            this.items = new ArrayList<>();
        }

        static class ConditionItem {
            boolean selected;
            String description;

            ConditionItem(boolean selected, String description) {
                this.selected = selected;
                this.description = description;
            }
        }
    }

    static class ClickableTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Category", "Conditions"};
        private final List<Object[]> data;

        public ClickableTableModel() {
            data = new ArrayList<>();
            initializeData();
        }

        private void initializeData() {
            // Epigastric Pain (Upper Middle Abdomen)
            Object[][] epigastricConditions = {
                    {false, "Peptic Ulcer Disease (Gastric or duodenal ulcers)"},
                    {false, "Gastroesophageal Reflux Disease (GERD)"},
                    {false, "Gastritis"},
                    {false, "Pancreatitis"},
                    {false, "Non-Ulcer Dyspepsia"},
                    {false, "Myocardial Infarction (MI)"}
            };
            data.add(new Object[]{"Epigastric Pain (Upper Middle Abdomen)", epigastricConditions});

            // Periumbilical Pain (Around the Navel)
            Object[][] periumbilicalConditions = {
                    {false, "Early Appendicitis"},
                    {false, "Small Bowel Issues (Small bowel obstruction, gastroenteritis)"},
                    {false, "Aortic Aneurysm"}
            };
            data.add(new Object[]{"Periumbilical Pain (Around the Navel)", periumbilicalConditions});

            // Generalized Abdominal Pain (Diffuse Pain)
            Object[][] generalizedConditions = {
                    {false, "Gastroenteritis"},
                    {false, "Irritable Bowel Syndrome (IBS)"},
                    {false, "Peritonitis"},
                    {false, "Bowel Obstruction"},
                    {false, "Mesenteric Ischemia"},
                    {false, "Metabolic Disorders (Diabetic ketoacidosis (DKA))"}
            };
            data.add(new Object[]{"Generalized Abdominal Pain (Diffuse Pain)", generalizedConditions});

        	// Right Upper Quadrant Pain
            Object[][] ruqConditions = {
                {false, "Gallbladder Disease (Cholecystitis, cholelithiasis, biliary colic)"},
                {false, "Liver Disease (Hepatitis, liver abscess, liver tumors, hepatic congestion)"},
                {false, "Duodenal Ulcer"},
                {false, "Right Kidney Issues (Pyelonephritis, nephrolithiasis)"},
                {false, "Pneumonia (Right Lower Lobe)"}
            };
            data.add(new Object[]{"Right Upper Quadrant Pain [ RUQ ]", ruqConditions});

            // Left Upper Quadrant Pain
            Object[][] luqConditions = {
                    {false, "Gastric Ulcer"},
                    {false, "Gastritis"},
                    {false, "Pancreatitis"},
                    {false, "Splenic Issues (Splenomegaly, splenic infarct, splenic rupture)"},
                    {false, "Left Kidney Issues (Pyelonephritis, nephrolithiasis)"},
                    {false, "Pneumonia (Left Lower Lobe)"}
            };
            data.add(new Object[]{"Left Upper Quadrant Pain [ LUQ ]", luqConditions});

            // Right Lower Quadrant Pain
            Object[][] rlqConditions = {
                    {false, "Appendicitis"},
                    {false, "Ovarian Issues (Ovarian cyst, ectopic pregnancy, ovarian torsion)"},
                    {false, "Right Kidney Issues (Pyelonephritis, nephrolithiasis)"},
                    {false, "Inflammatory Bowel Disease (Crohn's disease)"},
                    {false, "Hernia (Inguinal)"},
                    {false, "Ectopic Pregnancy"}
            };
            data.add(new Object[]{"Right Lower Quadrant Pain [ RLQ ]", rlqConditions});
            
            // Left Lower Quadrant Pain
            Object[][] llqConditions = {
                    {false, "Diverticulitis"},
                    {false, "Ovarian Issues (Ovarian cyst, ectopic pregnancy, ovarian torsion)"},
                    {false, "Left Kidney Issues (Pyelonephritis, nephrolithiasis)"},
                    {false, "Inflammatory Bowel Disease (Ulcerative colitis)"},
                    {false, "Hernia (Inguinal)"},
                    {false, "Ectopic Pregnancy"}
            };
            data.add(new Object[]{"Left Lower Quadrant (LLQ) Pain", llqConditions});

            // Add more categories as needed
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 0 ? String.class : Object[][].class;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data.get(rowIndex)[columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }

        @Override
        public void setValueAt(Object value, int rowIndex, int columnIndex) {
            data.get(rowIndex)[columnIndex] = value;
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    static class CheckboxListRenderer extends DefaultTableCellRenderer {
        private final JPanel panel;

        public CheckboxListRenderer() {
            panel = new JPanel(new GridLayout(0, 1));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            panel.removeAll();
            panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

            if (value instanceof Object[][]) {
                Object[][] items = (Object[][]) value;
                for (Object[] item : items) {
                    JCheckBox checkBox = new JCheckBox((String) item[1], (Boolean) item[0]);
                    checkBox.setBackground(panel.getBackground());
                    panel.add(checkBox);
                }
            }
            return panel;
        }
    }

    static class CheckboxListEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private final List<JCheckBox> checkboxes;
        private final JTextArea notesArea;
        private Object[][] currentValue;

        public CheckboxListEditor(JTextArea notesArea) {
            this.panel = new JPanel(new GridLayout(0, 1));
            this.checkboxes = new ArrayList<>();
            this.notesArea = notesArea;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            panel.removeAll();
            checkboxes.clear();
            panel.setBackground(table.getSelectionBackground());

            if (value instanceof Object[][]) {
                currentValue = (Object[][]) value;
                for (Object[] item : currentValue) {
                    JCheckBox checkBox = new JCheckBox((String) item[1], (Boolean) item[0]);
                    checkBox.setBackground(panel.getBackground());
                    checkBox.addActionListener(e -> {
                        item[0] = checkBox.isSelected();
                        if (checkBox.isSelected()) {
                            notesArea.append("   [R/O]: " + item[1] + "\n");
                        }
                    });
                    checkboxes.add(checkBox);
                    panel.add(checkBox);
                }
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return currentValue;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new PE_Abdominal_pain_diagnosis().setVisible(true);
        });
    }
}
