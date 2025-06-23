package je.pense.doro.chartplate.keybutton.EMR_Backup_Excute;

import javax.swing.*;	

import je.pense.doro.entry.EntryDir;

import java.awt.*;
import java.io.*;
import java.nio.file.*;

public class EMR_B_FileListFrame extends JFrame {
    private static final String DIR_PATH = EntryDir.HOME_DIR + "/tripikata/rescue/rescuefolder";
    
    public EMR_B_FileListFrame() {
        setTitle("File List");
        setSize(300, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JList<String> fileList = new JList<>(loadFiles());
        fileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) 
                    openEditor(new File(DIR_PATH, fileList.getSelectedValue()));
            }
        });
        
        add(new JScrollPane(fileList));
        setLocation((int)GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getMaximumWindowBounds().getMaxX() - 300, 0);
            
        new javax.swing.Timer(10000, e -> dispose()).start();
    }

    private DefaultListModel<String> loadFiles() {
        DefaultListModel<String> model = new DefaultListModel<>();
        File[] files = new File(DIR_PATH).listFiles();
        if (files != null) for (File f : files) model.addElement(f.getName());
        return model;
    }

    private void openEditor(File file) {
        JFrame editor = new JFrame("Editor: " + file.getName());
        JTextArea text = new JTextArea();
        
        try {
            text.setText(Files.readString(file.toPath()));
            editor.add(new JScrollPane(text));
            editor.setSize(500, 800);
            editor.setLocationRelativeTo(null);
            editor.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            editor.setVisible(true);
            
            new javax.swing.Timer(15000, e -> editor.dispose()).start();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EMR_B_FileListFrame().setVisible(true));
    }
}
