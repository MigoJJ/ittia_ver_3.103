package je.pense.doro.support.sqlite3_manager.lab.lablong;

import java.sql.*;

import je.pense.doro.entry.EntryDir;

/**
 * A class to append simplified laboratory code data into an existing SQLite database
 * and manage duplicates.
 */
public class AddLabCodeDatabase {

    private static final String DB_URL = "jdbc:sqlite:" + EntryDir.HOME_DIR + "/chartplate/filecontrol/database/LabCodeFullDis.db";

    /**
     * Appends simplified laboratory data to the 'labcodes' table and removes duplicates.
     */
    public static void appendData() {
        String insertSQL = "INSERT INTO labcodes (Category, B_code, Items, unit, comment) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            addLabData(pstmt);
            System.out.println("Data appended successfully.");
        } catch (SQLException e) {
            System.err.println("Error appending data: " + e.getMessage());
        }
        removeDuplicates(); // Remove duplicates after appending data
    }

    /**
     * Adds the simplified laboratory dataset to the database.
     *
     * @param pstmt The PreparedStatement to execute the insertions.
     * @throws SQLException If an error occurs during data insertion.
     */
    private static void addLabData(PreparedStatement pstmt) throws SQLException {
        String[][] labData = {
        		{"Chemistry", "D3230050Z", "Free T4", "", "ECLIA"},
        	    {"Chemistry", "D3230040Z", "T4", "", "ECLIA"},
        	    {"Chemistry", "D3230040Z", "T4, neonatal", "", "FIA"},
        	    {"Chemistry", "D3230010Z", "Free T3", "", "ECLIA"},
        	    {"Chemistry", "D3230020Z", "T3 uptake", "", "ECLIA"},
        	    {"Chemistry", "D3230060Z", "T3", "", "ECLIA"},
        	    {"Chemistry", "", "Reverse T3", "", "EIA"},
        	    {"Chemistry", "D3213020Z", "TBG(Thyroxin binding globulin)", "", "RIA"},
        	    {"Chemistry", "D3240000Z", "Thyroglobulin Ab", "", "ECLIA"},
        	    {"Chemistry", "D4250000Z", "Thyroglobulin Ag", "", "ECLIA"},
        	    {"Chemistry", "D3214010Z", "Thyroid stimulating Ab(TS Ab)", "", "Bio-assay"},

        	    {"Chemistry", "D3050020Z,D3022000Z", "HOMA of insulin resistance", "", "Calculation"},
        	    {"Chemistry", "D3050020Z", "Insulin", "", "ECLIA"},
        	    {"Chemistry", "", "Proinsulin", "", "ELISA"},
        	    {"Chemistry", "D8011000Z", "Insulin Ab", "", "RIA"},
        	    {"Chemistry", "", "Insulin Receptor Ab", "", "RRA"},

        	    {"Chemistry", "D5010010Z", "CTx", "", "ECLIA"},
        	    {"Chemistry", "D5010020Z", "NTx", "", "CIA"},
        	    {"Chemistry", "", "PTH-rp", "", "IRMA"},
        	    {"Chemistry", "D3050010Z", "C-peptide", "", "ECLIA"},
        	    {"Chemistry", "D3050010Z", "C-Peptide (U)", "", "ECLIA"},
        	    {"Chemistry", "", "Procollagen Ⅲ", "", "CLIA"},

        	    {"Chemistry", "D3702030Z", "LH", "", "ECLIA"},
        	    {"Chemistry", "D3702020Z", "FSH", "", "ECLIA"},
        	    {"Chemistry", "D3711030Z", "Estrogen,total", "", "RIA"},
        	    {"Chemistry", "D3410000Z", "Prolactin(PRL)", "", "ECLIA"},
        		
        		    {"Chemistry", "D6212016Z", "Aspergillus Ag", "", "EIA"},
        		    {"Chemistry", "D8120006Z", "Anti MAG Ab", "", "ELISA"},
        		    {"Chemistry", "D5740000Z", "SFLT-1/PLGF", "", "ECLIA"},
        		    {"Chemistry", "D2642056Z", "Fasciola hepatica Ab", "", "ELISA"},
        		    {"Chemistry", "D5896000Z", "UBT test", "", "GC/MS"},
        		    {"Chemistry", "D2642116Z", "Trichinella Ab", "", "EIA"},
        		    {"Chemistry", "D2642046Z", "Echinococcus Ab", "", "ELISA"},
        		    {"Chemistry", "D1780006Z", "LIA-ANA Profile 17s", "", "Line immunoassay"},
        		    {"Chemistry", "D7901010Z", "Anti β2-GPI IgA", "", "MFIA"},
        		    {"Chemistry", "D6212016Z", "Aspergillus Ag(BAL)", "", "EIA"},
        		    {"Chemistry", "D8130000Z", "Anti CCP", "", "Chemiluminoscent Microparticle Immunoassay(CMIA)"},
        		    {"Chemistry", "D7994016Z", "자가면역 간질환 항체검사", "", "Line immunoassay"},
        		    {"Chemistry", "D7840000Z", "항ENA 및 항DNA 항체 선별검사", "", "EIA"},
        		    {"Chemistry", "", "HIT antibody (PF4-heparin complex Ab)", "", "latex 응집법"},
        		    {"Chemistry", "CZ421", "RA Factor IgA", "", "ELISA"},
        		    {"Chemistry", "D7201000Z", "HIV Ab(일반)", "", "ECLIA"},
        		    {"Chemistry", "D5899000Z", "H.pylori Ag", "", "ICA"},
        		    {"Chemistry", "D3030000Z", "1,5-Anhydroglucitol", "", "Colorimetry"},
        		    {"Chemistry", "D7852036Z", "Ribosomal P Ab", "", "FEIA"},
        		    {"Chemistry", "", "Anti mitochondrial M2 Ab", "", "CLEIA"},
        		    {"Chemistry", "D7212000Z", "HIV Ag/Ab(Combo)", "", "ECLIA"},
        		    {"Chemistry", "", "Cardiolipin β2 GP 1 Ab", "", "ELISA"},
        		    {"Chemistry", "D7440010Z", "UniCAP F329(Watermelon)", "", "FEIA"},
        		    {"Chemistry", "D5873086Z", "O.tsutsugamushi Ab(IFA)", "", "IFA"},
        		    {"Chemistry", "", "Tetanus toxoid IgG", "", "Immuno assay"},
        		    {"Chemistry", "", "DBS(Pompe)", "", "LC/MS/MS"},
        		    {"Chemistry", "", "DBS(Fabry)", "", "LC/MS/MS"},
        		    {"Chemistry", "", "Entamoeba histolytica IgG", "", "ELISA"},
        		    {"Chemistry", "D6552010Z", "Hantaan virus Ab(IFA)", "", "IFA"},
        		    {"Chemistry", "D7440010Z", "UniCAP F202(Cashew nut)", "", "FEIA"},
        		    {"Chemistry", "D7440010Z", "UniCAP Mx1(Mold mix)", "", "FEIA"},
        		    {"Chemistry", "D7440010Z", "UniCAP Ex1(Animal epidermals & Pro.mix)", "", "FEIA"},
        		    {"Chemistry", "D7440010Z", "UniCAP F48(Onion)", "", "FEIA"},
        		    {"Chemistry", "", "만성 음식물 알러지 222종", "", "Microarray-based ELISA"},
        		    {"Chemistry", "", "STN(Sialyl Tn antigen)", "", "RIA"},
        		    {"Chemistry", "", "IgG4 Food Antibodies(90종)", "", "ELISA"},
        		    {"Chemistry", "CZ422외", "Paraneoplastic auto Ab", "", "Immunoblot Assay"},
        		    {"Chemistry", "D7510000Z", "Tryptase", "", "FEIA"},
        		    {"Chemistry", "D8101006Z", "Anti Aquaporin 4 Ab", "", "IFA"},
        		    {"Chemistry", "D7440010Z", "UniCAP F10(Sesame seed)", "", "FEIA"},
        		    {"Chemistry", "D5872096Z", "Leptospira Ab(정량)", "", "MAT"},
        		    {"Chemistry", "D5873076Z", "R.typhi Ab(정량)", "", "Fluorescent antibody test"},
        		    {"Chemistry", "D7440010Z", "UniCAP F287(Red kidney bean)", "", "FEIA"},
        		    {"Chemistry", "D4902010Z", "1,25-(OH)2 Vitamin D", "", "CIA"},
        		    {"Chemistry", "D0133000Z", "Calprotectin", "", "FEIA"},
        		    {"Chemistry", "D4470000Z", "Chromogranin A", "", "ELISA"},
        		    {"Chemistry", "D7440010Z", "UniCAP F260(Broccoli)", "", "FEIA"},
        		    {"Chemistry", "D7440010Z", "UniCAP F242(Cherry)", "", "FEIA"},
        		    {"Chemistry", "CZ261", "Anti GM1 IgG isotype", "", "Line immunoassay"},
        		    {"Chemistry", "CZ262", "Anti GM1 IgM isotype", "", "Line immunoassay"},
        		    {"Chemistry", "D4111000Z", "AFP-L3(%)", "", "LBA+CZE"},
        		    {"Chemistry", "D7490000Z", "Interleukin 6(IL-6)", "", "ECLIA"},
        		    {"Chemistry", "CZ261", "Ganglioside Ab IgG Panel", "", "Line immunoassay"},
        		    {"Chemistry", "CZ262,CZ425", "Ganglioside Ab IgM Panel", "", "Line immunoassay"},
        		    {"Chemistry", "D8110000Z", "Anti PLA2R IgG", "", "ELISA"},
        		    {"Chemistry", "D6204016Z", "(1-3)-β-D-Glucan", "", "Colorimetry"},
        		    {"Chemistry", "D4050000Z", "Soluble ST2", "", "ELISA"},
        		    {"Chemistry", "CZ117", "AlzOn(알츠하이머병 위험도검사)", "", "CLIA"},
        		    {"Chemistry", "D4300010외", "Prostate health index (phi)", "", "CLIA"}
        		
        		};

        for (String[] row : labData) {
            addRow(pstmt, row[0], row[1], row[2], row[3], row[4]);
        }
    }

    /**
     * Inserts a single row into the 'labcodes' table.
     *
     * @param pstmt    The PreparedStatement to execute the insertion.
     * @param category The category of the lab test (e.g., "Chemistry").
     * @param bCode    The insurance code (e.g., "D2280004Z").
     * @param items    The specific test item (e.g., "Creatinine(Urine)").
     * @param unit     The unit of measurement (e.g., "mg/dL").
     * @param comment  A placeholder comment (e.g., ".").
     * @throws SQLException If an error occurs during insertion.
     */
    private static void addRow(PreparedStatement pstmt, String category, String bCode, String items, String unit, String comment) throws SQLException {
        pstmt.setString(1, category);
        pstmt.setString(2, bCode);
        pstmt.setString(3, items);
        pstmt.setString(4, unit);
        pstmt.setString(5, comment);
        pstmt.executeUpdate();
    }

    /**
     * Removes duplicate entries from the 'labcodes' table based on the 'Items' column.
     * Keeps the first occurrence of each unique 'Items' value.
     */
    public static void removeDuplicates() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Create a temporary table for unique records
            stmt.execute("CREATE TABLE temp_labcodes (" +
                    "Category TEXT, " +
                    "B_code TEXT, " +
                    "Items TEXT, " +
                    "unit TEXT, " +
                    "comment TEXT" +
                    ");");

            // Insert unique records into the temporary table
            stmt.execute("INSERT INTO temp_labcodes (Category, B_code, Items, unit, comment) " +
                    "SELECT Category, B_code, Items, unit, comment " +
                    "FROM labcodes " +
                    "WHERE rowid IN (" +
                    "    SELECT MIN(rowid) " +
                    "    FROM labcodes " +
                    "    GROUP BY Items" +
                    ");");

            // Drop the original table and rename the temporary table
            stmt.execute("DROP TABLE IF EXISTS labcodes;");
            stmt.execute("ALTER TABLE temp_labcodes RENAME TO labcodes;");

            System.out.println("Duplicates removed successfully.");
        } catch (SQLException e) {
            System.err.println("Error removing duplicates: " + e.getMessage());
        }
    }

    /**
     * Main method to append the simplified laboratory data to the database and remove duplicates.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        appendData();
    }
}