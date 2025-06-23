package je.pense.doro.support.sqlite3_manager.abbreviation.setfindedit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import je.pense.doro.entry.EntryDir;

public class InsertAbbreviations {
    
    // Define the database URL
    private static final String dbURL = "jdbc:sqlite:" + EntryDir.HOME_DIR + "/chartplate/filecontrol/database/AbbFullDis.db"; // Replace with your actual path
    
    // The list of abbreviations to insert
    private static final String[][] abbreviations = {
        {":aa ", "Astrix 100 mg 1 tab po qd"},
        {":af ", "Atrial Fibrillation"},
        {":afr ", "Atrial Fibrillation with RVR"},
        {":afl ", "Atrial Flutter"},
        {":afp ", "AFP elevation"},
        {":ami ", "Acute Myocardial Infarction"},
        {":amis ", "Acute Myocardial Infarction with stent insertion"},
        {":anx ", "Anxiety disorder"},
        {":ap ", "Angina Pectoris"},
        {":apc ", "APC atrial premature complexes"},
        {":aps ", "Angina Pectoris with stent insertion"},
        {":as ", "Artherosclerosis Carotid artery"},
        {":asa ", "Artherosclerosis Carotid artery and Aorta"},
        {":at ", "Atypical Chest pain"},
        {":bco ", "s/p Breast Cancer Operation"},
        {":bcoc ", "s/p Breast Cancer Operation+ ChemoTx(+)"},
        {":bcor ", "s/p Breast Cancer Operation + RT(+)"},
        {":bcocr ", "s/p Breast Cancer Operation : ChemoTx(+) + RT(+)"},
        {":bcy ", "Breast Cyst"},
        {":bn ", "Breast Nodule"},
        {":bnb ", "Breast Nodule with biopsy"},
        {":bph ", "BPH"},
        {":call ", "The patient received a lab results phone call notification from the doctor's office."},
        {":ca1 ", "CA19-9 elevation"},
        {":cat ", "Cataract(+)"},
        {":cato ", "Cataract operation (+) [ ]"},
        {":c ", "Hypercholesterolemia"},
        {":cf ", "Hypercholwsterolemia F/U"},
        {":cd ", "Colonic diverticulum [  ] test"},
        {":cog ", "Cognitive Disorder"},
        {":con ", "Constipation"},
        {":cov ", "COVID-19 PCR (+)"},
        {":covc ", "s/p COVID-19 PCR (+) without complications [ ]"},
        {":covs ", "s/p COVID-19 PCR (+) with complications [ ]"},
        {":cp ", "Colonic Polyp"},
        {":cpm ", "Colonic Polyps multiple"},
        {":cps ", "Colonic Polyp single"},
        {":d ", "diabetes mellitus"},
        {":da ", "DM with Autonomic Neuropathy"},
        {":dep ", "Depression"},
        {":dia ", "Diarrhea"},
        {":dn ", "DM with Nephropathy"},
        {":dna ", "DM with Autonomic Neuropathy"},
        {":dnm ", "DM with Nephropathy with microalbuminuria"},
        {":dnc ", "DM with Nephropathy with CRF"},
        {":dnp ", "DM with Peripheral Neuropathy"},
        {":do ", "DM without complications"},
        {":dr ", "DM with retinopathy"},
        {":drm ", "DM with Retinopathy : Macular edema"},
        {":drn ", "DM with Retinopathy : Non-proliferative diabetic retinopathy"},
        {":drp ", "DM with Retinopathy : Proliferative diabetic retinopathy"},
        {":df ", "Diabetes Mellitus F/U"},
        {":dys ", "Dysuria and frequency"},
        {":epi ", "Epigastric pain"},
        {":fctg ", "HyperTriGlyceridemia F/U"},
        {":fc ", "Hypercholesterolemia F/U"},
        {":fd ", "Diabetes mellitus F/U"},
        {":fnti ", "Non-Thyroidal Illness F/U"},
        {":fte ", "Hyperthyroidism F/U"},
        {":ftep ", "Hyperthyroidism with Pregnancy [ ] weeks F/U"},
        {":fto ", "Hypothyroidism F/U"},
        {":ftop ", "Hypothyroidism with Pregnancy [ ] weeks F/U"},
        {":ft ", "Hypertension F/U"},
        {":gcon ", "Severe Constipation"},
        {":geg ", "r/o Erosive Gastritis"},
        {":ggil ", "Gilbert's syndrome"},
        {":ggp ", "Gastric Polyp"},
        {":ggr ", "공단검진 결과상담"},
        {":gg ", "공단검진"},
        {":gibs ", "r/o Irritable Bowel Syndrome"},
        {":gla ", "Glaucoma(+)"},
        {":gcag ", "Chronic Atrophic Gastritis"},
        {":gcsg ", "Chronic Superficial Gastritis"},
        {":got ", "GOT/GPT/GGT elevation"},
        {":gre ", "Reflux esophagitis"},
        {":grr ", "GDSRC Result Consultation"},
        {":gr ", "GDS RC"},
        {":gou ", "Gout"},
        {":hav ", "s/p Hepatitis A infection"},
        {":hea ", "Headache"},
        {":hbv ", "HBsAg(+) Carrier"},
        {":hcv ", "Hepatitis C virus (HCV) chronic infection"},
        {":hcvp ", "HCV-Ab(Positive) --> PCR(Negative) confirmed"},
        {":hc ", "Hepatic Cyst"},
        {":hf ", "Fatty Liver"},
        {":hfmi ", "Mild Fatty Liver"},
        {":hfmo ", "Moderate Fatty Liver"},
        {":hfse ", "Severe Fatty Liver"},
        {":hh ", "Hepatic Hemangioma"},
        {":hhn ", "Hepatic higher echoic nodule"},
        {":hiv ", "HIVD : herniated intervertebral disc"},
        {":hn ", "Hepatic Nodule"},
        {":ida ", "Iron Deficiency Anemia"},
        {":ind ", "Epigastric pain and Indigestion"},
        {":ins ", "Insomnia"},
        {":jj ", "Migo JJ"},
        {":leu ", "Leukocytopenia"},
        {":migo ", "DR. Koh Jae Joon"},
        {":nti ", "Non-Thyroidal Illness"},
        {":ntm ", "NTM : Nontuberculous Mycobacterial Pulmonary Disease"},
        {":oa ", "s/p Appendectomy"},
        {":oca ", "Cataract OP(+)"},
        {":oco ", "s/p Colon cancer op(+)"},
        {":ogc ", "s/p Gastric cancer cancer op(+)"},
        {":oh ", "s/p TAH : Total Abdominal Hysterectomy"},
        {":oho ", "s/p TAH with BSO"},
        {":opr ", "Prostate cancer operation(+)"},
        {":os ", "Osteoporosis"},
        {":ospe ", "Osteopenia"},
        {":ot ", "Papillary Thyroid Cancer OP(+) with Hypothyroidism"},
        {":pvc ", "PVC Premature Ventricular Contractions"},
        {":pa ", "s/p Bronchial Asthma"},
        {":pc ", "Chronic Cough"},
        {":pg ", "Gestational Diabetes Mellitus"},
        {":pf ", "Pneumonia"},
        {":pn ", "s/p Pulmonary Nodule"},
        {":pp ", "Pneumonia"},
        {":pt ", "s/p Pulmonary Tuberculosis"},
        {":rgh ", "gross hematuria"},
        {":rih ", "isolated hematuria"},
        {":rip ", "isolated proteinuria"},
        {":rc ", "Renal Cyst"},
        {":rn ", "Renal Nodule"},
        {":rr ", "Other clinic RC and Lab result consultation"},
        {":rs ", "Renal Stone"},
        {":rse ", "Renal Stone s/p ESWL"},
        {":sos ", "Severe Osteoporosis"},
        {":SxTx ", "Symptomatic treatment and supportive care"},
        {":teg ", "Hyperthyroidism : Greaves' disease"},
        {":te ", "Hyperthyroidism"},
        {":tef ", "Hyperthyroidism F/U"},
        {":tf ", "Hypertension F/U"},
        {":t ", "Hypertension"},
        {":tec ", "C/W Subacute Thyroiditis"},
        {":toh ", "Hypothyroidism : Hashimoto's thyroditis"},
        {":tn ", "Thyroid nodule"},
        {":top ", "Hypothyroidism with Pregnancy [     ] weeks"},
        {":to ", "Hypothyroidism"},
        {":tof ", "Hyperthyroidism F/U"},
        {":ts ", "C/W Subacute Thyroiditis"},
        {":tco ", "Papillary Thyroid Cancer OP(+) Hypothyroidism"},
        {":tcor ", "Papillary Thyroid Cancer OP(+) RAI Tx(+) Hypothyroidism"},
        {":uri ", "Upper Respiratory Infection"},
        {":uti ", "Urinary Tract Infection"},
        {":ver ", "Vertigo"},
        {":wei ", "Weight loss [ ] kg"},
        {":weig ", "Weight gain [ ] kg"},
        {":ww ", "with medication."},
        {":wq ", "without medication."}
    };

    public void insertAbbreviations() {
        String sql = "INSERT INTO Abbreviations (abbreviation, full_text) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (String[] entry : abbreviations) {
                pstmt.setString(1, entry[0]);
                pstmt.setString(2, entry[1]);
                pstmt.addBatch(); // Add the statement to the batch
            }

            pstmt.executeBatch(); // Execute all at once
            JOptionPane.showMessageDialog(null, "All abbreviations inserted successfully!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error inserting abbreviations: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
