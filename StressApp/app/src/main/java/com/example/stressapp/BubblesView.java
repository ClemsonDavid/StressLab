package com.example.stressapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class BubblesView extends SurfaceView {
    Random random = new Random();
    Canvas canvas = new Canvas();
    Display screen;
    Point size = new Point();
    int width, height;
    int radius = 100;
    Paint paint = new Paint();
    Context con;

    public BubblesView(Context context) {
        super(context);
        con = context;

        screen = context.getDisplay();
        screen.getRealSize(size);
        //width = size.x;
        //height = size.y;

        width = getWidth();
        height = getHeight();

        //canvas.drawColor(Color.BLACK);
        //Paint paint = new Paint();
        //paint.setStyle(Paint.Style.FILL);
        /*int num_bubbles = random.nextInt(21);
        int[] dot_colors = getResources().getIntArray(R.array.circle_colors);
        paint.setColor(Color.GREEN);
        //paint.setColor(random.nextInt(dot_colors.length));
        int randx = random.nextInt(width);
        int randy = randx;
        int randr = random.nextInt(radius + random.nextInt(100));
        canvas.drawCircle(randx, randy, randr, paint);*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        int num_bubbles = random.nextInt(21);
        int[] dot_colors = getResources().getIntArray(R.array.circle_colors);
        paint.setColor(Color.GREEN);
        //paint.setColor(random.nextInt(dot_colors.length));
        int randx = random.nextInt(width);
        int randy = randx;
        int randr = random.nextInt(radius + random.nextInt(100));
        canvas.drawCircle(randx, randy, randr, paint);
        ImageView image = new ImageView(con);

    }
}

/*setContentView(R.layout.activity_bubbles);
int num_bubbles = random.nextInt(21);
int[] dot_colors = getResources().getIntArray(R.array.circle_colors);
//for (int i = 0; i < num_bubbles; i++) {
    ShapeDrawable circle = new ShapeDrawable(new OvalShape());
    circle.getPaint().setColor(random.nextInt(dot_colors.length));
    int randx = random.nextInt(width);
    int randy = randx;
    int randr = random.nextInt(radius + random.nextInt(100));
    canvas.drawCircle(randx, randy, randr, circle.getPaint());
//}*/