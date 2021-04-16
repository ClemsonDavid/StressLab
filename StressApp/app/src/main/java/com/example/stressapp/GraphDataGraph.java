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

    /*TODO closing thoughts
        Need to think about possibly a new layout file for certain types
            -Social
            -Mood
 */


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

                    if(monthlyData[1][0] == null){
                        DailyText.setText(R.string.NullEating);
                        break;
                    }else if(monthlyData[2][0] == null){
                        String Daily = getString(R.string.DailyEatingBlurbFirst) + "" + monthlyData[1][1] + getString(R.string.DailyEatingBlurbSecond) + " " + monthlyData[1][0];
                        DailyText.setText(Daily);
                        break;
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
                            values.add(new BarEntry(i,(float) Double.parseDouble("0"+monthlyData[i][1])));
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
                            linevalues.add(new Entry(i,(float) Double.parseDouble("0"+monthlyData[i][1])));
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


                /*
                    TODO sleep Round minutes up to hours for simplicity, and add an area to explain
                 */
                //Set Titles
                TextView SleepTitleText = findViewById(R.id.GraphTitleText);
                SleepTitleText.setText(R.string.SleepGraphTitle);
                TextView SleepBarChartText = findViewById(R.id.WeeklyBarText);
                SleepBarChartText.setText(R.string.SleepWeek);
                TextView SleepMonthLineChartText = findViewById(R.id.MonthlyLineText);
                SleepMonthLineChartText.setText(R.string.SleepMonth);

                //Daily Set
                TextView SleepDailyText = findViewById(R.id.DailyText);
                Log.d("Before returnSleep","Before");
                String[][] SleepmonthlyData = db.returnSleep();
                for(int i = 1; i<SleepmonthlyData.length; i++){
                    Log.d("Date", ""+ SleepmonthlyData[i][0]);
                    Log.d("Calorie", ""+ SleepmonthlyData[i][1]);
                }
                SimpleDateFormat SleepformatDate = new SimpleDateFormat("dd");
                Date Sleepdate = new Date();
                Log.d("Today",""+SleepformatDate.format(Sleepdate));
                int SleepToday = Integer.parseInt(SleepformatDate.format(Sleepdate));



                if(SleepmonthlyData[1][0] == null){
                    SleepDailyText.setText(R.string.NullSleep);
                    break;
                }else if(SleepmonthlyData[2][0] == null){
                    String SleepDaily = getString(R.string.DailySleepBlurbFirst) + "" + SleepmonthlyData[1][1] + getString(R.string.DailySleepBlurbSecond) + " " + SleepmonthlyData[1][0];
                    SleepDailyText.setText(SleepDaily);
                    break;
                }else {
                    String SleepDaily = getString(R.string.DailySleepBlurbFirst) + "" + SleepmonthlyData[SleepToday][1] + getString(R.string.DailySleepBlurbSecond) + " " + SleepmonthlyData[SleepToday][0];
                    SleepDailyText.setText(SleepDaily);
                }
                //Weekly Sleep Data BARRRRRRRRCHARRRRT

                //BarChart for placing data
                BarChart SleepWeekChart = findViewById(R.id.WeekBarChart);



                //BarChart Entries
                ArrayList<BarEntry> Sleepvalues = new ArrayList<>();
                for(int i = SleepToday; i>1 && i >= SleepToday-6; i--){
                    if(SleepmonthlyData[i][0] == null){
                        Sleepvalues.add(new BarEntry(i,0));
                    }else{
                        Sleepvalues.add(new BarEntry(i,(float) Double.parseDouble("0"+SleepmonthlyData[i][1])));
                    }
                }

                //Do Not touch this logic too much please
                BarDataSet SleepFirstSet = new BarDataSet(Sleepvalues, "Past 7 Days");

                BarData Sleepdata = new BarData(SleepFirstSet);

                SleepWeekChart.setData(Sleepdata);

                SleepFirstSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);

                if(TheTheme == "Dark"){
                    SleepFirstSet.setValueTextColor(Color.WHITE);
                    SleepWeekChart.getAxisLeft().setTextColor(Color.WHITE);
                    SleepWeekChart.getAxisRight().setTextColor(Color.WHITE);
                    SleepWeekChart.getLegend().setTextColor(Color.WHITE);
                    SleepWeekChart.getXAxis().setTextColor(Color.WHITE);
                }else{
                    SleepFirstSet.setValueTextColor(Color.BLACK);
                }

                SleepFirstSet.setValueTextSize(10f);

                SleepWeekChart.getDescription().setEnabled(false);



                //Monthly Line Graph
                //Variables for Line Graph
                LineChart SleepMonthChart = findViewById(R.id.MonthLineGraph);

                LineData SleepDataforLine;

                LineDataSet SleepSetforLine;

                //LineChart Entries
                ArrayList<Entry> Sleeplinevalues = new ArrayList<>();
                for(int i = MaxMonth-1; i>1; i--){
                    if(i > SleepToday){
                        Sleeplinevalues.add(new Entry(i,0));
                    }else{
                        Sleeplinevalues.add(new Entry(i,(float) Double.parseDouble("0"+SleepmonthlyData[i][1])));
                    }
                }


                //Bug where linegraph entries need to be sorted by X entry, found it from the author fix post from 2016
                Collections.sort(Sleeplinevalues, new EntryXComparator());

                SleepSetforLine = new LineDataSet(Sleeplinevalues, "Last 30 days");
                SleepDataforLine = new LineData(SleepSetforLine);

                SleepMonthChart.setData(SleepDataforLine);
                SleepSetforLine.setColor(ColorTemplate.MATERIAL_COLORS[0]);
                if(TheTheme == "Dark"){
                    SleepSetforLine.setValueTextColor(Color.WHITE);
                    SleepMonthChart.getAxisLeft().setTextColor(Color.WHITE);
                    SleepMonthChart.getAxisRight().setTextColor(Color.WHITE);
                    SleepMonthChart.getLegend().setTextColor(Color.WHITE);
                    SleepMonthChart.getXAxis().setTextColor(Color.WHITE);
                }else{
                    SleepSetforLine.setValueTextColor(Color.BLACK);
                }
                SleepSetforLine.setValueTextSize(10f);

                break;
            case "Exercise":
                //Set Titles
                TextView ExcerciseTitleText = findViewById(R.id.GraphTitleText);
                ExcerciseTitleText.setText(R.string.ExcerciseGraphTitle);
                TextView ExcerciseBarChartText = findViewById(R.id.WeeklyBarText);
                ExcerciseBarChartText.setText(R.string.ExcerciseWeek);
                TextView ExcerciseMonthLineChartText = findViewById(R.id.MonthlyLineText);
                ExcerciseMonthLineChartText.setText(R.string.ExcerciseMonth);

                //Daily Set
                TextView ExcerciseDailyText = findViewById(R.id.DailyText);
                Log.d("Before returnExcercise","Before");
                String[][] ExcercisemonthlyData = db.returnExcercise();
                for(int i = 1; i<ExcercisemonthlyData.length; i++){
                    Log.d("Date", ""+ ExcercisemonthlyData[i][0]);
                    Log.d("Calorie", ""+ ExcercisemonthlyData[i][1]);
                }
                SimpleDateFormat ExcerciseformatDate = new SimpleDateFormat("dd");
                Date Excercisedate = new Date();
                Log.d("Today",""+ExcerciseformatDate.format(Excercisedate));
                int ExcerciseToday = Integer.parseInt(ExcerciseformatDate.format(Excercisedate));
                if(ExcercisemonthlyData[1][0] == null){
                    ExcerciseDailyText.setText(R.string.NullExcercise);
                    break;
                }else if(ExcercisemonthlyData[2][0] == null){
                    String ExcerciseDaily = getString(R.string.DailyExcerciseBlurbFirst) + "" + ExcercisemonthlyData[1][1] + getString(R.string.DailyExcerciseBlurbSecond) + " " + ExcercisemonthlyData[1][0];
                    ExcerciseDailyText.setText(ExcerciseDaily);
                    break;
                }else {
                    String ExcerciseDaily = getString(R.string.DailyExcerciseBlurbFirst) + "" + ExcercisemonthlyData[ExcerciseToday][1] + getString(R.string.DailyExcerciseBlurbSecond) + " " + ExcercisemonthlyData[ExcerciseToday][0];
                    ExcerciseDailyText.setText(ExcerciseDaily);
                }
                //Weekly Excercise Data BARRRRRRRRCHARRRRT

                //BarChart for placing data
                BarChart ExcerciseWeekChart = findViewById(R.id.WeekBarChart);



                //BarChart Entries
                ArrayList<BarEntry> Excercisevalues = new ArrayList<>();
                for(int i = ExcerciseToday; i>1 && i >= ExcerciseToday-6; i--){
                    if(ExcercisemonthlyData[i][0] == null){
                        Excercisevalues.add(new BarEntry(i,0));
                    }else{
                        Excercisevalues.add(new BarEntry(i,(float) Double.parseDouble("0"+ExcercisemonthlyData[i][1])));
                    }
                }

                //Do Not touch this logic too much please
                BarDataSet ExcerciseFirstSet = new BarDataSet(Excercisevalues, "Past 7 Days");

                BarData Excercisedata = new BarData(ExcerciseFirstSet);

                ExcerciseWeekChart.setData(Excercisedata);

                ExcerciseFirstSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);

                if(TheTheme == "Dark"){
                    ExcerciseFirstSet.setValueTextColor(Color.WHITE);
                    ExcerciseWeekChart.getAxisLeft().setTextColor(Color.WHITE);
                    ExcerciseWeekChart.getAxisRight().setTextColor(Color.WHITE);
                    ExcerciseWeekChart.getLegend().setTextColor(Color.WHITE);
                    ExcerciseWeekChart.getXAxis().setTextColor(Color.WHITE);
                }else{
                    ExcerciseFirstSet.setValueTextColor(Color.BLACK);
                }

                ExcerciseFirstSet.setValueTextSize(10f);

                ExcerciseWeekChart.getDescription().setEnabled(false);



                //Monthly Line Graph
                //Variables for Line Graph
                LineChart ExcerciseMonthChart = findViewById(R.id.MonthLineGraph);

                LineData ExcerciseDataforLine;

                LineDataSet ExcerciseSetforLine;

                //LineChart Entries
                ArrayList<Entry> Excerciselinevalues = new ArrayList<>();
                for(int i = MaxMonth-1; i>1; i--){

                    if(i > ExcerciseToday){
                        Excerciselinevalues.add(new Entry(i,0));
                    }else{
                        Excerciselinevalues.add(new Entry(i,(float) Double.parseDouble("0"+ExcercisemonthlyData[i][1])));
                    }
                }


                //Bug where linegraph entries need to be sorted by X entry, found it from the author fix post from 2016
                Collections.sort(Excerciselinevalues, new EntryXComparator());

                ExcerciseSetforLine = new LineDataSet(Excerciselinevalues, "Last 30 days");
                ExcerciseDataforLine = new LineData(ExcerciseSetforLine);

                ExcerciseMonthChart.setData(ExcerciseDataforLine);
                ExcerciseSetforLine.setColor(ColorTemplate.MATERIAL_COLORS[0]);
                if(TheTheme == "Dark"){
                    ExcerciseSetforLine.setValueTextColor(Color.WHITE);
                    ExcerciseMonthChart.getAxisLeft().setTextColor(Color.WHITE);
                    ExcerciseMonthChart.getAxisRight().setTextColor(Color.WHITE);
                    ExcerciseMonthChart.getLegend().setTextColor(Color.WHITE);
                    ExcerciseMonthChart.getXAxis().setTextColor(Color.WHITE);
                }else{
                    ExcerciseSetforLine.setValueTextColor(Color.BLACK);
                }
                ExcerciseSetforLine.setValueTextSize(10f);
                break;
            case "Social":
                setContentView(R.layout.activity_graphdatagraphsocial);
                //Set Titles
                TextView SocialTitleText = findViewById(R.id.GraphTitleText);
                SocialTitleText.setText(R.string.SocialGraphTitle);
                TextView SocialBarChartText = findViewById(R.id.WeeklyBarText);
                SocialBarChartText.setText(R.string.SocialWeek);
                TextView SocialMonthLineChartText = findViewById(R.id.MonthlyLineText);
                SocialMonthLineChartText.setText(R.string.SocialMonth);
                TextView SocialBarChartText2 = findViewById(R.id.WeeklyBarText2);
                SocialBarChartText2.setText(R.string.SocialWeek2);
                TextView SocialMonthLineChartText2 = findViewById(R.id.MonthlyLineText2);
                SocialMonthLineChartText2.setText(R.string.SocialMonth2);

                //Daily Set
                TextView SocialDailyText = findViewById(R.id.DailyText);
                Log.d("Before returnSocial","Before");
                String[][] SocialmonthlyData = db.returnSocial();
                for(int i = 1; i<SocialmonthlyData.length; i++){
                    Log.d("Date", ""+ SocialmonthlyData[i][0]);
                    Log.d("Calorie", ""+ SocialmonthlyData[i][1]);
                }
                SimpleDateFormat SocialformatDate = new SimpleDateFormat("dd");
                Date Socialdate = new Date();
                Log.d("Today",""+SocialformatDate.format(Socialdate));
                int SocialToday = Integer.parseInt(SocialformatDate.format(Socialdate));
                if(SocialmonthlyData[1][0] == null){
                    SocialDailyText.setText(R.string.NullSocial);
                    break;
                }else if(SocialmonthlyData[2][0] == null){
                    String SocialDaily = getString(R.string.DailySocialBlurbFirst) + "" + SocialmonthlyData[1][1] + getString(R.string.DailySocialBlurbSecond) + " " + SocialmonthlyData[1][0];
                    SocialDailyText.setText(SocialDaily);
                    break;
                }else {
                    String SocialDaily = getString(R.string.DailySocialBlurbFirst) + "" + SocialmonthlyData[SocialToday][1] + getString(R.string.DailySocialBlurbSecond) + " " + SocialmonthlyData[SocialToday][0];
                    SocialDailyText.setText(SocialDaily);
                }
                //Weekly Social Data BARRRRRRRRCHARRRRT

                //BarChart for placing data
                BarChart SocialWeekChart = findViewById(R.id.WeekBarChart);



                //BarChart Entries
                ArrayList<BarEntry> Socialvalues = new ArrayList<>();
                for(int i = SocialToday; i>1 && i >= SocialToday-6; i--){
                    if(SocialmonthlyData[i][0] == null){
                        Socialvalues.add(new BarEntry(i,0));
                    }else{
                        Socialvalues.add(new BarEntry(i,(float) Double.parseDouble("0"+SocialmonthlyData[i][1])));
                    }
                }

                //Do Not touch this logic too much please
                BarDataSet SocialFirstSet = new BarDataSet(Socialvalues, "Past 7 Days");

                BarData Socialdata = new BarData(SocialFirstSet);

                SocialWeekChart.setData(Socialdata);

                SocialFirstSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);

                if(TheTheme == "Dark"){
                    SocialFirstSet.setValueTextColor(Color.WHITE);
                    SocialWeekChart.getAxisLeft().setTextColor(Color.WHITE);
                    SocialWeekChart.getAxisRight().setTextColor(Color.WHITE);
                    SocialWeekChart.getLegend().setTextColor(Color.WHITE);
                    SocialWeekChart.getXAxis().setTextColor(Color.WHITE);
                }else{
                    SocialFirstSet.setValueTextColor(Color.BLACK);
                }

                SocialFirstSet.setValueTextSize(10f);

                SocialWeekChart.getDescription().setEnabled(false);



                //Monthly Line Graph
                //Variables for Line Graph
                LineChart SocialMonthChart = findViewById(R.id.MonthLineGraph);

                LineData SocialDataforLine;

                LineDataSet SocialSetforLine;

                //LineChart Entries
                ArrayList<Entry> Sociallinevalues = new ArrayList<>();
                for(int i = MaxMonth-1; i>1; i--){

                    if(i > SocialToday){
                        Sociallinevalues.add(new Entry(i,0));
                    }else{
                        Sociallinevalues.add(new Entry(i,(float) Double.parseDouble("0"+SocialmonthlyData[i][1])));
                    }
                }


                //Bug where linegraph entries need to be sorted by X entry, found it from the author fix post from 2016
                Collections.sort(Sociallinevalues, new EntryXComparator());

                SocialSetforLine = new LineDataSet(Sociallinevalues, "Last 30 days");
                SocialDataforLine = new LineData(SocialSetforLine);

                SocialMonthChart.setData(SocialDataforLine);
                SocialSetforLine.setColor(ColorTemplate.MATERIAL_COLORS[0]);
                if(TheTheme == "Dark"){
                    SocialSetforLine.setValueTextColor(Color.WHITE);
                    SocialMonthChart.getAxisLeft().setTextColor(Color.WHITE);
                    SocialMonthChart.getAxisRight().setTextColor(Color.WHITE);
                    SocialMonthChart.getLegend().setTextColor(Color.WHITE);
                    SocialMonthChart.getXAxis().setTextColor(Color.WHITE);
                }else{
                    SocialSetforLine.setValueTextColor(Color.BLACK);
                }
                SocialSetforLine.setValueTextSize(10f);

                //TODO HEEEEERRRRREEEE

                //BarChart for placing data
                BarChart Social2WeekChart = findViewById(R.id.WeekBarChart2);



                //BarChart Entries
                ArrayList<BarEntry> Social2values = new ArrayList<>();
                for(int i = SocialToday; i>1 && i >= SocialToday-6; i--){
                    if(SocialmonthlyData[i][0] == null){
                        Social2values.add(new BarEntry(i,0));
                    }else{
                        Social2values.add(new BarEntry(i,(float) Double.parseDouble("0"+SocialmonthlyData[i][2])));
                    }
                }

                //Do Not touch this logic too much please
                BarDataSet Social2FirstSet = new BarDataSet(Social2values, "Past 7 Days");

                BarData Social2data = new BarData(Social2FirstSet);

                Social2WeekChart.setData(Social2data);

                Social2FirstSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);

                if(TheTheme == "Dark"){
                    Social2FirstSet.setValueTextColor(Color.WHITE);
                    Social2WeekChart.getAxisLeft().setTextColor(Color.WHITE);
                    Social2WeekChart.getAxisRight().setTextColor(Color.WHITE);
                    Social2WeekChart.getLegend().setTextColor(Color.WHITE);
                    Social2WeekChart.getXAxis().setTextColor(Color.WHITE);
                }else{
                    Social2FirstSet.setValueTextColor(Color.BLACK);
                }

                Social2FirstSet.setValueTextSize(10f);

                Social2WeekChart.getDescription().setEnabled(false);



                //Monthly Line Graph
                //Variables for Line Graph
                LineChart Social2MonthChart = findViewById(R.id.MonthLineGraph2);

                LineData Social2DataforLine;

                LineDataSet Social2SetforLine;

                //LineChart Entries
                ArrayList<Entry> Social2linevalues = new ArrayList<>();
                for(int i = MaxMonth-1; i>1; i--){

                    if(i > SocialToday){
                        Social2linevalues.add(new Entry(i,0));
                    }else{
                        Social2linevalues.add(new Entry(i,(float) Double.parseDouble("0"+SocialmonthlyData[i][2])));
                    }
                }


                //Bug where linegraph entries need to be sorted by X entry, found it from the author fix post from 2016
                Collections.sort(Social2linevalues, new EntryXComparator());

                Social2SetforLine = new LineDataSet(Social2linevalues, "Last 30 days");
                Social2DataforLine = new LineData(Social2SetforLine);

                Social2MonthChart.setData(Social2DataforLine);
                Social2SetforLine.setColor(ColorTemplate.MATERIAL_COLORS[0]);
                if(TheTheme == "Dark"){
                    Social2SetforLine.setValueTextColor(Color.WHITE);
                    Social2MonthChart.getAxisLeft().setTextColor(Color.WHITE);
                    Social2MonthChart.getAxisRight().setTextColor(Color.WHITE);
                    Social2MonthChart.getLegend().setTextColor(Color.WHITE);
                    Social2MonthChart.getXAxis().setTextColor(Color.WHITE);
                }else{
                    Social2SetforLine.setValueTextColor(Color.BLACK);
                }
                Social2SetforLine.setValueTextSize(10f);




                break;
            case "Finance":
                //Set Titles
                TextView FinanceTitleText = findViewById(R.id.GraphTitleText);
                FinanceTitleText.setText(R.string.FinanceGraphTitle);
                TextView FinanceBarChartText = findViewById(R.id.WeeklyBarText);
                FinanceBarChartText.setText(R.string.FinanceWeek);
                TextView FinanceMonthLineChartText = findViewById(R.id.MonthlyLineText);
                FinanceMonthLineChartText.setText(R.string.FinanceMonth);

                //Daily Set
                TextView FinanceDailyText = findViewById(R.id.DailyText);
                Log.d("Before returnFinance","Before");
                String[][] FinancemonthlyData = db.returnFinance();
                for(int i = 1; i<FinancemonthlyData.length; i++){
                    Log.d("Date", ""+ FinancemonthlyData[i][0]);
                    Log.d("Calorie", ""+ FinancemonthlyData[i][1]);
                }
                SimpleDateFormat FinanceformatDate = new SimpleDateFormat("dd");
                Date Financedate = new Date();
                Log.d("Today",""+FinanceformatDate.format(Financedate));
                int FinanceToday = Integer.parseInt(FinanceformatDate.format(Financedate));
                if(FinancemonthlyData[1][0] == null){
                    FinanceDailyText.setText(R.string.NullFinance);
                    break;
                }else if(FinancemonthlyData[2][0] == null){
                    String FinanceDaily = getString(R.string.DailyFinanceBlurbFirst) + "" + FinancemonthlyData[1][1] + getString(R.string.DailyFinanceBlurbSecond) + " " + FinancemonthlyData[1][0];
                    FinanceDailyText.setText(FinanceDaily);
                    break;
                }else {
                    String FinanceDaily = getString(R.string.DailyFinanceBlurbFirst) + "" + FinancemonthlyData[FinanceToday][1] + getString(R.string.DailyFinanceBlurbSecond) + " " + FinancemonthlyData[FinanceToday][0];
                    FinanceDailyText.setText(FinanceDaily);
                }
                //Weekly Finance Data BARRRRRRRRCHARRRRT

                //BarChart for placing data
                BarChart FinanceWeekChart = findViewById(R.id.WeekBarChart);



                //BarChart Entries
                ArrayList<BarEntry> Financevalues = new ArrayList<>();
                for(int i = FinanceToday; i>1 && i >= FinanceToday-6; i--){
                    if(FinancemonthlyData[i][0] == null){
                        Financevalues.add(new BarEntry(i,0));
                    }else{
                        Financevalues.add(new BarEntry(i,(float) Double.parseDouble("0"+FinancemonthlyData[i][1])));
                    }
                }

                //Do Not touch this logic too much please
                BarDataSet FinanceFirstSet = new BarDataSet(Financevalues, "Past 7 Days");

                BarData Financedata = new BarData(FinanceFirstSet);

                FinanceWeekChart.setData(Financedata);

                FinanceFirstSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);

                if(TheTheme == "Dark"){
                    FinanceFirstSet.setValueTextColor(Color.WHITE);
                    FinanceWeekChart.getAxisLeft().setTextColor(Color.WHITE);
                    FinanceWeekChart.getAxisRight().setTextColor(Color.WHITE);
                    FinanceWeekChart.getLegend().setTextColor(Color.WHITE);
                    FinanceWeekChart.getXAxis().setTextColor(Color.WHITE);
                }else{
                    FinanceFirstSet.setValueTextColor(Color.BLACK);
                }

                FinanceFirstSet.setValueTextSize(10f);

                FinanceWeekChart.getDescription().setEnabled(false);



                //Monthly Line Graph
                //Variables for Line Graph
                LineChart FinanceMonthChart = findViewById(R.id.MonthLineGraph);

                LineData FinanceDataforLine;

                LineDataSet FinanceSetforLine;

                //LineChart Entries
                ArrayList<Entry> Financelinevalues = new ArrayList<>();
                for(int i = MaxMonth-1; i>1; i--){

                    if(i > FinanceToday){
                        Financelinevalues.add(new Entry(i,0));
                    }else{
                        Financelinevalues.add(new Entry(i,(float) Double.parseDouble("0"+FinancemonthlyData[i][1])));
                    }
                }


                //Bug where linegraph entries need to be sorted by X entry, found it from the author fix post from 2016
                Collections.sort(Financelinevalues, new EntryXComparator());

                FinanceSetforLine = new LineDataSet(Financelinevalues, "Last 30 days");
                FinanceDataforLine = new LineData(FinanceSetforLine);

                FinanceMonthChart.setData(FinanceDataforLine);
                FinanceSetforLine.setColor(ColorTemplate.MATERIAL_COLORS[0]);
                if(TheTheme == "Dark"){
                    FinanceSetforLine.setValueTextColor(Color.WHITE);
                    FinanceMonthChart.getAxisLeft().setTextColor(Color.WHITE);
                    FinanceMonthChart.getAxisRight().setTextColor(Color.WHITE);
                    FinanceMonthChart.getLegend().setTextColor(Color.WHITE);
                    FinanceMonthChart.getXAxis().setTextColor(Color.WHITE);
                }else{
                    FinanceSetforLine.setValueTextColor(Color.BLACK);
                }
                FinanceSetforLine.setValueTextSize(10f);
                break;
            case "Mental":
                setContentView(R.layout.activity_graphdatagraphmood);
                //Set Titles
                TextView MoodTitleText = findViewById(R.id.GraphTitleText);
                MoodTitleText.setText(R.string.MoodGraphTitle);
                TextView MoodBarChartText = findViewById(R.id.WeeklyBarText);
                MoodBarChartText.setText(R.string.MoodWeek);
                TextView MoodMonthLineChartText = findViewById(R.id.MonthlyLineText);
                MoodMonthLineChartText.setText(R.string.MoodMonth);

                //Daily Set
                TextView MoodDailyText = findViewById(R.id.DailyText);
                Log.d("Before returnMood","Before");
                String[][] MoodmonthlyData = db.returnMood();
                for(int i = 1; i<MoodmonthlyData.length; i++){
                    Log.d("Date", ""+ MoodmonthlyData[i][0]);
                    Log.d("Calorie", ""+ MoodmonthlyData[i][1]);
                }
                SimpleDateFormat MoodformatDate = new SimpleDateFormat("dd");
                Date Mooddate = new Date();
                Log.d("Today",""+MoodformatDate.format(Mooddate));
                int MoodToday = Integer.parseInt(MoodformatDate.format(Mooddate));
                String howyoufelt = "";
                if(MoodmonthlyData[1][0] == null){
                    MoodDailyText.setText(R.string.NullMood);
                    break;
                }else if(MoodmonthlyData[2][0] == null){
                    switch (MoodmonthlyData[1][1]){
                        case "0":
                            howyoufelt += "sad";
                            break;
                        case "1":
                            howyoufelt += "neutral";
                            break;
                        case "2":
                            howyoufelt += "happy";
                            break;
                    }
                    String MoodDaily = getString(R.string.DailyMoodBlurbFirst) + "" + howyoufelt + getString(R.string.DailyMoodBlurbSecond) + " " + MoodmonthlyData[1][0];
                    MoodDailyText.setText(MoodDaily);
                    break;
                }else {
                    switch (MoodmonthlyData[1][1]){
                        case "0":
                            howyoufelt += "sad";
                            break;
                        case "1":
                            howyoufelt += "neutral";
                            break;
                        case "2":
                            howyoufelt += "happy";
                            break;
                    }
                    String MoodDaily = getString(R.string.DailyMoodBlurbFirst) + "" + howyoufelt + getString(R.string.DailyMoodBlurbSecond) + " " + MoodmonthlyData[MoodToday][0];
                    MoodDailyText.setText(MoodDaily);
                }
                //Weekly Mood Data BARRRRRRRRCHARRRRT

                //BarChart for placing data
                BarChart MoodWeekChart = findViewById(R.id.WeekBarChart);



                //BarChart Entries
                ArrayList<BarEntry> Moodvalues = new ArrayList<>();
                for(int i = MoodToday; i>1 && i >= MoodToday-6; i--){
                    if(MoodmonthlyData[i][0] == null){
                        Moodvalues.add(new BarEntry(i,0));
                    }else{
                        Moodvalues.add(new BarEntry(i,(float) Double.parseDouble("0"+MoodmonthlyData[i][1])));
                    }
                }

                //Do Not touch this logic too much please
                BarDataSet MoodFirstSet = new BarDataSet(Moodvalues, "Past 7 Days");

                BarData Mooddata = new BarData(MoodFirstSet);

                MoodWeekChart.setData(Mooddata);

                MoodFirstSet.setColor(ColorTemplate.MATERIAL_COLORS[0]);

                if(TheTheme == "Dark"){
                    MoodFirstSet.setValueTextColor(Color.WHITE);
                    MoodWeekChart.getAxisLeft().setTextColor(Color.WHITE);
                    MoodWeekChart.getAxisRight().setTextColor(Color.WHITE);
                    MoodWeekChart.getLegend().setTextColor(Color.WHITE);
                    MoodWeekChart.getXAxis().setTextColor(Color.WHITE);
                }else{
                    MoodFirstSet.setValueTextColor(Color.BLACK);
                }

                MoodFirstSet.setValueTextSize(10f);

                MoodWeekChart.getDescription().setEnabled(false);



                //Monthly Line Graph
                //Variables for Line Graph
                LineChart MoodMonthChart = findViewById(R.id.MonthLineGraph);

                LineData MoodDataforLine;

                LineDataSet MoodSetforLine;

                //LineChart Entries
                ArrayList<Entry> Moodlinevalues = new ArrayList<>();
                for(int i = MaxMonth-1; i>1; i--){

                    if(i > MoodToday){
                        Moodlinevalues.add(new Entry(i,0));
                    }else{
                        Moodlinevalues.add(new Entry(i,(float) Double.parseDouble("0"+MoodmonthlyData[i][1])));
                    }
                }


                //Bug where linegraph entries need to be sorted by X entry, found it from the author fix post from 2016
                Collections.sort(Moodlinevalues, new EntryXComparator());

                MoodSetforLine = new LineDataSet(Moodlinevalues, "Last 30 days");
                MoodDataforLine = new LineData(MoodSetforLine);

                MoodMonthChart.setData(MoodDataforLine);
                MoodSetforLine.setColor(ColorTemplate.MATERIAL_COLORS[0]);
                if(TheTheme == "Dark"){
                    MoodSetforLine.setValueTextColor(Color.WHITE);
                    MoodMonthChart.getAxisLeft().setTextColor(Color.WHITE);
                    MoodMonthChart.getAxisRight().setTextColor(Color.WHITE);
                    MoodMonthChart.getLegend().setTextColor(Color.WHITE);
                    MoodMonthChart.getXAxis().setTextColor(Color.WHITE);
                }else{
                    MoodSetforLine.setValueTextColor(Color.BLACK);
                }
                MoodSetforLine.setValueTextSize(10f);
                break;
        }



    }



}
