package je.pense.doro.soap.assessment.kcd8.shortdb;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import je.pense.doro.GDSEMR_frame;
import je.pense.doro.entry.EntryDir;
import je.pense.doro.soap.assessment.kcd8.KCDViewer;

public class KCDShortViewer extends JFrame {
    private JTable table;
    private DatabaseManager_short dbManager;
    private JTextField searchField;
    private JTextArea selectedDataArea;

    public KCDShortViewer() {
        dbManager = new DatabaseManager_short();
        setTitle("KCD-8DB Short Viewer");
        setSize(1280, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadData();
    }

    private void initUI() {
        // Center: Table
        table = new JTable(new DefaultTableModel(new Object[]{"ID", "Code", "Korean Name", "English Name"}, 0));
        JScrollPane scrollPane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(350);
        table.getColumnModel().getColumn(3).setPreferredWidth(450);

        // North: Search Panel
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton loadAllButton = new JButton("Load All");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(loadAllButton);

        // East: TextArea for selected data
        selectedDataArea = new JTextArea(10, 20);
        selectedDataArea.setEditable(true);
        JScrollPane textAreaScrollPane = new JScrollPane(selectedDataArea);
        textAreaScrollPane.setPreferredSize(new Dimension(300, 15));
        textAreaScrollPane.setBorder(BorderFactory.createTitledBorder("Selected Data"));

        // South: Button Panel
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton clearButton = new JButton("Clear");
        JButton saveButton = new JButton("Save");
        JButton quitButton = new JButton("Quit");
        JButton othersButton = new JButton("KCD8 full");

        southPanel.add(clearButton);
        southPanel.add(saveButton);
        southPanel.add(quitButton);
        southPanel.add(othersButton);

        // Layout
        setLayout(new BorderLayout());
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(textAreaScrollPane, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);

        // Event Listeners
        searchButton.addActionListener(e -> searchData());
        loadAllButton.addActionListener(e -> loadData());
        searchField.addActionListener(e -> searchData());

        // Table row selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String code = table.getValueAt(selectedRow, 1).toString();
                    String koreanName = table.getValueAt(selectedRow, 2).toString();
                    String englishName = table.getValueAt(selectedRow, 3).toString();
                    String entry = String.format("Code: %s\n # %s\n   : %s\n\n", code, koreanName, englishName);
                    selectedDataArea.append(entry);
                }
            }
        });

        // Button Listeners
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDataArea.setText("");
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textToSave = selectedDataArea.getText();
                if (textToSave.isEmpty()) {
                    JOptionPane.showMessageDialog(KCDShortViewer.this,
                        "No text to save.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    GDSEMR_frame.setTextAreaText(7, textToSave);
                    JOptionPane.showMessageDialog(KCDShortViewer.this,
                        "Text saved to GDSEMR_frame (TextArea 7).",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(KCDShortViewer.this,
                        "Error saving text: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        othersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(KCDShortViewer.this,
//                    "'Others' functionality not implemented yet.",
//                    "Info", JOptionPane.INFORMATION_MESSAGE);
            	KCDViewer.main(null);
            }
        });
    }

    private void loadData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        ResultSet rs = getAllDataFromShortDB();
        try {
            if (rs != null) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("korean_name"),
                        rs.getString("english_name")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("Error closing ResultSet: " + e.getMessage());
                }
            }
        }
    }

    private void searchData() {
        String query = searchField.getText().trim();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        String sql = "SELECT * FROM kcd8db_short WHERE CAST(id AS TEXT) LIKE ? OR code LIKE ? OR korean_name LIKE ? OR english_name LIKE ?";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + EntryDir.homeDir + "/soap/assessment/kcd8/shortdb/kcd8db_short.db");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String likeQuery = "%" + query + "%";
            pstmt.setString(1, likeQuery);
            pstmt.setString(2, likeQuery);
            pstmt.setString(3, likeQuery);
            pstmt.setString(4, likeQuery);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("code"),
                    rs.getString("korean_name"),
                    rs.getString("english_name")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error searching data: " + e.getMessage(),
                "Search Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private ResultSet getAllDataFromShortDB() {
        String sql = "SELECT id, code, korean_name, english_name FROM kcd8db_short";
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + EntryDir.homeDir + "/soap/assessment/kcd8/shortdb/kcd8db_short.db");
            PreparedStatement pstmt = conn.prepareStatement(sql);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error getting all data: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KCDShortViewer().setVisible(true));
    }
}