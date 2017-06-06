package com.dainv.jpgrammar.Data;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Created by user on 4/21/2016.
 */
public class FileHelper {
    private static final String LOG_TAG = "FileHelper";
    // column index in CSV files
    private static final int COL_IDX_ID = 0;
    private static final int COL_IDX_FORM = 1;
    private static final int COL_IDX_DESC = 2;
    private static final int COL_IDX_LEVEL = 3;

    private static final int COL_IDX_KANJI = 1;
    private static final int COL_IDX_ROMAJI = 2;
    private static final int COL_IDX_TRANSLATION = 3;
    private static final int COL_IDX_GRM_ID = 4;

    private static Context context;

    public FileHelper(Context context) {
        // init properties
        this.context = context;
    }

    /**
     * Wrap function of read CSV file function in CSVReader class
     * This function uses opensource library openCSV-2.3.jar
     * @param fileName: CSV File path in asset folder
     * @return List of string arrays
     */
    public List<String[]> readCSV(String fileName) {
        List<String[]> lstRet = new ArrayList<>();
        String[] next = {};

        try {
            InputStreamReader streamReader = new InputStreamReader(context.getAssets().open(fileName));
            CSVReader reader = new CSVReader(streamReader);
            for(;;) {
                next = reader.readNext();
                if(next != null) {
                    lstRet.add(next);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            Log.v(LOG_TAG, "Read raw file error\n");
            e.printStackTrace();
        }
        return lstRet;
    }

    public int loadGrammar(DatabaseHelper dbHelper) {
        int ret = 0;
        List<String[]> lstForm;
        FileHelper fileHelper = new FileHelper(context);

        // parse grammar form file
        lstForm = fileHelper.readCSV(AppData.JP_GRAMMAR_FILE);
        if (lstForm != null && !lstForm.isEmpty()) {
            for (int i = 0; i < lstForm.size(); i++) {
                GrammarForm form = new GrammarForm();
                String[] line = lstForm.get(i);
                if (line.length < AppData.GRAMMAR_FILE_FIELD_COUNT)
                    continue;

                // verbose log for debug purpose only
                Log.v(LOG_TAG, "---> form id = " + line[0].replaceAll("[^\\d]", ""));

                form.setId(Integer.parseInt(line[COL_IDX_ID].replaceAll("[^\\d]", "")));
                form.setForm(line[COL_IDX_FORM]);
                form.setDescription(line[COL_IDX_DESC]);
                form.setLevel(Integer.parseInt(line[COL_IDX_LEVEL]));

                dbHelper.addGrammarForm(form);
            }
        } else {
            Log.v(LOG_TAG, "Load grammar form file failed.");
            ret = (-1);
        }
        return ret;
    }

    public int loadExample(DatabaseHelper dbHelper) {
        int ret = 0;
        List<String[]> lstExample;
        FileHelper fileHelper = new FileHelper(context);

        // parse example file
        lstExample = fileHelper.readCSV(AppData.JP_EXAMPLE_FILE);
        if (lstExample != null && !lstExample.isEmpty()) {
            for (int i = 0; i < lstExample.size(); i++) {
                Sentence sentence = new Sentence();
                String[] line = lstExample.get(i);
                if (line.length < AppData.EXAMPLE_FILE_FIELD_COUNT)
                    continue;

                // verbose log for debug purpose only
                String log = String.format("_CSV: %s, %s, %s, %s",
                        line[0].replaceAll("[^\\d]", ""), line[1], line[2], line[3]);
                //Log.v(LOG_TAG, log);

                int sentence_id = Integer.parseInt(line[COL_IDX_ID].replaceAll("[^\\d]", ""));
                sentence.setId(sentence_id);
                sentence.setKanji(line[COL_IDX_KANJI]);
                sentence.setRomaji(line[COL_IDX_ROMAJI]);
                sentence.setTranslation(line[COL_IDX_TRANSLATION]);

                // in case one example is used in many grammar forms
                long[] mapping_ids = new long[line.length - COL_IDX_GRM_ID];
                for (int j = 0; j < (line.length - COL_IDX_GRM_ID); j++) {
                    mapping_ids[j] = Integer.parseInt(line[j+COL_IDX_GRM_ID]);
                }

                dbHelper.addExample(sentence, mapping_ids);
            }
        } else {
            Log.v(LOG_TAG, "Read example file failed.");
            ret = (-1);
        }
        return ret;
    }

    public boolean dbExists() {
        File database= context.getDatabasePath(DatabaseHelper.getDbName());
        return database.exists();
    }
}
