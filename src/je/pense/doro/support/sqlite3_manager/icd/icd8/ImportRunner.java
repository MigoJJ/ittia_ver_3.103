package je.pense.doro.support.sqlite3_manager.icd.icd8;

import je.pense.doro.entry.EntryDir;

public class ImportRunner {
    public static void main(String[] args) {
        DatabaseManager dbManager = new DatabaseManager();
        CSVImporter importer = new CSVImporter(dbManager);
        String baseDir = EntryDir.HOME_DIR + "/soap/assessment/kcd8";
        importer.importCSV(baseDir + "/KCD-8DB.csv");
    }
}