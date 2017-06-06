package com.dainv.jpgrammar;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dainv.jpgrammar.Data.AppData;
import com.dainv.jpgrammar.Data.DatabaseHelper;
import com.dainv.jpgrammar.Data.FileHelper;

import java.io.File;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 1000; /* 1 second */
    private static final int MSG_LOADING_DATA_COMPLETE = 999;
    private TextView tvWaiting;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // after loading data from text file done, start main activity
            if (msg.what == MSG_LOADING_DATA_COMPLETE) {
                tvWaiting.setVisibility(View.VISIBLE);
                tvWaiting.setText(SplashActivity.this.getResources().getString(R.string.app_init_done));
                startMainActivity();
            }
            super.handleMessage(msg);
        }
    };

    public Handler getHandler() {
        return this.handler;
    }

    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        SplashActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tvWaiting = (TextView) findViewById(R.id.app_init_wait);
        tvWaiting.setVisibility(View.INVISIBLE);

        FileHelper helper = new FileHelper(this);
        /**
         * Check whether database is existed or not
         * If existed, prevent initialization which might clear added user data
         * If not, initialize database by loading data from CSV raw file
         */
        if (!helper.dbExists()) {
            tvWaiting.setVisibility(View.VISIBLE);
            Thread loadData = new Thread(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            });
            loadData.start();
        } else {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            AppData.getInstance().setDb(dbHelper);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMainActivity();
                }
            }, SPLASH_DURATION);
        }
    }

    private int loadData() {
        int result = 0;
        Context context = this;
        FileHelper helper = new FileHelper(context);
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        // clear content in case database was existed
        dbHelper.clearContent();

        result = helper.loadGrammar(dbHelper);
        result = helper.loadExample(dbHelper);
        AppData.getInstance().setDb(dbHelper);

        // send complete to main UI thread
        Message msg = Message.obtain();
        msg.what = MSG_LOADING_DATA_COMPLETE;
        SplashActivity.this.getHandler().sendMessage(msg);

        return result;
    }
}
