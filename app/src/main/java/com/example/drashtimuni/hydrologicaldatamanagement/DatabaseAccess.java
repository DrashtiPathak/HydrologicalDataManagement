package com.example.drashtimuni.hydrologicaldatamanagement;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Drashti Pathak
 * date: 15-May-2019
 */
public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if(instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.db = openHelper.getReadableDatabase();
    }

    public void close() {
        if(db != null) {
            this.db.close();
        }
    }

    private Cursor getDataCursor(String tableName, String latitude, String longitude, String day) {
        String query = "select * from " + tableName + " where lat = " + latitude
                + " and lon = " + longitude + " and time = '" + day + "'";
        Log.d("Specific query", query);
        return db.rawQuery(query, null);
    }

    private Cursor getApproxDataCursor(String tableName, String latitude, String longitude, String day) {
        Log.d("Fetch approx lat & lon", "original latitude : [ " + latitude + " ], longitude : [ " + longitude + " ]");
        Cursor result;
        double lat = Double.parseDouble(latitude);
        double lon = Double.parseDouble(longitude);
        String query = "select * from " + tableName + " where " +
                "lat > " + (lat - 0.05) + " and lat < " + (lat + 0.05)
                + " and lon > " + (lon - 0.05) + " and lon < " + (lon + 0.05) +
                " and time = '" + day + "'";
        Log.d("Approx query", query);
        result = db.rawQuery(query, null);
        return result;
    }

    public String getTableData(String tableName, String latitude, String longitude, Date date, String datePattern ) {
        String day = new SimpleDateFormat(datePattern).format(date);
        Cursor result = getDataCursor(tableName, latitude, longitude, day);
        if(result.getCount() == 0) {
            result = getApproxDataCursor(tableName, latitude, longitude, day);
        }
        String tableData = "No data";
        if(result.getCount() > 0) {
            result.moveToNext();
            tableData = result.getString(4);
        } else {
            Log.d("No data found", "Latitude : [ " + latitude + " ], Longitude : [ " + longitude + " ], Day : [ " + day + " ], Table : [ " + tableName + " ]");
        }
        return tableData;
    }

    public String getPrecipitationData(String modelName, String latitude, String longitude, Date date) {
        String tableData = getTableData("mri_cgcm_precipitation", latitude, longitude, date, "M/dd/yyyy H:mm:ss");
        if("BCC CSM".equalsIgnoreCase(modelName)) {
            tableData = getTableData("bcc_csm_precipitation", latitude, longitude, date, "M/dd/yyyy H:mm:ss");
        } else if("CCSM".equalsIgnoreCase(modelName)) {
            tableData = getTableData("ccsm_precipitation", latitude, longitude, date, "M/dd/yyyy H:mm:ss");
        }
        if("No data" != tableData) {
            double precipitation = Double.parseDouble(tableData);
            precipitation = precipitation / 25.4;
            tableData = Double.toString(precipitation);
        }
        return tableData;
    }

    public String getHumidityData(String modelName, String latitude, String longitude, Date date) {
        String tableData = getTableData("mri_cgcm_humidity", latitude, longitude, date, "M/dd/yyyy H:mm:ss");
        if("BCC CSM".equalsIgnoreCase(modelName)) {
            tableData = getTableData("bcc_csm_humidity", latitude, longitude, date, "M/dd/yyyy H:mm:ss");
        } else if("CCSM".equalsIgnoreCase(modelName)) {
            tableData = "No data";
        }
        return tableData;
    }

    public String getMinimumTemperatureData(String modelName, String latitude, String longitude, Date date, String temperatureUnit) {
        String tableData = getTableData("mri_cgcm_min_temperature", latitude, longitude, date, "M/dd/yyyy H:mm:ss");
        if("BCC CSM".equalsIgnoreCase(modelName)) {
            tableData = getTableData("bcc_csm_min_temperature", latitude, longitude, date, "M/dd/yyyy H:mm:ss");
        } else if("CCSM".equalsIgnoreCase(modelName)) {
            tableData = getTableData("ccsm_min_temperature", latitude, longitude, date, "M/dd/yyyy H:mm:ss");
        }
        if("No data" != tableData) {
            tableData = convertUnit(tableData, temperatureUnit);
        }
        return tableData;
    }

    public String getMaximumTemperatureData(String modelName, String latitude, String longitude, Date date, String temperatureUnit) {
        String tableData = getTableData("mri_cgcm_max_temperature", latitude, longitude, date, "M/dd/yyyy H:mm:ss");
        if("BCC CSM".equalsIgnoreCase(modelName)) {
            tableData = getTableData("bcc_csm_max_temperature", latitude, longitude, date, "M/dd/yyyy H:mm:ss");
        } else if("CCSM".equalsIgnoreCase(modelName)) {
            tableData = getTableData("ccsm_max_temperature", latitude, longitude, date, "M/dd/yyyy H:mm:ss");
        }
        if("No data" != tableData) {
            tableData = convertUnit(tableData, temperatureUnit);
        }
        return tableData;
    }

    private String convertUnit(String tableData, String temperatureUnit) {
        double minimumTemperature = Double.parseDouble(tableData);
        if("fahrenheit".equalsIgnoreCase(temperatureUnit)) {
            minimumTemperature = ((minimumTemperature - 273.15) * 9 / 5) + 32;
        } else {
            minimumTemperature = minimumTemperature - 273.15;
        }
        return Double.toString(minimumTemperature);
    }
}