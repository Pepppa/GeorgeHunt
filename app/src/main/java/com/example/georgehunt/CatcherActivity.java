package com.example.georgehunt;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class CatcherActivity extends AppCompatActivity implements UnlockListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(new CatcherGameView(this, this));
        startLockTask();
    }

    @Override
    public void onBackPressed() {
        // заблокировано
    }

    @Override
    public void onUnlock() {
        stopLockTask();
        finish();
    }
}