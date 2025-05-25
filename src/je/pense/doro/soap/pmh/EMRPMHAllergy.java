package je.pense.doro.soap.pmh;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import je.pense.doro.GDSEMR_frame;

import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.stream.IntStream;

public class EMRPMHAllergy extends JFrame {
    private static JTable table, eastTable;
    private static DefaultTableModel tableModel, eastTableModel;
    private static JTextArea textArea;
    private static EMRPMHAllergy instance;
    private static String default_comment = """
    	    ▣ Allergy\n
    	    During the medical check-up, the patient had no known allergies\n
    	    to food, injections and medications as of :cd \n
    	    """;

    public EMRPMHAllergy() {
        setTitle("Allergy Data Input");
        setSize(850, 715);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(createNorthPanel(), BorderLayout.NORTH);
        add(createCenterTable(), BorderLayout.CENTER);
        add(createEastPanel(), BorderLayout.EAST);
        add(createButtonPanel(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private JScrollPane createNorthPanel() {
        textArea = new JTextArea(10, 20);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return new JScrollPane(textArea);
    }

    private JScrollPane createCenterTable() {
        String[] columnNames = {"Category", "Selected", "Symptom"};
        Object[][] data = {
            {"Skin reactions", false, "Rash"},
            {"Skin reactions", false, "Hives (raised, itchy spots)"},
            {"Skin reactions", false, "Itching"},
            {"Swelling", false, "Swelling of the mouth, face, lip, tongue and throat"},
            {"Swelling", false, "Angioedema (tissue swelling under the skin)"},
            {"Respiratory symptoms", false, "Wheezing"},
            {"Respiratory symptoms", false, "Coughing"},
            {"Respiratory symptoms", false, "Shortness of breath or trouble breathing"},
            {"Gastrointestinal symptoms", false, "Nausea"},
            {"Gastrointestinal symptoms", false, "Vomiting"},
            {"Gastrointestinal symptoms", false, "Stomach cramps"},
            {"Other symptoms", false, "Fever"},
            {"Other symptoms", false, "Dizziness or lightheadedness"},
            {"Other symptoms", false, "Runny nose"},
            {"Other symptoms", false, "Itchy, watery eyes"},
            {"Anaphylaxis", false, "Difficulty swallowing"},
            {"Anaphylaxis", false, "Tightening of the airways"},
            {"Anaphylaxis", false, "Drop in blood pressure"},
            {"Anaphylaxis", false, "Weak, fast pulse"},
            {"Anaphylaxis", false, "Loss of consciousness"}
        };

        tableModel = new DefaultTableModel(data, columnNames) {
            public Class<?> getColumnClass(int col) {
                return col == 1 ? Boolean.class : String.class;
            }

            public boolean isCellEditable(int row, int col) {
                return col == 1;
            }
        };

        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 1) {
                int row = e.getFirstRow();
                updateTextArea(row, (boolean) tableModel.getValueAt(row, 1));
            }
        });

        table = new JTable(tableModel);
        table.setRowHeight(21);
        return new JScrollPane(table);
    }

    private JPanel createEastPanel() {
        String[] columnNames = {"Allergy Cause"};
String[] otherSymptoms = {
                "Pain relievers: NSAIDs ",
                "sulfa drugs (sulfamethoxazole, trimethoprim-sulfamethoxazole).",
                "Anesthesia: anesthetic medications such as propofol, ",
                "Dust Mite",
                ".......",
                "Antibiotics : penicillin and its derivatives ",
                "Antibiotics : Cephalosporins",
                "Medications : Alendronate",
                "Medications : Statin",
                "Medications : Metformin",
                "Medications : SGLT-2",
                "injections : Tramadol HCl inj. Huons",
                ".......",
                "Food : 우유 : Milk",
                "Food : 계란 : Eggs",
                "Food : 땅콩 : Peanuts",
                "Food : 견과류 : Tree nuts (e.g., 호두 - walnuts, 아몬드 - almonds, 캐슈넛 - cashews)",
                "Food : 콩 : Soybeans",
                "Food : 밀 : Wheat",
                "Food : 생선 : Fish (e.g., 고등어 - mackerel, 연어 - salmon, 참치 - tuna)",
                "Food : 갑각류 : Shellfish (e.g., 새우 - shrimp, 게 - crab, 가리비 - scallops)",
                "Food : 복숭아 : Peach (and sometimes other stone fruits like apricots and plums due to cross-reactivity)",
                "Food : 메밀 : Buckwheat"
        };

        eastTableModel = new DefaultTableModel(new Object[][]{}, columnNames);
        for (String symptom : otherSymptoms) {
            eastTableModel.addRow(new Object[]{symptom});
        }

        eastTable = new JTable(eastTableModel);
        eastTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = eastTable.getSelectedRow();
                if (row != -1) {
                    textArea.append("*** Allergy Cause: " + eastTableModel.getValueAt(row, 0) + "\n");
                }
            }
        });

        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(new JLabel("Allergy Causes:", JLabel.CENTER), BorderLayout.NORTH);
        eastPanel.add(new JScrollPane(eastTable), BorderLayout.CENTER);
        return eastPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        String[] buttonLabels = {"Default","All denied", "Anaphylaxis denied", "Clear", "Save", "Quit"};

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.addActionListener(e -> handleButtonAction(label));
            buttonPanel.add(button);
        }

        return buttonPanel;
    }

    private void handleButtonAction(String action) {
        switch (action) {
        	 case "Default" -> textArea.setText(default_comment);
            case "All denied" -> setSymptoms(false, false);
            case "Anaphylaxis denied" -> setSymptoms(true, false);
            case "Clear" -> textArea.setText("");
            case "Save" -> saveSymptoms();
            case "Quit" -> dispose();
        }
    }

    private void setSymptoms(boolean anaphylaxisOnly, boolean value) {
        StringBuilder result = new StringBuilder("\n [" + (anaphylaxisOnly ? "Anaphylaxis" : "Allergy") + " Symptoms Denied]\n");
        IntStream.range(0, table.getRowCount()).forEach(i -> {
            String category = (String) table.getValueAt(i, 0);
            if (anaphylaxisOnly == category.equals("Anaphylaxis") || !anaphylaxisOnly) {
                table.setValueAt(value, i, 1);
                result.append("    - ").append(table.getValueAt(i, 2)).append("\n");
            }
        });
        textArea.append(result.toString());
    }

    private void updateTextArea(int row, boolean selected) {
        SwingUtilities.invokeLater(() -> {
            String symptom = "    * " + table.getValueAt(row, 0) + ": " + table.getValueAt(row, 2) + "\n";
            if (selected) {
                textArea.append(symptom);
            } else {
                textArea.setText(textArea.getText().replace(symptom, ""));
            }
        });
    }

    private void saveSymptoms() {
        StringBuilder selected = new StringBuilder("Selected Symptoms:\n");
        IntStream.range(0, table.getRowCount()).filter(i -> (boolean) table.getValueAt(i, 1)).forEach(i ->
                selected.append("- ").append(table.getValueAt(i, 2)).append("\n"));
        selected.append("\nAdditional Notes:\n").append(textArea.getText());
        GDSEMR_frame.setTextAreaText(9, (textArea.getText())); // Assuming this method exists

//        JOptionPane.showMessageDialog(this, selected.toString(), "Saved Symptoms", JOptionPane.INFORMATION_MESSAGE);
    }

    public static EMRPMHAllergy getInstance() {
        if (instance == null) instance = new EMRPMHAllergy();
        return instance;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EMRPMHAllergy::new);
    }
}
