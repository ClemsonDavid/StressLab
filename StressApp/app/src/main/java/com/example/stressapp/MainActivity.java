package com.example.stressapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS = "prefs";
    private static final double MaxBright = 255.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Change appearance based on Theme
        SharedPreferences preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        String Theme = preferences.getString("Theme", "defaultreturn");


        int ImageSrc = R.drawable.titleimagee91e63_com;

        //Switch case for theme switching
        switch(Theme){
            case "Dark Theme":
                setTheme(R.style.Dark);
                ImageSrc = R.drawable.titleimagewhite_com;
                break;
            case "Red Theme":
                setTheme(R.style.LightPink);
                ImageSrc = R.drawable.titleimagee91e63_com;
                break;

        }

        //Auto Switch to dark theme if screen brightness is < 30% of max brightness
        try {
            float curBrightnessValue=android.provider.Settings.System.getInt(
                    getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
            if(curBrightnessValue < MaxBright * 0.3){
                setTheme(R.style.Dark);
                ImageSrc = R.drawable.titleimagewhite_com;
            }

        } catch (android.provider.Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }


        setContentView(R.layout.activity_main);


        ImageView TitleImage = findViewById(R.id.TitleImage);
        TitleImage.setImageResource(ImageSrc);









        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new MainFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }


    //Create menu taken from zybooks
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Action_Settings:
                //Settings Selected
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;

            case R.id.Action_Input:
                // Input selected
                intent = new Intent(this, InputDataTitle.class);
                startActivity(intent);
                return true;

            case R.id.Action_Graph:
                //Graph selected
                intent = new Intent(this, GraphDataTitle.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public void OnStartClick(View view){
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
}