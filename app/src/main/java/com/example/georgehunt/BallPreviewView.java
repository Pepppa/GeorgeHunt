package com.example.georgehunt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BallPreviewView extends View {

    private Paint paint;
    private float radiusFraction = 0.5f; // 0.0 .. 1.0

    public BallPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);
    }

    public void setRadiusFraction(float fraction) {
        this.radiusFraction = fraction;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float maxRadius = Math.min(getWidth(), getHeight()) / 2f;
        float radius = maxRadius * radiusFraction;
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, paint);
    }
}