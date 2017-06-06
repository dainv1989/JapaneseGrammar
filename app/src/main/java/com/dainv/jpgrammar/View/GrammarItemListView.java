package com.dainv.jpgrammar.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dainv.jpgrammar.Data.GrammarForm;
import com.dainv.jpgrammar.R;

import java.util.concurrent.TransferQueue;

/**
 * Created by user on 4/21/2016.
 */
public class GrammarItemListView extends LinearLayout {
    private TextView tvForm;
    private TextView tvIndex;

    public GrammarItemListView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_grammar_list_item, this);
        tvForm = (TextView)findViewById(R.id.grm_form);
        tvIndex = (TextView)findViewById(R.id.grm_form_id);
    }

    public void setItemList(GrammarForm item) {
        // set text view for an item
        tvForm.setText(item.getForm());
        tvIndex.setText(Integer.toString(item.getId()));
    }
}
