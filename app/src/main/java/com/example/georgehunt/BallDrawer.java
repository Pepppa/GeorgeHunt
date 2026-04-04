package com.example.georgehunt;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class BallDrawer {

    public static void draw(Canvas canvas, Paint paint, float cx, float cy, float radius, boolean isCaught) {

        // Тело
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(isCaught ? Color.RED : Color.parseColor("#FFD700"));
        canvas.drawCircle(cx, cy, radius, paint);

        // Румянец левый
        paint.setColor(Color.parseColor("#FFAAAA"));
        canvas.drawCircle(cx - radius * 0.38f, cy + radius * 0.15f, radius * 0.2f, paint);

        // Румянец правый
        canvas.drawCircle(cx + radius * 0.38f, cy + radius * 0.15f, radius * 0.2f, paint);

        // Глаз левый — белок
        paint.setColor(Color.WHITE);
        canvas.drawCircle(cx - radius * 0.28f, cy - radius * 0.2f, radius * 0.16f, paint);

        // Глаз правый — белок
        canvas.drawCircle(cx + radius * 0.28f, cy - radius * 0.2f, radius * 0.16f, paint);

        // Глаз левый — зрачок
        paint.setColor(Color.parseColor("#222222"));
        canvas.drawCircle(cx - radius * 0.28f, cy - radius * 0.18f, radius * 0.08f, paint);

        // Глаз правый — зрачок
        canvas.drawCircle(cx + radius * 0.28f, cy - radius * 0.18f, radius * 0.08f, paint);

        // Улыбка
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(radius * 0.08f);
        paint.setColor(Color.parseColor("#8B4513"));
        RectF smileRect = new RectF(
                cx - radius * 0.4f,
                cy - radius * 0.05f,
                cx + radius * 0.4f,
                cy + radius * 0.45f
        );
        canvas.drawArc(smileRect, 0, 180, false, paint);
        paint.setStyle(Paint.Style.FILL);
    }
}