package com.dainv.jpgrammar.Data;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

/**
 * Created by dainv on 7/8/2016.
 */
public class DataSuggestionProvider extends ContentProvider {
    private static final String LOG_TAG = "SUGGEST_PROVIDER";
    private List<GrammarForm> lstForms;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String query = uri.getLastPathSegment();
        Log.v(LOG_TAG, "----> search text: " + query + " selection = " + selection);

        // reset previous result
        if (lstForms != null)
            lstForms.clear();

        DatabaseHelper db = AppData.getInstance().getDb();
        lstForms = db.searchForm(query);

        MatrixCursor cursor = new MatrixCursor(
                new String[] {
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
                }
        );

        if (lstForms != null && !lstForms.isEmpty()) {
            GrammarForm form;
            int limit = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));
            int size = lstForms.size();
            Log.v(LOG_TAG, "---> limit = " + limit + " number items found = " + size);
            for (int i = 0; (i < size && cursor.getCount() < limit); i++) {
                form = lstForms.get(i);
                cursor.addRow(new Object[] {i, form.getForm(), form.getId()});
            }
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
