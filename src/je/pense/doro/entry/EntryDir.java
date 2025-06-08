package je.pense.doro.entry;

import java.io.File;								
import java.util.Arrays;

public class EntryDir {
    // Current directory of the user
    public static final String currentDir = System.getProperty("user.dir");
    public static final String homeDir;
    public static final String backupDir;

    static {
        // Determine environment (default to 'dev' if not set)
        String env = System.getProperty("app.env", "dev");
        String[] pathParts = {"je", "pense", "doro"};

        // Construct base path depending on environment
        String basePath = "prod".equals(env) ? currentDir 
                                             : currentDir + File.separator + "src";
//                                           : currentDir + File.separator;
        
        // Build paths for home and backup directories
        homeDir = buildPath(basePath, pathParts);
        backupDir = buildPath(homeDir, "tripikata", "rescue");

        // Ensure directories exist
        Arrays.asList(homeDir, backupDir).forEach(EntryDir::createDirectoryIfNotExists);
    }

    /**
     * Main method to test directory creation.
     */
    public static void main(String[] args) {
        System.out.println("Current user's directory: " + currentDir);
        System.out.println("Home Directory: " + homeDir);
        System.out.println("Backup Directory: " + backupDir);
    }

    /**
     * Builds a file path from base and additional parts.
     * 
     * @param base  The base path
     * @param parts Additional path components
     * @return The constructed file path
     */
    private static String buildPath(String base, String... parts) {
        File path = new File(base);
        for (String part : parts) {
            path = new File(path, part);
        }
        return path.getAbsolutePath();
    }

    /**
     * Creates a directory if it does not already exist.
     * 
     * @param directoryPath The path of the directory to create
     */
    public static void createDirectoryIfNotExists(String directoryPath) {
        File dir = new File(directoryPath);
        if (!dir.exists()) {
            boolean wasSuccessful = dir.mkdirs();
            if (wasSuccessful) {
                System.out.println("✅ Successfully created directory: " + directoryPath);
            } else {
                System.err.println("❌ Failed to create directory: " + directoryPath);
            }
        } else {
            System.out.println("ℹ️ Directory already exists: " + directoryPath);
        }
    }

    /**
     * Returns the full path for a given file under the Thyroid folder.
     * 
     * @param fileName The name of the file
     * @return The full path to the file
     */
    public static String getThyroidFilePath(String fileName) {
        return buildPath(homeDir, "support", "EMR_support_Folder", "Thyroid", fileName);
    }
}
