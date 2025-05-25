package je.pense.doro.fourgate.thyroid.pregnancy;

import javax.swing.*;		
import java.awt.*;

public class EMR_Thyroid_Preg_te extends JFrame {
    private String[] sections = {
        "Personal Medical History",
        "Family Medical History",
        "Current Pregnancy Details",
        "Symptoms",
        "Medications",
        "Previous Pregnancy History",
        "Laboratory Tests",
        "Other Medical Conditions"
    };

    private String[][] sectionItems = {
        {
            "Previous diagnosis of hyperthyroidism",
            "Any previous treatments for hyperthyroidism",
            "Any history of thyroid surgery or radioactive iodine treatment"
        },
        {
            "Any family history of thyroid disorders or autoimmune conditions",
            "Any family history of hyperthyroidism during pregnancy"
        },
        // Add empty arrays for the remaining sections
        {
            "Gestational age (number of weeks pregnant)",
            "Any complications or high-risk factors associated with the pregnancy"
        },
        {
            "Weight loss or difficulty gaining weight during pregnancy",
            "Rapid or irregular heartbeat",
            "Tremors or excessive shaking",
            "Increased sensitivity to heat",
            "Fatigue or weakness",
            "Sleep disturbances",
            "Changes in bowel movements (increased frequency or diarrhea)",
            "Emotional or mood changes"
        },
        {
            "Any current medications taken for hyperthyroidism",
            "Any changes in medication dosage during pregnancy"
        },
        {
            "Any history of hyperthyroidism during previous pregnancies",
            "Any complications or adverse outcomes associated with hyperthyroidism during pregnancy"
        },
        {
            "Results of thyroid function tests (TSH, T3, T4) during pregnancy",
            "Results of thyroid antibody tests (if applicable)"
        },
        {
            "Any other medical conditions or chronic illnesses",
            "Any autoimmune conditions or thyroid-related disorders",
            "     Autoimmune thyroiditis",
            "     Graves' disease",
            "     Hashimoto's thyroiditis",
            "     Thyroid nodules",
            "     Thyroid cancer",
            "     Thyroid storm",
            "     Thyroid hormone resistance",
            "     Thyroid eye disease",
            "     Subacute thyroiditis",
            "     Postpartum thyroiditis"
        }
    };

    public EMR_Thyroid_Preg_te() {
        setTitle("Hyperthyroidism with pregnancy Medical History");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2)); // 4 rows, 2 columns

        for (int i = 0; i < sections.length; i++) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Set vertical layout
            panel.setBorder(BorderFactory.createTitledBorder(sections[i]));

            for (String item : sectionItems[i]) {
                JCheckBox checkBox = new JCheckBox(item);
                panel.add(checkBox);
            }

            add(panel);
        }

        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EMR_Thyroid_Preg_te();
            }
        });
    }
}
