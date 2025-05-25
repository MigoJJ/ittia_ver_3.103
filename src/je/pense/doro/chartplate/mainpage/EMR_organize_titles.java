package je.pense.doro.chartplate.mainpage;

import java.awt.TextArea;	

public class EMR_organize_titles {

	public static String EMR_organize_titles(String text) {
	   String[] titles = { "CC>", "PI>", "ROS>", "PMH>", "S>", "O>", "Physical Exam>", "A>", "P>", "Comment>" };
	   boolean matchFound = false;
	   
	   for (int i = 0; i < titles.length; i++) { // Start from index 1
	      if (text.trim().equals(titles[i])) {
	         text = "";
	         matchFound = true;
	         break;
	      }
	   }
	   if (!matchFound) {
	      text = "\n" + text;
	   }
	   return text;
	}
}