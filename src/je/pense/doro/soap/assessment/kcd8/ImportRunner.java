package je.pense.doro.soap.assessment.kcd8;

import je.pense.doro.entry.EntryDir;

public class ImportRunner {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();
        CSVImporter importer = new CSVImporter(dbManager);
        String baseDir = EntryDir.homeDir + "/soap/assessment/kcd8";
        importer.importCSV(baseDir + "/KCD-8DB.csv");
    }
}