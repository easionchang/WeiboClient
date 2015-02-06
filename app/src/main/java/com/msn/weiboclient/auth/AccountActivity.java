package com.msn.weiboclient.auth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;

import com.msn.weiboclient.R;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        final List<Class> activityList = new ArrayList<>();
        List<String> itemValueList = new ArrayList<>();
        itemValueList.add("网页登陆");
        activityList.add(OAuthActivity.class);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_account) {
            new AlertDialog.Builder(this).setItems(itemValueList.toArray(new CharSequence[itemValueList.size()]), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(AccountActivity.this,activityList.get(which));
                    AccountActivity.this.startActivity(intent);
                }
            }).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
