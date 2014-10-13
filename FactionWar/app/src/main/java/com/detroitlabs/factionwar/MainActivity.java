package com.detroitlabs.factionwar;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.logging.Handler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;


public class MainActivity extends Activity {
    TextView helloWorld;
    LinearLayout barAmarr;
    LinearLayout barMinmatar;
    LinearLayout barGallente;
    LinearLayout barCaldari;
    LinearLayout.LayoutParams paramsMinmatar;
    LinearLayout.LayoutParams paramsAmarr;
    LinearLayout.LayoutParams paramsGallente;
    LinearLayout.LayoutParams paramsCaldari;
    float minmatar;
    float amarr;
    float gallente;
    float caldari;
    float barGoalMinmatar;
    float barGoalAmarr;
    float barGoalGallente;
    float barGoalCaldari;

    // https://neweden-dev.com/API
    // https://api.eveonline.com/Map/FacWarSystems.xml.aspx
    // http://public-crest.eveonline.com/districts/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barMinmatar = (LinearLayout) findViewById(R.id.bar_minmatar);
        paramsMinmatar = (LinearLayout.LayoutParams) barMinmatar.getLayoutParams();
        paramsMinmatar.weight = 0;
        barAmarr = (LinearLayout) findViewById(R.id.bar_amarr);
        paramsAmarr = (LinearLayout.LayoutParams) barAmarr.getLayoutParams();
        paramsAmarr.weight = 0;
        barGallente = (LinearLayout) findViewById(R.id.bar_gallente);
        paramsGallente = (LinearLayout.LayoutParams) barGallente.getLayoutParams();
        paramsGallente.weight = 0;
        barCaldari = (LinearLayout) findViewById(R.id.bar_caldari);
        paramsCaldari = (LinearLayout.LayoutParams) barCaldari.getLayoutParams();
        paramsCaldari.weight = 0;

        helloWorld = (TextView) findViewById(R.id.pending_data);
        new FactionService().execute();

        ValueAnimator animator = ValueAnimator.ofFloat(0,1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float val = (Float) valueAnimator.getAnimatedValue();
                paramsMinmatar.weight = val*barGoalMinmatar;
                paramsAmarr.weight = val*barGoalAmarr;
                paramsGallente.weight = val*barGoalGallente;
                paramsCaldari.weight = val*barGoalCaldari;
                barAmarr.setLayoutParams(paramsAmarr);
                barMinmatar.setLayoutParams(paramsMinmatar);
                barCaldari.setLayoutParams(paramsCaldari);
                barGallente.setLayoutParams(paramsGallente);
            }
        });
        animator.setDuration(5000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();

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
        // When this string value appears, increment +1 for relevant faction
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
            //helloWorld.setText("M"+minmatar + "  A" + amarr + "  G" + gallente + "  C" + caldari);
            helloWorld.setText("Faction occupancy:");

            barGoalMinmatar = (minmatar/(minmatar+amarr))*100;
            barGoalAmarr = (amarr/(minmatar+amarr))*100;
            barGoalGallente = (gallente/(gallente+caldari))*100;
            barGoalCaldari = (caldari/(gallente+caldari))*100;

//            paramsMinmatar.weight = (minmatar/(minmatar+amarr))*100;
//            paramsAmarr.weight = (amarr/(minmatar+amarr))*100;
//            paramsGallente.weight = (gallente/(gallente+caldari))*100;
//            paramsCaldari.weight = (caldari/(gallente+caldari))*100;
//            barAmarr.setLayoutParams(paramsAmarr);
//            barMinmatar.setLayoutParams(paramsMinmatar);
//            barCaldari.setLayoutParams(paramsCaldari);
//            barGallente.setLayoutParams(paramsGallente);
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