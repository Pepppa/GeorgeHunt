package com.example.georgehunt;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class MouseDrawer {

    public static void draw(Canvas canvas, Paint paint, float cx, float cy, float radius) {

        // Тело — серый круг
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#999999"));
        canvas.drawCircle(cx, cy, radius, paint);

        // Уши — два кружка сверху
        paint.setColor(Color.parseColor("#999999"));
        canvas.drawCircle(cx - radius * 0.6f, cy - radius * 0.85f, radius * 0.35f, paint);
        canvas.drawCircle(cx + radius * 0.6f, cy - radius * 0.85f, radius * 0.35f, paint);

        // Внутри ушей
        paint.setColor(Color.parseColor("#FFBBCC"));
        canvas.drawCircle(cx - radius * 0.6f, cy - radius * 0.85f, radius * 0.2f, paint);
        canvas.drawCircle(cx + radius * 0.6f, cy - radius * 0.85f, radius * 0.2f, paint);

        // Глаза — чёрные бусины
        paint.setColor(Color.parseColor("#111111"));
        canvas.drawCircle(cx - radius * 0.3f, cy - radius * 0.15f, radius * 0.12f, paint);
        canvas.drawCircle(cx + radius * 0.3f, cy - radius * 0.15f, radius * 0.12f, paint);

        // Блики
        paint.setColor(Color.WHITE);
        canvas.drawCircle(cx - radius * 0.24f, cy - radius * 0.2f, radius * 0.04f, paint);
        canvas.drawCircle(cx + radius * 0.36f, cy - radius * 0.2f, radius * 0.04f, paint);

        // Носик
        paint.setColor(Color.parseColor("#FF8899"));
        canvas.drawCircle(cx, cy + radius * 0.15f, radius * 0.1f, paint);

        // Хвост
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(radius * 0.08f);
        paint.setColor(Color.parseColor("#888888"));
        Path tail = new Path();
        tail.moveTo(cx + radius, cy + radius * 0.2f);
        tail.quadTo(cx + radius * 1.8f, cy - radius * 0.5f, cx + radius * 1.5f, cy + radius * 0.8f);
        canvas.drawPath(tail, paint);

        paint.setStyle(Paint.Style.FILL);
    }
}