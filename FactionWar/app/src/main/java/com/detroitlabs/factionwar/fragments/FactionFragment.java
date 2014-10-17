package com.detroitlabs.factionwar.fragments;

import android.animation.ValueAnimator;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.detroitlabs.factionwar.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FactionFragment extends Fragment {

    public TextView helloWorld;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_faction, null);

        barMinmatar = (LinearLayout) rootView.findViewById(R.id.bar_minmatar);
        paramsMinmatar = (LinearLayout.LayoutParams) barMinmatar.getLayoutParams();
        paramsMinmatar.weight = 0;
        barAmarr = (LinearLayout) rootView.findViewById(R.id.bar_amarr);
        paramsAmarr = (LinearLayout.LayoutParams) barAmarr.getLayoutParams();
        paramsAmarr.weight = 0;
        barGallente = (LinearLayout) rootView.findViewById(R.id.bar_gallente);
        paramsGallente = (LinearLayout.LayoutParams) barGallente.getLayoutParams();
        paramsGallente.weight = 0;
        barCaldari = (LinearLayout) rootView.findViewById(R.id.bar_caldari);
        paramsCaldari = (LinearLayout.LayoutParams) barCaldari.getLayoutParams();
        paramsCaldari.weight = 0;

        helloWorld = (TextView) rootView.findViewById(R.id.pending_data);
        new FactionService().execute();

        return rootView;
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

            helloWorld.setText("");
            //helloWorld.setText("M"+minmatar + "  A" + amarr + "  G" + gallente + "  C" + caldari);

            barGoalMinmatar = (minmatar/(minmatar+amarr))*100;
            barGoalAmarr = (amarr/(minmatar+amarr))*100;
            barGoalGallente = (gallente/(gallente+caldari))*100;
            barGoalCaldari = (caldari/(gallente+caldari))*100;

            // Animate the bars
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
            animator.setDuration(500);

            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
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
