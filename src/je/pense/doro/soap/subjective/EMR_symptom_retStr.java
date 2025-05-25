package je.pense.doro.soap.subjective;

public class EMR_symptom_retStr {
    public static String[] returnStr(String nameStr) {
	    String[] returnargs;

	    switch (nameStr) {
	        case "Diabetes Mellitus":
	            returnargs = new String[]{"Polyuria", "Polydipsia", "Polyphagia", "Weight loss", "Fatigue", "Blurred vision","Skin changes", "Recurring infections", "Sexual dysfunction", "Mood changes", "Cognitive issues", "Oral health problems", "Hearing changes", "Digestive issues"};
	            break;
	        case "Hyperthyroidism":
	            returnargs = new String[]{"Anxiety", "Irritability", "Heart palpitations", "Heat sensitivity", "Tremors", "Weight loss", "Fatigue", "Muscle weakness", "Excessive sweating", "Sleep problems", "Irregular heartbeat", "Goiter"};
	            break;
	        case "Hypothyroidism":
	            returnargs = new String[]{"Fatigue", "Weight gain", "Cold sensitivity", "Muscle weakness", "Joint pain", "Dry skin", "Depression", "Constipation", "Hair loss", "Hoarse voice", "Puffy face", "Memory problems", "Heavy menstrual periods", "Slow heart rate" };
	            break;
	        case "URI":
	            returnargs = new String[]{"Cough", "Sore throat", "Nasal congestion", "Runny nose", "Sneezing", "Headache", "Low-grade fever", "Fatigue", "Body aches", "Difficulty breathing"};
	            break;
	        case "UTI":
	            returnargs = new String[]{"Painful urination", "Frequent urination", "Urgency", "Cloudy urine", "Blood in urine", "Strong-smelling urine", "Lower abdominal pain", "Back pain", "Fever", "Fatigue", "Nausea", "Vomiting", "Mental confusion", "Pelvic pressure"};
	            break;
	        case "Abdominal pain":
	            returnargs = new String[]{"Nausea", "Vomiting", "Loss of appetite", "Bloating", "Fever", "Diarrhea", "Constipation", "Heartburn", "Belching", "Urinary problems", "Fatigue", "Changes in bowel habits", "Gas", "Cramping", "Back pain"};
	            break;
	        case "Atypical chest pain":
	            returnargs = new String[]{"Burning sensation", "Stabbing pain", "Upper abdominal pain", "Back pain", "Shortness of breath", "Dizziness", "Palpitations", "Numbness", "Fatigue", "Anxiety", "Nausea", "Sweating", "Jaw pain", "Neck discomfort", "Arm pain" };
	            break;
	        case "Osteoporosis":
	            returnargs = new String[]{"Loss of height", "Back pain", "Stooped posture", "Easy fractures", "Receding gums", "Brittle fingernails", "Weak grip strength", "Bone pain", "Dental problems", "Shortness of breath" };
	            break;
	        case "Hypercholesterolemia":
	            returnargs = new String[]{"Xanthomas", "Xanthelasmas", "Corneal arcus", "Chest pain", "Shortness of breath", "Fatigue", "Numbness", "Leg pain", "Memory problems", "High blood pressure" };
	            break;
	        	        
	        case "Quit":
	        	returnargs = new String[]{""};
	            break;
	        default:
	            returnargs = new String[]{};
	            break;
	    }

	    return returnargs;
	}
}
