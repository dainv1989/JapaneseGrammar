package com.dainv.jpgrammar.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.dainv.jpgrammar.Data.GrammarForm;
import com.dainv.jpgrammar.View.GrammarItemListView;

import java.util.List;

/**
 * Created by user on 4/21/2016.
 */
public class GrammarListAdapter extends ArrayAdapter<GrammarForm> {
    private List<GrammarForm> lstForm;
    private Context context;

    public GrammarListAdapter(Context context, int resource, List<GrammarForm> objects) {
        super(context, resource, objects);
        this.context = context;
        lstForm = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GrammarItemListView view = new GrammarItemListView(context);
        GrammarForm item = lstForm.get(position);
        view.setItemList(item);
        return view;
    }
}
