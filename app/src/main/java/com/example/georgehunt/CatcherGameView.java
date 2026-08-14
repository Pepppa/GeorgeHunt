package com.example.georgehunt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;

public class CatcherGameView extends View {

    private final UnlockListener unlockListener;
    private final Paint paint;
    private final Vibrator vibrator;

    // Размеры
    private float catSize;      // радиус кошки
    private float mouseSize;    // радиус мышки

    // Кошка — фиксированная, по центру экрана
    private float catX, catY;
    private boolean mouthOpen = true;

    // Мышка
    private float mouseX, mouseY;
    private boolean isDragging = false;
    private int draggingPointerId = -1;

    // Слайдер (тот же что в GameView)
    private float sliderX = 0;
    private boolean isDraggingSlider = false;
    private int sliderPointerId = -1;
    private static final float SLIDER_SIZE = 120f;
    private static final float UNLOCK_THRESHOLD = 2f / 3f;
    private long lastVibrateTime = 0;
    private SoundPool soundPool;
    private int soundMeow;
    private int soundPi;
    private long lastSoundTime = 0;

    private float catSpeedX = 0, catSpeedY = 0;
    private float mouseSpeedX = 0, mouseSpeedY = 0;
    private final Handler handler = new Handler();
    private final Runnable gameLoop = new Runnable() {
        @Override
        public void run() {
            // Движение кошки
            if (catSpeedX != 0 || catSpeedY != 0) {
                catX += catSpeedX;
                catY += catSpeedY;
                if (catX - catSize < 0 || catX + catSize > getWidth()) catSpeedX = -catSpeedX;
                if (catY - catSize < 0 || catY + catSize > getHeight()) catSpeedY = -catSpeedY;
            }

            // Движение мышки (только если не тащим)
            if (!isDragging && (mouseSpeedX != 0 || mouseSpeedY != 0)) {
                mouseX += mouseSpeedX;
                mouseY += mouseSpeedY;
                if (mouseX - mouseSize < 0 || mouseX + mouseSize > getWidth()) mouseSpeedX = -mouseSpeedX;
                if (mouseY - mouseSize < 0 || mouseY + mouseSize > getHeight()) mouseSpeedY = -mouseSpeedY;
            }

            // Вибрация и писк
            if (isDragging) {
                float d = dist(mouseX, mouseY, catX, catY);
                float maxDist = (float) Math.sqrt(getWidth() * getWidth() + getHeight() * getHeight());
                float proximity = 1f - Math.min(d / maxDist, 1f);
                long interval = (long) (600 - proximity * 520);
                long now = System.currentTimeMillis();
                if (now - lastVibrateTime > interval) {
                    vibrator.vibrate(25);
                    lastVibrateTime = now;
                }
                if (now - lastSoundTime > interval) {
                    soundPool.play(soundPi, 1, 1, 0, 0, 1);
                    lastSoundTime = now;
                }
            }

            invalidate();
            handler.postDelayed(this, 16);
        }
    };
    public CatcherGameView(Context context, UnlockListener unlockListener) {
        super(context);
        this.unlockListener = unlockListener;
        paint = new Paint();
        paint.setAntiAlias(true);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        android.media.AudioAttributes attrs = new android.media.AudioAttributes.Builder()
                .setUsage(android.media.AudioAttributes.USAGE_GAME)
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(2)
                .setAudioAttributes(attrs)
                .build();
        float catSpeed = CatcherSettings.toSpeed(CatcherSettings.loadCatSpeedProgress(context));
        float mouseSpeed = CatcherSettings.toSpeed(CatcherSettings.loadMouseSpeedProgress(context));

        if (catSpeed > 0) {
            catSpeedX = catSpeed;
            catSpeedY = catSpeed * 0.7f;
        }
        if (mouseSpeed > 0) {
            mouseSpeedX = mouseSpeed;
            mouseSpeedY = mouseSpeed * 0.7f;
        }
        soundMeow = soundPool.load(context, R.raw.meow, 1);
        soundPi = soundPool.load(context, R.raw.pi, 1);
        handler.post(gameLoop);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float screenMin = Math.min(w, h);
        catSize = screenMin * 0.18f;
        mouseSize = screenMin * 0.07f;

        // Кошка по центру
        catX = w / 2f;
        catY = h / 2f;

        // Мышка в случайном месте (не слишком близко к краям)
        spawnMouse(w, h);
    }

