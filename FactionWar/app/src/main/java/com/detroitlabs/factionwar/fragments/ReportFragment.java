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
import android.renderscript.Sampler;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by andrewjb on 10/15/14.
 */
public class ReportFragment extends Fragment {

    public static TextView helloWorld;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report, null);
        helloWorld = (TextView) rootView.findViewById(R.id.test_string);
        MainActivity.reportReady=true;

        new ReportService().execute();

        return rootView;
    }

    /*
    **  Get the data and store it
    */

    public class ReportService extends AsyncTask<Void, Void, String> {

        int ownedCount;
        String apiOutput=null;

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

            String searchFor = String.valueOf(MainActivity.searchQuery);
            getStringCount(result, "\"name\": \""+MainActivity.searchQuery+"\"");
            helloWorld.setText(String.valueOf(ownedCount));
            //helloWorld.setText(result);
        }

        private String getStringCount(String result, String countThis) {
            result = result.toLowerCase();
            countThis = countThis.toLowerCase();
            int index = result.indexOf(countThis);
            while (index != -1) {
                ownedCount++;
                result = result.substring(index + 1);
                index = result.indexOf(countThis);
            }
            return result;
        }
    }
}