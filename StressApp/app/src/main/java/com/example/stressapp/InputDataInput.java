
package com.example.stressapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
        long success = db.addEat(Total);
        Log.d("After DB Insert", "");

        if(success != -1){
            Context context = getApplicationContext();
            CharSequence text = "Input Recorded!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            Context context = getApplicationContext();
            CharSequence text = "Error in Input!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }



    public void SleepConfirm(View v){
            TimePicker WakePicker = findViewById(R.id.WakePicker);
            TimePicker SleepPicker = findViewById(R.id.SleepPicker);
            int wakeHour, wakeMin, sleepHour, sleepMin, wakeAM, sleepAM;
            Log.d("Test Time Hour", ""+WakePicker.getHour());
            Log.d("Test Time Hour", ""+WakePicker.getMinute());

            wakeHour = WakePicker.getHour();
            sleepHour = SleepPicker.getHour();
            wakeMin = WakePicker.getMinute();
            sleepMin = SleepPicker.getMinute();
            //Get Am or Pm
            if(wakeHour > 12){
                wakeHour -= 12;
                wakeAM = 0;
            }else{
                wakeAM = 1;
            }
            if(sleepHour > 12){
                sleepHour -= 12;
                sleepAM = 0;
            }else{
                sleepAM = 1;
            }

        long success = db.addSleep(wakeHour, wakeMin, wakeAM, sleepHour, sleepMin, sleepAM);
        Log.d("After DB Insert", "");

        if(success != -1){
            Context context = getApplicationContext();
            CharSequence text = "Input Recorded!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            Context context = getApplicationContext();
            CharSequence text = "Error in Input!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }




    }


    public void ExerciseConfirm(View v){
        EditText EditHours = findViewById(R.id.EditHours);
        EditText EditMins = findViewById(R.id.EditMins);
        String Hours = EditHours.getText().toString();
        String Mins = EditMins.getText().toString();
        int hours, mins;
        hours = Integer.parseInt("0"+Hours);
        mins = Integer.parseInt("0"+Mins);
        if(hours >24){
            hours = 24;
        }
        if(mins > 59){
            mins = 59;
        }

        long success = db.addExcercise(hours, mins);
        Log.d("After DB Insert", "");

        if(success != -1){
            Context context = getApplicationContext();
            CharSequence text = "Input Recorded!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            Context context = getApplicationContext();
            CharSequence text = "Error in Input!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }


    }



    /*TODO
       Get Data and put into SQL database
    */
    public void SocialConfirm(View v){
        EditText EditHours = findViewById(R.id.EditHours);
        EditText EditMins = findViewById(R.id.EditMins);
        EditText EditNumPpl = findViewById(R.id.EditNumPpl);
        String Hours = EditHours.getText().toString();
        String Mins = EditMins.getText().toString();
        String NumPpl = EditNumPpl.getText().toString();
        int hours, mins, numPpl;
        hours = Integer.parseInt("0"+Hours);
        mins = Integer.parseInt("0"+Mins);
        numPpl = Integer.parseInt("0"+NumPpl);
        if(hours >24){
            hours = 24;
        }
        if(mins > 59){
            mins = 59;
        }

        Log.d("Test before insert", "hours: "+hours+" mins: "+mins+" numPpl: "+numPpl);
        long success = db.addSocial(hours, mins, numPpl);
        Log.d("After DB Insert", "");

        if(success != -1){
            Context context = getApplicationContext();
            CharSequence text = "Input Recorded!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            Context context = getApplicationContext();
            CharSequence text = "Error in Input!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }




    }



    public void FinanceConfirm(View v){
        EditText EditMoney = findViewById(R.id.EditMoney);
        String Money = EditMoney.getText().toString();
        double money;
        money = Double.parseDouble("0"+Money);
        money = Math.round(money * 100.0)/ 100.0;
        Log.d("Test before insert", ""+money);


        long success = db.addFinance(money);
        Log.d("After DB Insert", "");

        if(success != -1){
            Context context = getApplicationContext();
            CharSequence text = "Input Recorded!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            Context context = getApplicationContext();
            CharSequence text = "Error in Input!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }


    }


    public void MentalConfirm(View v){

        Log.d("Which pic", ""+v.getTag());
        String FaceType = v.getTag().toString();
        long success = -1;
        switch (FaceType){
            case "Sad":
                success = db.addMood(0);
                break;
            case "Neutral":
                success = db.addMood(1);
                break;
            case "Happy":
                success = db.addMood(2);
                break;
        }

        if(success != -1){
            Context context = getApplicationContext();
            CharSequence text = "Input Recorded!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }else{
            Context context = getApplicationContext();
            CharSequence text = "Error in Input!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }






}