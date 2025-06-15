package je.pense.doro.soap.assessment.kcd8.shortdb;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.soap.assessment.kcd8.KCDViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * A Swing GUI for viewing, searching, and selecting entries from the "short" KCD-8 database.
 * Delegates all data access operations to DatabaseManager_short.
 */
public class KCDShortViewer extends JFrame {

    private static final Logger logger = LoggerFactory.getLogger(KCDShortViewer.class);

    private final DatabaseManager_short dbManager;
    private JTable table;
    private JTextField searchField;
    private JTextArea selectedDataArea;
    private DefaultTableModel tableModel;

    public KCDShortViewer() {
        this.dbManager = new DatabaseManager_short();
        setupFrame();
        initUI();
        loadAllData();
    }

    private void setupFrame() {
        setTitle("KCD-8 Short Code Viewer");
        setSize(1280, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void initUI() {
        add(createSearchPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createSelectionPanel(), BorderLayout.EAST);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        searchField = new JTextField(30);
        searchField.addActionListener(e -> searchData());

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchData());

        JButton loadAllButton = new JButton("Load All");
        loadAllButton.addActionListener(e -> loadAllData());

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(loadAllButton);
        return searchPanel;
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {"ID", "Code", "Korean Name", "English Name"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(400);
        table.getColumnModel().getColumn(3).setPreferredWidth(500);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                appendSelectedRowToTextArea();
            }
        });

        return new JScrollPane(table);
    }

    private JScrollPane createSelectionPanel() {
        selectedDataArea = new JTextArea();
        selectedDataArea.setEditable(true);
        selectedDataArea.setLineWrap(true);
        selectedDataArea.setWrapStyleWord(true);

        JScrollPane textAreaScrollPane = new JScrollPane(selectedDataArea);
        textAreaScrollPane.setPreferredSize(new Dimension(300, 0));
        textAreaScrollPane.setBorder(BorderFactory.createTitledBorder("Selected Data for SOAP Note"));
        return textAreaScrollPane;
    }

    private JPanel createButtonPanel() {
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton clearButton = new JButton("Clear Selection");
        clearButton.addActionListener(e -> selectedDataArea.setText(""));

        JButton saveButton = new JButton("Save to Assessment");
        saveButton.addActionListener(e -> saveSelectionToEMR());

        JButton openFullViewerButton = new JButton("Open KCD-8 Full Viewer");
        openFullViewerButton.addActionListener(e -> openFullViewer());

        JButton quitButton = new JButton("Close");
        quitButton.addActionListener(e -> dispose());

        southPanel.add(clearButton);
        southPanel.add(saveButton);
        southPanel.add(openFullViewerButton);
        southPanel.add(quitButton);
        return southPanel;
    }

    private void loadAllData() {
        logger.info("Loading all entries from the short database.");
        List<DatabaseManager_short.KcdCodeEntry> entries = dbManager.getAllShortCodes();
        populateTable(entries);
    }

    private void searchData() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadAllData();
            return;
        }
        logger.info("Searching for: '{}'", query);
        List<DatabaseManager_short.KcdCodeEntry> entries = dbManager.searchShortCodes(query);
        populateTable(entries);
    }

    private void populateTable(List<DatabaseManager_short.KcdCodeEntry> entries) {
        tableModel.setRowCount(0);
        for (DatabaseManager_short.KcdCodeEntry entry : entries) {
            tableModel.addRow(new Object[]{
                    entry.id(),
                    entry.code(),
                    entry.koreanName(),
                    entry.englishName()
            });
        }
    }

    private void appendSelectedRowToTextArea() {
        int selectedRow = table.getSelectedRow();
        String code = tableModel.getValueAt(selectedRow, 1).toString();
        String koreanName = tableModel.getValueAt(selectedRow, 2).toString();
        String entry = String.format("#%s (%s)\n", koreanName, code);
        selectedDataArea.append(entry);
    }

    private void saveSelectionToEMR() {
        String textToSave = selectedDataArea.getText();
        if (textToSave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "The selection area is empty.", "Nothing to Save", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            GDSEMR_frame.setTextAreaText(7, textToSave);
            JOptionPane.showMessageDialog(this, "Selection saved to the Assessment section.", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            logger.error("Failed to save text to GDSEMR_frame", ex);
            JOptionPane.showMessageDialog(this, "Error saving text: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openFullViewer() {
        SwingUtilities.invokeLater(() -> new KCDViewer().setVisible(true));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KCDShortViewer().setVisible(true));
    }
}
