package com.meltwin.lsw;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import com.meltwin.lsw.widgets.EscapeGame;
import com.meltwin.lsw.widgets.TimeElementRing;

public class TimeElement extends android.support.v7.widget.AppCompatTextView {
    private int _time;
    private String _text;
    private String _start;
    private String _end;
    private Context _context;
    private String _art;
    private String _artistes;
    private String _salle;
    @SuppressLint("ResourceAsColor")
    public TimeElement(Context context, int time, boolean pause,String color,String person, String art,String start, String end,String salle) {
            super(context);
            _time = time;
            _context = context;
            _start = start;
            _end = end;
            _art = art;
            _artistes = person;
            _salle = salle;

            _text = String.format("%s\n%s",art,person);

            // Set global Properties
            switch (time) {
                case 5:
                    this.setHeight((int) getResources().getDimension(R.dimen.five));
                    break;
                case 10:
                    this.setHeight((int) getResources().getDimension(R.dimen.ten_minutes));
                    break;
                case 15:
                    this.setHeight((int) getResources().getDimension(R.dimen.fifteen_minutes));
                    break;
                case 20:
                    this.setHeight((int) getResources().getDimension(R.dimen.twenty_minutes));
                    break;
                case 25:
                    this.setHeight((int) getResources().getDimension(R.dimen.twentyfive_minutes));
                    break;
                case 30:
                    this.setHeight((int) getResources().getDimension(R.dimen.half_minutes));
                    break;
                case 60:
                    this.setHeight((int) getResources().getDimension(R.dimen.hour));
                    break;
                case 115:
                    this.setHeight((int) getResources().getDimension(R.dimen.cent_quinze));
                    break;
                default:
                    this.setHeight((int) getResources().getDimension(R.dimen.five));
                    break;
            }
            this.setTextColor(getResources().getColor(R.color.time));
            this.setGravity(Gravity.CENTER);


            /*
                ######################
                        Display
                ######################
           */
            if (!pause) {
                if (time <=5) {
                    this.setTextSize((float)12.0);
                }
                else if (time <=15) {
                    this.setTextSize((float)14.0);
                }
                else {
                    this.setTextSize((float)16.0);
                }

                if (color == "a") {
                    this.setBackgroundColor(getResources().getColor(R.color.timeScoreA));
                }
                else {
                    this.setBackgroundColor(getResources().getColor(R.color.timeScoreB));
                }

                this.setText(_text);

                /*
                ######################
                        OnTouch
                ######################
                */
                this.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener();
                    }
                });

            }
            else {
                this.setText(R.string.pause);
            }


    }
    private void clickListener() {
        if (_art.equals("Escape Game")) {
            Intent i = new Intent(_context,EscapeGame.class);
            i.putExtra("start",_start);
            i.putExtra("end",_end);
            i.putExtra("time",_time);
            i.putExtra("art",_art);
            i.putExtra("persons",_artistes);
            i.putExtra("salle",_salle);
            _context.startActivity(i);
        }else {
            Intent i = new Intent(_context,TimeElementRing.class);
            i.putExtra("start",_start);
            i.putExtra("end",_end);
            i.putExtra("time",_time);
            i.putExtra("art",_art);
            i.putExtra("persons",_artistes);
            i.putExtra("salle",_salle);
            _context.startActivity(i);
        }
    }
}
