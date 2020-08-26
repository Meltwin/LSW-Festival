package com.meltwin.lsw.alarms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.meltwin.lsw.Main;
import com.meltwin.lsw.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class BaseAlarm extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        int id = getIntent().getIntExtra("id",0);

        // Notif
        Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vib.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
                    vib.vibrate(VibrationEffect.createOneShot(500,1));

                    // Create notification channel
                    CharSequence name = "Alarmes";
                    String description = "Vous prévient des spectacles qui vous interresse";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel("alarms", name, importance);
                    channel.setDescription(description);
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"alarms");
                    builder.setSmallIcon(R.drawable.notif);
                    builder.setContentTitle("Réweil-toi Simone !");

                    JSONObject json = Main._alarm.getAlarmInfo(Integer.toString(id));
                    builder.setContentText( String.format("%s - %s (%s) en %s dans 2 minutes",json.getString("hour"),json.getString("what"),json.getString("who"),Main.formatRoom(json.getString("salle"))));
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText(String.format("%s - %s (%s) %s dans 2 minutes",json.getString("hour"),json.getString("what"),json.getString("who"),Main.formatRoom(json.getString("salle")))));

                    // Launch Notif
                    notificationManager.notify((int) (Math.random() * 1000),builder.build());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                vib.vibrate(500);
                try {
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                    builder.setSmallIcon(R.drawable.notif);
                    builder.setContentTitle("Réweil-toi Simone !");

                    JSONObject json = Main._alarm.getAlarmInfo(Integer.toString(id));

                    builder.setContentText( String.format("%s - %s (%s) %s dans 2 minutes",json.getString("hour"),json.getString("what"),json.getString("who"),Main.formatRoom(json.getString("salle"))));
                    builder.setStyle(new NotificationCompat.BigTextStyle().bigText(String.format("%s - %s (%s) %s dans 2 minutes",json.getString("hour"),json.getString("what"),json.getString("who"),Main.formatRoom(json.getString("salle")))));


                    // Launch Notif
                    notificationManager.notify((int) (Math.random() * 1000),builder.build());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        // End Alarm
        Main._alarm.alarmSucceed(id);

        // Kill activity
        finish();
    }
}
