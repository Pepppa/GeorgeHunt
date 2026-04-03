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

        Button btnGame = findViewById(R.id.btnGame);
        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnExit = findViewById(R.id.btnExit);

        btnGame.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        btnExit.setOnClickListener(v -> finishAffinity());
    }
}