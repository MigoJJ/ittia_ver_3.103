package je.pense.doro.fourgate.diabetes.dmPeripheral;

import java.awt.BorderLayout;									
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;

public class PNPdm {

	public static JFrame frame = new JFrame("PNPdm");
    public static JTextArea textArea = new JTextArea(); // Assuming this is your text area
    public static TableModel model; // Initialize this with your table model

    public static void main(String[] args) {
        initComponents();
    }

    public static void initComponents() {
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // or EXIT_ON_CLOSE depending on your requirement
	        frame.setLayout(new BorderLayout());
	        frame.setLocation(600, 300);
	        frame.setVisible(true);

     // Add JTextArea in the NORTH panel with a specific size
        JTextArea textArea = new JTextArea("    < Peripheral Neuroapthy  :cd > \n");
	        DefaultTableModel tableModel = createTableModel();
	        JTable table = new JTable(tableModel);
	        JScrollPane scrollPaneForTextArea = new JScrollPane(textArea);
	        scrollPaneForTextArea.setPreferredSize(new Dimension(500, 250)); // Set preferred size of the JScrollPane
	        frame.add(scrollPaneForTextArea, BorderLayout.NORTH);
        
     // Add buttons in the WEST panel with a specific width using ANPdmButton class
        JPanel westPanel = new JPanel(new GridLayout(5, 1)); // Adjusted for 5 rows as per the example
	        westPanel.setPreferredSize(new Dimension(300, westPanel.getPreferredSize().height));
	        String[] buttonNames = {"Save", "Clear", "Copy", "Quit", "SelectAll", "ClearAll"}; // Add more names as needed

		        for (String name : buttonNames) {
		        	// In ANPdm class, modify the button creation to pass textArea
		        	westPanel.add(PNPdmButtonWest.createButton(name, textArea, table));
		        }
	        frame.add(westPanel, BorderLayout.WEST);

		        // Add a TableModelListener to listen for checkbox changes
		        table.getModel().addTableModelListener(e -> {
		            if (e.getType() == TableModelEvent.UPDATE) {
		                int row = e.getFirstRow();
		                int column = e.getColumn();
		                if (column == 1 || column == 2) { // Assuming columns 1 and 2 are checkboxes
		                    Boolean isChecked = (Boolean) table.getModel().getValueAt(row, column);
		                    PNPdmCheckbox.checkboxAction(textArea, (DefaultTableModel)table.getModel(), row, column, isChecked);
		                }
		            }
		        });
        
        configureTable(table);
        	
        	JScrollPane scrollPane = new JScrollPane(table);
        	scrollPane.setPreferredSize(new Dimension(900, 420)); // Adjusted size
        frame.add(scrollPane, BorderLayout.CENTER);


        //	Inside your frame setup or initialization method	
		        JPanel southPanel = new JPanel(new GridLayout(1, 10)); // 1 row, 10 columns
		        for (int i = 1; i <= 10; i++) {
		            JButton button = new JButton("Button " + (i + 10));
		            button.setPreferredSize(new Dimension(button.getPreferredSize().width, 45));
		
		            // Assume buttonMethod1 is to be called for buttons 11 through 16 with the button number as an argument
		            if (i >= 1 && i <= 10) { // Buttons 11 through 16
		                int buttonNumber = i + 10; // Calculate button number
		                button.addActionListener(e -> PNPdmButtonSouth.buttonMethod1(buttonNumber));
		            } else {
		                // For other buttons, you can use a switch or if-else structure to decide which method to call
		                // For example, calling buttonMethod10 for Button 20 (which doesn't directly match your current setup)
		                // This is just a placeholder to illustrate handling for other buttons
		                button.addActionListener(e -> PNPdmButtonSouth.buttonMethod10());
		            }
		            southPanel.add(button);
		        }
        frame.add(southPanel, BorderLayout.SOUTH);

        // Adjust frame size to fit its content
        frame.pack(); // Adjusts frame to just fit its components
        frame.setVisible(true);
    }

    public static DefaultTableModel createTableModel() {
        String[] columnNames = {"Column 1", "[ + ]", "[ - ]", "Items"};
        String[] defaultItems = PNPdmReturnString.getDefaultStrings();
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, defaultItems.length) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 1 || columnIndex == 2 ? Boolean.class : super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Optionally make only certain columns editable if desired
                return column != 0; // Example: makes column 0 (Column 1) non-editable
            }
        };

        // Indent each item with 4 spaces before adding it to the table
        for (int i = 0; i < defaultItems.length; i++) {
            String indentedItem = "    " + defaultItems[i]; // Prepend 4 spaces for indentation
            tableModel.setValueAt(indentedItem, i, 3); // Populate the "Items" column with indented strings
        }
        return tableModel;
    }

    public static void configureTable(JTable table) {
        table.setRowHeight(30);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
        columnModel.getColumn(1).setPreferredWidth(5);
        columnModel.getColumn(2).setPreferredWidth(5);
        columnModel.getColumn(3).setPreferredWidth(500);
    }

	public static void exitFrame(String buttonName) {
      System.out.println("Exit action performed    " + buttonName);
      frame.dispose(); // This will close the window and release all resources.	
      }
}