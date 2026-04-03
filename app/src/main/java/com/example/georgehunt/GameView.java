package com.example.georgehunt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.view.View;
import android.view.MotionEvent;
import android.os.Vibrator;
import android.media.AudioAttributes;
import android.media.SoundPool;
import com.example.georgehunt.MainActivity;

public class GameView extends View {

    private Vibrator vibrator;
    private SoundPool soundPool;
    private int catchSound;
    private UnlockListener unlockListener;

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

    // --- Screen lock slider ---
    private float sliderX = 0;           // current slider position (shift to the right)
    private boolean isDraggingSlider = false;
    private int sliderPointerId = -1;    // which finger is dragging the slider
    private static final float SLIDER_SIZE = 120f;
    private static final float UNLOCK_THRESHOLD = 2f / 3f; // two thirds of screen width

    private Handler handler = new Handler();
    private Runnable gameLoop = new Runnable() {
        @Override
        public void run() {
            update();
            invalidate();
            handler.postDelayed(this, 16);
        }
    };

    public GameView(Context context, UnlockListener unlockListener) {
        super(context);
        this.unlockListener = unlockListener;
        paint = new Paint();
        paint.setAntiAlias(true);
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

        handler.post(gameLoop);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int actionIndex = event.getActionIndex();

        // Handle slider and game touches simultaneously
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                float x = event.getX(actionIndex);
                float y = event.getY(actionIndex);
                // Check if this finger hits the slider
                if (isOnSlider(x, y)) {
                    isDraggingSlider = true;
                    sliderPointerId = event.getPointerId(actionIndex);
                }
                // Always pass to game handler too
                handleGameTouch(event);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Update slider if being dragged
                if (isDraggingSlider && sliderPointerId != -1) {
                    int pointerIndex = event.findPointerIndex(sliderPointerId);
                    if (pointerIndex != -1) {
                        float x = event.getX(pointerIndex);
                        sliderX = Math.max(0, x - SLIDER_SIZE / 2);
                        // Check if unlock threshold reached
                        if (sliderX > getWidth() * UNLOCK_THRESHOLD) {
                            unlockScreen();
                        }
                    }
                }
                // Always pass to game handler too
                handleGameTouch(event);
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                int pointerId = event.getPointerId(actionIndex);
                if (pointerId == sliderPointerId) {
                    // Finger released - snap slider back
                    isDraggingSlider = false;
                    sliderPointerId = -1;
                    sliderX = 0;
                }
                // Always pass to game handler too
                handleGameTouch(event);
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                isDraggingSlider = false;
                sliderPointerId = -1;
                sliderX = 0;
                handleGameTouch(event);
                break;
            }
        }
        return true;
    }

    // Check if finger is on the slider icon
    private boolean isOnSlider(float x, float y) {
        RectF sliderRect = new RectF(
                sliderX,
                0,
                sliderX + SLIDER_SIZE,
                SLIDER_SIZE
        );
        return sliderRect.contains(x, y);
    }

    private void unlockScreen() {
        // Short vibration on unlock
        vibrator.vibrate(200);
        unlockListener.onUnlock();
    }

    // --- Game touch handler ---
    private void handleGameTouch(MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:
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
                int releasedIndex = event.getActionIndex();
                boolean stillOnBall = false;
                for (int i = 0; i < event.getPointerCount(); i++) {
                    if (i == releasedIndex) continue;
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
    }

    private void update() {
        if (isPaused) {
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

        // Draw ball
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(isCaught ? Color.RED : Color.YELLOW);
        canvas.drawCircle(ballX + shakeX, ballY + shakeY, ballRadius, paint);

        // Draw lock slider
        drawLockSlider(canvas);
    }

    private void drawLockSlider(Canvas canvas) {
        float top = 0;
        float left = sliderX;

        // Slider track background
        paint.setColor(Color.argb(100, 255, 255, 255));
        paint.setStyle(Paint.Style.FILL);
        RectF track = new RectF(0, 0, getWidth() * UNLOCK_THRESHOLD, SLIDER_SIZE);
        canvas.drawRoundRect(track, 20, 20, paint);

        // Lock icon body
        paint.setColor(Color.WHITE);
        float centerX = left + SLIDER_SIZE / 2;
        float bodyLeft = centerX - SLIDER_SIZE * 0.4f;
        float bodyTop = top + SLIDER_SIZE * 0.45f;
        float bodyRight = centerX + SLIDER_SIZE * 0.4f;
        float bodyBottom = top + SLIDER_SIZE * 0.95f;
        RectF body = new RectF(bodyLeft, bodyTop, bodyRight, bodyBottom);
        canvas.drawRoundRect(body, 15, 15, paint);

        // Lock icon arc
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12f);
        RectF arc = new RectF(
                centerX - SLIDER_SIZE * 0.25f,
                top + SLIDER_SIZE * 0.05f,
                centerX + SLIDER_SIZE * 0.25f,
                top + SLIDER_SIZE * 0.6f
        );
        canvas.drawArc(arc, 180, 180, false, paint);
        paint.setStyle(Paint.Style.FILL);

        // Hint text
        paint.setColor(Color.WHITE);
        paint.setTextSize(32f);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("slide to unlock →", left + SLIDER_SIZE + 10, SLIDER_SIZE * 0.65f, paint);
    }
}