package com.simpledeveloper.businessrecon.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.simpledeveloper.businessrecon.R;
import com.simpledeveloper.businessrecon.adapters.QuestionsAdapter;
import com.simpledeveloper.businessrecon.db.Answer;
import com.simpledeveloper.businessrecon.listeners.OnItemClickListener;
import com.simpledeveloper.businessrecon.listeners.RecyclerItemClickListener;
import com.simpledeveloper.businessrecon.ui.Question;
import com.simpledeveloper.businessrecon.utils.DividerItemDecorator;
import com.simpledeveloper.businessrecon.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class AddNewQuestionActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private PopupMenu popup;
    private RecyclerView recyclerView;
    private List<Question> addQuestions;
    private QuestionsAdapter mAdapter;
    private Realm mRealm;
    private TextView mNoQuestionsUi;
    private EditText questionReference;
    private TextInputLayout questionWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.activity_add_new_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog(false, -1);
            }
        });

        mNoQuestionsUi = (TextView) findViewById(R.id.no_questions);

        mRealm = Realm.getDefaultInstance();

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

        initQuestions();
    }

    private void showInputDialog(boolean isEditMode, int position){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle(getString(R.string.add_new_question));

        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.add_question_layout, null);
        dialogBuilder.setView(dialogView);

        questionReference = (EditText) dialogView.findViewById(R.id.question);
        questionWrapper = (TextInputLayout) dialogView.findViewById(R.id.question_wrapper);

        if (isEditMode){
            com.simpledeveloper.businessrecon.db.Question qEdit = mRealm.where(com.simpledeveloper.businessrecon.db
                    .Question.class)
                    .equalTo("id", mAdapter.getItem(position).getId()).findFirst();
            questionReference.setText(qEdit.getQuestion());
            questionReference.setSelection(questionReference.getText().length());
        }

        dialogBuilder.setPositiveButton(isEditMode ? getString(R.string.update_button): getString(R.string.add_button), new
                DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialogBuilder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        Button saveQuestion = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        saveQuestion.setOnClickListener(new CustomClickListener(alertDialog, isEditMode, position));
    }

    private class CustomClickListener implements View.OnClickListener {

        private final Dialog dialog;
        private final boolean isEditMode;
        private final int position;

        CustomClickListener(Dialog dialog, boolean mode, int position) {
            this.dialog = dialog;
            this.isEditMode = mode;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            String questionValue = questionReference.getText().toString();

            if (questionValue.equals("")) {
                questionWrapper.setError(getString(R.string.question_cannot_be_empty));
                questionWrapper.setErrorEnabled(true);
            }else {

                dialog.dismiss();

                if (isEditMode){

                    com.simpledeveloper.businessrecon.db.Question edit = mRealm.where(com.simpledeveloper.businessrecon
                            .db.Question.class)
                            .equalTo("id", mAdapter.getItem(position).getId())
                            .findFirst();
                    if (edit != null){

                        mRealm.beginTransaction();
                        edit.setQuestion(questionValue.trim());
                        edit.setUpdatedAt(Utils.getCurrentDate());
                        mRealm.copyToRealmOrUpdate(edit);
                        mRealm.commitTransaction();
                    }
                }else{
                    long lastQuestionId;

                    RealmResults<com.simpledeveloper.businessrecon.db.Question> questions = mRealm.where(com.simpledeveloper
                            .businessrecon.db.Question.class)
                            .findAllSorted("id");

                    com.simpledeveloper.businessrecon.db.Question question = new com.simpledeveloper.businessrecon.db.Question();

                    if (questions.isEmpty()){
                        question.setId(0);
                    }else{
                        lastQuestionId = questions.last().getId();
                        question.setId(lastQuestionId + 1);
                    }

                    question.setQuestion(questionValue.trim());
                    question.setCreatedAt(Utils.getCurrentDate());
                    question.setUpdatedAt(Utils.getCurrentDate());

                    mRealm.beginTransaction();
                    mRealm.copyToRealm(question);
                    mRealm.commitTransaction();
                }

                finish();
                startActivity(getIntent());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.generic_search_menu, menu);

        final MenuItem item = menu.findItem(R.id.action_search);

        MenuItemCompat.expandActionView(item);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setQueryHint(getString(R.string.search_questions));

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

    private void initQuestions(){
        addQuestions = new ArrayList<>();

        RealmResults<com.simpledeveloper.businessrecon.db.Question> questions = mRealm.where(com.simpledeveloper
                .businessrecon.db.Question.class).findAllSorted("id", Sort.DESCENDING);

        if (!questions.isEmpty()){
            for (com.simpledeveloper.businessrecon.db.Question question: questions) {
                addQuestions.add(new Question(question.getId(), question.getQuestion(), question.getCreatedAt()));
            }
        }

        mAdapter = new QuestionsAdapter(addQuestions, onItemClickCallback);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        if (mAdapter.getItemCount() != 0){
            mNoQuestionsUi.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private OnItemClickListener.OnItemClickCallback onItemClickCallback = new OnItemClickListener.OnItemClickCallback() {
        @Override
        public void onItemClicked(View view, final int position) {
            switch (view.getId()){

                case R.id.more:
                    popup = new PopupMenu(AddNewQuestionActivity.this, view);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();

                            if (id == R.id.action_edit){
                                showInputDialog(true, position);
                                return true;
                            }else if(id == R.id.action_delete){
                                confirmDeleteAction(position);
                                return true;
                            }

                            return true;
                        }
                    });

                    popup.inflate(R.menu.question_menu_options);
                    popup.setGravity(Gravity.END);
                    popup.show();

                    break;

            }
        }
    };

    void confirmDeleteAction(final int position){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirm_delete_question))
                .setMessage(getString(R.string.delete_details))
                .setCancelable(false)
                .setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        RealmResults<com.simpledeveloper.businessrecon.db.Question> questionsToDelete = mRealm.where(com.simpledeveloper.businessrecon.db.Question
                                .class)
                                .equalTo("id", mAdapter.getItem(position).getId())
                                .findAllSorted("id");

                        mRealm.beginTransaction();

                        for (int i = 0; i < questionsToDelete.size(); i++) {
                            RealmResults<Answer> answersByQuestionId = mRealm.where(Answer.class)
                                    .equalTo("questionId", questionsToDelete.get(i).getId())
                                    .findAll();
                            if (!answersByQuestionId.isEmpty()){
                                answersByQuestionId.deleteAllFromRealm();
                            }

                        }

                        mRealm.commitTransaction();

                        mRealm.beginTransaction();
                        questionsToDelete.deleteAllFromRealm();
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText.equals("")){
            initQuestions();
            return true;
        }else{
            final List<Question> filteredModelList = filter(addQuestions, newText);

            mAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(mAdapter);
            mAdapter.animateTo(filteredModelList);
            recyclerView.scrollToPosition(0);
            return true;
        }
    }

    List<Question> filter(List<Question> models, String query) {

        query = query.toLowerCase();

        final List<Question> filteredModelList = new ArrayList<>();

        if(query.equals("")) { return addQuestions; }

        for (Question model : models) {
            final String text = model.getQuestion().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
