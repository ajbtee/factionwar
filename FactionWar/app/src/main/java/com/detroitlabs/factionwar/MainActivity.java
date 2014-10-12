package com.detroitlabs.factionwar;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.w3c.dom.Document;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;


public class MainActivity extends Activity {

    //

    // https://neweden-dev.com/API
    // https://api.eveonline.com/Map/FacWarSystems.xml.aspx
    // http://public-crest.eveonline.com/districts/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView helloWorld = (TextView) findViewById(R.id.hello_world);
        FactionService apiGet = new FactionService();
        new FactionService().execute();

        helloWorld.setText(apiGet.doInBackground());
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

        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=London&mode=xml");
                urlConnection = (HttpURLConnection) url.openConnection();

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(urlConnection.getInputStream());
                TransformerFactory transFactory = TransformerFactory.newInstance();
                Transformer xform = transFactory.newTransformer();

                System.out.println("I FOUND WHAT YOU WANTED: "+urlConnection.getInputStream());
                apiOutput = String.valueOf(urlConnection.getInputStream());
                //DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                //DocumentBuilder db = dbf.newDocumentBuilder();
                //Document doc = db.parse(new InputSource(url.openStream()));
                //doc.getDocumentElement().normalize();
            } catch (Exception e) {
                System.out.println("\n");
                System.out.println("SOMETHING WENT WRONG: " + e);
                System.out.println("\n");
            } finally {
                urlConnection.disconnect();
            }

            System.out.println("\n");
            System.out.println("IS IT ME YOU'RE LOOKING FOR: " + apiOutput);
            System.out.println("\n");

            return apiOutput;
        }
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {
            System.out.println("\n");
            System.out.println("I COULD BE THE ONE: " + result);
            System.out.println("\n");
        }
    }
}