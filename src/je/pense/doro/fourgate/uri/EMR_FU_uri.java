package je.pense.doro.fourgate.uri;

import java.awt.BorderLayout;	
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import je.pense.doro.entry.EntryDir;

public class EMR_FU_uri extends JFrame implements ActionListener {
    private static ArrayList<JTextArea> textAreas;
    private static JButton inputButton, editButton, saveButton, exitButton;

    public EMR_FU_uri() {
        setTitle("URI");
        setSize(400, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create JTextAreas with default text and make them scrollable
        textAreas = new ArrayList<JTextArea>();
        String[] defaultTexts = { "CC>", "PI>", "ROS>", "PMH>", "S>", "O>", "Physical Exam>", "A>", "P>", "Comment>" };

        // Create panel for JTextAreas
        JPanel textAreaPanel = new JPanel();
        textAreaPanel.setLayout(new GridLayout(textAreas.size(), 1));
        for (int i = 0; i < textAreas.size(); i++) {
            textAreaPanel.add(textAreas.get(i));
        }
        
	    for (int i = 0; i < defaultTexts.length; i++) {
	        JTextArea textArea = new JTextArea();
	        textArea.setText(getSavedText(i)); // Load saved text from file
	        textArea.setLineWrap(true);
	        textArea.setWrapStyleWord(true);
	        JScrollPane scrollPane = new JScrollPane(textArea);
	        textAreaPanel.add(scrollPane);
	        textAreas.add(textArea);
	    }
        
        // Create buttons
        saveButton = new JButton("Save");
        exitButton = new JButton("Exit");

        // Attach action listeners to buttons
        saveButton.addActionListener(this);
        exitButton.addActionListener(this);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4));
        buttonPanel.add(saveButton);
        buttonPanel.add(exitButton);

        // Gradually darken background color using orange
        float hue = 0.13f;
        float saturation = 1.0f;
        float brightness = 1.0f;
        float increment = 0.03f;
        for (int i = 0; i < textAreas.size(); i++) {
            JTextArea textArea = textAreas.get(i);
            Color color = Color.getHSBColor(hue, saturation, brightness);
            textArea.setBackground(color);
            brightness -= increment;
        }

        // Add components to JFrame
        add(textAreaPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH);

        setVisible(true);

    exitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        		dispose();
        	}
        });

    saveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < textAreas.size(); i++) {
                JTextArea textArea = textAreas.get(i);
                try {
                    // Open existing text file and overwrite with new text
                    String filename = EntryDir.HOME_DIR + "/fourgate/uri/textarea" + i;
                    File file = new File(filename);
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                    writer.write(textArea.getText());
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            JOptionPane.showMessageDialog(null, "Text saved to files.");
        }
    });
    // Add panels to JFrame
    getContentPane().add(buttonPanel, BorderLayout.NORTH);
    getContentPane().add(textAreaPanel, BorderLayout.CENTER);
    setVisible(true);
    }

	private static String getSavedText(int index) {
		String filename = EntryDir.HOME_DIR + "/fourgate/uri/textarea" + index;
		File file = new File(filename);
	    if (!file.exists()) {
	        return ""; // Return empty string if file doesn't exist yet
	    }
	    try {
	        BufferedReader reader = new BufferedReader(new FileReader(file));
	        StringBuilder sb = new StringBuilder();
	        String line = reader.readLine();
	        while (line != null) {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = reader.readLine();
	        }
	        reader.close();
	        return sb.toString();
	    } catch (IOException ex) {
	        ex.printStackTrace();
	        return ""; // Return empty string if there was an error reading the file
	    }
	}
    
	public static void main(String[] args) {
	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            new EMR_FU_uri();
	        }
	    });
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
	}
}