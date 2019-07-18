package com.example.drashtimuni.hydrologicaldatamanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "weather.db";
    public static final String TABLE_NAME = "weather_data";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_MODEL_TYPE = "model_type";
    public static final String COLUMN_WEATHER_DATA_TYPE = "weather_data_type";
    public static final String COLUMN_WEATHER_DATA = "weather_data";
    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        System.loadLibrary("sqliteX");
        this.context = context;
        //SQLiteDatabase db = this.getWritableDatabase();
        getDatabase(context);
    }

    private SQLiteDatabase getDatabase(Context context) {
        SQLiteDatabase db = null;
        //try
        //{
            File databaseFile = context.getDatabasePath(DATABASE_NAME);
            db = SQLiteDatabase.openOrCreateDatabase(databaseFile,null);
        //}
        //catch (Exception e)
        //{
            //String databasePath =  context.getFilesDir().getPath() +  "/" + DATABASE_NAME;
            //File databaseFile = new File(databasePath);
            //db = SQLiteDatabase.openOrCreateDatabase(databaseFile, null);
        //}
        return db;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        getDatabase(context).execSQL("create table " + TABLE_NAME + " (" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_DAY + " text, " +
                COLUMN_LATITUDE + " real, " +
                COLUMN_LONGITUDE + " real, " +
                COLUMN_MODEL_TYPE + " text, " +
                COLUMN_WEATHER_DATA_TYPE + " text, " +
                COLUMN_WEATHER_DATA + " real " +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        getDatabase(context).execSQL("drop  table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(WeatherData weatherData) {
        SQLiteDatabase db = getDatabase(context);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DAY, weatherData.getDayString("yyyy-mm-dd"));
        contentValues.put(COLUMN_LATITUDE, weatherData.getLatitude());
        contentValues.put(COLUMN_LONGITUDE, weatherData.getLongitude());
        contentValues.put(COLUMN_MODEL_TYPE, weatherData.getModelType());
        contentValues.put(COLUMN_WEATHER_DATA_TYPE, weatherData.getWeatherDataType());
        contentValues.put(COLUMN_WEATHER_DATA, weatherData.getWeatherData());
        long result = db.insert(TABLE_NAME, null, contentValues);
        return (result != -1);
    }

    public Cursor getAllData() {
        SQLiteDatabase db = getDatabase(context);
        Cursor result = db.rawQuery("select * from " + TABLE_NAME, new String[] {});
        return result;
    }

    public double getData(String latitude, String longitude, Date date, String modelType, String weatherType) {
        String day = new SimpleDateFormat("yyyy-mm-dd").format(date);
        SQLiteDatabase db = getDatabase(context);
        Cursor result = db.rawQuery("select * from " + TABLE_NAME + " where latitude = " + latitude
                        + " and longitude = " + longitude + " and day = '" + day + "' and model_type = '"
                        + modelType + "' and weather_data_type = '" + weatherType + "'", null);
        result.moveToNext();
        return result.getDouble(6);
    }

    public double getData(String latitude, String longitude, Date date) {
        String day = new SimpleDateFormat("M/dd/yy H:mm").format(date);
        SQLiteDatabase db = getDatabase(context);
        Cursor result = db.rawQuery("select * from mri_cgcm_precipitation " + " where lat = " + latitude
                + " and lon = " + longitude + " and time = '" + day + "'", null);
        result.moveToNext();
        return result.getDouble(6);
    }
}

