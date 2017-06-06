package com.dainv.jpgrammar.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dainv.jpgrammar.R;

/**
 * Created by dainv on 8/1/2016.
 */
public class DialogInfo extends DialogFragment {
    TextView tvVersion;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Activity activity = getActivity();
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_info, null);

        String appVersion = "1.0.0";
        try {
            PackageManager manager = activity.getPackageManager();
            final PackageInfo packageInfo = manager.getPackageInfo(activity.getPackageName(), 0);
            appVersion = packageInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException exp) {
            Log.v("AboutDialog", "EXCEPTION: " + exp.toString());
            exp.printStackTrace();
        }

        tvVersion = (TextView)view.findViewById(R.id.info_message);
        tvVersion.setText("version " + appVersion + "\nThis app uses data from Saromalang.com");

        builder.setView(view);
        return builder.create();
    }
}
