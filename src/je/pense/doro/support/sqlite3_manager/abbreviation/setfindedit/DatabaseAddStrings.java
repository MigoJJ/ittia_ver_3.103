package je.pense.doro.support.sqlite3_manager.abbreviation.setfindedit;

import java.sql.Connection;	
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import je.pense.doro.entry.EntryDir;

public class DatabaseAddStrings {
    private static String dbURL = "jdbc:sqlite:" + EntryDir.HOME_DIR + "/chartplate/filecontrol/database/AbbFullDis.db";

    public static void main(String[] args) {
        // Ensure the table exists
        createTable();

        // Define the data to be inserted
        String[][] data = {
            {":cd", "cdate"},
            {":call", "The patient received a lab results phone call notification from the doctor's office."},
            {":d", "diabetes mellitus"},
            {":dr", "DM with retinopathy"},
            {":dn", "DM with Nephropathy"},
            {":dnp", "DM with Peripheral Neuropathy"},
            {":dna", "DM with Autonomic Neuropathy"},
            {":dp", "Prediabetes"},
            {":dg", "Gestational Diabetes Mellitus"},
            {":c", "Hypercholesterolemia F/U"},
            {":fd", "Diabetes mellitus F/U"},
            {":ft", "Hypertension F/U"},
            {":fc", "Hypercholesterolemia F/U"},
            {":fctg", "HyperTriGlyceridemia F/U"},
            {":fte", "Hyperthyroidism F/U"},
            {":fto", "Hypothyroidism F/U"},
            {":fnti", "Non-Thyroidal Illness F/U"},
            {":ftep", "Hyperthyroidism with Pregnancy [ ] weeks F/U"},
            {":ftop", "Hypothyroidism with Pregnancy [ ] weeks F/U"},
            {":do", "DM without complications"},
            {":drn", "DM with Retinopathy \n\t: Non-proliferative diabetic retinopathy"},
            {":drp", "DM with Retinopathy \n\t: Proliferative diabetic retinopathy"},
            {":drm", "DM with Retinopathy \n\t: Macular edema"},
            {":dnm", "DM with Nephropathy with microalbuminuria"},
            {":dnc", "DM with Nephropathy with CRF"},
            {":da", "DM with Autonomic Neuropathy"},
            {":pd", "Prediabetes"},
            {":pg", "Gestational Diabetes Mellitus"},
            {":t", "Hypertension"},
            {":ctg", "HyperTriGlyceridemia"},
            {":te", "Hyperthyroidism : Graves' disease"},
            {":to", "Hypothyroidism : Hashimoto's thyroiditis"},
            {":ts", "Subacute Thyroiditis"},
            {":tt", "c/w Chronic Thyroiditis on USG"},
            {":tn", "Thyroid nodule"},
            {":tc", "Thyroid cyst"},
            {":tsg", "Simple Goiter"},
            {":at", "Atypical Chest pain"},
            {":ap", "Angina Pectoris"},
            {":aps", "Angina Pectoris with stent insertion"},
            {":omi", "Old Myocardial Infarction"},
            {":ami", "Acute Myocardial Infarction"},
            {":amis", "Acute Myocardial Infarction with stent insertion"},
            {":as", "Artherosclerosis Carotid artery"},
            {":asa", "Artherosclerosis Carotid artery and Aorta"},
            {":af", "Atrial Fibrillation"},
            {":afr", "Atrial Fibrillation with RVR"},
            {":afl", "Atrial Flutter"},
            {":pvc", "PVC Premature Ventricular Contractions"},
            {":apc", "APC atrial premature complexes"},
            {":gre", "Reflux esophagitis"},
            {":gcag", "Chronic Atrophic Gastritis"},
            {":gcsg", "Chronic Superficial Gastritis"},
            {":geg", "r/o Erosive Gastritis"},
            {":gibs", "r/o Irritable Bowel Syndrome"},
            {":ggil", "Gilbert's syndrome"},
            {":gcon", "Severe Constipation"},
            {":ctg", "HyperTriGlyceridemia"},
            {":te", "Hyperthyroidism : Graves' disease"},
            {":to", "Hypothyroidism : Hashimoto's thyroiditis"},
            {":ts", "Subacute Thyroiditis"},
            {":nti", "Non-Thyroidal Illness"},
            {":tep", "Hyperthyroidism with Pregnancy [ ] weeks"},
            {":top", "Hypothyroidism with Pregnancy [ ] weeks"},
            {":tco", "Papillary Thyroid Cancer OP(+)\n\tHypothyroidism"},
            {":tcor", "Papillary Thyroid Cancer OP(+) RAI Tx(+)\n\tHypothyroidism"},
            {":sos", "Severe Osteoporosis"},
            {":os", "Osteoporosis"},
            {":ospe", "Osteopenia"},
            {":cp", "Colonic Polyp"},
            {":cpm", "Colonic Polyps multiple"},
            {":cps", "Colonic Polyp single"},
            {":cd", "Colonic diverticulum"},
            {":gp", "GB polyp"},
            {":gs", "GB stone"},
            {":ggp", "Gastric Polyp"},
            {":oc", "s/p Cholecystectomy d/t GB stone"},
            {":oa", "s/p Appendectomy"},
            {":occ", "s/p Colon cancer op(+)"},
            {":ogc", "s/p Gastric cancer cancer op(+)"},
            {":oh", "s/p TAH : Total Abdominal Hysterectomy"},
            {":oho", "s/p TAH with BSO"},
            {":bph", "BPH"},
            {":op", "Prostate cancer operation(+)"},
            {":ob", "s/p Breast Cancer Operation"},
            {":ot", "Papillary Thyroid Cancer OP(+)\n\twith Hypothyroidism"},
            {":oca", "Cataract OP(+)"},
            {":hav", "s/p Hepatitis A infection"},
            {":hbv", "HBsAg(+) Carrier"},
            {":hcv", "Hepatitis C virus (HCV) chronic infection"},
            {":hcvp", "HCV-Ab(Positive) --> PCR(Negative) confirmed"},
            {":hh", "Hepatic Hemangioma"},
            {":hc", "Hepatic Cyst"},
            {":hn", "Hepatic Nodule"},
            {":hhn", "Hepatic higher echoic nodule"},
            {":hf", "Fatty Liver"},
            {":hfmi", "Mild Fatty Liver"},
            {":hfmo", "Moderate Fatty Liver"},
            {":hfse", "Severe Fatty Liver"},
            {":rc", "Renal Cyst"},
            {":rs", "Renal Stone"},
            {":rse", "Renal Stone s/p ESWL"},
            {":rn", "Renal Nodule"},
            {":rih", "isolated hematuria"},
            {":rgh", "gross hematuria"},
            {":rip", "isolated proteinuria"},
            {":bc", "Breast Cyst"},
            {":bn", "Breast Nodule"},
            {":bnb", "Breast Nodule with biopsy"},
            {":bco", "s/p Breast Cancer Operation"},
            {":bcoc", "s/p Breast Cancer Operation+ ChemoTx(+)"},
            {":bcor", "s/p Breast Cancer Operation + RT(+)"},
            {":bcocr", "s/p Breast Cancer Operation \n\t: ChemoTx(+) + RT(+)"},
            {":ins", "Insomnia"},
            {":epi", "Epigastric pain"},
            {":dys", "Dysuria and frequency"},
            {":ind", "Epigastric pain and Indigestion"},
            {":dir", "Diarrhea"},
            {":con", "Constipation"},
            {":cov", "COVID-19 PCR (+)"},
            {":covc", "s/p COVID-19 PCR (+) without complications [ ]"},
            {":covs", "s/p COVID-19 PCR (+) with complications [ ]"},
            {":ver", "Vertigo"},
            {":hea", "Headache"},
            {":wei", "Weight loss [ ] kg"},
            {":weig", "Weight gain [ ] kg"},
            {":eas", "Easy fatigue"},
            {":obe", "Obesity"},
            {":obec", "Central Obesity"},
            {":gla", "Glaucoma(+)"},
            {":cat", "Cataract(+)"},
            {":cato", "Cataract operation (+) [ ]"},
            {":ida", "Iron Deficiency Anemia"},
            {":leu", "Leukocytopenia"},
            {":thr", "Thrombocytopenia"},
            {":got", "GOT/GPT/GGT elevation"},
            {":afp", "AFP elevation"},
            {":ca1", "CA19-9 elevation"},
            {":her", "Herpes Zoster"},
            {":uti", "Urinary Tract Infection"},
            {":uri", "Upper Respiratory Infection"},
            {":gou", "Gout"},
            {":dis", "HIVD : herniated intervertebral disc"},
            {":dep", "Depression"},
            {":anx", "Anxiety disorder"},
            {":cog", "Cognitive Disorder"},
            {":pa", "s/p Bronchial Asthma"},
            {":pc", "Chronic Cough"},
            {":pp", "Pneumonia"},
            {":pn", "s/p Pulmonary Nodule"},
            {":pt", "s/p Pulmonary Tuberculosis"},
            {":ntm", "NTM : Nontuberculous Mycobacterial Pulmonary Disease"},
            {":gr", "GDS RC"},
            {":grr", "GDSRC Result Consultation"},
            {":gg", "공단검진"},
            {":ggr", "공단검진 결과상담"},
            {":rr", "Other clinic RC and Lab result consultation"},
            {":SxTx", "Symptomatic treatment and supportive care"}
            // Add more entries as needed
        };

        // Insert data into the database
        insertData(data);
    }

    private static void createTable() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS abbreviations ("
                              + "abbreviation TEXT PRIMARY KEY, "
                              + "full_text TEXT);";

        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void insertData(String[][] data) {
        String sql = "INSERT INTO abbreviations (abbreviation, full_text) VALUES (?, ?)"
                   + "ON CONFLICT(abbreviation) DO UPDATE SET full_text=excluded.full_text";

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (String[] entry : data) {
                pstmt.setString(1, entry[0]);
                pstmt.setString(2, entry[1]);
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("Data inserted/updated successfully");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
