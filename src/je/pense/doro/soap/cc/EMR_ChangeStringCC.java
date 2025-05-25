package je.pense.doro.soap.cc;

public class EMR_ChangeStringCC {

    public static String EMR_ChangeString_abr(String word) {
        String[] wordArray = word.split(" ");
        for (int i = 0; i < wordArray.length; i++) {
            if (wordArray[i].contains(":(")) {

                if (wordArray[i].contains("d")) {
                    wordArray[i] = wordArray[i].replace("d", "-day ago  :cd )");
                    wordArray[i] = wordArray[i].replace(":(", " (onset ");
                } else if (wordArray[i].contains("w")) {
                    wordArray[i] = wordArray[i].replace("w", "-week ago  :cd )");
                    wordArray[i] = wordArray[i].replace(":(", " (onset ");
                } else if (wordArray[i].contains("m")) {
                    wordArray[i] = wordArray[i].replace("m", "-month ago  :cd )");
                    wordArray[i] = wordArray[i].replace(":(", " (onset ");
                } else if (wordArray[i].contains("y")) {
                    wordArray[i] = wordArray[i].replace("y", "-year ago :cd )");
                    wordArray[i] = wordArray[i].replace(":(", " (onset ");
                } else {
                    return word;
                }
                word = String.join(" ", wordArray);
            }
        }
        return word;
    }

    public static String EMR_ChangeString_Px(String word) {
        String[] wordArray = word.split(" ");
        StringBuilder retWord = new StringBuilder();

        for (String w : wordArray) {
            if (w.contains(":>")) {
                if (w.contains("1")) {
                    w = w.replace(":>1", " mg 1 tab p.o. q.d.");
                } else if (w.contains("2")) {
                    w = w.replace(":>2", " mg 1 tab p.o. b.i.d.");
                } else if (w.contains("3")) {
                    w = w.replace(":>3", " mg 1 tab p.o. t.i.d.");
                } else if (w.contains(":>0")) {
                    w = w.replace(":>0", " without medications");
                } else if (w.contains(":>4")) {
                    w = w.replace(":>4", " with medications");
                } else {
                    return retWord.toString().trim();
                }
            }
            retWord.append(w).append(" ");
        }
        return retWord.toString().trim();
    }
}
