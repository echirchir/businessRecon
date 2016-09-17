package com.simpledeveloper.businesssrecon.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.simpledeveloper.businesssrecon.R;

public class QuestionViewHolder extends RecyclerView.ViewHolder {

    TextView mQuestion;
    TextView mCreatedAt;
    ImageView mMore;

    public QuestionViewHolder(View itemView) {
        super(itemView);

        mQuestion = (TextView) itemView.findViewById(R.id.question);
        mCreatedAt = (TextView) itemView.findViewById(R.id.date);
        mMore = (ImageView) itemView.findViewById(R.id.more);
    }
}
