package com.detroitlabs.factionwar.fragments;

import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.detroitlabs.factionwar.MainActivity;
import com.detroitlabs.factionwar.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by andrewjb on 10/15/14.
 */
public class ReportFragment extends Fragment {

    TextView helloWorld;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report, null);
        helloWorld = (TextView) rootView.findViewById(R.id.test_string);

        new ReportService().execute();

        return rootView;
    }

    /*
    **  Get the data and store it
    */
    class ReportService extends AsyncTask<Void, Void, String> {

        String apiOutput = null;

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder responseString = new StringBuilder();
            String inputLine;
            try {
                URL oracle = new URL("http://public-crest.eveonline.com/districts/");
                HttpURLConnection yc = (HttpURLConnection) oracle.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        yc.getInputStream()));
                while ((inputLine = in.readLine()) != null)
                    responseString.append(inputLine);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            apiOutput = responseString.toString();

            return apiOutput;
        }
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {
            helloWorld.setText(apiOutput);
        }

    }
}