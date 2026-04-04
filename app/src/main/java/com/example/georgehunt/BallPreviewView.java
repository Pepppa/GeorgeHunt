package com.example.georgehunt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BallPreviewView extends View {

    private Paint paint;
    private int radiusProgress = 50;

    public BallPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);
    }

    public void setRadiusProgress(int progress) {
        this.radiusProgress = progress;
        invalidate();
    }

    public float getCurrentRadius() {
        float screenMin = Math.min(getWidth(), getHeight());
        return GameSettings.toRadius(radiusProgress, screenMin);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float radius = getCurrentRadius();
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, paint);
    }
}