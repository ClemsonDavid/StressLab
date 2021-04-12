
package com.example.stressapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class InputDataInput extends AppCompatActivity {
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
        switch(Theme){
            case "Dark Theme":
                setTheme(R.style.Dark);
                break;
            case "Red Theme":
                setTheme(R.style.LightPink);
                break;

        }
        //Auto Switch to dark theme if screen brightness is < 30% of max brightness
        try {
            float curBrightnessValue=android.provider.Settings.System.getInt(
                    getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
            if(curBrightnessValue < MaxBright * 0.3){
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

    /*TODO
        -Finsih up other type layouts with appropriate edittexts to grab info
        -After that, set up SQLite for datastorage
        -Make all default returns
     */


    /*TODO
        Get Data and put into SQL database
     */

    public void EatConfirm(View v){

        EditText BreakfastText = findViewById(R.id.EditBreakfast);
        String Breakfast = BreakfastText.getText().toString();
        Log.d("Test Get Breakfast", ""+Breakfast);
        EditText LunchText = findViewById(R.id.EditLunch);
        String Lunch = LunchText.getText().toString();
        Log.d("Test Get Lunch", ""+Lunch);
        EditText DinnerText = findViewById(R.id.EditDinner);
        String Dinner = DinnerText.getText().toString();
        Log.d("Test Get Dinner", ""+Dinner);

        int Total = Integer.parseInt("0"+Breakfast) + Integer.parseInt("0"+Lunch) + Integer.parseInt("0"+Dinner);
        Log.d("Test Total", ""+Total);

        Log.d("Before DB Insert", "");
        db.addEat(Total);
        Log.d("After DB Insert", "");


    }




    /*TODO
        Get Data from fragment and display, then put into database on confirm
     */
    private void SleepingLayout(){

    }

    /*TODO
        Get Data from edittext
        Round down to 24 or 59 respectively
     */

    private void ExcerciseLayout(){

    }

    /*TODO
       Get Data and put into SQL database
    */
    private void SocialInterLayout(){

    }


    /*TODO
        Get Data and put into SQL database
        round to 0.00 place
     */
    private void FinancesLayout(){

    }

    /*TODO
        Get Data and put into SQL database
        Set Up Input
     */
    private void MentalLayout(){

    }


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }



}