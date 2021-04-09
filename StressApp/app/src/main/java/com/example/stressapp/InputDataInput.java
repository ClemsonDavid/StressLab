package com.example.stressapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InputDataInput extends AppCompatActivity {
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
                break;
            case "Red Theme":
                setTheme(R.style.LightPink);
                break;

        }
        setContentView(R.layout.activity_inputdatainput);
        Intent intent = getIntent();
        String Type = intent.getExtras().getString("Type");
        switch(Type){
            case "Eating":
                EatingLayout();
                break;
            case "Sleeping":

                break;
            case "Exercise":

                break;
            case "Social":

                break;
            case "Finance":

                break;
            case "Mental":

                break;
        }



    }

    /*TODO Finsih up other type layouts with appropriate edittexts to grab info
        After that, set up SQLite for datastorage
     */

    private void EatingLayout(){
        setContentView(R.layout.activity_eatinginput);
        TextView view = findViewById(R.id.InputTitleTextInput);
        view.setText("Eating");
    }




}
