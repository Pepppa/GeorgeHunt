package com.example.georgehunt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnKolobok = findViewById(R.id.btnKolobok);
        Button btnCatcher = findViewById(R.id.btnCatcher);
        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnExit = findViewById(R.id.btnExit);

        btnKolobok.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        btnCatcher.setOnClickListener(v ->
                startActivity(new Intent(this, CatcherActivity.class)));

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsMenuActivity.class)));

        btnExit.setOnClickListener(v -> finishAffinity());
    }
}