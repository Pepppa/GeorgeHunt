package com.example.georgehunt;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class CatDrawer {

    public static void draw(Canvas canvas, Paint paint, float cx, float cy, float radius, boolean mouthOpen) {

        // Тело — серый круг
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#AAAAAA"));
        canvas.drawCircle(cx, cy, radius, paint);

        // Уши — два треугольника, острым углом вверх
        paint.setColor(Color.parseColor("#AAAAAA"));
        Path earLeft = new Path();
        earLeft.moveTo(cx - radius * 0.75f, cy - radius * 0.75f); // левый нижний
        earLeft.lineTo(cx - radius * 0.2f, cy - radius * 0.75f);  // правый нижний
        earLeft.lineTo(cx - radius * 0.48f, cy - radius * 1.35f); // острый верх
        earLeft.close();
        canvas.drawPath(earLeft, paint);

        Path earRight = new Path();
        earRight.moveTo(cx + radius * 0.2f, cy - radius * 0.75f);  // левый нижний
        earRight.lineTo(cx + radius * 0.75f, cy - radius * 0.75f); // правый нижний
        earRight.lineTo(cx + radius * 0.48f, cy - radius * 1.35f); // острый верх
        earRight.close();
        canvas.drawPath(earRight, paint);

// Внутри ушей — розовый
        paint.setColor(Color.parseColor("#FFAABB"));
        Path earLeftInner = new Path();
        earLeftInner.moveTo(cx - radius * 0.68f, cy - radius * 0.82f);
        earLeftInner.lineTo(cx - radius * 0.27f, cy - radius * 0.82f);
        earLeftInner.lineTo(cx - radius * 0.48f, cy - radius * 1.2f);
        earLeftInner.close();
        canvas.drawPath(earLeftInner, paint);

        Path earRightInner = new Path();
        earRightInner.moveTo(cx + radius * 0.27f, cy - radius * 0.82f);
        earRightInner.lineTo(cx + radius * 0.68f, cy - radius * 0.82f);
        earRightInner.lineTo(cx + radius * 0.48f, cy - radius * 1.2f);
        earRightInner.close();
        canvas.drawPath(earRightInner, paint);

        // Глаза — зелёные
        paint.setColor(Color.parseColor("#22AA44"));
        canvas.drawCircle(cx - radius * 0.3f, cy - radius * 0.2f, radius * 0.15f, paint);
        canvas.drawCircle(cx + radius * 0.3f, cy - radius * 0.2f, radius * 0.15f, paint);

        // Зрачки
        paint.setColor(Color.parseColor("#111111"));
        canvas.drawCircle(cx - radius * 0.3f, cy - radius * 0.2f, radius * 0.07f, paint);
        canvas.drawCircle(cx + radius * 0.3f, cy - radius * 0.2f, radius * 0.07f, paint);

        // Блики
        paint.setColor(Color.WHITE);
        canvas.drawCircle(cx - radius * 0.25f, cy - radius * 0.25f, radius * 0.04f, paint);
        canvas.drawCircle(cx + radius * 0.35f, cy - radius * 0.25f, radius * 0.04f, paint);

        // Носик
        paint.setColor(Color.parseColor("#FF8899"));
        canvas.drawOval(new RectF(cx - radius * 0.1f, cy + radius * 0.05f,
                cx + radius * 0.1f, cy + radius * 0.18f), paint);

        // Усы
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(radius * 0.03f);
        paint.setColor(Color.parseColor("#DDDDDD"));
        canvas.drawLine(cx - radius * 0.12f, cy + radius * 0.12f, cx - radius * 0.7f, cy + radius * 0.05f, paint);
        canvas.drawLine(cx - radius * 0.12f, cy + radius * 0.15f, cx - radius * 0.7f, cy + radius * 0.22f, paint);
        canvas.drawLine(cx + radius * 0.12f, cy + radius * 0.12f, cx + radius * 0.7f, cy + radius * 0.05f, paint);
        canvas.drawLine(cx + radius * 0.12f, cy + radius * 0.15f, cx + radius * 0.7f, cy + radius * 0.22f, paint);

        // Рот
        paint.setStyle(Paint.Style.FILL);
        if (mouthOpen) {
            // Открытый рот — тёмный овал
            paint.setColor(Color.parseColor("#331111"));
            canvas.drawOval(new RectF(cx - radius * 0.3f, cy + radius * 0.22f,
                    cx + radius * 0.3f, cy + radius * 0.55f), paint);
            // Язык
            paint.setColor(Color.parseColor("#FF6688"));
            canvas.drawOval(new RectF(cx - radius * 0.2f, cy + radius * 0.35f,
                    cx + radius * 0.2f, cy + radius * 0.55f), paint);
        } else {
            // Закрытый рот — дужка
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(radius * 0.04f);
            paint.setColor(Color.parseColor("#885566"));
            canvas.drawArc(new RectF(cx - radius * 0.25f, cy + radius * 0.22f,
                    cx + radius * 0.25f, cy + radius * 0.48f), 0, 180, false, paint);
            paint.setStyle(Paint.Style.FILL);
        }
    }
}