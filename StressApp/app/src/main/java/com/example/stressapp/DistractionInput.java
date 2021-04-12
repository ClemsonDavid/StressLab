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
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getBaseContext();
        screen = context.getDisplay();

        screen.getRealSize(size);
        width = size.x;
        height = size.y;

        mediaPlayer = MediaPlayer.create(this, R.raw.popping_sound);

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
            case "Bubbles":
                BubblesLayout();
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
            //ImageView image2 = new ImageView(context);
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
            //ImageView image2 = new ImageView(context);
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
}
