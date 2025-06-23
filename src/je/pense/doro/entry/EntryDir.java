// File: je/pense/doro/entry/EntryDir.java

package je.pense.doro.entry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages and provides access to essential application directories using the modern Path API.
 * It supports different base paths for 'dev' and 'prod' environments
 * and ensures that all required directories exist on startup.
 */
public class EntryDir {
    private static final Logger logger = LoggerFactory.getLogger(EntryDir.class);

    // --- Environment Configuration ---
    // Determines the runtime environment. Default is 'dev'.
    // To run in production mode, start the JVM with: -Dapp.env=prod
    private static final String ENV = System.getProperty("app.env", "dev");
    private static final boolean IS_PROD = "prod".equals(ENV);

    // --- CRITICAL: All directory fields are now 'Path' objects ---
    public static final Path CURRENT_DIR = Paths.get(System.getProperty("user.dir"));
    public static final Path HOME_DIR;
    public static final Path BACKUP_DIR;
    public static final Path SUPPORT_DIR;
    public static final Path dbDir; // Kept original name for direct compatibility

    static {
        // Determine the base path based on the environment
        // In 'prod' mode, paths are relative to the execution directory.
        // In 'dev' mode, paths are relative to the 'src' folder for IDE compatibility.
        Path basePath = IS_PROD ? CURRENT_DIR : CURRENT_DIR.resolve("src");

        // Define main directory paths using Path.resolve() for clarity and type safety
        HOME_DIR = basePath.resolve("je/pense/doro");
        BACKUP_DIR = HOME_DIR.resolve("tripikata/rescue");
        SUPPORT_DIR = HOME_DIR.resolve("support/EMR_support_Folder");
        
        // This is the path required by Database_Control.java
        dbDir = HOME_DIR.resolve("chartplate/filecontrol/database");

        // Ensure all application directories exist at startup
        // Using a Stream is a clean way to handle a list of paths.
        Stream.of(HOME_DIR, BACKUP_DIR, SUPPORT_DIR, dbDir)
              .forEach(EntryDir::ensureDirectoryExists);
    }

    /**
     * Ensures a directory exists at the given path, creating it and any
     * parent directories if necessary.
     *
     * @param directoryPath The Path of the directory to check and create.
     */
    public static void ensureDirectoryExists(Path directoryPath) {
        if (Files.notExists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
                logger.info("Created directory: {}", directoryPath);
            } catch (IOException e) {
                logger.error("Failed to create directory {}: {}", directoryPath, e.getMessage(), e);
            }
        }
    }
    
    // Overloaded method for backward compatibility if other parts of your code still use Strings.
    public static void ensureDirectoryExists(String directoryPath) {
        ensureDirectoryExists(Paths.get(directoryPath));
    }

    /**
     * Gets the full path for a file within the "Thyroid" support sub-directory.
     *
     * @param fileName The name of the file.
     * @return The full, platform-independent Path to the file.
     */
    public static Path getThyroidFilePath(String fileName) {
        // Now returns a Path object for consistency
        return SUPPORT_DIR.resolve("Thyroid").resolve(fileName);
    }

    /**
     * Main method for diagnostic purposes, printing configured paths.
     */
    public static void main(String[] args) {
        System.out.println("--- EntryDir Configuration ---");
        System.out.println("Environment: " + ENV + (IS_PROD ? " (Production)" : " (Development)"));
        System.out.println("Current Dir: " + CURRENT_DIR);
        System.out.println("Home Dir:    " + HOME_DIR);
        System.out.println("DB Dir:      " + dbDir);
        System.out.println("Backup Dir:  " + BACKUP_DIR);
        System.out.println("Support Dir: " + SUPPORT_DIR);
        System.out.println("----------------------------");
        System.out.println("Example Thyroid File Path: " + getThyroidFilePath("sample.txt"));
    }
}