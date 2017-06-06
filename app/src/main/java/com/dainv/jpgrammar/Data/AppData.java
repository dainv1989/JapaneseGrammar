package com.dainv.jpgrammar.Data;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by user on 4/21/2016.
 */
public class AppData {
    private final static String LOG_TAG = "APP_DATA";
    public final static int N5_LEVEL = 5;
    public final static int N4_LEVEL = 4;
    public final static int N3_LEVEL = 3;

    public final static int NUMBER_FORMS_PER_TAB = 15;
    public final static String EXTRA_GRAMMAR_LEVEL = "GRAMMAR_LEVEL";
    public final static String EXTRA_GRAMMAR_ID = "GRAMMAR_ID";

    public final static String JP_GRAMMAR_FILE = "jp_grammar.csv";
    public final static int GRAMMAR_FILE_FIELD_COUNT = 4;

    public final static String JP_EXAMPLE_FILE = "jp_example.csv";
    public final static int EXAMPLE_FILE_FIELD_COUNT = 5;

    private static AppData data;
    private AppData() {
        // private constructor
        // this class implemented follows singleton design pattern
    }

    public static AppData getInstance() {
        if (data == null) {
            data = new AppData();
        }
        return data;
    }

    private static DatabaseHelper dbHelper;
    public void setDb(DatabaseHelper dbHelper) {
        // set database for application data
        Log.v(LOG_TAG, "---> set database");
        this.dbHelper = dbHelper;
    }

    public DatabaseHelper getDb() {
        // get global app database
        return this.dbHelper;
    }
}
