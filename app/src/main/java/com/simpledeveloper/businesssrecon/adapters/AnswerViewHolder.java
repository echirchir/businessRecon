package com.simpledeveloper.businesssrecon.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.simpledeveloper.businesssrecon.R;

public class AnswerViewHolder extends RecyclerView.ViewHolder {

    TextView mQuestion;
    TextView mAnswer;
    public AnswerViewHolder(View itemView) {
        super(itemView);

        mQuestion = (TextView) itemView.findViewById(R.id.question);
        mAnswer = (TextView) itemView.findViewById(R.id.answer);
    }
}
