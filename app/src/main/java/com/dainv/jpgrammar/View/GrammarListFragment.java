package com.dainv.jpgrammar.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dainv.jpgrammar.Adapter.GrammarListAdapter;
import com.dainv.jpgrammar.Data.AppData;
import com.dainv.jpgrammar.Data.DatabaseHelper;
import com.dainv.jpgrammar.Data.GrammarForm;
import com.dainv.jpgrammar.GrammarItemActivity;
import com.dainv.jpgrammar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 4/20/2016.
 */
public class GrammarListFragment extends Fragment {
    private final static String LOG_TAG = "GRAMMAR_LIST_FRAGMENT";
    private ListView lvGrammarList;
    private List<GrammarForm> lstForm;
    private Bundle args;
    private int position = 0;

    public GrammarListFragment() {
        // require empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // call father method.
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grammar_list, container, false);
        lvGrammarList = (ListView)view.findViewById(R.id.grm_sub_list);

        args = getArguments();
        extractArguments(args);


        GrammarListAdapter adapter = new GrammarListAdapter(
                this.getContext(), lvGrammarList.getId(),lstForm);
        lvGrammarList.setAdapter(adapter);

        final Context context = this.getContext();
        lvGrammarList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get grammar id in database
                int grammar_id = lstForm.get(position).getId();
                // pass it to next activity
                Intent intent = new Intent(context, GrammarItemActivity.class);
                intent.putExtra(AppData.EXTRA_GRAMMAR_ID, grammar_id);
                startActivity(intent);
            }
        });

        return view;
    }

    private void extractArguments(Bundle args) {
        this.args = args;
        int grammar_level = args.getInt(AppData.EXTRA_GRAMMAR_LEVEL);
        DatabaseHelper db = AppData.getInstance().getDb();
        lstForm = db.getAllGrammarForms(grammar_level);

        int form_count = lstForm.size();
        int start_index = position * AppData.NUMBER_FORMS_PER_TAB;
        int end_index = (position + 1) * AppData.NUMBER_FORMS_PER_TAB;
        if (end_index > form_count)
            end_index = form_count;

        lstForm = lstForm.subList(start_index, end_index);
        Log.v(LOG_TAG, "---> start index = " + start_index + ", end index = " + end_index);
    }

    /**
     * set position of fragment in a certain list
     * @param position
     */
    public void setPosition(int position) {
        this.position = position;
    }
}
