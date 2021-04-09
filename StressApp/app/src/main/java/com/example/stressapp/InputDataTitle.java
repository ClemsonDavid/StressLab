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

public class InputDataTitle extends AppCompatActivity {
    private static final String PREFS = "prefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Change appearance based on Theme
        SharedPreferences preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        String Theme = preferences.getString("Theme", "defaultreturn");

        //Switch case for theme switching
        switch(Theme){
            case "Dark Theme":
                setTheme(R.style.Dark);
                Log.d("onResume", "Popping Dark");
                break;
            case "Red Theme":
                setTheme(R.style.LightPink);
                Log.d("onResume", "Popping Red");
                break;

        }
        setContentView(R.layout.activity_inputdatatitle);







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
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;

            case R.id.Action_Input:
                // Logout selected
                return true;

            case R.id.Action_Graph:
                // About selected
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }




}