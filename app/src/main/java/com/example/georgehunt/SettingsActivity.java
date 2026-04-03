package com.example.georgehunt;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private BallPreviewView ballPreview;
    private SeekBar seekBarSize;
    private SeekBar seekBarSpeed;
    private TextView tvSpeedLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ballPreview = findViewById(R.id.ballPreview);
        seekBarSize = findViewById(R.id.seekBarSize);
        seekBarSpeed = findViewById(R.id.seekBarSpeed);
        tvSpeedLabel = findViewById(R.id.tvSpeedLabel);
        Button btnSave = findViewById(R.id.btnSave);

        // Загрузить сохранённые значения
        seekBarSize.setProgress(GameSettings.loadRadiusProgress(this));
        seekBarSpeed.setProgress(GameSettings.loadSpeedProgress(this));

        // Инициализировать превью и лейбл
        ballPreview.setRadiusFraction(seekBarSize.getProgress() / 100f);
        tvSpeedLabel.setText(GameSettings.speedLabel(seekBarSpeed.getProgress()));

        seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ballPreview.setRadiusFraction(progress / 100f);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSpeedLabel.setText(GameSettings.speedLabel(progress));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSave.setOnClickListener(v -> {
            GameSettings.save(this, seekBarSize.getProgress(), seekBarSpeed.getProgress());
            finish();
        });
    }
}