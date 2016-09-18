package com.simpledeveloper.businessrecon.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simpledeveloper.businessrecon.R;

public class QuestionViewHolder extends RecyclerView.ViewHolder {

    TextView mQuestion;
    ImageView mMore;

    public QuestionViewHolder(View itemView) {
        super(itemView);

        mQuestion = (TextView) itemView.findViewById(R.id.question);
        mMore = (ImageView) itemView.findViewById(R.id.more);
    }
}
