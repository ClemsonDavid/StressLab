package com.example.stressapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


public class GraphDataGraph extends AppCompatActivity {
    private static final String PREFS = "prefs";
    private static final double MaxBright = 255.0;
    Database db;
    private static final int MaxMonth = 32;
    private String TheTheme;

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
                TheTheme = "Dark";
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
                TheTheme = "Dark";
            }

        } catch (android.provider.Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        //Create Instance of db
        db = new Database(this);

        setContentView(R.layout.activity_graphdatagraph);
        Intent intent = getIntent();
        String Type = intent.getExtras().getString("Type");
        setContents(Type);



    }

    private void setContents(String Type){

        switch(Type){
            case "Eating":
                //Set Titles
                TextView TitleText = findViewById(R.id.GraphTitleText);
                TitleText.setText(R.string.EatingGraphTitle);
                TextView BarChartText = findViewById(R.id.WeeklyBarText);
                BarChartText.setText(R.string.EatWeek);
                TextView MonthLineChartText = findViewById(R.id.MonthlyLineText);
                MonthLineChartText.setText(R.string.EatMonth);

                //Daily Set
                    TextView DailyText = findViewById(R.id.DailyText);
                    Log.d("Before returnEat","Before");
                    String[][] monthlyData = db.returnEat();
                    for(int i = 1; i<monthlyData.length; i++){
                        Log.d("Date", ""+ monthlyData[i][0]);
                        Log.d("Calorie", ""+ monthlyData[i][1]);
                    }
                    SimpleDateFormat formatDate = new SimpleDateFormat("dd");
                    Date date = new Date();
                    Log.d("Today",""+formatDate.format(date));
                    int Today = Integer.parseInt(formatDate.format(date));
                    if(monthlyData[Today][0] == null){
                        DailyText.setText(R.string.NullEating);
                    }else {
                        String Daily = getString(R.string.DailyEatingBlurbFirst) + "" + monthlyData[Today][1] + getString(R.string.DailyEatingBlurbSecond) + " " + monthlyData[Today][0];
                        DailyText.setText(Daily);
                    }
                //Weekly Eat Data BARRRRRRRRCHARRRRT

                    //BarChart for placing data
                    BarChart WeekChart = findViewById(R.id.WeekBarChart);



                    //BarChart Entries
                    ArrayList<BarEntry> values = new ArrayList<>();
                    for(int i = Today; i>1 && i >= Today-6; i--){
                        if(monthlyData[i][0] == null){
                            values.add(new BarEntry(i,0));
                        }else{
                            values.add(new BarEntry(i,(float) Integer.parseInt("0"+monthlyData[i][1])));
                        }
                    }

                    //Do Not touch this logic too much please
                    BarDataSet FirstSet = new BarDataSet(values, "Past 7 Days");

                    BarData data = new BarData(FirstSet);

                    WeekChart.setData(data);

                    FirstSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);

                    if(TheTheme == "Dark"){
                        FirstSet.setValueTextColor(Color.WHITE);
                        WeekChart.getAxisLeft().setTextColor(Color.WHITE);
                        WeekChart.getAxisRight().setTextColor(Color.WHITE);
                        WeekChart.getLegend().setTextColor(Color.WHITE);
                        WeekChart.getXAxis().setTextColor(Color.WHITE);
                    }else{
                        FirstSet.setValueTextColor(Color.BLACK);
                    }

                    FirstSet.setValueTextSize(10f);

                    WeekChart.getDescription().setEnabled(false);



                //Monthly Line Graph
                    //Variables for Line Graph
                    LineChart MonthChart = findViewById(R.id.MonthLineGraph);

                    LineData DataforLine;

                    LineDataSet SetforLine;

                    //LineChart Entries
                    ArrayList<Entry> linevalues = new ArrayList<>();
                    for(int i = MaxMonth-1; i>1; i--){

                        if(i > Today){
                            linevalues.add(new Entry(i,0));
                        }else{
                            linevalues.add(new Entry(i,(float) Integer.parseInt("0"+monthlyData[i][1])));
                        }
                    }


                    //Bug where linegraph entries need to be sorted by X entry, found it from the author fix post from 2016
                    Collections.sort(linevalues, new EntryXComparator());

                    SetforLine = new LineDataSet(linevalues, "Last 30 days");
                    DataforLine = new LineData(SetforLine);

                    MonthChart.setData(DataforLine);
                    SetforLine.setColor(ColorTemplate.MATERIAL_COLORS[0]);
                    if(TheTheme == "Dark"){
                        SetforLine.setValueTextColor(Color.WHITE);
                        MonthChart.getAxisLeft().setTextColor(Color.WHITE);
                        MonthChart.getAxisRight().setTextColor(Color.WHITE);
                        MonthChart.getLegend().setTextColor(Color.WHITE);
                        MonthChart.getXAxis().setTextColor(Color.WHITE);
                    }else{
                        SetforLine.setValueTextColor(Color.BLACK);
                    }
                    SetforLine.setValueTextSize(10f);





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



}
