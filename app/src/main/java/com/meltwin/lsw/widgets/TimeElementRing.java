package com.meltwin.lsw.widgets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.meltwin.lsw.Main;
import com.meltwin.lsw.R;

public class TimeElementRing extends AppCompatActivity {
    private String _end;
    private String _start;
    private String _salle;
    private String _what;
    private String _who;
    private Button alarmLaunch;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spec_info);

        _end = getIntent().getStringExtra("end");
        _start = getIntent().getStringExtra("start");
        _salle = getIntent().getStringExtra("salle");

        TextView art = (TextView) findViewById(R.id.spec_title);
        TextView artiste = (TextView) findViewById(R.id.spec_artiste);
        art.setText(getIntent().getStringExtra("art"));
        _what = getIntent().getStringExtra("art");
        artiste.setText(String.format("par %s",getIntent().getStringExtra("persons")));
        _who = getIntent().getStringExtra("persons");

        TextView horaire = (TextView) findViewById(R.id.spec_horaire);
        String hor = String.format("%s - %s (%s minutes)",getIntent().getStringExtra("start"),getIntent().getStringExtra("end"),getIntent().getIntExtra("time",5));
        horaire.setText(hor);

        alarmLaunch = (Button) findViewById(R.id.spec_alarm);
        alarmLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSetAlarm();
            }
        });
    }

    private void onSetAlarm() {
        if (Main._alarm.checkAlarm(_salle,_start)) {
            Main._alarm.removeAlarm(_salle,_start);
            alarmLaunch.setText("+ Ajouter une alarme");
        }
        else {
            Main._alarm.addAlarm(_salle,_start,_what,_who);
            alarmLaunch.setText("Suprimer l'alarme");
        }
    }
}
