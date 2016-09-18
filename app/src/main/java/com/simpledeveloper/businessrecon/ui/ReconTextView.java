package com.simpledeveloper.businessrecon.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class ReconTextView extends TextView {
    public ReconTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public ReconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public ReconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        applyCustomFont(context);
    }

    public ReconTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = TypeFaceSingleton.getInstance().getTypeface(context);
        setTypeface(customFont);
    }
}
