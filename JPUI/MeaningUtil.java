/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPUI;

import JPWord.Data.IExample;
import JPWord.Data.IMeaning;
import JPWord.Data.IWord;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author u0151316
 */
public class MeaningUtil {

//    private static void parseMeanline(String temp) {
////        str = str.trim();
////        str = str.replace(']', ':');
////        if () {
////
////        }
//    }
//
//    private static void parseExampleLine(String temp) {
////        str = str.trim();
////        str = str.replace(']', ':');
////        if () {
////
////        }
//    }
//
//    public static MeaningFormat parseStringToMeaning(String temp) {
//        String str = temp;
//        str = str.replace('\r', ' ');
//        String[] lines = str.split("\n");
//        MeaningFormat format = new MeaningFormat();
//        int lineIndex = -1;
//        for (String line : lines) {
//            lineIndex++;
//            if (line.equals("")) {
//                continue;
//            }
//            int space = -1;
//            for (char c : line.toCharArray()) {
//                if (c == ' ' || c == '\t') {
//                    space++;
//                } else {
//                    break;
//                }
//            }
//            if (space == -1) {
//                format.addMeaningLine(line, lineIndex);
//            } else {
//                format.addExampleLine(line, lineIndex);
//            }
//        }
//        return format;
//    }

    public static List<IMeaning> parseStringToMeaning(String temp) {
        List<IMeaning> means = new LinkedList<>();
        String str = temp;
        str = str.replace('\r', ' ');
        String[] lines = str.split("\n");
        IMeaning currentMean = null;
        for (String line : lines) {
            if (line.equals("")) {
                continue;
            }
            if (line.charAt(0) == ' ' || line.charAt(0) == '\t') {
                //Exp
                if (currentMean == null) {
                    return null;
                }
                IExample example = Database.getInstance().getDatabase().createExample();
                example.decodeFromString(line);
                currentMean.addExample(example);
            } else {
                //Mean
                currentMean = Database.getInstance().getDatabase().createMeaning();
                line = line.replace('[', ' ');
                line = line.replace(']', ':');
                currentMean.decodeFromString(line);
                means.add(currentMean);
            }
        }
        return means;
    }

    public static String meaningToString(List<IMeaning> means) {
        String mean = "";
        for (IMeaning m : means) {
            mean += "[" + m.getType() + "] ";
            mean += m.getInCHS();
            if (!m.getInJP().equals("")) {
                mean += " / " + m.getInJP();
            }
            mean += "\r\n";
            for (IExample example : m.getExamples()) {
                boolean hasE = false;
                if (!example.getExampleInJP().equals("")) {
                    mean += "    " + example.getExampleInJP();
                    hasE = true;
                }
                if (!example.getExampleInCHS().equals("")) {
                    mean += " / " + example.getExampleInCHS();
                    hasE = true;
                }
                if (hasE) {
                    mean += "\r\n";
                }
            }
            mean += "\r\n";
        }
        return mean;
    }

    public static String meaningToString(IWord word) {
        return meaningToString(word.getMeanings());
    }
}
