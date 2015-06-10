package com.msn.weiboclient;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.msn.support.utils.DisplayUtil;
import com.msn.weiboclient.homepage.fragment.MainContentFragment;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity {
    public static final String ACEESS_TOKEN_TAG = "ACEESS_TOKEN_TAG";

    FrameLayout contentLayout;
    ListView leftMenuLsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
        MainContentFragment fragment = (MainContentFragment)getSupportFragmentManager()
                                            .findFragmentById(R.id.content);

        fragment.showTimeline(getIntent().getStringExtra(ACEESS_TOKEN_TAG));

        final TextView tv = (TextView)findViewById(R.id.type_class);
        tv.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        //重置tv高度，让popup看起来像在toolbar下面
                        ViewGroup.LayoutParams vllp =   tv.getLayoutParams();
                        vllp.height = toolbar.getHeight();
                        tv.setLayoutParams(vllp);
                        tv.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                    }
                });

        //ActionBarActivity actionBarActivity = (ActionBarActivity)getActivity();


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                PopupMenu popup = new PopupMenu(MainActivity.this, tv);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        DisplayUtil.init(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            Intent searchIntent = new Intent(this,SearchActivity.class);
            this.startActivity(searchIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
