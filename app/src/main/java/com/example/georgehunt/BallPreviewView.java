package com.example.georgehunt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class BallPreviewView extends View {

    private Paint paint;
    private int radiusProgress = 50;
    private float realRadius = 0f;

    public BallPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);
        updateRealRadius(context);
    }

    public void setRadiusProgress(int progress) {
        this.radiusProgress = progress;
        updateRealRadius(getContext());
        invalidate();
    }

    public float getCurrentRadius() {
        return realRadius;
    }

    private void updateRealRadius(Context context) {
        realRadius = GameSettings.toRadius(radiusProgress, context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        BallDrawer.draw(canvas, paint, getWidth() / 2f, getHeight() / 2f, realRadius, false);
    }
}