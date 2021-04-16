package com.example.stressapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class DistractionInput extends AppCompatActivity {

    private static final String PREFS = "prefs";
    Random random = new Random();
    Canvas canvas = new Canvas();
    Context context;
    Display screen;
    Point size = new Point();
    int width, height;
    int radius = 100;
    int bubble_count = 0;
    private MediaPlayer mediaPlayer;
    private MediaPlayer wind_media, waves_media, fire_media;
    int solution = 0;
    StringBuilder s = new StringBuilder(100);
    StringBuilder healthystring = new StringBuilder(200);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getBaseContext();
        screen = context.getDisplay();

        screen.getRealSize(size);
        width = size.x;
        height = size.y;

        mediaPlayer = MediaPlayer.create(this, R.raw.popping_sound);
        wind_media = MediaPlayer.create(this, R.raw.beautiful_bells);
        waves_media = MediaPlayer.create(this, R.raw.hawaii_waves);
        fire_media = MediaPlayer.create(this, R.raw.small_fireplace_crackles);

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
        setContentView(R.layout.activity_distractiondatatitle);
        Intent intent = getIntent();
        String Type = intent.getExtras().getString("Type");
        switch(Type){
            case "Bubbles":
                BubblesLayout();
                break;
            case "Ambient":
                AmbientLayout();
                break;
            case "Math":
                MathLayout();
                break;
            case "Exercise":
                ExerciseLayout();
                break;
            case "Encourage":
                EncourageLayout();
                break;
        }

    }

    /*TODO Finsih up other type layouts with appropriate edittexts to grab info
        After that, set up SQLite for datastorage
     */

    private void BubblesLayout(){

        setContentView(R.layout.activity_bubbles);
        int num_bubbles = random.nextInt(21);
        bubble_count = num_bubbles;
        int[] dot_colors = getResources().getIntArray(R.array.circle_colors);

        for (int i = 0; i < num_bubbles; i++) {
            ImageView image = new ImageView(context);
            image.setLayoutParams(new LinearLayout.LayoutParams(random.nextInt(width), random.nextInt(height)));
            ShapeDrawable circle = new ShapeDrawable(new OvalShape());
            int randx = random.nextInt(width);
            circle.setIntrinsicWidth(randx);
            circle.setIntrinsicHeight(randx);
            circle.getPaint().setColor(dot_colors[random.nextInt(dot_colors.length)]);
            image.setImageDrawable(circle);
            addContentView(image, image.getLayoutParams());
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.start();
                    bubble_count--;
                    if (bubble_count < 3) {
                        moreBubbles();
                    }
                    image.setVisibility(View.GONE);
                }
            });
        }
    }

    private void moreBubbles() {
        int num_bubbles = random.nextInt(21);
        bubble_count += num_bubbles;
        int[] dot_colors = getResources().getIntArray(R.array.circle_colors);

        for (int i = 0; i < num_bubbles; i++) {
            ImageView image = new ImageView(context);
            image.setLayoutParams(new LinearLayout.LayoutParams(random.nextInt(width), random.nextInt(height)));
            ShapeDrawable circle = new ShapeDrawable(new OvalShape());
            int randx = random.nextInt(width);
            circle.setIntrinsicWidth(randx);
            circle.setIntrinsicHeight(randx);
            circle.getPaint().setColor(dot_colors[random.nextInt(dot_colors.length)]);
            image.setImageDrawable(circle);
            addContentView(image, image.getLayoutParams());
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.start();
                    bubble_count--;
                    if (bubble_count < 3) {
                        moreBubbles();
                    }
                    image.setVisibility(View.GONE);
                }
            });
        }
    }

    private void AmbientLayout() {
        setContentView(R.layout.activity_ambient);
    }

    public void AmbientOptionSelect(View v){
        switch(v.getId()){
            case R.id.WavesButton:
                if (wind_media.isPlaying()) wind_media.pause();
                if(fire_media.isPlaying()) fire_media.pause();
                waves_media.setLooping(true);
                waves_media.start();
                break;
            case R.id.WindButton:
                if (waves_media.isPlaying()) waves_media.pause();
                if (fire_media.isPlaying()) fire_media.pause();
                wind_media.setLooping(true);
                wind_media.start();
                break;
            case R.id.FireButton:
                if (waves_media.isPlaying()) waves_media.pause();
                if (wind_media.isPlaying()) wind_media.pause();
                fire_media.setLooping(true);
                fire_media.start();
                break;
        }

    }

    private void MathLayout() {
        setContentView(R.layout.activity_math);
        int[] math_symbols = getResources().getIntArray(R.array.math_symbols);
        int variable_1 = random.nextInt(20);
        int variable_2 = random.nextInt(20);
        int symbol = random.nextInt(math_symbols.length);
        TextView problem = findViewById(R.id.MathProblem);
        switch (symbol) {
            case 0:
                solution = variable_1 + variable_2;
                problem.setText(variable_1 + " + " + variable_2);
                break;
            case 1:
                solution = variable_1 - variable_2;
                problem.setText(variable_1 + " - " + variable_2);
                break;
            case 2:
                solution = variable_1 * variable_2;
                problem.setText(variable_1 + " * " + variable_2);
                break;
        }
    }

    public void checkAnswer(View view) {
        EditText text = findViewById(R.id.MathAnswer);
        String temp = text.getText().toString();
        int value = 0;
        if (!temp.equals("")) {
            value = Integer.parseInt(temp);
        }
        else value = 0;
        if (value == solution) {
            Toast.makeText(this, R.string.correct, Toast.LENGTH_LONG).show();
        }
        else {
            s.append(getResources().getString(R.string.wrong));
            s.append(" ");
            s.append(String.valueOf(solution));
            Toast.makeText(this, s.toString(), Toast.LENGTH_LONG).show();
            s.setLength(0);
        }
        text.setText("");
        int[] math_symbols = getResources().getIntArray(R.array.math_symbols);
        int variable_1 = random.nextInt(20);
        int variable_2 = random.nextInt(20);
        int symbol = random.nextInt(math_symbols.length);
        TextView problem = findViewById(R.id.MathProblem);
        switch (symbol) {
            case 0:
                solution = variable_1 + variable_2;
                problem.setText(variable_1 + " + " + variable_2);
                break;
            case 1:
                solution = variable_1 - variable_2;
                problem.setText(variable_1 + " - " + variable_2);
                break;
            case 2:
                solution = variable_1 * variable_2;
                problem.setText(variable_1 + " * " + variable_2);
                break;
        }
    }

    private void ExerciseLayout() {
        setContentView(R.layout.activity_exercise);
        String[] exercises = getResources().getStringArray(R.array.exercise_types);
        int size1 = random.nextInt(10);
        int size2 = random.nextInt(10);
        int size3 = random.nextInt(10);
        if (size1 == 0) size1++;
        if (size2 == 0) size2++;
        if (size3 == 0) size3++;
        int exercise1 = random.nextInt(exercises.length);
        int exercise2 = random.nextInt(exercises.length);
        int exercise3 = random.nextInt(exercises.length);
        Button button1 = findViewById(R.id.Exercise1);
        Button button2 = findViewById(R.id.Exercise2);
        Button button3 = findViewById(R.id.Exercise3);
        healthystring.append(String.valueOf(size1));
        healthystring.append(" ");
        healthystring.append(exercises[exercise1]);
        //healthystring.append(exercises[exercise1].toString());
        //healthystring.append(getResources().getString(R.array.exercise_types[exercise1].toString()));
        button1.setText(healthystring.toString());
        healthystring.setLength(0);

        healthystring.append(String.valueOf(size2));
        healthystring.append(" ");
        healthystring.append(exercises[exercise2]);
        button2.setText(healthystring.toString());
        healthystring.setLength(0);

        healthystring.append(String.valueOf(size3));
        healthystring.append(" ");
        healthystring.append(exercises[exercise3]);
        button3.setText(healthystring.toString());
        healthystring.setLength(0);
    }

    public void clickExercise(View view) {
        Toast.makeText(this, R.string.exercise_cheer, Toast.LENGTH_SHORT).show();
        String[] exercises = getResources().getStringArray(R.array.exercise_types);
        if (view.getId() == R.id.Exercise1) {
            int size = random.nextInt(10);
            if (size == 0) size++;
            int exercise = random.nextInt(exercises.length);
            Button button = findViewById(R.id.Exercise1);
            healthystring.append(String.valueOf(size));
            healthystring.append(" ");
            healthystring.append(exercises[exercise]);
            button.setText(healthystring.toString());
            healthystring.setLength(0);
        }
        else if (view.getId() == R.id.Exercise2) {
            int size = random.nextInt(10);
            if (size == 0) size++;
            int exercise = random.nextInt(exercises.length);
            Button button = findViewById(R.id.Exercise2);
            healthystring.append(String.valueOf(size));
            healthystring.append(" ");
            healthystring.append(exercises[exercise]);
            button.setText(healthystring.toString());
            healthystring.setLength(0);
        }
        else {
            int size = random.nextInt(10);
            if (size == 0) size++;
            int exercise = random.nextInt(exercises.length);
            Button button = findViewById(R.id.Exercise3);
            healthystring.append(String.valueOf(size));
            healthystring.append(" ");
            healthystring.append(exercises[exercise]);
            button.setText(healthystring.toString());
            healthystring.setLength(0);
        }
    }

    private void EncourageLayout() {
        setContentView(R.layout.activity_encourage);
        String[] words = getResources().getStringArray(R.array.encouragements);
        int rand = random.nextInt(words.length);
        TextView text = findViewById(R.id.EncourageText);
        text.setText(words[rand]);
    }
}
