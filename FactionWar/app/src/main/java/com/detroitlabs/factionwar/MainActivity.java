package com.detroitlabs.factionwar;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.detroitlabs.factionwar.fragments.FactionFragment;
import com.detroitlabs.factionwar.fragments.ReportFragment;

/**
 * Created by andrewjb on 10/14/14.
 **/

public class MainActivity extends Activity implements SearchView.OnQueryTextListener {

    public static CharSequence searchQuery;
    SearchView searchView;
    Fragment factionFragment;
    Fragment reportFragment;

    // https://neweden-dev.com/API
    // https://api.eveonline.com/Map/FacWarSystems.xml.aspx
    // http://public-crest.eveonline.com/districts/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        // Create new fragments and transaction
        factionFragment = new FactionFragment();
        reportFragment = new ReportFragment();

        // Commit the transaction
        fragmentTransaction.add(R.id.fragment_container, factionFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
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

    @Override
    public boolean onQueryTextSubmit(String s) {

        // Commit the transaction

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, reportFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        searchQuery = searchView.getQuery();
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(R.id.search);
        // FactionFragment.helloWorld.setText(searchQuery);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }
}