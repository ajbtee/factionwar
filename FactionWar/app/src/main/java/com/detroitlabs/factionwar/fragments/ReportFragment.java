package com.detroitlabs.factionwar.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.detroitlabs.factionwar.R;

/**
 * Created by andrewjb on 10/15/14.
 */
public class ReportFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report, null);
        return null;
    }

    /*
    **  Get the data and store it
    */
    class FactionService extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {

        }

    }
}
