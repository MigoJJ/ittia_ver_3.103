// File: je/pense/doro/chartplate/filecontrol/database/Database_Control.java

package je.pense.doro.chartplate.filecontrol.database;

import javax.swing.*;
import je.pense.doro.entry.EntryDir; // This imports the class with the new variable names

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Database_Control extends JFrame {

    private static final Path USER_DOCS_PATH = Paths.get(System.getProperty("user.home"), "Documents");
    private static final Path BACKUP_DB_DIR = USER_DOCS_PATH.resolve("ITTIA_EMR_db");
    private static final Path BACKUP_SUPPORT_DIR = USER_DOCS_PATH.resolve("Support_directory");

    private static final List<String> DB_FILES = List.of(
        "javalabtests.db", "icd11.db", "kcd8db.db",
        "AbbFullDis.db", "LabCodeFullDis.db", "extracteddata.txt"
    );

    public Database_Control() {
        setTitle("Database File Control");
        setSize(400, 120);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        add(createButton("Backup to Documents", e -> performBackup()));
        add(createButton("Restore from Documents", e -> performRestore()));
        add(createButton("Quit", e -> dispose()));

        try {
            Files.createDirectories(BACKUP_DB_DIR);
            Files.createDirectories(BACKUP_SUPPORT_DIR);
        } catch (IOException e) {
            showError("Initialization Error", "Could not create backup directories:\n" + e.getMessage());
        }
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }

    private void performBackup() {
        List<String> issues = new ArrayList<>();
        issues.addAll(copyDbFiles(EntryDir.dbDir, BACKUP_DB_DIR));
        // --- FIX: Changed 'supportDir' to 'SUPPORT_DIR' ---
        issues.addAll(copyDirectoryRecursive(EntryDir.SUPPORT_DIR, BACKUP_SUPPORT_DIR));
        showCompletionMessage("Backup", issues);
    }

    private void performRestore() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "This will overwrite current application files with the backup.\nAre you sure you want to proceed?",
            "Confirm Restore",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        List<String> issues = new ArrayList<>();
        issues.addAll(copyDbFiles(BACKUP_DB_DIR, EntryDir.dbDir));
        // --- FIX: Changed 'supportDir' to 'SUPPORT_DIR' ---
        issues.addAll(copyDirectoryRecursive(BACKUP_SUPPORT_DIR, EntryDir.SUPPORT_DIR));
        showCompletionMessage("Restore", issues);
    }

    private List<String> copyDbFiles(Path sourceDir, Path destDir) {
        List<String> issues = new ArrayList<>();
        try {
            Files.createDirectories(destDir);
        } catch (IOException e) {
            issues.add("Could not create destination DB dir: " + destDir);
            return issues;
        }

        for (String fileName : DB_FILES) {
            Path sourceFile = sourceDir.resolve(fileName);
            Path destFile = destDir.resolve(fileName);
            if (Files.notExists(sourceFile)) {
                issues.add("Missing source file: " + sourceFile.getFileName());
                continue;
            }
            try {
                Files.copy(sourceFile, destFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                issues.add("Failed to copy " + fileName + ": " + e.getMessage());
            }
        }
        return issues;
    }

    private List<String> copyDirectoryRecursive(Path sourceDir, Path destDir) {
        List<String> issues = new ArrayList<>();
        if (Files.notExists(sourceDir)) {
            issues.add("Missing source directory: " + sourceDir);
            return issues;
        }
        try (Stream<Path> stream = Files.walk(sourceDir)) {
            stream.forEach(sourcePath -> {
                try {
                    Path targetPath = destDir.resolve(sourceDir.relativize(sourcePath));
                    if (Files.isDirectory(sourcePath)) {
                        Files.createDirectories(targetPath);
                    } else {
                        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    issues.add("Failed on: " + sourcePath.getFileName() + " -> " + e.getMessage());
                }
            });
        } catch (IOException e) {
            issues.add("Could not walk source directory: " + sourceDir);
        }
        return issues;
    }

    private void showCompletionMessage(String action, List<String> issues) {
        if (issues.isEmpty()) {
            JOptionPane.showMessageDialog(this, action + " completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String issueDetails = String.join("\n- ", issues);
            showError(action + " Issues", "The following issues occurred:\n- " + issueDetails);
        }
    }
    
    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Database_Control().setVisible(true));
    }
}