package com.example.georgehunt;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CatcherSettingsActivity extends AppCompatActivity {

    private SeekBar seekBarMouseSpeed;
    private SeekBar seekBarCatSpeed;
    private TextView tvMouseSpeedLabel;
    private TextView tvCatSpeedLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catcher_settings);

        seekBarMouseSpeed = findViewById(R.id.seekBarMouseSpeed);
        seekBarCatSpeed = findViewById(R.id.seekBarCatSpeed);
        tvMouseSpeedLabel = findViewById(R.id.tvMouseSpeedLabel);
        tvCatSpeedLabel = findViewById(R.id.tvCatSpeedLabel);
        Button btnSave = findViewById(R.id.btnSave);

        seekBarMouseSpeed.setProgress(CatcherSettings.loadMouseSpeedProgress(this));
        seekBarCatSpeed.setProgress(CatcherSettings.loadCatSpeedProgress(this));

        tvMouseSpeedLabel.setText(CatcherSettings.speedLabel(seekBarMouseSpeed.getProgress()));
        tvCatSpeedLabel.setText(CatcherSettings.speedLabel(seekBarCatSpeed.getProgress()));

        seekBarMouseSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvMouseSpeedLabel.setText(CatcherSettings.speedLabel(progress));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarCatSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvCatSpeedLabel.setText(CatcherSettings.speedLabel(progress));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSave.setOnClickListener(v -> {
            CatcherSettings.save(this,
                    seekBarMouseSpeed.getProgress(),
                    seekBarCatSpeed.getProgress());
            finish();
        });
    }
}