package com.example.stressapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class GraphDataInput extends AppCompatActivity {
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

        //Create Instance of db
        db = new Database(this);

        Intent intent = getIntent();
        String Type = intent.getExtras().getString("Type");
        switch(Type){
            case "Eating":
                setContentView(R.layout.activity_eatinginput);
                break;
            case "Sleeping":
                setContentView(R.layout.activity_sleepinginput);
                break;
            case "Exercise":
                setContentView(R.layout.activity_excerciseinput);
                break;
            case "Social":
                setContentView(R.layout.activity_socialinput);
                break;
            case "Finance":
                setContentView(R.layout.activity_financesinput);
                break;
            case "Mental":
                setContentView(R.layout.activity_mentalinput);
                break;
        }

    }

}
