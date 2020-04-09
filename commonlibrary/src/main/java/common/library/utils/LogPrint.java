package common.library.utils;

import android.util.Log;

public class LogPrint {
    public final static boolean isLog = true;


    public static void println(Object content) {
        if (isLog) {
            System.out.println(content);
        }
    }

    public static void logD(String title, Object content) {
        if (isLog) {
            Log.d(title, content.toString());
        }
    }

    public static void logI(String title, Object content) {
        if (isLog) {
            Log.i(title, content.toString());
        }
    }

    public static void logE(String title, Object content) {
        if (isLog) {
            Log.e(title, content.toString());
        }
    }
}
