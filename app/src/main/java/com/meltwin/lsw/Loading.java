package com.meltwin.lsw;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.util.JsonReader;
import android.util.Log;

import org.json.JSONArray;

public class Loading extends AppCompatActivity {
    private int _toLoad = 1;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        //loadData("170.json");
        finish();
    }

    private class GetFile extends AsyncTask<String, Integer, Long> {
        public InputStream resp;
        private Loading _parent;
        public String _JSONString = "";
        private String _filename;
        private String _url;
        public GetFile(Loading parent,String filename) {
            _parent = parent;
            _filename = filename;

            switch(filename) {
                case "scene.json":
                    _url = "https://orchitic-solenoid.000webhostapp.com/test.json";
                    break;
                case "170.json":
                    _url = "https://orchitic-solenoid.000webhostapp.com/test.json";
                    break;
                case "160.json":
                    _url = "https://orchitic-solenoid.000webhostapp.com/test.json";
                    break;
                case "poncet.json":
                    _url = "https://orchitic-solenoid.000webhostapp.com/test.json";
                    break;
            }
        }
        // these Strings / or String are / is the parameters of the task, that can be handed over via the excecute(params) method of AsyncTask
        protected Long doInBackground(String... params) {
            HttpURLConnection connect;
            try {
                URL url = new URL(_url);
                connect  = (HttpURLConnection) url.openConnection();
                //connect.setDoOutput(true);
                connect.connect();

                System.out.println(connect.getResponseCode());
                resp = connect.getInputStream();

                BufferedReader in = new BufferedReader(new InputStreamReader(resp));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    _JSONString = _JSONString + inputLine;
                }
                in.close();
                connect.disconnect();

                File f = new File(getFilesDir(),_filename);
                FileOutputStream outputStream;

                try {
                    if(f.exists()) {
                        f.delete();
                        f.createNewFile();
                        outputStream = openFileOutput(_filename, Context.MODE_PRIVATE);
                        outputStream.write(_JSONString.getBytes());
                        outputStream.close();
                    }
                    else {
                        f.createNewFile();
                        outputStream = openFileOutput(_filename, Context.MODE_PRIVATE);
                        outputStream.write(_JSONString.getBytes());
                        outputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return Long.valueOf(0);
        }

        // the onPostexecute method receives the return type of doInBackGround()
        protected void onPostExecute(Long result) {
            _parent.getDataResult(_JSONString);

        }
    }
    private void loadData(String filename) {
        ConnectivityManager net;
        net = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        if (net != null) {
            NetworkInfo wifi = net.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo data = net.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifi.isConnected() || data.isConnected()) {
                GetFile getter = null;
                getter = new GetFile(this,filename);
                try {
                    getter.execute().get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            else {
                finish();
            }
        }
        else {
            finish();
        }
    }
    private void getDataResult(String t) {
        _toLoad -= 1;
        if (_toLoad == 0) {
            finish();
        }
    }
}
