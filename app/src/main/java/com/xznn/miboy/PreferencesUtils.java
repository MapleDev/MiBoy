package com.xznn.miboy;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {
    private static final String CONFIG_NAME = "system_config";

    public static final String IS_OPEN_FUCK_VIVO = "IS_OPEN_FUCK_VIVO";
    public static final String IS_OPEN_MI_BOY = "IS_OPEN_MI_BOY";
    public static final String KEY_PASSWORD = "key_password";

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }
}
