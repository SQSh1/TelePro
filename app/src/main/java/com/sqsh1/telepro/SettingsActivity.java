package com.sqsh1.telepro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("TeleProPrefs", MODE_PRIVATE);
        CheckBox premiumToggle = findViewById(R.id.premium_toggle);
        premiumToggle.setChecked(prefs.getBoolean("premium_enabled", true));

        premiumToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean("premium_enabled", isChecked).apply();
        });
    }
}
