package com.example.georgehunt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;

public class GameView extends View {

    private Paint paint;
    private float ballX = 200;
    private float ballY = 200;
    private float ballRadius = 100;

    private float speedX = 5;
    private float speedY = 5;

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

    private void update() {
        ballX += speedX;
        ballY += speedY;

        // Bounce off left/right walls
        if (ballX - ballRadius < 0 || ballX + ballRadius > getWidth()) {
            speedX = -speedX;
        }

        // Bounce off top/bottom walls
        if (ballY - ballRadius < 0 || ballY + ballRadius > getHeight()) {
            speedY = -speedY;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.parseColor("#1a237e"));

        paint.setColor(Color.YELLOW);
        canvas.drawCircle(ballX, ballY, ballRadius, paint);
    }
}