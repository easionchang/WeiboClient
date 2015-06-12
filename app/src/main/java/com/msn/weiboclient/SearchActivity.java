package com.msn.weiboclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.msn.support.utils.DisplayUtil;
import com.msn.weiboclient.homepage.fragment.MainContentFragment;

public class SearchActivity extends ActionBarActivity {
    public static final String ACEESS_TOKEN_TAG = "ACEESS_TOKEN_TAG";

    FrameLayout contentLayout;
    ListView leftMenuLsv;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        searchView = (SearchView)toolbar.findViewById(R.id.searchview);
        searchView.setIconified(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //ActionBarActivity actionBarActivity = (ActionBarActivity)getActivity();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String queryText) {
                Log.e("Test","queryText==="+queryText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String queryText) {
                if (searchView != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(
                                searchView.getWindowToken(), 0);
                    }
                    searchView.clearFocus();
                }
                Log.e("Test", "submit queryText===" + queryText);
                return true;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        DisplayUtil.init(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        }  else if(id == android.R.id.home) {
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
