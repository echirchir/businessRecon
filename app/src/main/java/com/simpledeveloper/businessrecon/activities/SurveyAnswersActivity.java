package com.simpledeveloper.businessrecon.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.simpledeveloper.businessrecon.R;
import com.simpledeveloper.businessrecon.adapters.AnswersAdapter;
import com.simpledeveloper.businessrecon.listeners.RecyclerItemClickListener;
import com.simpledeveloper.businessrecon.ui.Answer;
import com.simpledeveloper.businessrecon.utils.DividerItemDecorator;

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

    private ActionMode currentActionMode;
    private int selectedItemPosition;
    private CardView selectedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.activity_survey_answers);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                selectedItemPosition = position;

                if (currentActionMode != null) {
                    return;
                }

                selectedView = (CardView) view;

                selectedView.setCardBackgroundColor(ContextCompat.getColor(SurveyAnswersActivity.this, R.color
                        .core_background_dark));
                currentActionMode = startSupportActionMode(modeCallBack);
                view.setSelected(true);
            }
        }));
    }

    private ActionMode.Callback modeCallBack = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(getResources().getString(R.string.actions));
            mode.getMenuInflater().inflate(R.menu.actions_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {

                case R.id.action_edit:
                    share(selectedItemPosition);
                    mode.finish();
                    return true;
                case R.id.action_delete:
                    confirmDeleteAction(selectedItemPosition);
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            currentActionMode = null;

            if (selectedView != null){
                selectedView.setCardBackgroundColor(ContextCompat.getColor(SurveyAnswersActivity.this, R.color
                        .core_background));
            }
        }
    };

    void share(int position){

    }

    void confirmDeleteAction(final int position){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete_answer))
                .setMessage(getString(R.string.delete_answer_desc))
                .setCancelable(false)
                .setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RealmResults<com.simpledeveloper.businessrecon.db.Answer> answersToDelete = mRealm.where(com
                                .simpledeveloper.businessrecon.db.Answer
                                .class)
                                .equalTo("id", mAdapter.getItem(position).getId())
                                .findAllSorted("id");

                        mRealm.beginTransaction();
                        answersToDelete.deleteAllFromRealm();
                        mRealm.commitTransaction();
                        mAdapter.remove(position);
                        mAdapter.notifyDataSetChanged();

                        dialog.dismiss();

                        if (mAdapter.getItemCount() == 0){
                            finish();
                            startActivity(getIntent());
                        }
                    }
                }).setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
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

        RealmResults<com.simpledeveloper.businessrecon.db.Answer> answers = mRealm.where(com.simpledeveloper
                .businessrecon.db.Answer.class).findAllSorted("id", Sort.DESCENDING);

        if (!answers.isEmpty()){
            for (com.simpledeveloper.businessrecon.db.Answer answer: answers) {
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
