package je.pense.doro.soap.ros;

public class EMR_ROS_JtableDATA {
	public static String[] columnNames() {
	    return new String[] {
	        "< General >", 
	        "< Vision >", 
	        "< Head_and_Neck >",
	        "< Pulmonary >", 
	        "< Cardiovascular >", 
	        "< Gastrointestinal >", 
	        "< Genito-Urinary >", 
	        "< Hematology/Oncology >",
	        "< Neurological >", 
	        "< Endocrine >", 
	        "< Mental Health >", 
	        "< Skin and Hair >", 
	    };
	}

	public static String[] General() {
	    return new String[] {
	        "Fever or chills", 
	        "Fatigue or weakness",
	        "Weight changes",
	        "Night sweats",
	        "Headache",
	        "Changes in vision or hearing",
	        "Chest pain or discomfort",
	        "Shortness of breath or difficulty breathing",
	        "Changes in bowel habits",
	        "Joint pain or stiffness",
	        "Muscle pain or weakness",
	        "Skin changes or lesions",
	        "Sleep disturbances",
	    };
	}
    public static String[] Neurological() {
        return new String[] {
            "Headache",
            "Seizures",
            "Dizziness or vertigo",
            "Changes in vision",
            "Numbness or tingling",
            "Muscle weakness",
            "Tremors or involuntary movements",
            "Memory loss or confusion",
            "Sleep disturbances",
            "Speech difficulties"
        };
    }
    public static String[] Vision() {
    	return new String[] {    
		    "Changes in visual acuity",
		    "Blurry or hazy vision",
		    "Double vision or diplopia",
		    "Eye pain or discomfort",
		    "Redness or swelling of the eyes",
		    "Excessive tearing or dryness",
		    "Flashing lights or floaters",
		    "Loss of peripheral vision",
		    "Difficulty seeing at night",
		    "Sensitivity to light",
		    "Previous eye surgeries or injuries",
        };
    }
    public static String[] Cardiovascular() {
    	return new String[] {    
			"Chest pain or discomfort",
			"Shortness of breath or dyspnea",
			"Palpitations or irregular heartbeats",
			"Dizziness or lightheadedness",
			"Edema or swelling of the legs or ankles",
			"Fatigue or weakness with exertion",
			"Previous cardiac surgeries or interventions",
			"Use of tobacco or alcohol",
			"Physical inactivity or sedentary lifestyle",
        };
    }
    public static String[] Head_and_Neck() {
    	return new String[] {
    	    "Headache",
    	    "Neck pain",
    	    "Earache",
    	    "Difficulty swallowing",
    	    "Hoarseness",
    	    "Facial swelling",
    	    "Dizziness or Vertigo",
    	    "Tinnitus (ringing in the ears)",
    	    "Vision changes",
    	    "Mouth sores",
    	    "Temporomandibular joint (TMJ) issues",
    	    "Snoring",
    	    "Sleep apnea"
    	};
	}
    public static String[] Pulmonary() {
    	return new String[] {
		    "Cough",
		    "Wheezing",
		    "Chest pain or tight ness",
		    "Shortness of breath",
		    "Sputum production",
		    "Hemoptysis (coughing up blood)",
		    "Frequent respiratory infections",
		    "Cyanosis (bluish discoloration of lips or skin)",
		    "Clubbing of fingers",
		    "Orthopnea (difficulty breathing while lying flat)",
		    "Paroxysmal nocturnal dyspnea (sudden difficulty breathing at night)",
		    "Snoring",
		    "Allergies",
		    "Asthma",
		    "Chronic obstructive pulmonary disease (COPD)",
		    
    	};
	}
    public static String[] Gastrointestinal() {
    	return new String[] {
		    "Abdominal pain",
		    "Nausea",
		    "Vomiting",
		    "Heartburn",
		    "Indigestion",
		    "Loss of appetite",
		    "Weight loss",
		    "Difficulty swallowing",
		    "Abdominal bloating",
		    "Change in bowel habits",
		    "Constipation",
		    "Diarrhea",
		    "Blood in stool",
		    "Black, tarry stools",
		    "Jaundice (yellowing of skin or eyes)",
	    
    	};
	}
    public static String[] GenitoUrinary() {
    	return new String[] {
    		 "Frequent urination",
		    "Urgency to urinate",
		    "Burning sensation during urination",
		    "Blood in urine",
		    "Cloudy or foul-smelling urine",
		    "Difficulty starting or maintaining urination",
		    "Urinary incontinence",
		    "Nocturia (frequent urination at night)",
		    "Urinary retention",
		    "Testicular pain",
		    "Urinary incontinence",
		    "Sexually transmitted infections (STIs)",
    	};
	}
    public static String[] HematologyOncology() {
    	return new String[] {
		    "Easy bruising",
		    "Fatigue",
		    "Enlarged lymph nodes",
		    "Night sweats",
		    "Unexplained weight loss",
		    "Recurrent infections",
		    "Chest pain",
		    "Shortness of breath",
		    "Skin changes",
    	};
	}
    public static String[] Endocrine() {
    	return new String[] {
		    "Heat or cold intolerance",
		    "Excessive sweating",
		    "Increased thirst",
		    "Frequent urination",
		    "Hunger",
		    "Tremors",
		    "Hair loss",
		    "Dry skin",
		    "Depression",
		    "Anxiety",
		    "Sleep disturbances",
		    "Enlarged thyroid (goiter)",
    	};
	}
    public static String[] MentalHealth() {
    	return new String[] {
			"Depressed mood",
		    "Anxiety",
		    "Panic attacks",
		    "Mood swings",
		    "Sleep disturbances",
		    "Appetite changes",
		    "Loss of interest or pleasure",
		    "Poor concentration",
		    "Memory problems",
		    "Feelings of guilt or worthlessness",
		    "Suicidal thoughts",
		    "Hallucinations",
		    "Delusions",
		    "Impulsivity",
		    "Agitation",
		    "Social withdrawal",
		    "Substance abuse",
		    "Eating disorders",
		    "Obsessions or compulsions",
    	};
	}
    public static String[] SkinAndHair() {
    	return new String[] {
		    "Rash or Redness",
		    "Itching",
		    "Swelling",
		    "Dry skin",
		    "Oily skin",
		    "Acne",
		    "Eczema",
		    "Psoriasis",
		    "Hives",
		    "Skin discoloration",
		    "Hair loss",
		    "Excessive hair growth",
		    "Nail abnormalities",
		    "Warts",
		    "Scars",
		    "Rosacea",
		    "Dandruff",
		};
	}
}