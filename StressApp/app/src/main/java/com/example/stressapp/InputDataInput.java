
package com.example.stressapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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

        Intent intent = getIntent();
        String Type = intent.getExtras().getString("Type");
        switch(Type){
            case "Eating":
                EatingLayout();
                break;
            case "Sleeping":
                SleepingLayout();
                break;
            case "Exercise":
                ExcerciseLayout();
                break;
            case "Social":
                SocialInterLayout();
                break;
            case "Finance":
                FinancesLayout();
                break;
            case "Mental":
                MentalLayout();
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
    private void EatingLayout(){
        setContentView(R.layout.activity_eatinginput);

    }


    /*TODO
        Get Data from fragment and display, then put into database on confirm
     */
    private void SleepingLayout(){
        setContentView(R.layout.activity_sleepinginput);
    }

    /*TODO
        Get Data from edittext
        Round down to 24 or 59 respectively
     */

    private void ExcerciseLayout(){
        setContentView(R.layout.activity_excerciseinput);
    }

    /*TODO
       Get Data and put into SQL database
    */
    private void SocialInterLayout(){
        setContentView(R.layout.activity_socialinput);
    }


    /*TODO
        Get Data and put into SQL database
        round to 0.00 place
     */
    private void FinancesLayout(){
        setContentView(R.layout.activity_financesinput);
    }

    /*TODO
        Get Data and put into SQL database
        Set Up Input
     */
    private void MentalLayout(){
        setContentView(R.layout.activity_mentalinput);
    }


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }



}