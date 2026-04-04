package com.example.georgehunt;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class GameSettings {

    private static final String PREFS_NAME = "game_settings";
    private static final String KEY_BALL_RADIUS = "ball_radius";
    private static final String KEY_SPEED = "speed";

    // Defaults
    public static final int DEFAULT_RADIUS_PROGRESS = 50; // из 100
    public static final int DEFAULT_SPEED_PROGRESS = 3;   // из 6

    public static void save(Context context, int radiusProgress, int speedProgress) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(KEY_BALL_RADIUS, radiusProgress);
        editor.putInt(KEY_SPEED, speedProgress);
        editor.apply();
    }

    public static int loadRadiusProgress(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getInt(KEY_BALL_RADIUS, DEFAULT_RADIUS_PROGRESS);
    }

    public static int loadSpeedProgress(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getInt(KEY_SPEED, DEFAULT_SPEED_PROGRESS);
    }

    public static float maxRadius(Context context) {
        float dp100 = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 100f,
                context.getResources().getDisplayMetrics());
        return dp100;
    }

    public static float toRadius(int progress, Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float screenMin = Math.min(dm.widthPixels, dm.heightPixels);
        float minRadius = screenMin * 0.05f;
        float maxRadius = maxRadius(context);
        return minRadius + (maxRadius - minRadius) * (progress / 100f);
    }

    // Конвертация progress (0..6) в скорость
    public static float toSpeed(int progress) {
        float[] speeds = {2f, 3f, 5f, 7f, 10f, 14f, 19f};
        return speeds[progress];
    }

    // Метка для отображения
    public static String speedLabel(int progress) {
        String[] labels = {"Very low", "Low", "Medium low", "Medium", "Medium high", "High", "Very high"};
        return labels[progress];
    }
}