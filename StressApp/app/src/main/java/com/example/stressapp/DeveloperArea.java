package com.example.stressapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DeveloperArea extends AppCompatActivity {
    private static final String PREFS = "prefs";
    private static final double MaxBright = 255.0;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Change appearance based on Theme
        SharedPreferences preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        String Theme = preferences.getString("Theme", "defaultreturn");


        //Switch case for theme switching
        switch (Theme) {
            case "Dark Theme":
                setTheme(R.style.Dark);
                break;
            case "Red Theme":
                setTheme(R.style.LightPink);
                break;

        }

        //Auto Switch to dark theme if screen brightness is < 30% of max brightness
        try {
            float curBrightnessValue = android.provider.Settings.System.getInt(
                    getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
            if (curBrightnessValue < MaxBright * 0.3) {
                setTheme(R.style.Dark);
            }

        } catch (android.provider.Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        db = new Database(this);

        setContentView(R.layout.activity_dev);





    }

    public void OnGenerateClick(View v){
        db.BuildEatData();
        db.BuildSleepData();
        db.BuildExcerciseData();
        db.BuildFinanceData();
        db.BuildSocialData();
        db.BuildMoodData();

    }

}
