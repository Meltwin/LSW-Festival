package com.meltwin.lsw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.meltwin.lsw.widgets.Alarm;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class AlarmList extends AppCompatActivity {
    public LinearLayout ly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list);

        this.setTitle("Mes alarmes");
        ly = (LinearLayout) findViewById(R.id.alarmListList);
        load();
    }
    public void load() {
        ly.removeAllViews();
        try {
            JSONObject json = Main._alarm.getAlarmList();
            Iterator<String> temp = json.keys();
            while (temp.hasNext()) {
                String key = temp.next();
                JSONObject value = (JSONObject) json.get(key);

                ly.addView(new Alarm(this,getApplicationContext(),value.getString("hour"),value.getString("salle"),value.getString("what"),value.getString("who")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
