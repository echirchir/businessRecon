package com.simpledeveloper.businessrecon.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.simpledeveloper.businessrecon.R;

import java.util.Calendar;

public class Utils {

    public static String getCurrentDate(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return (month+1) +"/"+ day + "/" + year + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
    }

    public static void showSnackBar(Context context, View layout, String resId){
        Snackbar snackbar = Snackbar
                .make(layout, resId, Snackbar.LENGTH_LONG)
                .setAction(context.getResources().getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        snackbar.show();
    }
}
