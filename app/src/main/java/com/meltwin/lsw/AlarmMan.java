package com.meltwin.lsw;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.meltwin.lsw.alarms.CentSoixante;
import com.meltwin.lsw.alarms.CentSoixanteDix;
import com.meltwin.lsw.alarms.Foyer;
import com.meltwin.lsw.alarms.Poncet;
import com.meltwin.lsw.alarms.Scene;
import com.meltwin.lsw.widgets.Alarm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import static java.lang.String.valueOf;

public class AlarmMan {
    private Context _cont;
    private JSONObject _data;
    private File _dataFile;
    private String _filename = "alarm.json";
    private String _date = "03-05-2019";
    public AlarmMan(Context cont) {
        _cont = cont;
        _dataFile = new File(cont.getFilesDir(),_filename);

        loadAlarmData();
    }
    private void loadAlarmData() {
        try {
            // If file doesn't exist, create him
            if (!_dataFile.exists()) {
                _dataFile.createNewFile();
                FileOutputStream output;
                output = _cont.openFileOutput(_filename,Context.MODE_PRIVATE);
                output.write("{}".getBytes());
                output.close();
            }

            // Load Data
            FileInputStream fData;
            fData = _cont.openFileInput(_filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(fData));
            String inputLine;
            String result = "";
            while ((inputLine = in.readLine()) != null) {
                result += inputLine;
            }
            fData.close();

            // Convert to JSON
            _data = new JSONObject(result);

        } catch (IOException | org.json.JSONException e) {
            e.printStackTrace();
        }
    }
    private void saveData() {
        try {
            _dataFile.delete();
            _dataFile.createNewFile();
            FileOutputStream output;
            output = _cont.openFileOutput(_filename,Context.MODE_PRIVATE);
            output.write(_data.toString().getBytes());
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addAlarm(String salle, String hour,String what, String who) {
        try {
            // Searching for the time
            Calendar c=Calendar.getInstance();
            SimpleDateFormat fd= new SimpleDateFormat("dd-MM-yyyy HH-mm", Locale.FRANCE);
            String[] h =  hour.split("h");
            int[] timing = new int[2];
            timing[0] = Integer.valueOf(h[0]);
            timing[1] = Integer.valueOf(h[1])-2;
            if (timing[1]<0) {
                timing[0] -=1;
                timing[1] += 60;
            }
            String minEnd = Integer.toString(timing[1]);
            if (minEnd.length() == 1) {
                minEnd = "0"+minEnd;
            }

            hour = String.format("%s-%s",h[0],h[1]);
            c.setTime(fd.parse(String.format("%s %s",_date,String.format("%s-%s",timing[0],minEnd))));
            long t = c.getTimeInMillis();

            /*
                ID LIST
                1 => Scene
                2 => PONCET
                3 => 170
                4 => 160

             */

            // Intent selection
            String[] s = hour.split("-");
            Intent i;
            switch (salle) {
                case "scene":
                    i = new Intent(_cont,Scene.class);
                    break;
                case "poncet":
                    i = new Intent(_cont,Poncet.class);
                    break;
                case "170":
                    i = new Intent(_cont,CentSoixanteDix.class);
                    break;
                case "113":
                    i = new Intent(_cont,CentSoixante.class);
                    break;
                case "foyer":
                    i = new Intent(_cont,Foyer.class);
                    break;
                default:
                    i = new Intent(_cont,Scene.class);
                    break;
            }
            int alarmID = getID(salle,hour);

            i.putExtra("id",alarmID);
            // Create the activity
            PendingIntent pending = PendingIntent.getActivity(_cont,alarmID,i,PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager am = (AlarmManager) _cont.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP,t,pending);

            // Save Alarm in local
            JSONObject alarm = new JSONObject();
            alarm.put("salle",salle);
            alarm.put("hour",s[0]+"h"+s[1]);
            alarm.put("what",what);
            alarm.put("who",who);
            _data.put(Integer.toString(alarmID), alarm);
            saveData();

        } catch (ParseException | org.json.JSONException e) {
            e.printStackTrace();
        }
    }
    public void removeAlarm(String salle, String hour) {
        try {
            // Searching for the time
            Calendar c=Calendar.getInstance();
            String[] h =  hour.split("h");
            hour = String.format("%s-%s",h[0],h[1]);
            SimpleDateFormat fd= new SimpleDateFormat("dd-MM-yyyy HH-mm", Locale.FRANCE);

            c.setTime(fd.parse(String.format("%s %s",_date,hour)));
            long t = c.getTimeInMillis();

                /*
                    ID LIST
                    1 => Scene
                    2 => PONCET
                    3 => 170
                    4 => 160

                 */

            // Intent selection
            String[] s = hour.split("-");
            Intent i;
            switch (salle) {
                case "scene":
                    i = new Intent(_cont,Scene.class);
                    break;
                case "poncet":
                    i = new Intent(_cont,Poncet.class);
                    break;
                case "170":
                    i = new Intent(_cont,CentSoixanteDix.class);
                    break;
                case "113":
                    i = new Intent(_cont,CentSoixante.class);
                    break;
                case "foyer":
                    i = new Intent(_cont,Foyer.class);
                    break;
                default:
                    i = new Intent(_cont,Scene.class);
                    break;
            }
            int alarmID = getID(salle,hour);

            i.putExtra("id",alarmID);
            // Create the activity
            PendingIntent pending = PendingIntent.getActivity(_cont,alarmID,i,PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager am = (AlarmManager) _cont.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP,t,pending);
            am.cancel(pending);

            _data.remove(Integer.toString(alarmID));
            saveData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void alarmSucceed(int id) {
        _data.remove(Integer.toString(id));
        saveData();
    }

    public int getID(String salle,String hour) {
        String[] s = hour.split("-");
        int alarmID;
        switch (salle) {
            case "scene":
                alarmID = 10000+(Integer.valueOf(s[0])*100)+Integer.valueOf(s[1]);
                break;
            case "poncet":
                alarmID = 20000+(Integer.valueOf(s[0])*100)+Integer.valueOf(s[1]);
                break;
            case "170":
                alarmID = 30000+(Integer.valueOf(s[0])*100)+Integer.valueOf(s[1]);
                break;
            case "113":
                alarmID = 40000+(Integer.valueOf(s[0])*100)+Integer.valueOf(s[1]);
                break;
            case "foyer":
                alarmID = 50000+(Integer.valueOf(s[0])*100)+Integer.valueOf(s[1]);
                break;
            default:
                alarmID = 000000;
                break;
        }
        return alarmID;
    }
    public JSONObject getAlarmList() {
        return _data;
    }
    public JSONObject getAlarmInfo(String id) {
        try {
            return _data.getJSONObject(id);
        } catch (JSONException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return new JSONObject();
    }
    public boolean checkAlarm(String salle,String hour) {
        String[] h =  hour.split("h");
        hour = String.format("%s-%s",h[0],h[1]);
        int id =  getID(salle,hour);
        Iterator<String> k = _data.keys();
        while (k.hasNext()) {
            if (Integer.toString(id).equals(k.next())) {
                return true;
            }
        }
        return false;
    }
}
