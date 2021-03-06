package DataEngine;

import android.util.Log;

/**
 * Created by u0151316 on 6/5/2018.
 */

public class AppLogging {
    private static String LOGTAG = "[JPWord]";

    public static void showLog(String log) {
        Log.v(LOGTAG, log);
        //System.out.print("[JPWord] " + log);
    }

    public static void showDebug(Class<?> cls , String log) {
        Log.d(LOGTAG, cls.getSimpleName() + ": " + log);
        //System.out.print("[JPWord] " + classname + log);
    }
}
