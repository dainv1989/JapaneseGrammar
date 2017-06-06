package com.dainv.jpgrammar.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 4/20/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String LOG_TAG = "DatabaseHelper";
    private final static String DATABASE_NAME = "jp_grammar.db";
    private final static int DATABASE_VERSION = 1;

    // table name
    private final static String TBL_GRAMMAR = "grammars";
    private final static String TBL_EXAMPLE = "examples";
    private final static String TBL_MAPPING = "mapping";

    // column name for each table
    private final static String KEY_ID = "id";

    // grammar table columns
    private final static String COL_FORM = "form";
    private final static String COL_EXPLAIN = "explanation";
    private final static String COL_LEVEL = "level";

    // example table columns
    private final static String COL_KANJI = "kanji";
    private final static String COL_ROMAJI = "romaji";
    private final static String COL_TRANSLATION = "translation";

    // mapping table columns
    private final static String COL_GRAMMAR_ID = "grammar_id";
    private final static String COL_EXAMPLE_ID = "example_id";

    // create virtual table statements
    private final static String CREATE_GRAMMAR_TBL =
            "CREATE VIRTUAL TABLE " + TBL_GRAMMAR + " USING fts3 (" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            COL_FORM + " TEXT, " +
            COL_EXPLAIN + " TEXT, " +
            COL_LEVEL + " INTEGER" + ")";

    private final static String CREATE_EXAMPLE_TBL =
            "CREATE VIRTUAL TABLE " + TBL_EXAMPLE + " USING fts3 (" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            COL_KANJI + " TEXT, " +
            COL_ROMAJI + " TEXT, " +
            COL_TRANSLATION + " TEXT" + ")";

    private final static String CREATE_MAPPING_TBL =
            "CREATE VIRTUAL TABLE " + TBL_MAPPING + " USING fts3 (" +
            KEY_ID + " INTEGER PRIMARY KEY, " +
            COL_GRAMMAR_ID + " INTEGER, " +
            COL_EXAMPLE_ID + " INTEGER" + ")";

    private static Context context;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        Log.v(LOG_TAG, "---> data helper contructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(LOG_TAG, "---> create new database");
        db.execSQL(CREATE_GRAMMAR_TBL);
        db.execSQL(CREATE_EXAMPLE_TBL);
        db.execSQL(CREATE_MAPPING_TBL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete old database
        db.execSQL("DROP TABLE IF EXISTS " + TBL_GRAMMAR);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_EXAMPLE);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_MAPPING);
        // create new ones
        onCreate(db);
    }

    public static String getDbName() {
        return DATABASE_NAME;
    }

    /**
     * this function clear content to prevent the same data be appended to existed database
     */
    public void clearContent() {
        SQLiteDatabase db = this.getWritableDatabase();
        String del_grammar_tbl = "DELETE FROM " + TBL_GRAMMAR;
        String del_example_tbl = "DELETE FROM " + TBL_EXAMPLE;
        String del_mapping_tbl = "DELETE FROM " + TBL_MAPPING;
        db.execSQL(del_grammar_tbl);
        db.execSQL(del_example_tbl);
        db.execSQL(del_mapping_tbl);
    }

    public long addGrammarForm(GrammarForm form) {
        long form_id = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_ID, form.getId());
        values.put(COL_FORM, form.getForm());
        values.put(COL_EXPLAIN, form.getDescription());
        values.put(COL_LEVEL, form.getLevel());

        form_id = this.getWritableDatabase().insert(TBL_GRAMMAR, null, values);
        Log.v(LOG_TAG, "----> form_id = " + Long.toString(form_id));
        return form_id;
    }

    public long addExample(Sentence sentence, long[] grammar_ids) {
        long sentence_id = 0;
        ContentValues values = new ContentValues();
        Log.v(LOG_TAG, "---> " + sentence.getId() + " -- " + sentence.getKanji() + " -- " +
                sentence.getRomaji() + " -- " + sentence.getTranslation());
        values.put(KEY_ID, sentence.getId());
        values.put(COL_KANJI, sentence.getKanji());
        values.put(COL_ROMAJI, sentence.getRomaji());
        values.put(COL_TRANSLATION, sentence.getTranslation());

        sentence_id = this.getWritableDatabase().insert(TBL_EXAMPLE, "", values);

        Log.v(LOG_TAG, "----> sentence_id = " + Long.toString(sentence_id));

        for (long form_id : grammar_ids) {
            addMapping(sentence_id, form_id);
        }
        return sentence_id;
    }

    private long addMapping(long sentenceId, long formId) {
        long mapping_id = 0;
        ContentValues values = new ContentValues();
        values.put(COL_GRAMMAR_ID, formId);
        values.put(COL_EXAMPLE_ID, sentenceId);

        mapping_id = this.getWritableDatabase().insert(TBL_MAPPING, null, values);
        return mapping_id;
    }

    /**
     * Get total number of example sentences in database
     * @return
     */
    public int getExampleCount() {
        int count = 0;
        String query = "SELECT * FROM " + TBL_EXAMPLE;

        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    /**
     * Get number of example sentence for each level in database
     * @param level
     * @return
     */
    public int getExampleCount(int level) {
        int count = 0;
        String query = "SELECT * FROM " + TBL_GRAMMAR + " WHERE " + COL_LEVEL + "=" + level;

        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    /**
     * Get total number of grammar forms in database
     * @return
     */
    public int getFormCount() {
        int count = 0;
        String query = "SELECT * FROM " + TBL_GRAMMAR;

        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    /**
     * get number of grammar forms for each level in database
     * @param level
     * @return
     */
    public int getFormCount(int level) {
        int count = 0;
        String query = "SELECT * FROM " + TBL_GRAMMAR +
                " WHERE " + COL_LEVEL + "=" + level;

        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    /**
     * Get grammar form of a level at specific index
     * @param index
     * @return
     */
    public GrammarForm getGrammarForm(int index) {
        GrammarForm form = new GrammarForm();
        String query = "SELECT * FROM " + TBL_GRAMMAR + " WHERE " +
                KEY_ID + "=" + Integer.toString(index);
        Log.v(LOG_TAG, query);
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            form.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            form.setForm(cursor.getString(cursor.getColumnIndex(COL_FORM)));
            form.setDescription(cursor.getString(cursor.getColumnIndex(COL_EXPLAIN)));
            form.setLevel(cursor.getInt(cursor.getColumnIndex(COL_LEVEL)));

            cursor.close();
        } else {
            form = null;
        }
        return form;
    }

    public List<GrammarForm> getAllGrammarForms(int level) {
        List<GrammarForm> lstForm = new ArrayList<>();
        String query = "SELECT * FROM " + TBL_GRAMMAR + " WHERE " +
                COL_LEVEL + "=" + Integer.toString(level);
        Log.v(LOG_TAG, "----> QUERY: " + query);
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                GrammarForm form = new GrammarForm();
                form.setLevel(level);
                form.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                form.setForm(cursor.getString(cursor.getColumnIndex(COL_FORM)));
                form.setDescription(cursor.getString(cursor.getColumnIndex(COL_EXPLAIN)));

                lstForm.add(form);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return lstForm;
    }

    private Sentence getExample(int index) {
        Sentence sentence = new Sentence();
        String query = "SELECT * FROM " + TBL_EXAMPLE + " WHERE " +
                KEY_ID + "=" + Integer.toString(index);
        Log.v(LOG_TAG, query);
        Cursor cursor = this.getReadableDatabase().rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            sentence.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            sentence.setKanji(cursor.getString(cursor.getColumnIndex(COL_KANJI)));
            sentence.setRomaji(cursor.getString(cursor.getColumnIndex(COL_ROMAJI)));
            sentence.setTranslation(cursor.getString(cursor.getColumnIndex(COL_TRANSLATION)));

            cursor.close();
        }
        return sentence;
    }
    public List<Sentence> getExampleByForm(int grammarId) {
        List<Sentence> lstExample = new ArrayList<>();
        List<Long> lstExampleIds = new ArrayList<>();

        String query = "SELECT * FROM " + TBL_MAPPING + " WHERE " +
                COL_GRAMMAR_ID + "=" + Integer.toString(grammarId);
        Log.v(LOG_TAG, query);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Long example_id;
                example_id = cursor.getLong(cursor.getColumnIndex(COL_EXAMPLE_ID));
                lstExampleIds.add(example_id);
            } while (cursor.moveToNext());
            cursor.close();
        }

        for (int i = 0; i < lstExampleIds.size(); i++) {
            Sentence sentence = getExample(lstExampleIds.get(i).intValue());
            lstExample.add(sentence);
            Log.v(LOG_TAG, "----> retrieved example: " + sentence.getKanji() +
                    " -- " + sentence.getTranslation());
        }
        return lstExample;
    }

    public Cursor searchWord(String query, String[] columns) {
        String selection = COL_FORM + " MATCH ? ";
        String[] selectionArgs = new String[] {query + "*"};
        return query(selection, selectionArgs, columns);
    }

    public List<GrammarForm> searchForm(String query) {
        List<GrammarForm> lstForms = new ArrayList<>();
        String selection = COL_FORM + " MATCH ? ";
        String[] selectionArgs = new String[] {"*" + query + "*"};

        Cursor cursor = query(selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                GrammarForm form = new GrammarForm();
                form.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                form.setForm(cursor.getString(cursor.getColumnIndex(COL_FORM)));
                form.setDescription(cursor.getString(cursor.getColumnIndex(COL_EXPLAIN)));
                form.setLevel(cursor.getInt(cursor.getColumnIndex(COL_LEVEL)));
                Log.v(LOG_TAG, "---> found FORM: " + form.getForm());
                lstForms.add(form);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return lstForms;
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TBL_GRAMMAR);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = builder.query(db, columns,
                selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
}

