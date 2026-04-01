package com.example.georgehunt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.View;
import android.view.MotionEvent;
import android.os.Vibrator;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.media.AudioManager;

public class GameView extends View {

    private Vibrator vibrator;
    private SoundPool soundPool;
    private int catchSound;

    private Paint paint;
    private float ballX = 200;
    private float ballY = 200;
    private float ballRadius = 100;

    private float speedX = 5;
    private float speedY = 5;
    private float shakeX = 0;
    private float shakeY = 0;
    private int soundStreamId = 0;

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
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(attrs)
                .build();

        catchSound = soundPool.load(context, R.raw.soundfile, 1);

        handler.post(gameLoop); // start the loop
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Проверяем все пальцы - достаточно хотя бы одного в периметре
                boolean anyFingerOnBall = false;
                for (int i = 0; i < event.getPointerCount(); i++) {
                    float dx = event.getX(i) - ballX;
                    float dy = event.getY(i) - ballY;
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
                    if (distance < ballRadius) {
                        anyFingerOnBall = true;
                        break;
                    }
                }
                if (anyFingerOnBall && !isCaught) {
                    isPaused = true;
                    isCaught = true;
                    soundStreamId = soundPool.play(catchSound, 1, 1, 0, 0, 1);
                    long[] pattern = {0, 100, 100};
                    vibrator.vibrate(pattern, 0);
                } else if (!anyFingerOnBall && isCaught) {
                    isPaused = false;
                    isCaught = false;
                    soundPool.stop(soundStreamId);
                    soundStreamId = 0;
                    vibrator.cancel();
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                // Проверяем оставшиеся пальцы после отрыва
                int releasedIndex = event.getActionIndex();
                boolean stillOnBall = false;
                for (int i = 0; i < event.getPointerCount(); i++) {
                    if (i == releasedIndex) continue; // пропускаем оторвавшийся палец
                    float dx = event.getX(i) - ballX;
                    float dy = event.getY(i) - ballY;
                    float distance = (float) Math.sqrt(dx * dx + dy * dy);
                    if (distance < ballRadius) {
                        stillOnBall = true;
                        break;
                    }
                }
                if (!stillOnBall && isCaught) {
                    isPaused = false;
                    isCaught = false;
                    soundPool.stop(soundStreamId);
                    soundStreamId = 0;
                    vibrator.cancel();
                }
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