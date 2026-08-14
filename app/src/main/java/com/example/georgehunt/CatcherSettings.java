package com.example.georgehunt;

import android.content.Context;
import android.content.SharedPreferences;

public class CatcherSettings {

    private static final String PREFS_NAME = "catcher_settings";
    private static final String KEY_MOUSE_SPEED = "mouse_speed";
    private static final String KEY_CAT_SPEED = "cat_speed";

    public static final int DEFAULT_MOUSE_SPEED = 0;
    public static final int DEFAULT_CAT_SPEED = 0;

    public static void save(Context context, int mouseSpeed, int catSpeed) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
                .putInt(KEY_MOUSE_SPEED, mouseSpeed)
                .putInt(KEY_CAT_SPEED, catSpeed)
                .apply();
    }

    public static int loadMouseSpeedProgress(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getInt(KEY_MOUSE_SPEED, DEFAULT_MOUSE_SPEED);
    }

    public static int loadCatSpeedProgress(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getInt(KEY_CAT_SPEED, DEFAULT_CAT_SPEED);
    }

    // 0 = нет движения, 1..6 = скорость
    public static float toSpeed(int progress) {
        float[] speeds = {0f, 2f, 3f, 5f, 7f, 10f, 14f};
        return speeds[progress];
    }

    public static String speedLabel(int progress) {
        String[] labels = {"None", "Very low", "Low", "Medium", "Medium high", "High", "Very high"};
        return labels[progress];
    }
}