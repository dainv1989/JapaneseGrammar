package com.dainv.jpgrammar.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dainv.jpgrammar.R;

/**
 * Created by dainv on 7/18/2016.
 */
public class GrammarSummaryLayout extends LinearLayout {
    private TextView tvLevel;
    private TextView tvFormCnt;
    private TextView tvExampleCnt;
    private Context context;

    public GrammarSummaryLayout(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public GrammarSummaryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public GrammarSummaryLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.grammar_level_summary, this);

        tvLevel = (TextView)findViewById(R.id.sum_level);
        tvFormCnt = (TextView)findViewById(R.id.sum_form_count);
        tvExampleCnt = (TextView)findViewById(R.id.sum_exp_count);

        tvLevel.setText("N5");
        tvFormCnt.setText("123");
        tvExampleCnt.setText("456");
    }

    public void setItem(String level, int form, int example) {
        String formDesc = " ";
        String expDesc = " ";
        formDesc += context.getResources().getString(R.string.form_hdr).toLowerCase();
        expDesc += context.getResources().getString(R.string.example_hdr).toLowerCase();

        tvLevel.setText(level);
        tvFormCnt.setText(Integer.toString(form) + formDesc);
        tvExampleCnt.setText(Integer.toString(example) + expDesc);
    }
}
