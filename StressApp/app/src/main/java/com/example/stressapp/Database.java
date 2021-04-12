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


    //Database class for eating
    private static final class EatTable {
        private static final String TABLE = "eat";
        private static final String COL_ID = "_id";
        private static final String COL_DATE = "date";
        private static final String COL_CALORIES = "calories";
    }

    //Database class for sleeping
    //0 AM for am, 1 AM for pm
    private static final class SleepTable{
        private static final String TABLE = "sleep";
        private static final String COL_ID = "_id";
        private static final String COL_DATE = "date";
        private static final String COL_WAKEUPHOUR = "wakeuphour";
        private static final String COL_BEDTIMEHOUR = "bedtimehour";
        private static final String COL_WAKEUPMIN = "wakeupmin";
        private static final String COL_BEDTIMEMIN = "bedtimemin";
        private static final String COL_WAKEUPAM = "wakeupAM";
        private static final String COL_BEDTIMEAM = "bedtimeAM";


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
        private static final String COL_UNIQUENUM = "people";
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
                SleepTable.COL_WAKEUPHOUR + " integer, " +
                SleepTable.COL_WAKEUPMIN + " integer, " +
                SleepTable.COL_WAKEUPAM + " integer, " +
                SleepTable.COL_BEDTIMEHOUR + " integer, " +
                SleepTable.COL_BEDTIMEMIN + " integer, " +
                SleepTable.COL_BEDTIMEAM + " integer)");
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
                SocialTable.COL_UNIQUENUM + " integer)");
        //Create FinanceTable
        db.execSQL("create table " + FinanceTable.TABLE + " (" +
                FinanceTable.COL_ID + " integer primary key autoincrement, " +
                FinanceTable.COL_DATE + " text, " +
                FinanceTable.COL_MONEY + " double)");
        //Create MoodTable
        db.execSQL("create table " + MoodTable.TABLE + " (" +
                MoodTable.COL_ID + " integer primary key autoincrement, " +
                MoodTable.COL_DATE + " text, " +
                MoodTable.COL_MOOD + " integer)");

    }



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

    /*
        TODO create all Add functions
     */

    public long addEat(int calories){

        //Check for repeats and delete so only one record per day
        SQLiteDatabase db = getReadableDatabase();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        String sqlCheck = "select * from " + EatTable.TABLE + " where date = ? ";
        Cursor cursorCheck = db.rawQuery(sqlCheck, new String[]{ formatDate.format(date) });
        long removeid = -1;
        if (cursorCheck.moveToFirst()){
            Log.d("Repeat", "Found");
            removeid = cursorCheck.getLong(0);
        }else{
            Log.d("Repeat", "Not Found");
        }


        db = getWritableDatabase();

        //Delete Repeat if needed
        if(removeid != -1){
            db.delete(EatTable.TABLE, EatTable.COL_ID + " = ?", new String[]{Long.toString(removeid)});
        }

        ContentValues values = new ContentValues();
        values.put(EatTable.COL_DATE, formatDate.format(date));
        values.put(EatTable.COL_CALORIES, calories);



        long Id = db.insert(EatTable.TABLE, null, values);
        Log.d("Database Insert Eat","Returned a : "+Id);


        //TODO remove below

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





        return Id;

    }

    public long addSleep(int wakehour, int wakemin, int wakeam, int sleephour, int sleepmin, int sleepam){
        //Check for repeats and delete so only one record per day
        SQLiteDatabase db = getReadableDatabase();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        String sqlCheck = "select * from " + SleepTable.TABLE + " where date = ? ";
        Cursor cursorCheck = db.rawQuery(sqlCheck, new String[]{ formatDate.format(date) });
        long removeid = -1;
        if (cursorCheck.moveToFirst()){
            Log.d("Repeat", "Found");
            removeid = cursorCheck.getLong(0);
        }else{
            Log.d("Repeat", "Not Found");
        }



        db = getWritableDatabase();

        if(removeid != -1){
            db.delete(SleepTable.TABLE, SleepTable.COL_ID + " = ?", new String[]{Long.toString(removeid)});
        }



        ContentValues values = new ContentValues();
        values.put(SleepTable.COL_DATE, formatDate.format(date));
        values.put(SleepTable.COL_WAKEUPHOUR, wakehour);
        values.put(SleepTable.COL_WAKEUPMIN, wakemin);
        values.put(SleepTable.COL_WAKEUPAM, wakeam);
        values.put(SleepTable.COL_BEDTIMEHOUR, sleephour);
        values.put(SleepTable.COL_BEDTIMEMIN, sleepmin);
        values.put(SleepTable.COL_BEDTIMEAM, sleepam);

        long Id = db.insert(SleepTable.TABLE, null, values);
        Log.d("Database Insert Eat","Returned a : "+Id);


        //TODO remove below

        db = getReadableDatabase();

        String sql = "select * from " + SleepTable.TABLE + " ";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String time = cursor.getString(1);
                String COL_WAKEUPHOUR = cursor.getString(2);
                String COL_WAKEUPMIN = cursor.getString(3);
                String COL_WAKEUPAM = cursor.getString(4);
                String COL_BEDTIMEHOUR = cursor.getString(5);
                String COL_BEDTIMEMIN = cursor.getString(6);
                String COL_BEDTIMEAM = cursor.getString(7);
                Log.d(TAG, "Returned = " + id + ", DATE " + time + ", COL_WAKEUPHOUR " + COL_WAKEUPHOUR + ", COL_WAKEUPMIN " +COL_WAKEUPMIN+ "," +
                        "COL_WAKEUPAM " +COL_WAKEUPAM+", COL_BEDTIMEHOUR "+COL_BEDTIMEHOUR+", COL_BEDTIMEMIN" +COL_BEDTIMEMIN+", COL_BEDTIMEAM"+COL_BEDTIMEAM);
            } while (cursor.moveToNext());
        }
        cursor.close();



        return Id;

    }


    public long addExcercise(int hours, int mins){
        //Check for repeats and delete so only one record per day
        SQLiteDatabase db = getReadableDatabase();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        String sqlCheck = "select * from " + ExcerciseTable.TABLE + " where date = ? ";
        Cursor cursorCheck = db.rawQuery(sqlCheck, new String[]{ formatDate.format(date) });
        long removeid = -1;
        if (cursorCheck.moveToFirst()){
            Log.d("Repeat", "Found");
            removeid = cursorCheck.getLong(0);
        }else{
            Log.d("Repeat", "Not Found");
        }



        db = getWritableDatabase();

        if(removeid != -1){
            db.delete(ExcerciseTable.TABLE, ExcerciseTable.COL_ID + " = ?", new String[]{Long.toString(removeid)});
        }

        ContentValues values = new ContentValues();
        values.put(ExcerciseTable.COL_DATE, formatDate.format(date));
        values.put(ExcerciseTable.COL_HOURS, hours);
        values.put(ExcerciseTable.COL_MINS, mins);

        long Id = db.insert(ExcerciseTable.TABLE, null, values);
        Log.d("Database Insert Eat","Returned a : "+Id);


        //TODO remove below

        db = getReadableDatabase();

        String sql = "select * from " + ExcerciseTable.TABLE + " ";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String time = cursor.getString(1);
                String COL_HOURS = cursor.getString(2);
                String COL_MINS = cursor.getString(3);
                Log.d(TAG, "Returned = " + id + ", " + time + ", COL_HOURS " + COL_HOURS +", COL_MINS "+COL_MINS);
            } while (cursor.moveToNext());
        }
        cursor.close();





        return Id;

    }

    public long addSocial(int hours, int mins, int numppl){
        //Check for repeats and delete so only one record per day
        SQLiteDatabase db = getReadableDatabase();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        String sqlCheck = "select * from " + SocialTable.TABLE + " where date = ? ";
        Cursor cursorCheck = db.rawQuery(sqlCheck, new String[]{ formatDate.format(date) });
        long removeid = -1;
        if (cursorCheck.moveToFirst()){
            Log.d("Repeat", "Found");
            removeid = cursorCheck.getLong(0);
        }else{
            Log.d("Repeat", "Not Found");
        }



        db = getWritableDatabase();

        if(removeid != -1){
            db.delete(SocialTable.TABLE, SocialTable.COL_ID + " = ?", new String[]{Long.toString(removeid)});
        }

        Log.d("Test inside insert", "hours: "+hours+" mins: "+mins+" numPpl: "+numppl);
        ContentValues values = new ContentValues();
        values.put(SocialTable.COL_DATE, formatDate.format(date));
        values.put(SocialTable.COL_HOURS, hours);
        values.put(SocialTable.COL_MINS, mins);
        values.put(SocialTable.COL_UNIQUENUM, numppl);

        long Id = db.insert(SocialTable.TABLE, null, values);
        Log.d("Database Insert Eat","Returned a : "+Id);


        //TODO remove below

        db = getReadableDatabase();

        String sql = "select * from " + SocialTable.TABLE + " ";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String time = cursor.getString(1);
                String COL_HOURS = cursor.getString(2);
                String COL_MINS = cursor.getString(3);
                String COL_UNIQUENUM = cursor.getString(4);
                Log.d(TAG, "Returned = " + id + ", " + time + ", COL_HOURS " + COL_HOURS +", COL_MINS "+COL_MINS+", COL_UNIQUENUM "+COL_UNIQUENUM);
            } while (cursor.moveToNext());
        }
        cursor.close();



        return Id;

    }

    public long addFinance(double money){
        //Check for repeats and delete so only one record per day
        SQLiteDatabase db = getReadableDatabase();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        String sqlCheck = "select * from " + FinanceTable.TABLE + " where date = ? ";
        Cursor cursorCheck = db.rawQuery(sqlCheck, new String[]{ formatDate.format(date) });
        long removeid = -1;
        if (cursorCheck.moveToFirst()){
            Log.d("Repeat", "Found");
            removeid = cursorCheck.getLong(0);
        }else{
            Log.d("Repeat", "Not Found");
        }



        db = getWritableDatabase();

        if(removeid != -1){
            db.delete(FinanceTable.TABLE, FinanceTable.COL_ID + " = ?", new String[]{Long.toString(removeid)});
        }

        ContentValues values = new ContentValues();
        values.put(FinanceTable.COL_DATE, formatDate.format(date));
        values.put(FinanceTable.COL_MONEY, money);

        long Id = db.insert(FinanceTable.TABLE, null, values);
        Log.d("Database Insert Eat","Returned a : "+Id);


        //TODO remove below

        db = getReadableDatabase();

        String sql = "select * from " + FinanceTable.TABLE + " ";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String time = cursor.getString(1);
                String Money = cursor.getString(2);
                Log.d(TAG, "Returned = " + id + ", " + time + ",COL_MONEY " + Money );
            } while (cursor.moveToNext());
        }
        cursor.close();





        return Id;
    }

    public long addMood(int mood){
        //Check for repeats and delete so only one record per day
        SQLiteDatabase db = getReadableDatabase();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();


        String sqlCheck = "select * from " + MoodTable.TABLE + " where date = ? ";
        Cursor cursorCheck = db.rawQuery(sqlCheck, new String[]{ formatDate.format(date) });
        long removeid = -1;
        if (cursorCheck.moveToFirst()){
            Log.d("Repeat", "Found");
            removeid = cursorCheck.getLong(0);
        }else{
            Log.d("Repeat", "Not Found");
        }



        db = getWritableDatabase();

        if(removeid != -1){
            db.delete(MoodTable.TABLE, MoodTable.COL_ID + " = ?", new String[]{Long.toString(removeid)});
        }

        ContentValues values = new ContentValues();
        values.put(MoodTable.COL_DATE, formatDate.format(date));
        values.put(MoodTable.COL_MOOD, mood);

        long Id = db.insert(MoodTable.TABLE, null, values);
        Log.d("Database Insert Eat","Returned a : "+Id);


        //TODO remove below

        db = getReadableDatabase();

        String sql = "select * from " + MoodTable.TABLE + " ";
        Cursor cursor = db.rawQuery(sql, new String[]{});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String time = cursor.getString(1);
                String Mood = cursor.getString(2);
                Log.d(TAG, "Returned = " + id + ", " + time + ",Mood " + Mood );
            } while (cursor.moveToNext());
        }
        cursor.close();





        return Id;
    }

    /*
        TODO create all get functions
     */

}