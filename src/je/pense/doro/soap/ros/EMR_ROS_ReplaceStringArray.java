package je.pense.doro.soap.ros;

import java.util.ArrayList;

public class EMR_ROS_ReplaceStringArray {
    public static String main(ArrayList<String> columnS, ArrayList<String> rowsS) {
        for (int i = 0; i < columnS.size(); i++) {
            for (int j = 0; j < rowsS.size(); j++) {
                if (columnS.get(i).equals(rowsS.get(j))) {
                    columnS.set(i, "[◆] " + columnS.get(i));
                }
            }
        }

        // Print the updated columnS array
        for (int i = 0; i < columnS.size(); i++) {
            String s = columnS.get(i);
            if (!s.startsWith("[") && !s.startsWith("<")) {
                columnS.set(i, "[◇] " + s);
            }
        }
//      System.out.println(columnS);
      String delimiter = ",\n\t";
      String stringColumnS = String.join(delimiter, columnS);
      System.out.println(stringColumnS);
      return stringColumnS;
    }
}
