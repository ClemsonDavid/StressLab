package com.example.stressapp;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/*
Database Structure heavily inspired by Zybooks
 */


public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "stressinfo.db";
    private static final int VERSION = 1;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /*
        TODO create tables for all data tracking
     */
    //Database class for eating
    private static final class EatTable {
        private static final String TABLE = "eat";
        private static final String COL_ID = "_id";
        private static final String COL_DATE = "date";
        private static final String COL_CALORIES = "calories";
    }

    //Database class for sleeping
    private static final class SleepTable{
        private static final String TABLE = "sleep";
        private static final String COL_ID = "_id";
        private static final String COL_DATE = "date";
        private static final String COL_WAKEUP = "wakeup";
        private static final String COL_BEDTIME = "bedtime";
    }

    //Database class for ExcerciseTable
    private static final class ExcerciseTable {
        private static final String TABLE = "exercise";
        private static final String COL_ID = "_id";
        private static final String COL_DATE = "date";
        private static final String COL_HOURS = "hours";
        private static final String COL_MINS = "minutes";
    }

    //Database class for SocialTable
    private static final class SocialTable {
        private static final String TABLE = "social";
        private static final String COL_ID = "_id";
        private static final String COL_DATE = "date";
        private static final String COL_HOURS = "hours";
        private static final String COL_MINS = "minutes";
        private static final String COL_UNIQUE = "unique";
    }
    //Database class for FinanceTable
    private static final class FinanceTable {
        private static final String TABLE = "finance";
        private static final String COL_ID = "_id";
        private static final String COL_DATE = "date";
        private static final String COL_MONEY = "money";
    }
    //Database class for MoodTable
    private static final class MoodTable {
        private static final String TABLE = "mood";
        private static final String COL_ID = "_id";
        private static final String COL_DATE = "date";
        private static final String COL_MOOD = "mood";
    }


    /*
        TODO make all tables
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create EatTable
        db.execSQL("create table " + EatTable.TABLE + " (" +
                EatTable.COL_ID + " integer primary key autoincrement, " +
                EatTable.COL_DATE + " text, " +
                EatTable.COL_CALORIES + " integer)");
        //Create SleepTable
        db.execSQL("create table " + SleepTable.TABLE + " (" +
                SleepTable.COL_ID + " integer primary key autoincrement, " +
                SleepTable.COL_DATE + " text, " +
                SleepTable.COL_WAKEUP + " text, " +
                SleepTable.COL_BEDTIME + " text)");
        //Create ExerciseTable
        db.execSQL("create table " + ExcerciseTable.TABLE + " (" +
                ExcerciseTable.COL_ID + " integer primary key autoincrement, " +
                ExcerciseTable.COL_DATE + " text, " +
                ExcerciseTable.COL_HOURS + " integer, " +
                ExcerciseTable.COL_MINS + " integer)");
        //Create SocialTable
        db.execSQL("create table " + SocialTable.TABLE + " (" +
                SocialTable.COL_ID + " integer primary key autoincrement, " +
                SocialTable.COL_DATE + " text, " +
                SocialTable.COL_HOURS + " integer, " +
                SocialTable.COL_MINS + " integer, " +
                SocialTable.COL_UNIQUE + "integer)");
        //Create FinanceTable
        db.execSQL("create table " + FinanceTable.TABLE + " (" +
                FinanceTable.COL_ID + " integer primary key autoincrement, " +
                FinanceTable.COL_DATE + " text, " +
                FinanceTable.COL_MONEY + " integer)");
        //Create MoodTable
        db.execSQL("create table " + MoodTable.TABLE + " (" +
                MoodTable.COL_ID + " integer primary key autoincrement, " +
                MoodTable.COL_DATE + " text, " +
                MoodTable.COL_MOOD + " text)");

    }


    /*
        TODO make sure to include all tables
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        //Drop Tables if necessary
        db.execSQL("drop table if exists " + EatTable.TABLE);
        db.execSQL("drop table if exists " + SleepTable.TABLE);
        db.execSQL("drop table if exists " + ExcerciseTable.TABLE);
        db.execSQL("drop table if exists " + SocialTable.TABLE);
        db.execSQL("drop table if exists " + FinanceTable.TABLE);
        db.execSQL("drop table if exists " + MoodTable.TABLE);
        onCreate(db);
    }


    public long addEat(int calories){
        SQLiteDatabase db = getWritableDatabase();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();



        ContentValues values = new ContentValues();
        values.put(EatTable.COL_DATE, formatDate.format(date));
        values.put(EatTable.COL_CALORIES, calories);

        long movieId = db.insert(EatTable.TABLE, null, values);
        Log.d("Database Insert Eat","Returned a : "+movieId);


        db = getReadableDatabase();

        String sql = "select * from " + EatTable.TABLE + " ";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String time = cursor.getString(1);
                String calorie = cursor.getString(2);
                Log.d(TAG, "Returned = " + id + ", " + time + ", " + calorie );
            } while (cursor.moveToNext());
        }
        cursor.close();





        return movieId;

    }



}