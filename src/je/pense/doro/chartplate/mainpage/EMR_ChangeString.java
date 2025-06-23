package je.pense.doro.chartplate.mainpage;

import java.io.BufferedReader;	
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import je.pense.doro.chartplate.filecontrol.datetime.Date_current;
import je.pense.doro.entry.EntryDir;
import je.pense.doro.soap.cc.EMR_ChangeStringCC;

public class EMR_ChangeString {

    private static final Map<String, String> replacements = new HashMap<>();

    static {
        String filePath = EntryDir.HOME_DIR + "/chartplate/filecontrol/database/extracteddata.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("replacements.put(")) {
                    String[] parts = line.split("\"");
                    if (parts.length >= 4) {
                        String key = parts[1];
                        String value = parts[3];
                        replacements.put(key, value);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading abbreviations: " + e.getMessage());
        }
    }

    public static String EMR_ChangeString(String lines) {
        // Handle special abbreviations
        if (lines.contains(":(")) {
            lines = EMR_ChangeStringCC.EMR_ChangeString_abr(lines);
        } else if (lines.contains(":>")) {
            lines = EMR_ChangeStringCC.EMR_ChangeString_Px(lines);
        }

        // Replace current date placeholder
        String cdate = Date_current.main("d");
        lines = lines.replace(":cd ", cdate);

        // Perform bulk replacements from the map
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            lines = lines.replace(entry.getKey(), entry.getValue());
        }

        return "  " + lines;
    }
}