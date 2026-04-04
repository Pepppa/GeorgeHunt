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
    private TextView tvSizeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ballPreview = findViewById(R.id.ballPreview);
        seekBarSize = findViewById(R.id.seekBarSize);
        seekBarSpeed = findViewById(R.id.seekBarSpeed);
        tvSpeedLabel = findViewById(R.id.tvSpeedLabel);
        tvSizeLabel = findViewById(R.id.tvSizeLabel);
        Button btnSave = findViewById(R.id.btnSave);

        seekBarSize.setProgress(GameSettings.loadRadiusProgress(this));
        seekBarSpeed.setProgress(GameSettings.loadSpeedProgress(this));

        ballPreview.setRadiusProgress(seekBarSize.getProgress());
        tvSpeedLabel.setText(GameSettings.speedLabel(seekBarSpeed.getProgress()));

        // Размер в пикселях можно узнать только после layout
        ballPreview.post(() -> updateSizeLabel(seekBarSize.getProgress()));

        seekBarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ballPreview.setRadiusProgress(progress);
                updateSizeLabel(progress);
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

    private void updateSizeLabel(int progress) {
        int px = Math.round(ballPreview.getCurrentRadius());
        tvSizeLabel.setText(px + " px");
    }
}