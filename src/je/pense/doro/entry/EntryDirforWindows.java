package je.pense.doro.entry;

public class EntryDirforWindows {
    public static String currentDir = System.getProperty("user.dir");
    // Windows path with backslashes
    public static String homeDir = currentDir + "\\src\\je\\panse\\doro";
    // Windows path with backslashes
    public static String backupDir = homeDir + "\\tripikata\\rescue";

    public static void main(String[] args) {
        // Get the path to the current user's directory
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current user's directory: " + currentDir);
    }
}
