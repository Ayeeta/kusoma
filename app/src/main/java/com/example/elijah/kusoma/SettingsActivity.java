package com.example.elijah.kusoma;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.elijah.kusoma.eventbus.SettingsChangeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences mSettingsPreferences;

    private SeekBar seekBar_pitch, seekBar_speed;

    private float mPitch;
    private float mSpeed;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mSettingsPreferences = getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);

        seekBar_pitch = findViewById(R.id.pitch_seek_bar);
        seekBar_speed = findViewById(R.id.speed_seek_bar);

        mPitch = mSettingsPreferences.getFloat(getString(R.string.spk_pitch), 1.0f);
        mSpeed = mSettingsPreferences.getFloat(getString(R.string.spk_speed), 1.0f);

        seekBar_pitch.setProgress((int) (mPitch * 50));
        seekBar_speed.setProgress((int) (mSpeed * 50));

        seekBar_pitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float pitch = (float) i / 50;
                if (pitch < 0.1) pitch = 0.1f;
                float speed = (float) seekBar_speed.getProgress() / 50;
                if (speed < 0.1) speed = 0.1f;

                EventBus.getDefault().post(new SettingsChangeEvent(pitch, speed));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_speed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float pitch = (float) seekBar_pitch.getProgress() / 50;
                if (pitch < 0.1) pitch = 0.1f;
                float speed = (float) i / 50;
                if (speed < 0.1) speed = 0.1f;

                EventBus.getDefault().post(new SettingsChangeEvent(pitch, speed));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingsChangeEvent(SettingsChangeEvent settingsChangeEvent) {
        SharedPreferences.Editor editor = mSettingsPreferences.edit();
        editor.putFloat(getString(R.string.spk_pitch), settingsChangeEvent.mPitch);
        editor.putFloat(getString(R.string.spk_speed), settingsChangeEvent.mSpeed);
        editor.apply();
    }
}
