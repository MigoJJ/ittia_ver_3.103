package je.pense.doro.chartplate.filecontrol.database;

import javax.swing.*;

import je.pense.doro.entry.EntryDir;

import java.awt.*;
import java.nio.file.*;
import java.util.stream.Stream;
import java.io.IOException;

public class Database_Control extends JFrame {
    private static final String USER_DOCS = Paths.get(System.getProperty("user.home"), "문서").toString();
    private static final String BACKUP_DB = Paths.get(USER_DOCS, "ITTIA_EMR_db").toString();
    private static final String BACKUP_SUPPORT = Paths.get(USER_DOCS, "Support_directory").toString();
    private static final String[] DB_FILES = {
        "javalabtests.db", "icd11.db", "kcd8db.db", "kcd8db_short.db",
        "AbbFullDis.db", "LabCodeFullDis.db", "extracteddata.txt"
    };

    public Database_Control() {
        setTitle("Database File Control");
        setSize(350, 120);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        add(makeButton("Backup to Documents", e -> copyFiles(EntryDir.dbDir, BACKUP_DB, EntryDir.supportDir, BACKUP_SUPPORT, "Backup")));
        add(makeButton("Restore from Documents", e -> {
            int c = JOptionPane.showConfirmDialog(this, "Restore will overwrite application files.\nProceed?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION)
                copyFiles(BACKUP_DB, EntryDir.dbDir, BACKUP_SUPPORT, EntryDir.supportDir, "Restore");
        }));
        add(makeButton("Quit", e -> dispose()));

        EntryDir.ensureDirectoryExists(BACKUP_DB);
        EntryDir.ensureDirectoryExists(BACKUP_SUPPORT);
    }

    private JButton makeButton(String text, java.awt.event.ActionListener a) {
        JButton b = new JButton(text);
        b.addActionListener(a);
        return b;
    }

    private void copyFiles(String dbSrc, String dbDst, String supSrc, String supDst, String action) {
        StringBuilder err = new StringBuilder();
        try {
            Files.createDirectories(Paths.get(dbDst));
            for (String f : DB_FILES) {
                Path src = Paths.get(dbSrc, f), dst = Paths.get(dbDst, f);
                if (Files.exists(src))
                    Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
                else
                    err.append("Missing: ").append(f).append("\n");
            }
            copyDir(Paths.get(supSrc), Paths.get(supDst), err);
        } catch (Exception ex) {
            err.append("Error: ").append(ex.getMessage());
        }
        JOptionPane.showMessageDialog(this, err.length() == 0 ? action + " Success!" : action + " Issues:\n" + err);
    }

    private void copyDir(Path src, Path dst, StringBuilder err) {
        if (!Files.exists(src)) { err.append("Missing dir: ").append(src).append("\n"); return; }
        try (Stream<Path> s = Files.walk(src)) {
            s.forEach(p -> {
                try {
                    Path t = dst.resolve(src.relativize(p));
                    if (Files.isDirectory(p)) Files.createDirectories(t);
                    else Files.copy(p, t, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) { err.append("Dir copy fail: ").append(p).append("\n"); }
            });
        } catch (IOException e) { err.append("Dir walk fail: ").append(src).append("\n"); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Database_Control().setVisible(true));
    }
}
