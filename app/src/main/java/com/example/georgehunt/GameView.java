package com.example.georgehunt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;
import android.view.MotionEvent;

public class GameView extends View {

    private Paint paint;
    private float ballX = 200;
    private float ballY = 200;
    private float ballRadius = 100;

    private float speedX = 5;
    private float speedY = 5;
    private float shakeX = 0;
    private float shakeY = 0;

    private boolean isPaused = false;
    private boolean isCaught = false;

    private Handler handler = new Handler();
    private Runnable gameLoop = new Runnable() {
        @Override
        public void run() {
            update();
            invalidate(); // triggers onDraw
            handler.postDelayed(this, 16); // ~60 FPS
        }
    };

    public GameView(Context context) {
        super(context);
        paint = new Paint();
        handler.post(gameLoop); // start the loop
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float dx = event.getX() - ballX;
                float dy = event.getY() - ballY;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);
                if (distance < ballRadius) {
                    isPaused = true;
                    isCaught = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                isPaused = false;
                isCaught = false;
                break;
        }
        return true;
    }

    private void update() {
            if (isPaused) {
                // Small random shake while caught
                shakeX = (float) (Math.random() * 6 - 3);
                shakeY = (float) (Math.random() * 6 - 3);
                return;
            }

            shakeX = 0;
            shakeY = 0;

            ballX += speedX;
            ballY += speedY;

            if (ballX - ballRadius < 0 || ballX + ballRadius > getWidth()) {
                speedX = -speedX;
            }
            if (ballY - ballRadius < 0 || ballY + ballRadius > getHeight()) {
                speedY = -speedY;
            }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.parseColor("#1a237e"));

        paint.setColor(isCaught ? Color.RED : Color.YELLOW);
        canvas.drawCircle(ballX + shakeX, ballY + shakeY, ballRadius, paint);
    }
}