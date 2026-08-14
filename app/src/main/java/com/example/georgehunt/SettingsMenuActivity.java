package com.example.georgehunt;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        Button btnKolobok = findViewById(R.id.btnSettingsKolobok);
        Button btnCatcher = findViewById(R.id.btnSettingsCatcher);
        Button btnBack = findViewById(R.id.btnBack);

        btnKolobok.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        btnCatcher.setOnClickListener(v ->
                startActivity(new Intent(this, CatcherSettingsActivity.class)));

        btnBack.setOnClickListener(v -> finish());
    }
}