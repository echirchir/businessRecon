package com.simpledeveloper.businesssrecon.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.simpledeveloper.businesssrecon.R;
import com.simpledeveloper.businesssrecon.db.Question;

import io.realm.Realm;
import io.realm.RealmResults;

public class SurveyActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private Realm mRealm;

    private static RealmResults<Question> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        mRealm = Realm.getDefaultInstance();

        questions = mRealm.where(Question.class).findAllSorted("id");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), questions.size());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_survey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class SurveyQuestionFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public SurveyQuestionFragment() {
        }

        public static SurveyQuestionFragment newInstance(int sectionNumber) {
            SurveyQuestionFragment fragment = new SurveyQuestionFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber -1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_survey, container, false);

            getActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            TextView textView = (TextView) rootView.findViewById(R.id.question);
            int qPosition = getArguments().getInt(ARG_SECTION_NUMBER);

            ImageView rounded = (ImageView) rootView.findViewById(R.id.question_number);

            assert rounded != null;
            TextDrawable drawable = TextDrawable.builder().buildRound(""+(qPosition+1),
                    ContextCompat.getColor(getActivity(), R.color.colorAccent));
            rounded.setImageDrawable(drawable);

            if (qPosition < questions.size()){
                textView.setText(questions.get(qPosition).getQuestion());
            }

            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int sizeOfQuestions;

        public SectionsPagerAdapter(FragmentManager fm, int size) {
            super(fm);

            this.sizeOfQuestions = size;
        }

        @Override
        public Fragment getItem(int position) {
            return SurveyQuestionFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return sizeOfQuestions;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return null;
        }
    }
}
