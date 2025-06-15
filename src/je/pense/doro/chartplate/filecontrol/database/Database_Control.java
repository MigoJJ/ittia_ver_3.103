package je.pense.doro.chartplate.filecontrol.database;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import je.pense.doro.entry.EntryDir;

public class Database_Control extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(Database_Control.class);
    private static final String userHomeDirectory = System.getProperty("user.home");
    
    private static final String TARGET_DIR = userHomeDirectory + "/문서/ITTIA_EMR_db";
    private static final String TARGET_DIR_SUPP = userHomeDirectory + "/문서/Support_directory";
    private static final String SOURCE_DIR_SUPP = EntryDir.homeDir +"/support/EMR_support_Folder";
    private static final String DEST_DIR = EntryDir.homeDir + "/chartplate/filecontrol/database";
    private static final String[] DB_FILE_NAMES = {
        "javalabtests.db", "icd11.db", "kcd8db.db","kcd8db_short.db", 
        "AbbFullDis.db", "LabCodeFullDis.db", "extracteddata.txt"
    };

    public Database_Control() {
        setTitle("Database File Control");
        setSize(300, 100);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton saveButton = new JButton("Save");
        JButton rescueButton = new JButton("Rescue");
        JButton quitButton = new JButton("Quit");

        saveButton.addActionListener(e -> handleCopy("Save", EntryDir.homeDir + "/chartplate/filecontrol/database", TARGET_DIR));
        rescueButton.addActionListener(this::confirmAndRescue);
        quitButton.addActionListener(e -> dispose());

        add(saveButton); add(rescueButton); add(quitButton);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        EntryDir.createDirectoryIfNotExists(TARGET_DIR);
        EntryDir.createDirectoryIfNotExists(TARGET_DIR_SUPP);
        EntryDir.createDirectoryIfNotExists(DEST_DIR);
        createFileIfMissing(DEST_DIR + "/extracteddata.txt");
    }

    private void confirmAndRescue(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(this,
            "Rescue will overwrite files in:\n" + DEST_DIR + "\nProceed?",
            "Confirm Rescue", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            handleCopy("Rescue", TARGET_DIR, DEST_DIR);
        }
    }

    private void handleCopy(String action, String fromDir, String toDir) {
        try {
            Files.createDirectories(Paths.get(toDir));
            boolean copied = false;
            StringBuilder skipped = new StringBuilder();

            // Copy individual database files
            for (String fileName : DB_FILE_NAMES) {
                Path source = Paths.get(fromDir, fileName);
                Path target = Paths.get(toDir, fileName);

                if (Files.exists(source)) {
                    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    logger.info("{}: Copied {} → {}", action, source, target);
                    copied = true;
                } else {
                    logger.warn("{}: Missing {}", action, source);
                    skipped.append(source).append("\n");
                }
            }

            // Copy the entire EMR_support_Folder
            boolean dirCopied = copyDirectory(action, SOURCE_DIR_SUPP, TARGET_DIR_SUPP);
            if (dirCopied) {
                copied = true;
                logger.info("{}: Copied directory {} → {}", action, SOURCE_DIR_SUPP, TARGET_DIR_SUPP);
            } else {
                logger.warn("{}: Failed to copy directory {} or directory not found", action, SOURCE_DIR_SUPP);
                skipped.append("Directory: ").append(SOURCE_DIR_SUPP).append("\n");
            }

            if (!copied) {
                JOptionPane.showMessageDialog(this, "No files or directories found to " + action.toLowerCase() + ".", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (skipped.length() > 0) {
                JOptionPane.showMessageDialog(this, "Partial success. Missing:\n" + skipped, action + " Result", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, action + " completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            logger.error("Error during {}", action, ex);
            JOptionPane.showMessageDialog(this, action + " failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean copyDirectory(String action, String sourceDir, String targetDir) throws IOException {
        Path sourcePath = Paths.get(sourceDir);
        Path targetPath = Paths.get(targetDir);

        if (!Files.exists(sourcePath)) {
            return false;
        }

        Files.createDirectories(targetPath);
        
        try (var stream = Files.walk(sourcePath)) {
            stream.forEach(source -> {
                try {
                    Path target = targetPath.resolve(sourcePath.relativize(source));
                    if (Files.isDirectory(source)) {
                        Files.createDirectories(target);
                    } else {
                        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    logger.error("{}: Failed to copy {}: {}", action, source, e.getMessage());
                }
            });
            return true;
        } catch (IOException e) {
            logger.error("{}: Error walking directory {}: {}", action, sourceDir, e.getMessage());
            return false;
        }
    }

    private void createFileIfMissing(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                Files.createFile(path);
                logger.info("Created missing file: {}", filePath);
            }
        } catch (IOException e) {
            logger.error("Failed to create file {}: {}", filePath, e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Database_Control().setVisible(true));
    }
}