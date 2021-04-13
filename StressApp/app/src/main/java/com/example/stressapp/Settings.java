package com.example.stressapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class Settings extends AppCompatActivity {
    private static final String PREFS = "prefs";
    private String Theme;
    private static final double MaxBright = 255.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Change appearance based on Theme
        SharedPreferences preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        String Theme = preferences.getString("Theme", "defaultreturn");

        super.onCreate(savedInstanceState);
        int ImageSrc = R.drawable.titleimagee91e63_com;

        //Switch case for theme switching
        switch(Theme){
            case "Dark Theme":
                setTheme(R.style.Dark);
                ImageSrc = R.drawable.settingswhite;
                break;
            case "Red Theme":
                setTheme(R.style.LightPink);
                ImageSrc = R.drawable.settingsred;
                break;

        }
        //Auto Switch to dark theme if screen brightness is < 30% of max brightness
        try {
            float curBrightnessValue=android.provider.Settings.System.getInt(
                    getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
            if(curBrightnessValue < MaxBright * 0.3){
                setTheme(R.style.Dark);
                ImageSrc = R.drawable.settingswhite;
            }

        } catch (android.provider.Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_settings);


        ImageView TitleImage = findViewById(R.id.TitleImage);
        TitleImage.setImageResource(ImageSrc);



        Spinner spinner = findViewById(R.id.ThemeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Themes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setTheme(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




    }

    private void setTheme(String theme){
        Theme = theme;
    }

    public void OnStartClick(View view){

        SharedPreferences.Editor editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
        editor.putString("Theme", Theme);
        editor.apply();

        Intent intent = getIntent();
        finish();

        startActivity(intent);
    }

    public void OnDevClick(View v){
        Intent intent = new Intent(this, DeveloperArea.class);
        startActivity(intent);



    }


}
