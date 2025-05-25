package je.pense.doro.chartplate.filecontrol;


import java.awt.Desktop;
import java.net.URI;

public class OpenOneNotePage {

    public static void main(String[] args) {
        String oneNotePageLink = "https://onedrive.live.com/redir?resid=CB5E027006DF23C3%218672&page=Edit&wd=target%28DM.one%7C902c6b4a-2317-4bb2-8012-9427f919c699%2FRetinipathy%7C06e70f13-93a8-485b-b465-91b5483c1f34%2F%29&wdorigin=NavigationUrl";
        try {
            Desktop.getDesktop().browse(new URI(oneNotePageLink));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
