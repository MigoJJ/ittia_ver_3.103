package je.pense.doro.fourgate.diabetes.dmAutonomic;

import java.awt.BorderLayout;                
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;

public class ANPdm {

    public static JFrame frame = new JFrame("ANPdm");
    public static JTextArea textArea = new JTextArea();
    public static TableModel model;

    public static void main(String[] args) {
        initComponents();
    }

    public static void initComponents() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocation(300, 150);
        frame.setVisible(true);

        JTextArea textArea = new JTextArea("    < Autonomic Neuroapthy  :cd > \n");
        DefaultTableModel tableModel = createTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPaneForTextArea = new JScrollPane(textArea);
        scrollPaneForTextArea.setPreferredSize(new Dimension(600, 100));
        frame.add(scrollPaneForTextArea, BorderLayout.NORTH);
        
        JPanel westPanel = new JPanel(new GridLayout(5, 1));
        westPanel.setPreferredSize(new Dimension(300, westPanel.getPreferredSize().height));
        String[] buttonNames = {"Save", "Clear", "Copy", "Quit", "SelectAll", "ClearAll"};

        for (String name : buttonNames) {
            westPanel.add(ANPdmButtonWest.createButton(name, textArea, table));
        }
        frame.add(westPanel, BorderLayout.WEST);

        table.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column == 1 || column == 2) {
                    Boolean isChecked = (Boolean) table.getModel().getValueAt(row, column);
                    ANPdmCheckbox.checkboxAction(textArea, (DefaultTableModel)table.getModel(), row, column, isChecked);
                }
            }
        });
        
        configureTable(table);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 420));
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new GridLayout(1, 10));
        for (int i = 1; i <= 10; i++) {
            JButton button = new JButton("Button " + (i + 10));
            button.setPreferredSize(new Dimension(button.getPreferredSize().width, 45));
            
            if (i >= 1 && i <= 10) {
                int buttonNumber = i + 10;
                button.addActionListener(e -> ANPdmButtonSouth.buttonMethod1(buttonNumber));
            } else {
                button.addActionListener(e -> ANPdmButtonSouth.buttonMethod10());
            }
            southPanel.add(button);
        }
        frame.add(southPanel, BorderLayout.SOUTH);

        // Changed from frame.pack() to setSize
        frame.setSize(950, 500); // Set frame size to 150x100 pixels
        frame.setVisible(true);
    }

    public static DefaultTableModel createTableModel() {
        String[] columnNames = {"Column 1", "[ + ]", "[ - ]", "Items"};
        String[] defaultItems = ANPdmReturnString.getDefaultStrings();
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, defaultItems.length) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 1 || columnIndex == 2 ? Boolean.class : super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        for (int i = 0; i < defaultItems.length; i++) {
            String indentedItem = "    " + defaultItems[i];
            tableModel.setValueAt(indentedItem, i, 3);
        }
        return tableModel;
    }

    public static void configureTable(JTable table) {
        table.setRowHeight(30);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);  // Reduced from 200 to 50 to minimize Column 1
        columnModel.getColumn(1).setPreferredWidth(5);
        columnModel.getColumn(2).setPreferredWidth(5);
        columnModel.getColumn(3).setPreferredWidth(500);
    }

    public static void exitFrame(String buttonName) {
        frame.dispose();
    }
}