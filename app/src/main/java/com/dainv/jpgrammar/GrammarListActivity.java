package com.dainv.jpgrammar;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dainv.jpgrammar.Data.AppData;
import com.dainv.jpgrammar.Data.DatabaseHelper;
import com.dainv.jpgrammar.View.GrammarListFragment;

import java.util.ArrayList;
import java.util.List;

public class GrammarListActivity extends AppCompatActivity {
    private final static String LOG_TAG = "GRM_LIST_ACTIVITY";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private static int grammar_level = 0;
    private static int tab_count = 0;
    private static int form_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grammar_list);
        grammar_level = getIntent().getIntExtra(AppData.EXTRA_GRAMMAR_LEVEL, 0);

        DatabaseHelper db = AppData.getInstance().getDb();
        if (db == null) {
            Log.v(LOG_TAG, "----------> database NULL");
        }
        form_count = db.getFormCount(grammar_level);

        tab_count = form_count / AppData.NUMBER_FORMS_PER_TAB;
        if (tab_count * AppData.NUMBER_FORMS_PER_TAB < form_count)
            tab_count += 1;

        viewPager = (ViewPager)findViewById(R.id.grm_list);
        tabLayout = (TabLayout)findViewById(R.id.grm_index_tabs);

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int position = 0; position < tab_count; position++) {
            long start_index = position * AppData.NUMBER_FORMS_PER_TAB + 1;
            long last_index = (position + 1) * AppData.NUMBER_FORMS_PER_TAB;
            if (last_index > form_count)
                last_index = form_count;

            String title = String.format("%d - %d", start_index, last_index);
            GrammarListFragment fragment = new GrammarListFragment();
            adapter.addFragment(fragment, title);

            Bundle args = buildArguments();
            fragment.setArguments(args);
            fragment.setPosition(position);
        }
        viewPager.setAdapter(adapter);
    }

    /**
     * build arguments for a fragment at position of list viewpager
     */
    private Bundle buildArguments() {
        Bundle args = new Bundle();
        args.putInt(AppData.EXTRA_GRAMMAR_LEVEL, grammar_level);
        return args;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<GrammarListFragment> lstFragments;
        private List<String> lstTitles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            lstFragments = new ArrayList<>();
            lstTitles = new ArrayList<>();
        }

        @Override
        public Fragment getItem(int position) {
            return lstFragments.get(position);
        }

        @Override
        public int getCount() {
            // number of fragments item
            return lstFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // get title of item in list at specific position
            return lstTitles.get(position);
        }

        public void addFragment(GrammarListFragment fragment, String title) {
            lstFragments.add(fragment);
            lstTitles.add(title);
        }
    }
}
