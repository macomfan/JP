package DataEngine;

import java.util.List;

import JPWord.Data.IExample;
import JPWord.Data.IMeaning;

/**
 * Created by u0151316 on 1/10/2018.
 */

public class MeaningUtil {
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
}
