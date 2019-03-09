package com.xznn.miboy;


import android.os.Handler;
import android.os.Looper;

/**
 * Created with IntelliJ IDEA.
 * User: chen
 * Date: 8/16/13
 * Time: 11:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class HandlerUtil {

    private static volatile Handler sHandler;

    public static Handler getMainLooperHandler() {
        if (sHandler == null) {
            synchronized (HandlerUtil.class) {
                if (sHandler == null) {
                    sHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return sHandler;
    }

    public static void runOnUiThread(Runnable runnable) {
        runOnUiThread(runnable, 0);
    }

    public static void runOnUiThread(Runnable runnable, int delay) {
        if (runnable != null) {
            getMainLooperHandler().postDelayed(runnable, delay);
        }
    }

    public static void runOnBackThread(final Runnable runnable) {
        runOnBackThread(runnable, 0);
    }

    public static void runOnBackThread(final Runnable runnable, int delay) {
        if (runnable != null) {
            getMainLooperHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Thread thread = new Thread(runnable);
                    thread.start();
                }
            }, delay);
        }
    }

}
