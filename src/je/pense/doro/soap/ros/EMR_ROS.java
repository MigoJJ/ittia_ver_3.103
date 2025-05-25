package je.pense.doro.soap.ros;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class EMR_ROS extends JFrame {
    private JTextArea outputArea;
    private JTable dataTable;
    private static JFrame f = new JFrame();
    public static ArrayList<String> selectList = new ArrayList<String>();
    public static ArrayList<String> allList = new ArrayList<String>();
    private String saveString = "";

    public EMR_ROS() {
        // Set the frame size and layout
        f.setSize(1800, 1100);
        f.setLayout(new BorderLayout());

        selectList.clear();
        allList.clear();

        // Create the table model and add the data
        String[] columnNames = EMR_ROS_JtableDATA.columnNames();
        DefaultTableModel tableModel = new DefaultTableModel(0, columnNames.length);
        tableModel.setColumnIdentifiers(columnNames);

        // Create a 2D array to hold the row data
        String[][] rowData = new String[columnNames.length][];

        // Add the data for each column to the row data array
        rowData[0] = EMR_ROS_JtableDATA.General();
        rowData[1] = EMR_ROS_JtableDATA.Vision();
        rowData[2] = EMR_ROS_JtableDATA.Head_and_Neck();
        rowData[3] = EMR_ROS_JtableDATA.Pulmonary();
        rowData[4] = EMR_ROS_JtableDATA.Cardiovascular();
        rowData[5] = EMR_ROS_JtableDATA.Gastrointestinal();
        rowData[6] = EMR_ROS_JtableDATA.GenitoUrinary();
        rowData[7] = EMR_ROS_JtableDATA.HematologyOncology();
        rowData[8] = EMR_ROS_JtableDATA.Neurological();
        rowData[9] = EMR_ROS_JtableDATA.Endocrine();
        rowData[10] = EMR_ROS_JtableDATA.MentalHealth();
        rowData[11] = EMR_ROS_JtableDATA.SkinAndHair();

        // Find the maximum row length
        int maxRowLength = 0;
        for (String[] column : rowData) {
            if (column != null && column.length > maxRowLength) {
                maxRowLength = column.length;
            }
        }

        // Iterate over the row data array to add each row to the table model
        for (int i = 0; i < maxRowLength; i++) {
            Vector<String> row = new Vector<>();
            for (String[] column : rowData) {
                String cellValue = "";
                if (column != null && i < column.length) {
                    cellValue = column[i];
                }
                row.add(cellValue);
            }
            tableModel.addRow(row);
        }
        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add the components to the frame
        f.add(scrollPane, BorderLayout.CENTER);

        // Add the output area to the west
        outputArea = new JTextArea(10, 0);
        outputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        f.add(outputScrollPane, BorderLayout.NORTH);
        
        // Add the clear, save, and quit buttons to the south
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton clearButton = new JButton("Clear");
        JButton saveButton = new JButton("Save");
        JButton quitButton = new JButton("Quit");

        // Set the button actions
        clearButton.addActionListener(new EMR_ROS_ButtonActions(dataTable, outputArea));
        saveButton.addActionListener(new EMR_ROS_ButtonActions(dataTable, outputArea));
        quitButton.addActionListener(new EMR_ROS_ButtonActions(dataTable, outputArea));

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int rowIndex = table.getSelectedRow();
                int columnIndex = table.getSelectedColumn();
                    Object cellValue = table.getValueAt(rowIndex, columnIndex);
                    outputArea.append("   [ ▶ ] " + cellValue.toString() + "\n");
                    selectList.add(cellValue.toString());
                }
        });

        table.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int columnIndex = table.columnAtPoint(e.getPoint());
                String columnTitle = table.getColumnName(columnIndex);
                allList.add(columnTitle);
                outputArea.append("\n" + columnTitle);

                for (int i = 0; i < table.getRowCount(); i++) {
                    Object cellValue = table.getValueAt(i, columnIndex);
						if (!((String) cellValue).isEmpty()) {
							outputArea.append("\n   [-]" + cellValue.toString());
							allList.add(cellValue.toString());
						}
                }
                StringBuilder rowData = new StringBuilder();
                if (rowData.length() > 0) {
                    outputArea.append(rowData.toString());
                }
            }
        });
        
        // Add the buttons to the panel and the panel to the frame
        buttonPanel.add(clearButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(quitButton);
        f.add(buttonPanel, BorderLayout.SOUTH);

        // Set the frame properties
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("My JFrame");
        f.setVisible(true);
    }

    public static void main(String[] args) {
        new EMR_ROS();
    }
    public static void disposemain(String[] args) {
        f.dispose();
    }
    public static void listnerAct(String[] args) {
    }
    
}