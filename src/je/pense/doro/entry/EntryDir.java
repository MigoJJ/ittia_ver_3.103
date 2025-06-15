package je.pense.doro.entry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages and provides access to essential application directories.
 * It supports different base paths for 'dev' and 'prod' environments
 * and ensures that all required directories exist on startup.
 */
public class EntryDir {
    private static final Logger logger = LoggerFactory.getLogger(EntryDir.class);

    // Environment configuration
    private static final String ENV = System.getProperty("app.env", "dev");
    private static final boolean IS_PROD = "prod".equals(ENV);

    // Core application directories
    public static final String currentDir = System.getProperty("user.dir");
    public static final String homeDir;
    public static final String backupDir;
    public static final String supportDir;
    public static final String dbDir;

    static {
        // Determine the base path based on the environment
        String basePath = IS_PROD ? currentDir : Paths.get(currentDir, "src").toString();

        // Define main directory paths
        homeDir = buildPath(basePath, "je", "pense", "doro");
        backupDir = buildPath(homeDir, "tripikata", "rescue");
        supportDir = buildPath(homeDir, "support", "EMR_support_Folder");
        dbDir = buildPath(homeDir, "chartplate", "filecontrol", "database");

        // Ensure all application directories exist at startup
        Arrays.asList(homeDir, backupDir, supportDir, dbDir)
              .forEach(EntryDir::ensureDirectoryExists);
    }

    /**
     * Builds a platform-independent path from a base and subsequent parts.
     *
     * @param base  The starting path.
     * @param parts The parts to append to the base path.
     * @return The combined path as a string.
     */
    private static String buildPath(String base, String... parts) {
        return Paths.get(base, parts).toString();
    }

    /**
     * Ensures a directory exists at the given path, creating it if necessary.
     *
     * @param directoryPath The path of the directory to check and create.
     */
    public static void ensureDirectoryExists(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
                logger.info("Created directory: {}", directoryPath);
            } catch (IOException e) {
                logger.error("Failed to create directory {}: {}", directoryPath, e.getMessage(), e);
            }
        }
    }

    /**
     * Gets the full path for a file within the "Thyroid" support sub-directory.
     *
     * @param fileName The name of the file.
     * @return The full, platform-independent path to the file.
     */
    public static String getThyroidFilePath(String fileName) {
        return buildPath(supportDir, "Thyroid", fileName);
    }

    /**
     * Main method for diagnostic purposes, printing configured paths.
     */
    public static void main(String[] args) {
        System.out.println("--- EntryDir Configuration ---");
        System.out.println("Environment: " + ENV);
        System.out.println("Current Dir: " + currentDir);
        System.out.println("Home Dir:    " + homeDir);
        System.out.println("DB Dir:      " + dbDir);
        System.out.println("Backup Dir:  " + backupDir);
        System.out.println("Support Dir: " + supportDir);
        System.out.println("----------------------------");
    }
}