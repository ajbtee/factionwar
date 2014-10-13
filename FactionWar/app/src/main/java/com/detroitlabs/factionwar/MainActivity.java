package com.detroitlabs.factionwar;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;


public class MainActivity extends Activity {
    TextView helloWorld;
    int minmatar;
    int amarr;
    int gallente;
    int caldari;

    // https://neweden-dev.com/API
    // https://api.eveonline.com/Map/FacWarSystems.xml.aspx
    // http://public-crest.eveonline.com/districts/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FactionService apiGet = new FactionService();
        helloWorld = (TextView) findViewById(R.id.pending_data);
        new FactionService().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    **  Get the data and store it
    */
    class FactionService extends AsyncTask<Void, Void, String> {

        String apiOutput = null;
        String minmatarCheck = "owningFactionName=\"Minmatar Republic\"";
        String amarrCheck = "owningFactionName=\"Amarr Empire\"";
        String gallenteCheck = "owningFactionName=\"Gallente Federation\"";
        String caldariCheck = "owningFactionName=\"Caldari State\"";

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder responseString = new StringBuilder();
            String inputLine;
            try {
                URL oracle = new URL("https://api.eveonline.com/Map/FacWarSystems.xml.aspx");
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
            getStringCount(result, minmatarCheck);
            getStringCount(result, amarrCheck);
            getStringCount(result, gallenteCheck);
            getStringCount(result, caldariCheck);
            helloWorld.setText("[ M: " + minmatar + " ] [ A: " + amarr + " ] [ G: " + gallente + " ] [ C: " + caldari + " ]");
        }

        // Count the number of occupied systems in result
        private String getStringCount(String result, String countThis) {
            int index = result.indexOf(countThis);
            while (index != -1) {
                if (countThis == minmatarCheck)
                    minmatar++;
                else if (countThis == amarrCheck)
                    amarr++;
                else if (countThis == gallenteCheck)
                    gallente++;
                else if (countThis == caldariCheck)
                    caldari++;
                result = result.substring(index + 1);
                index = result.indexOf(countThis);
            }
            return result;
        }
    }
}