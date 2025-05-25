package je.pense.doro.fourgate.A_editmain;

import java.util.Random;

import je.pense.doro.GDSEMR_frame;

public class EMR_FU_Comments {

    private static final Random rand = new Random(); 

    public static String getRandomRecommendation(String dcode) {
        switch (dcode) {

                case "DM": // Diabetes Recommendations
                    return getRandomFromArray(new String[] {
                        "Eat plenty of fruits, vegetables, and whole grains.",
                        "Maintain a healthy weight to improve blood sugar control.",
                        "Get at least 30 minutes of moderate exercise most days.", 
                        "Take your diabetes medication as prescribed.",
                        "Monitor your blood sugar regularly.",
                        "Get regular eye, foot, and dental checkups."
                    });
                case "BP": // Blood Pressure Recommendations
                    return getRandomFromArray(new String[] {
                        "Eat a healthy diet with fruits, vegetables, and whole grains.",
                        "Maintain a healthy weight to lower blood pressure.",
                        "Get at least 30 minutes of moderate exercise most days.", 
                        "Limit sodium intake (less than 2,300mg daily).",
                        "Increase potassium intake (from fruits and vegetables).",
                        "Limit alcohol consumption.",
                        "Manage stress through exercise, yoga, or meditation."
                    });
                case "Chol": // Cholesterol Recommendations
                    return getRandomFromArray(new String[] {
                        "Limit sugary drinks, processed foods, and unhealthy fats.", 
                        "Maintain a healthy weight to lower cholesterol.",
                        "Get at least 30 minutes of moderate exercise most days.", 
                        "Limit saturated fat (less than 16g men/12g women daily).", 
                        "Avoid trans fat.", 
                        "Choose lean protein sources.",
                        "Eat plenty of fiber.", 
                        "Include nuts and seeds in your diet.", 
                        "Consider soy products (tofu, tempeh).",
                        "Add garlic to your meals." 
                    });
                default:
                    return "\n"; 
            }
        }

    // Helper function to get a random element from a String array
    private static String getRandomFromArray(String[] array) {
        return array[rand.nextInt(array.length)];
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            String dcode = args[0];
            String recommendation = getRandomRecommendation(dcode);
//            System.out.println(recommendation);
            GDSEMR_frame.setTextAreaText(9, recommendation); // Assuming this method exists
        } else {
            System.err.println("Error: Please provide a dcode (DM, BP, or Chol) as an argument.");
            // Consider exiting with an error code here: System.exit(1);
        }
    }
}