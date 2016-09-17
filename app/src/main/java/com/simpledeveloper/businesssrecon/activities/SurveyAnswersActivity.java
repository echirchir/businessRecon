package com.simpledeveloper.businesssrecon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.simpledeveloper.businesssrecon.R;
import com.simpledeveloper.businesssrecon.adapters.AnswersAdapter;
import com.simpledeveloper.businesssrecon.listeners.RecyclerItemClickListener;
import com.simpledeveloper.businesssrecon.ui.Answer;
import com.simpledeveloper.businesssrecon.utils.DividerItemDecorator;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class SurveyAnswersActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Realm mRealm;
    private List<Answer> addedAnswers;
    private AnswersAdapter mAdapter;

    private RecyclerView recyclerView;
    private TextView mNoAnswersUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_answers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SurveyAnswersActivity.this, SurveyActivity.class));
            }
        });

        mRealm = Realm.getDefaultInstance();

        mNoAnswersUi = (TextView) findViewById(R.id.no_completed_surveys);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecorator(this, DividerItemDecorator.VERTICAL_LIST));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener
                .OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO: 9/17/16 probably disable clicks here
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAnswers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.generic_search_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);

        MenuItemCompat.expandActionView(item);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setQueryHint(getString(R.string.search_answers));

        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                startActivity(new Intent(this, BusinessReconActivity.class));
                return true;

            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void initAnswers(){
        addedAnswers = new ArrayList<>();

        RealmResults<com.simpledeveloper.businesssrecon.db.Answer> answers = mRealm.where(com.simpledeveloper
                .businesssrecon.db.Answer.class).findAllSorted("id", Sort.DESCENDING);

        if (!answers.isEmpty()){
            for (com.simpledeveloper.businesssrecon.db.Answer answer: answers) {
                addedAnswers.add(new Answer(answer.getId(), answer.getQuestionId(), answer.getAnswer()));
            }
        }

        mAdapter = new AnswersAdapter(addedAnswers);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        if (mAdapter.getItemCount() != 0){
            mNoAnswersUi.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText.equals("")){
            initAnswers();
            return true;
        }else{
            final List<Answer> filteredModelList = filter(addedAnswers, newText);

            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
            mAdapter.animateTo(filteredModelList);
            recyclerView.scrollToPosition(0);
            return true;
        }
    }

    List<Answer> filter(List<Answer> models, String query) {

        query = query.toLowerCase();

        final List<Answer> filteredModelList = new ArrayList<>();

        if(query.equals("")) { return addedAnswers; }

        for (Answer model : models) {
            final String text = model.getAnswer().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}
