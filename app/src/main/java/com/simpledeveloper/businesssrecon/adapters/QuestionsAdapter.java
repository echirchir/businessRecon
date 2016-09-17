package com.simpledeveloper.businesssrecon.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simpledeveloper.businesssrecon.R;
import com.simpledeveloper.businesssrecon.listeners.OnItemClickListener;
import com.simpledeveloper.businesssrecon.ui.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionViewHolder>{

    private List<Question> questions;
    private OnItemClickListener.OnItemClickCallback onItemClickCallback;

    public QuestionsAdapter(List<Question> questions, OnItemClickListener.OnItemClickCallback onItemClickCallback) {
        this.questions = questions;
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_card_view, parent, false);

        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        holder.mQuestion.setText(questions.get(position).getQuestion());
        holder.mMore.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public void remove(int position){
        questions.remove(position);
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setModels(List<Question> questionList){
        questions = new ArrayList<>(questionList);
    }

    public void animateTo(List<Question> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Question> newModels) {
        for (int i = questions.size() - 1; i >= 0; i--) {
            final Question model = questions.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Question> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Question model = newModels.get(i);
            if (!questions.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Question> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Question model = newModels.get(toPosition);
            final int fromPosition = questions.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    private Question removeItem(int position) {
        final Question model = questions.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, Question model) {
        questions.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final Question model = questions.remove(fromPosition);
        questions.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public Question getItem(int position){
        return questions.get(position);
    }
}
