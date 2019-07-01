package com.example.elijah.kusoma;

import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.elijah.kusoma.eventbus.SettingsChangeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mSettingsPreferences;

    private ViewFlipper viewFlipper;
    public TextToSpeech textToSpeech;
    private TextView textView;

    private Button btnPrev;
    private Button btnSay;
    private Button btnNext;

    private float mPitch;
    private float mSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        mSettingsPreferences = getSharedPreferences(getString(R.string.shared_pref_key), MODE_PRIVATE);

        mPitch = mSettingsPreferences.getFloat(getString(R.string.spk_pitch), 1.0f);
        mSpeed = mSettingsPreferences.getFloat(getString(R.string.spk_speed), 1.0f);

        viewFlipper = findViewById(R.id.view_flipper);

        btnPrev = findViewById(R.id.btn_prev);
        btnSay = findViewById(R.id.btn_say);
        btnNext = findViewById(R.id.btn_nxt);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.UK);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("Text To Speech", "Language not supported");
                    }
                } else {
                    Log.e("Text To Speech", "Initialization failed");
                }

            }
        });
        textToSpeech.setPitch(mPitch);
        textToSpeech.setSpeechRate(mSpeed);
    }

    public void prevbtn(View v) {
       viewFlipper.showPrevious();
    }

    public void nextbtn(View v) {
       viewFlipper.showNext();
    }

    public void saybtn(View v) {
        speak();
    }

    private void speak() {
        int i = viewFlipper.getCurrentView().getId();
        textView = findViewById(i);
        String text = textView.getText().toString();
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_mainMenu) {

            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
