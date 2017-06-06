package com.dainv.jpgrammar;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.LauncherActivity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dainv.jpgrammar.Data.AppData;
import com.dainv.jpgrammar.Data.DatabaseHelper;
import com.dainv.jpgrammar.View.DialogInfo;
import com.dainv.jpgrammar.View.GrammarSummaryLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String LOG_TAG = "MAIN_ACTIVITY";

    private GrammarSummaryLayout sumN5;
    private GrammarSummaryLayout sumN4;
    private GrammarSummaryLayout sumN3;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerNav;
    private Toolbar toolbar;
    private NavigationView nvView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
            actionBar.setIcon(R.drawable.pear);
            actionBar.setHomeButtonEnabled(true);
        }

        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerNav = (ListView)findViewById(R.id.sidebar);

        // init drawer list view
        List<String> lstNavItems = new ArrayList<>();
        lstNavItems.add("Rate this app");
        lstNavItems.add("Information");
        lstNavItems.add("Share");
        drawerNav.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lstNavItems));
        DrawerItemClickListener drawerListener = new DrawerItemClickListener();
        drawerNav.setOnItemClickListener(drawerListener);

        sumN5 = (GrammarSummaryLayout) findViewById(R.id.n5_grammar);
        sumN4 = (GrammarSummaryLayout) findViewById(R.id.n4_grammar);
        sumN3 = (GrammarSummaryLayout) findViewById(R.id.n3_grammar);

        DatabaseHelper db = AppData.getInstance().getDb();
        sumN5.setItem("N5", db.getFormCount(AppData.N5_LEVEL), db.getExampleCount(AppData.N5_LEVEL));
        sumN4.setItem("N4", db.getFormCount(AppData.N4_LEVEL), db.getExampleCount(AppData.N4_LEVEL));
        sumN3.setItem("N3", db.getFormCount(AppData.N3_LEVEL), db.getExampleCount(AppData.N3_LEVEL));

        final Context context = this;
        sumN3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GrammarListActivity.class);
                intent.putExtra(AppData.EXTRA_GRAMMAR_LEVEL, AppData.N3_LEVEL);
                startActivity(intent);
            }
        });
        sumN4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GrammarListActivity.class);
                intent.putExtra(AppData.EXTRA_GRAMMAR_LEVEL, AppData.N4_LEVEL);
                startActivity(intent);
            }
        });
        sumN5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GrammarListActivity.class);
                intent.putExtra(AppData.EXTRA_GRAMMAR_LEVEL, AppData.N5_LEVEL);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawer.isDrawerOpen(drawerNav);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Show customized menu which consists of search button
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(searchItem);

        SearchManager manager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(manager.getSearchableInfo(
                new ComponentName(this, SearchActivity.class)));
        searchView.setIconifiedByDefault(false);

        searchItem.expandActionView();
        searchView.requestFocus();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // The action bar home/up action should open or close the drawer.

        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            Context context = getApplicationContext();
            switch (position) {
                // rate this app
                // TODO: test rating app feature
                case 0:
                    Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                    Intent gotoMarket = new Intent(Intent.ACTION_VIEW, uri);
                    gotoMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(gotoMarket);
                    } catch (ActivityNotFoundException e) {
                        uri = Uri.parse("http://play.google.com/store/app/details?id=" +
                                        context.getPackageName());
                        Intent webMarket = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(webMarket);
                    }
                    break;
                // information
                case 1:
                    DialogFragment dlgInfo = new DialogInfo();
                    dlgInfo.show(getFragmentManager(), "about");
                    break;
                // share this app
                case 2:
                    String textToShare = getResources().getText(R.string.app_share).toString();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.app_share_text));
                    startActivity(Intent.createChooser(intent, textToShare));
                    break;
                case 3:
                    break;
                default:
                    break;
            }
            drawer.closeDrawer(drawerNav);
        }
    }
}
