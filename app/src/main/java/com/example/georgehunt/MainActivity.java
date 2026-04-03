package com.example.georgehunt;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements UnlockListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(new GameView(this, this));

        // Lock the app - prevent minimizing
        startLockTask();
    }

    @Override
    public void onUnlock() {
        stopLockTask();
        finish();
    }
}