    private void spawnMouse(int w, int h) {
        float margin = mouseSize * 2;
        float newX, newY;
        // Спауним вдали от кошки
        do {
            newX = margin + (float) Math.random() * (w - margin * 2);
            newY = margin + (float) Math.random() * (h - margin * 2);
        } while (dist(newX, newY, catX, catY) < catSize * 2.5f);
        mouseX = newX;
        mouseY = newY;
        mouthOpen = true;
    }

    private float dist(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int actionIndex = event.getActionIndex();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                float x = event.getX(actionIndex);
                float y = event.getY(actionIndex);
                int pointerId = event.getPointerId(actionIndex);

                if (isOnSlider(x, y)) {
                    isDraggingSlider = true;
                    sliderPointerId = pointerId;
                } else if (!isDragging && dist(x, y, mouseX, mouseY) < mouseSize * 1.5f) {
                    isDragging = true;
                    draggingPointerId = pointerId;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Слайдер
                if (isDraggingSlider && sliderPointerId != -1) {
                    int idx = event.findPointerIndex(sliderPointerId);
                    if (idx != -1) {
                        sliderX = Math.max(0, event.getX(idx) - SLIDER_SIZE / 2);
                        if (sliderX > getWidth() * UNLOCK_THRESHOLD) {
                            vibrator.vibrate(200);
                            unlockListener.onUnlock();
                        }
                    }
                }
                // Мышка
                if (isDragging && draggingPointerId != -1) {
                    int idx = event.findPointerIndex(draggingPointerId);
                    if (idx != -1) {
                        mouseX = event.getX(idx);
                        mouseY = event.getY(idx);
                        // Проверяем попадание в рот кошки
                        if (dist(mouseX, mouseY, catX, catY) < catSize * 0.6f) {
                            onMouseCaught();
                        }
                    }
                }
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                int pointerId = event.getPointerId(actionIndex);
                if (pointerId == sliderPointerId) {
                    isDraggingSlider = false;
                    sliderPointerId = -1;
                    sliderX = 0;
                }
                if (pointerId == draggingPointerId) {
                    isDragging = false;
                    draggingPointerId = -1;
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
                isDraggingSlider = false;
                sliderPointerId = -1;
                sliderX = 0;
                isDragging = false;
                draggingPointerId = -1;
                break;
        }
        return true;
    }

    private void onMouseCaught() {
        isDragging = false;
        draggingPointerId = -1;
        mouthOpen = false;
        vibrator.vibrate(80);
        soundPool.play(soundMeow, 1, 1, 0, 0, 1);

        handler.postDelayed(() -> {
            spawnMouse(getWidth(), getHeight());
            invalidate();
        }, 600);
    }

    private boolean isOnSlider(float x, float y) {
        return new RectF(sliderX, 0, sliderX + SLIDER_SIZE, SLIDER_SIZE).contains(x, y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#1a4a1a")); // тёмно-зелёный фон

        // Кошка
        CatDrawer.draw(canvas, paint, catX, catY, catSize, mouthOpen);

        // Мышка
        MouseDrawer.draw(canvas, paint, mouseX, mouseY, mouseSize);

        // Слайдер
        drawLockSlider(canvas);
    }

    private void drawLockSlider(Canvas canvas) {
        paint.setColor(Color.argb(100, 255, 255, 255));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(new RectF(0, 0, getWidth() * UNLOCK_THRESHOLD, SLIDER_SIZE), 20, 20, paint);

        float centerX = sliderX + SLIDER_SIZE / 2;
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(new RectF(centerX - SLIDER_SIZE * 0.4f, SLIDER_SIZE * 0.45f,
                centerX + SLIDER_SIZE * 0.4f, SLIDER_SIZE * 0.95f), 15, 15, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(12f);
        canvas.drawArc(new RectF(centerX - SLIDER_SIZE * 0.25f, SLIDER_SIZE * 0.05f,
                centerX + SLIDER_SIZE * 0.25f, SLIDER_SIZE * 0.6f), 180, 180, false, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(32f);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("slide to finish →", sliderX + SLIDER_SIZE + 10, SLIDER_SIZE * 0.65f, paint);
    }
}