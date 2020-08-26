package com.meltwin.lsw.widgets;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Constraints;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.util.Log;

import com.meltwin.lsw.AlarmList;
import com.meltwin.lsw.Main;
import com.meltwin.lsw.R;

public class Alarm extends ConstraintLayout {
    private int _height = 100;
    private String _salle;
    private String _hour;
    private AlarmList _list;
    public Alarm(AlarmList list,Context context, String hour, String salle, String what, String who) {
        super(context);
        _salle = salle;
        _hour = hour;
        _list = list;

        // Set Widget Parameter
        this.setBackgroundColor(getResources().getColor(R.color.time));
        //this.setMinWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //this.setMaxHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //this.setMaxHeight(80);
        ViewGroup.LayoutParams cons = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,_height+8);
        this.setLayoutParams(cons);
        this.setForegroundGravity(Gravity.CENTER);
        this.setPadding(4,4,4,4);


        /*
            ##############################################
                            Add Inner
            ##############################################
         */

        LinearLayout ln = new LinearLayout(context);
        ln.setId(View.generateViewId());
        ln.setOrientation(LinearLayout.HORIZONTAL);
        ln.setGravity(Gravity.CENTER);
        ln.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        ViewGroup.LayoutParams r = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,_height);
        ln.setLayoutParams(r);
        this.addView(ln);

        // Time Info
        int color = getResources().getColor(R.color.time);

        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,_height,4);
        TextView timeStart = new TextView(context);
        timeStart.setText(hour);
        timeStart.setId(View.generateViewId());
        timeStart.setTextColor(color);
        timeStart.setGravity(Gravity.CENTER);
        timeStart.setLayoutParams(params1);
        ln.addView(timeStart);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,_height,32);
        TextView text = new TextView(context);
        text.setText(what+" ("+who+")\n"+Main.formatRoom(salle));
        text.setTextColor(color);
        text.setGravity(Gravity.CENTER);
        text.setLayoutParams(params2);
        ln.addView(text);

        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,_height,1);
        Space t = new Space(context);
        t.setBackgroundColor(getResources().getColor(R.color.timeScoreB));
        t.setLayoutParams(params3);
        ln.addView(t);

        // Cancel the alarm

        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(40,_height,6);
        ImageView cancel = new ImageView(context);
        cancel.setPadding(4,30,4,30);
        cancel.setId(View.generateViewId());
        cancel.setBackgroundColor(getResources().getColor(R.color.colorAccentDarker));
        cancel.setImageResource(R.drawable.cancel);
        cancel.setLayoutParams(params4);
        ln.addView(cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

    }
    private void cancelAlarm() {
        _list.ly.removeView(this);
        Main._alarm.removeAlarm(_salle,_hour);
    }
}
