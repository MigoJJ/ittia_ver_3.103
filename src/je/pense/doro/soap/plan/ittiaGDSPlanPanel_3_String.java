package je.pense.doro.soap.plan;

public class ittiaGDSPlanPanel_3_String {
	public static String[] getCheckboxLabels() {
        return new String[]{
			"...Next Lab F/U with NPO",
			"...Coservative symptomatic treatment",
			"...F/U without medications",
			"...Continie current medications or supplements",
			"...D/C all kinds of dietary[food, health, nutritional] supplements",
			"...The patient Refused dose-adjustment", 
			"...",
			"...Ophthalmologist consultation[+]", 
			"...Plan to review of other clinic RC result",
			"...",
			"...History of surgeries or hospitalizations",
			"...Check for Family medical history",
    		"...to refer patients to receive additional health care services.",
    		"...",
    		"...Gastroenterology consult in GDS clinic",
    		"...Pulmonolgy consult in GDS clinic",
			"2", "3", "2" };

	}
    public static String[] getboxs(int i) {
        String[] comboboxesLabels = {};

        switch (i) {
            case 1:
                comboboxesLabels = new String[]{"Consutation---------------------", 
                		"to refer patients to receive additional health care services.",
                		"Gastroenterology consult in GDS clinic",
                		"Pulmonolgy consult in GDS clinic",
                };
                break;
            case 2:
                comboboxesLabels = new String[]{"2", "2", "23", "2", "23", "24"};
                break;
            case 3:
                comboboxesLabels = new String[]{"1", "2", "3", "2", "3", "4"};
                break;
            case 4:
                comboboxesLabels = new String[]{"Transfer to University Hospital----------", 
                		"transfer to Severence Univ. Hospital",
                		"transfer to AMC Hospital",
                		"transfer to SMC Univ. Hospital",
                		"transfer to SNUH Seoul Hospital",
                		"transfer to SNUH BUNDang Hospital",
                		"transfer to catholic Univ. Hospital",
                		"consult to Emergency Room"
                };
                break;    
            case 5:
                comboboxesLabels = new String[]{"Consutation---------------------", 
                		"to refer patients to receive additional health care services.",
                		"Gastroenterology consult to Univ Hospital",
                		"Pulmonology consult to Univ Hospital",
                		"Cardiology consult to Univ Hospital",
                		"Nephrology consult to Univ Hospital",
                		"Endocrinology and metabolism consult to Univ Hospital",
                		"Hemato-Oncology consult to Univ Hospital",
                		"---",
                		"Psychiatry consult to Univ Hospital",
                		"Neurology consult to Univ Hospital",
                		"Opthalmology consult to Univ Hospital",
                		"Orthopedics consult to Univ Hospital",
                		"Neurosurgery consult to Univ Hospital",
                		"Urology consult to Univ Hospital",
                		"Dermatology consult to Univ Hospital",
               			"ENT consult to Univ Hospital",

                		};
                break;

            default:
                System.out.println("ReEnter the Number !!!");
                break;
        }
        return (comboboxesLabels);
    }
}

