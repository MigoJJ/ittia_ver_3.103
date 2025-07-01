package je.pense.doro.support.sqlite3_manager.icd.icd8;

import java.nio.file.*;	
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVImporter {
    private DatabaseManager dbManager;

    public CSVImporter(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void importCSV(String filePath) {
        List<String> invalidRows = new ArrayList<>();
        int lineCount = 0;

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                lineCount++;
                // Skip header row
                if (lineCount == 1 && line.toLowerCase().contains("code")) {
                    continue;
                }

                // Split the line, handling commas within quoted fields
                String[] values = parseCSVLine(line);

                // Expecting at least 3 columns: Code, Kdiseasename, Ediseasename
                if (values.length >= 3) {
                    String code = values[0].trim();
                    String kDiseaseName = values[1].trim();
                    String eDiseaseName = values[2].trim();

                    // Insert data into the database
                    dbManager.insertData(code, kDiseaseName, eDiseaseName);
                } else {
                    invalidRows.add("Invalid row at line " + lineCount + ": " + line);
                }
            }

            // Report results
            System.out.println("CSV Import completed successfully. Processed " + (lineCount - 1) + " rows.");
            if (!invalidRows.isEmpty()) {
                System.err.println("Found " + invalidRows.size() + " invalid rows:");
                invalidRows.forEach(System.err::println);
            }

        } catch (IOException e) {
            System.err.println("CSV Import error: " + e.getMessage());
        }
    }

    // Simple CSV parser to handle commas within quoted fields
    private String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(field.toString());
                field = new StringBuilder();
            } else {
                field.append(c);
            }
        }
        // Add the last field
        result.add(field.toString());

        return result.toArray(new String[0]);
    }
}