package je.pense.doro.fourgate.thyroid.entry;

import javax.swing.*;

import je.pense.doro.GDSEMR_frame;

import java.awt.*;

public class EMR_thyroid_PE extends JFrame {
    private static final String[][] EXAM_SECTIONS = {
        {"Goiter Ruled", "Goiter ruled out", "Goiter ruled in Diffuse Enlargement", 
         "Goiter ruled in Nodular Enlargement", "Single Nodular Goiter", "Multiple Nodular Goiter"},
        {"Detect any nodules", "None", "Single nodule", "Multinodular Goiter"},
        {"Thyroid gland consistency", "Soft", "Soft to Firm", "Firm", "Cobble-stone", "Firm to Hard", "Hard"},
        {"Evaluate the thyroid gland for tenderness", "Tender", "Non-tender"},
        {"Systolic or continuous Bruit (y/n)", "Yes", "No"},
        {"DTR deep tendon reflex", "1+ = present but depressed", "2+ = normal / average", 
         "3+ = increased", "4+ = clonus", "Doctor has not performed DTR test"},
        {"TED: Thyroid Eye Disease", "Class 0: No signs or symptoms", 
         "Class 1: Only signs", "Class 2: Soft tissue involvement", 
         "Class 3: Proptosis", "Class 4: Extraocular muscle involvement", 
         "Class 5: Corneal involvement", "Class 6: Sight loss"}
    };

    private final JTextField goiterSizeField = new JTextField(10);
    private final JTextArea outputArea = new JTextArea();
    private final JCheckBox[][] checkBoxGroups = new JCheckBox[EXAM_SECTIONS.length][];

    public EMR_thyroid_PE() {
        setupFrame();
        createUI();
    }

    private void setupFrame() {
        setTitle("Thyroid Physical Exam");
        setSize(1000, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private void createUI() {
        add(createMainPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
        setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(createExamPanel(), BorderLayout.NORTH);
        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createExamPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 3));
        panel.add(createGoiterSizePanel());
        
        for (int i = 0; i < EXAM_SECTIONS.length; i++) {
            panel.add(createSectionPanel(i));
        }
        return panel;
    }

    private JPanel createSectionPanel(int sectionIndex) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(EXAM_SECTIONS[sectionIndex][0]));

        checkBoxGroups[sectionIndex] = new JCheckBox[EXAM_SECTIONS[sectionIndex].length - 1];
        for (int i = 0; i < checkBoxGroups[sectionIndex].length; i++) {
            checkBoxGroups[sectionIndex][i] = new JCheckBox(EXAM_SECTIONS[sectionIndex][i + 1]);
            panel.add(checkBoxGroups[sectionIndex][i]);
        }
        return panel;
    }

    private JPanel createGoiterSizePanel() {
        JPanel panel = new JPanel();
        goiterSizeField.setPreferredSize(new Dimension(100, 35));
        goiterSizeField.setHorizontalAlignment(JTextField.CENTER);
        panel.add(new JLabel("Goiter size (mL) "));
        panel.add(goiterSizeField);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        addButton(panel, "Clear", this::clearAll);
        addButton(panel, "Execute", this::saveExam);
        addButton(panel, "Save and Quit", () -> {
            GDSEMR_frame.setTextAreaText(6, outputArea.getText());
            dispose();
        });
        return panel;
    }

    private void addButton(JPanel panel, String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        panel.add(button);
    }

    private void saveExam() {
        StringBuilder output = new StringBuilder("<Thyroid Exam>\n");
        output.append("   Goiter size  :\t[ ").append(goiterSizeField.getText()).append("  ] cc\n");
        
        String[] categories = {"Goiter", "Nodules", "Consistency", "Tenderness", "Bruit", "DTR", "Werner's Report"};
        for (int i = 0; i < checkBoxGroups.length; i++) {
            output.append(String.format("   %-12s:\t%s\n", 
                categories[i], getSelectedItems(checkBoxGroups[i])));
        }
        
        outputArea.setText(output.toString());
    }

    private String getSelectedItems(JCheckBox[] boxes) {
        return String.join(", ", java.util.Arrays.stream(boxes)
            .filter(JCheckBox::isSelected)
            .map(JCheckBox::getText)
            .toArray(String[]::new));
    }

    private void clearAll() {
        goiterSizeField.setText("");
        outputArea.setText("");
        for (JCheckBox[] group : checkBoxGroups) {
            for (JCheckBox box : group) {
                box.setSelected(false);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EMR_thyroid_PE::new);
    }
}
