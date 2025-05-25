package je.pense.doro.fourgate.n_vaccinations;

import java.awt.Dimension;				
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;

import je.pense.doro.GDSEMR_frame;

public class InjectionApp {

    private static final int FRAME_WIDTH = 300;
    private static final int FRAME_HEIGHT = 400;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    private static final String[] BUTTON_LABELS = {
        "Sanofi's VaxigripTetra® Vaccine(4가) [유독]",
        "Kovax Influ 4ga PF® vaccine [nip] ",
        "Tdap (Tetanus, Diphtheria, Pertussis)",
        "Td (Tetanus, Diphtheria)",
        "Shingles Vaccine (Shingrix) #1/2",
        "HAV vaccination #1/2",
        "HBV vaccination #1/3",
        "Prevena 13 (pneumococcal vaccine (PCV13))",
        "Side Effect",
        "Quit"
    };

    public static void main(String[] args) {
        JFrame frame = createMainFrame();
        addButtonsToFrame(frame);
        positionFrameToBottomRight(frame);
        frame.setVisible(true);
    }

    private static JFrame createMainFrame() {
        JFrame frame = new JFrame("Injections");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(BUTTON_LABELS.length, 1));
        return frame;
    }

    private static void addButtonsToFrame(JFrame frame) {
        ActionListener buttonClickListener = e -> {
            String clickedButtonText = ((JButton) e.getSource()).getText();
            updateInjectionDetails(frame, clickedButtonText);
        };

        for (String label : BUTTON_LABELS) {
            JButton button = new JButton(label);
            button.addActionListener(buttonClickListener);
            frame.add(button);
        }
    }

    private static void positionFrameToBottomRight(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int xPosition = (int) (screenSize.getWidth() - frame.getWidth());
        int yPosition = (int) (screenSize.getHeight() - frame.getHeight());
        frame.setLocation(xPosition, yPosition);
    }

    private static void updateInjectionDetails(JFrame frame, String clickedButtonText) {
        String currentDate = DATE_FORMAT.format(new Date());

        if ("Quit".equals(clickedButtonText)) {
            frame.dispose();
            return;
        }

        String CCstring = "The patient visits for Vaccination\n";
        String PIstring = "  [ ✔ ]  no allergy to eggs, chicken\n"
                + "           , or any other component of the vaccine.\n"
                + "  [ ✔ ]  no s/p Guillain-Barré syndrome.\n"
                + "  [ ✔ ]  no adverse reactions to previous vaccines.\n"
                + "  [ ✔ ]  no immunosuppression.\n";
        String Pstring = "\n   ...Vaccination as scheduled";
        String Influ = CCstring + PIstring;

        if (clickedButtonText.contains("Side Effect")) {
        	 InjectionCheck.main(null);
//            GDSEMR_frame.setTextAreaText(8, "\n  #  " + clickedButtonText + "  [" + currentDate + "]");
        } else {
            GDSEMR_frame.setTextAreaText(0, Influ);
            GDSEMR_frame.setTextAreaText(7, "\n  #  " + clickedButtonText + "  [" + currentDate + "]");
            GDSEMR_frame.setTextAreaText(8, Pstring);
        }
    }
}
