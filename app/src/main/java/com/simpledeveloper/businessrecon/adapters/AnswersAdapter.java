package com.simpledeveloper.businessrecon.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simpledeveloper.businessrecon.R;
import com.simpledeveloper.businessrecon.db.Question;
import com.simpledeveloper.businessrecon.ui.Answer;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class AnswersAdapter extends RecyclerView.Adapter<AnswerViewHolder>{

    private List<Answer> answers;

    public AnswersAdapter(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_answer_card_view, parent, false);

        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnswerViewHolder holder, int position) {
        Realm realm = Realm.getDefaultInstance();
        Question q = realm.where(Question.class).equalTo("id", answers.get(position).getQuestionId()).findFirst();
        holder.mQuestion.setText(q.getQuestion());
        holder.mAnswer.setText(answers.get(position).getAnswer());
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public void remove(int position){
        answers.remove(position);
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setModels(List<com.simpledeveloper.businessrecon.ui.Answer> answersList){
        answers = new ArrayList<>(answersList);
    }

    public void animateTo(List<com.simpledeveloper.businessrecon.ui.Answer> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<com.simpledeveloper.businessrecon.ui.Answer> newModels) {
        for (int i = answers.size() - 1; i >= 0; i--) {
            final com.simpledeveloper.businessrecon.ui.Answer model = answers.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<com.simpledeveloper.businessrecon.ui.Answer> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final com.simpledeveloper.businessrecon.ui.Answer model = newModels.get(i);
            if (!answers.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<com.simpledeveloper.businessrecon.ui.Answer> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final com.simpledeveloper.businessrecon.ui.Answer model = newModels.get(toPosition);
            final int fromPosition = answers.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private com.simpledeveloper.businessrecon.ui.Answer removeItem(int position) {
        final com.simpledeveloper.businessrecon.ui.Answer model = answers.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, com.simpledeveloper.businessrecon.ui.Answer model) {
        answers.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final com.simpledeveloper.businessrecon.ui.Answer model = answers.remove(fromPosition);
        answers.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public com.simpledeveloper.businessrecon.ui.Answer getItem(int position){
        return answers.get(position);
    }
}
