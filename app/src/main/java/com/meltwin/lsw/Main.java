package com.meltwin.lsw;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static Intent loading;
    public static Main _main;
    public static AlarmMan _alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        _main = this;
        _alarm = new AlarmMan(getApplicationContext());

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                openAlarmList();
            }
        });

        loading = new Intent(Main.this,Loading.class);
        startActivity(loading);
    }

    private void openAlarmList() {
        Intent i = new Intent(Main.this,AlarmList.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String loadData(String filename,int raw) {
        File f = new File(getFilesDir(),filename);
        if(f.exists()) {
            try {
                FileInputStream t = openFileInput(filename);

                BufferedReader in = new BufferedReader(new InputStreamReader(t));
                String inputLine;
                String result = "";
                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        else {
            InputStream r = getResources().openRawResource(raw);
            BufferedReader in = new BufferedReader(new InputStreamReader(r));
            String result ="";
            try {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
            return result;
        }
        return "";
    }
    public static String formatRoom(String salle) {
        switch(salle) {
            case "scene":
                return "sur scène";
            case "poncet":
                return "en salle Poncet";
            case "170":
                return "en 170";
            case "113":
                return "en 113";
            case "foyer":
                return "au foyer";
            default:
                return "en 160";
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        private String _salle;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View rootView;
            if (getArguments().getInt(ARG_SECTION_NUMBER) != 1) {
                rootView = inflater.inflate(R.layout.fragment_main, container, false);

                TextView title = (TextView) rootView.findViewById(R.id.salle);
                LinearLayout timeBoard = (LinearLayout) rootView.findViewById(R.id.timeBoard);
                String _json="";
                switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                    case 2:
                        title.setText(R.string.scene);
                        _json = Main._main.loadData("scene.json",R.raw.scene);
                        _salle = "scene";
                        break;
                    case 3:
                        title.setText(R.string.poncet);
                        _json = Main._main.loadData("poncet.json",R.raw.poncet);
                        _salle = "poncet";
                        break;
                    case 4:
                        title.setText(R.string.salle_ds);
                        _json = Main._main.loadData("170.json",R.raw.salle_ds);
                        _salle = "170";
                        break;
                    case 5:
                        title.setText(R.string.salle_centsoixante);
                        _json = Main._main.loadData("160.json",R.raw.salle_centsoixante);
                        _salle = "160";
                        break;
                    case 6:
                        title.setText(R.string.foyer);
                        _json = Main._main.loadData("foyer.json",R.raw.foyer);
                        _salle = "foyer";
                        break;
                }

                try {
                    String _color = "a";
                    JSONArray t = new JSONArray(_json);
                    int hour = 14;
                    int min = 00;
                    for(int i = 0; i<t.length();i++) {
                        JSONObject data = t.getJSONObject(i);
                        String type = data.getString("type");

                        String mStart = Integer.toString(min);
                        if (mStart.length() == 1) {
                            mStart = "0"+mStart;
                        }

                        String start = String.format("%sh%s", hour, mStart);
                        min += data.getInt("time");
                        while(min>=60) {
                            hour +=1;
                            min -= 60;
                        }
                        String m = Integer.toString(min);
                        if (m.length() == 1) {
                            m = "0"+m;
                        }

                        String end = String.format("%sh%s",hour,m);

                        if (!type.equals("pause")) {
                            timeBoard.addView(new TimeElement(getContext(),data.getInt("time"),false,_color,data.getString("name"),data.getString("art"),start,end,_salle));

                            if (_color.equals("a")) {
                                _color = "b";
                            }
                            else {
                                _color = "a";
                            }
                        }
                        else {
                            timeBoard.addView(new TimeElement(getContext(),data.getInt("time"),true,null,"","",start,end,_salle));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
            else {
                rootView = inflater.inflate(R.layout.fragment_buvette, container, false);
            }
            return rootView;
        }
    }
        /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 6;
        }
    }
}
