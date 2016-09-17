package com.simpledeveloper.businesssrecon.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.simpledeveloper.businesssrecon.R;
import com.simpledeveloper.businesssrecon.db.Answer;
import com.simpledeveloper.businesssrecon.db.Question;

import io.realm.Realm;
import io.realm.RealmResults;

public class BusinessReconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.activity_business_recon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BusinessReconActivity.this, SurveyActivity.class));
            }
        });

        Realm realm = Realm.getDefaultInstance();

        RealmResults<Answer> answers = realm.where(Answer.class).findAllSorted("id");
        RealmResults<Question> questions = realm.where(Question.class).findAllSorted("id");

        ImageView surveysRounded = (ImageView) findViewById(R.id.stats_value);
        ImageView questionsRounded = (ImageView) findViewById(R.id.questions_value);

        assert surveysRounded != null;
        TextDrawable drawable1 = TextDrawable.builder().buildRound((!answers.isEmpty() ? ""+answers.size() : "0"), Color
                .LTGRAY);
        surveysRounded.setImageDrawable(drawable1);

        TextDrawable drawable2 = TextDrawable.builder().buildRound((!questions.isEmpty() ? ""+questions.size() : "0"), Color
                .LTGRAY);
        questionsRounded.setImageDrawable(drawable2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_business_recon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            startActivity(new Intent(this, AddNewQuestionActivity.class));
            return true;
        }else if(id == R.id.action_answers){
            startActivity(new Intent(this, SurveyAnswersActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
