package je.pense.doro.chartplate.mainpage;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextArea;

import je.pense.doro.GDSEMR_frame;

public class EMR_BlendColors extends GDSEMR_frame {
	public EMR_BlendColors() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void blendColors(JTextArea textAreas,JTextArea outputtextAreas, int i) throws Exception {
		  int r = 255; // Red value remains constant at 255 for orange color
		  int g = 255 - i * 10; // Green value decreases gradually from 165 to 65
		  int b = 0; // Blue value remains constant at 0 for orange color
		  Color color = new Color(r, g, b);
		  textAreas.setBackground(color);
		  
	  	  Font font = new Font("Inconsolata", Font.PLAIN, 12);
		  textAreas.setFont(font);
		  outputtextAreas.setFont(font);

	}
}
