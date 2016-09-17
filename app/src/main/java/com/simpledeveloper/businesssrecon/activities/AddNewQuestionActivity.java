package com.simpledeveloper.businesssrecon.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.simpledeveloper.businesssrecon.R;
import com.simpledeveloper.businesssrecon.adapters.QuestionsAdapter;
import com.simpledeveloper.businesssrecon.listeners.OnItemClickListener;
import com.simpledeveloper.businesssrecon.listeners.RecyclerItemClickListener;
import com.simpledeveloper.businesssrecon.ui.Question;
import com.simpledeveloper.businesssrecon.utils.DividerItemDecorator;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddNewQuestionActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private PopupMenu popup;
    private RecyclerView recyclerView;
    private List<Question> addQuestions;
    private QuestionsAdapter mAdapter;
    private Realm mRealm;
    private TextView mNoQuestionsUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        initQuestions();

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
                finish();
                return true;

            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void initQuestions(){
        addQuestions = new ArrayList<>();

        RealmResults<com.simpledeveloper.businesssrecon.db.Question> questions = mRealm.where(com.simpledeveloper
                .businesssrecon.db.Question.class).findAllSorted("id");

        if (!questions.isEmpty()){
            for (com.simpledeveloper.businesssrecon.db.Question question: questions) {
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
        public void onItemClicked(View view, int position) {
            switch (view.getId()){

                case R.id.more:
                    popup = new PopupMenu(AddNewQuestionActivity.this, view);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();

                            if (id == R.id.action_edit){

                                return true;
                            }else if(id == R.id.action_delete){

                                return true;
                            }

                            return true;
                        }
                    });

                    popup.inflate(R.menu.question_menu_options);
                    popup.setGravity(Gravity.END);
                    popup.show();

                    Toast.makeText(AddNewQuestionActivity.this, "Clicked more icon", Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

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
