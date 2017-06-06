package com.dainv.jpgrammar;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.dainv.jpgrammar.Data.AppData;
import com.dainv.jpgrammar.Data.DatabaseHelper;
import com.dainv.jpgrammar.Data.GrammarForm;
import com.dainv.jpgrammar.Data.Sentence;

import java.util.ArrayList;
import java.util.List;

public class GrammarItemActivity extends AppCompatActivity {
    private static final String LOG_TAG = "GRAMMAR_ITEM_ACTIVITY";
    private TextView tvGrmForm;
    private TextView tvGrmDesc;
    private TextView tvExample;

    private int grammar_id = 0;
    private GrammarForm form;
    private List<Sentence> examples;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_item);

        tvGrmForm = (TextView)findViewById(R.id.grm_item_form);
        tvGrmDesc = (TextView)findViewById(R.id.grm_item_desc);
        tvExample = (TextView)findViewById(R.id.grm_item_example);

        grammar_id = getIntent().getIntExtra(AppData.EXTRA_GRAMMAR_ID, 0);
        DatabaseHelper db = AppData.getInstance().getDb();
        form = db.getGrammarForm(grammar_id);
        examples = db.getExampleByForm(grammar_id);

        if (!form.getForm().isEmpty())
            tvGrmForm.setText(form.getForm());

        if (!form.getDescription().isEmpty())
            tvGrmDesc.setText(form.getDescription());

        Sentence sentence;
        String txtExample = "";

        for (int i = 0; i < examples.size(); i++) {
            sentence = examples.get(i);
            txtExample += "<font color=\""+ getResources().getString(R.string.htmlColorFormText) +
                    "\">" + sentence.getKanji() + "</font><br>";
            txtExample += "<font color=\""+ getResources().getString(R.string.htmlColorPronText) +
                    "\">" + sentence.getRomaji() + "</font><br>";
            txtExample += "<font color=\""+ getResources().getString(R.string.htmlColorTranText) +
                    "\">" + sentence.getTranslation() + "</font><br><br>";
        }
        tvExample.setText(Html.fromHtml(txtExample));
    }

    /**
     * force ActionBar back button behave same way as hardware back button
     * this will prevent in situation empty grammar form list because grammar_level is 0
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
