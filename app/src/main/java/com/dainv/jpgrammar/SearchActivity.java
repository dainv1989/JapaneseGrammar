package com.dainv.jpgrammar;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dainv.jpgrammar.Adapter.GrammarListAdapter;
import com.dainv.jpgrammar.Data.AppData;
import com.dainv.jpgrammar.Data.GrammarForm;

import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private final static String LOG_TAG = "SEARCH_ACTIVITY";
    private ListView lvResult;
    private TextView tvSummary;

    private List<GrammarForm> lstForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        lvResult = (ListView)findViewById(R.id.search_result);
        tvSummary = (TextView)findViewById(R.id.search_result_summary);

        Intent intent = getIntent();
        /**
         * User click search button: Display list of result
         * User select item in suggestion list: Show up grammar form
         */
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            tvSummary.setText("Searching by: "+ query);

            lstForm = AppData.getInstance().getDb().searchForm(query);
            GrammarListAdapter adapter = new GrammarListAdapter(this, lvResult.getId(), lstForm);
            lvResult.setAdapter(adapter);

            final Context context = this;
            lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // get grammar id in database
                    int grammar_id = lstForm.get(position).getId();
                    Log.v(LOG_TAG, "----> grammar id = " + grammar_id);
                    // pass it to next activity
                    Intent intent = new Intent(context, GrammarItemActivity.class);
                    intent.putExtra(AppData.EXTRA_GRAMMAR_ID, grammar_id);
                    startActivity(intent);
                }
            });
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            int grammar_id = 0;
            Uri uri = intent.getData();
            if (uri != null) {
                grammar_id = Integer.parseInt(uri.getLastPathSegment());
                Log.v(LOG_TAG, "----> URI: " + uri.toString());
                Intent it = new Intent(SearchActivity.this, GrammarItemActivity.class);
                it.putExtra(AppData.EXTRA_GRAMMAR_ID, grammar_id);
                startActivity(it);
            } else {
                tvSummary.setText(R.string.err_access_db);
            }
        }
    }
}
