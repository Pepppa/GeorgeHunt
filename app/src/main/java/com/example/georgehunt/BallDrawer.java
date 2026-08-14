package com.example.georgehunt;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class BallDrawer {

    public static void draw(Canvas canvas, Paint paint, float cx, float cy, float radius, boolean isCaught) {

        // Тело
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(isCaught ? Color.RED : Color.parseColor("#F0A030"));
        canvas.drawCircle(cx, cy, radius, paint);

        // Румянец левый
        paint.setColor(Color.parseColor("#E0507A"));
        paint.setAlpha(115);
        canvas.drawOval(new RectF(cx - radius * 0.66f, cy + radius * 0.02f, cx - radius * 0.04f, cy + radius * 0.44f), paint);

        // Румянец правый
        canvas.drawOval(new RectF(cx + radius * 0.04f, cy + radius * 0.02f, cx + radius * 0.66f, cy + radius * 0.44f), paint);
        paint.setAlpha(255);

        // Бровь левая
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(radius * 0.03f);
        paint.setColor(Color.parseColor("#C06010"));
        canvas.drawArc(new RectF(cx - radius * 0.44f, cy - radius * 0.48f, cx - radius * 0.14f, cy - radius * 0.28f), 210, 120, false, paint);

        // Бровь правая
        canvas.drawArc(new RectF(cx + radius * 0.14f, cy - radius * 0.48f, cx + radius * 0.44f, cy - radius * 0.28f), 210, 120, false, paint);

        // Глаз левый
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#111111"));
        canvas.drawCircle(cx - radius * 0.28f, cy - radius * 0.08f, radius * 0.1f, paint);

        // Глаз правый
        canvas.drawCircle(cx + radius * 0.28f, cy - radius * 0.08f, radius * 0.1f, paint);

        // Блик левый
        paint.setColor(Color.WHITE);
        canvas.drawCircle(cx - radius * 0.22f, cy - radius * 0.13f, radius * 0.035f, paint);

        // Блик правый
        canvas.drawCircle(cx + radius * 0.34f, cy - radius * 0.13f, radius * 0.035f, paint);

        // Носик
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(radius * 0.03f);
        paint.setColor(Color.parseColor("#C06010"));
        canvas.drawArc(new RectF(cx - radius * 0.05f, cy + radius * 0.08f, cx + radius * 0.05f, cy + radius * 0.18f), 0, 180, false, paint);

        // Верхняя линия рта
        paint.setStrokeWidth(radius * 0.03f);
        paint.setColor(Color.parseColor("#6A0010"));
        Path mouthTop = new Path();
        mouthTop.moveTo(cx - radius * 0.54f, cy + radius * 0.27f);
        mouthTop.cubicTo(
                cx - radius * 0.15f, cy + radius * 0.54f,
                cx + radius * 0.12f, cy + radius * 0.48f,
                cx + radius * 0.54f, cy + radius * 0.27f
        );
        canvas.drawPath(mouthTop, paint);

        paint.setStyle(Paint.Style.FILL);
    }
}