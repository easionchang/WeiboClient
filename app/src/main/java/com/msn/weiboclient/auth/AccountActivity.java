package com.msn.weiboclient.auth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;
import com.msn.weiboclient.R;
import com.msn.weiboclient.db.DBHelper;
import com.msn.weiboclient.db.vo.UserInfoVO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends ActionBarActivity
        implements LoaderManager.LoaderCallbacks<List<UserInfoVO>>{
    public static final int LOADER_USER_LIST = 0;

    private List<UserInfoVO> allUser = new ArrayList<>();
    private ListView userListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Log.e("Test","onCreate...............");
        getSupportLoaderManager().initLoader(LOADER_USER_LIST,null,this).forceLoad();

        userListView = (ListView)findViewById(R.id.list);

    }

    private List<String> getShowName(){
        List<String> userNameList = new ArrayList<>();
        for (int i=0;i<allUser.size();i++){
            userNameList.add(allUser.get(i).getScreen_name());
        }
        return userNameList;
    }

    @Override
    public Loader<List<UserInfoVO>> onCreateLoader(int id, Bundle args) {
        Log.e("Test","onCreateLoader...............");
        return new UserInfoLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<UserInfoVO>> loader, List<UserInfoVO> data) {
        allUser = data;
        Log.e("Test","user size====="+data.size());
        for (int i=0;i<data.size();i++){
            Log.e("Test",">>>>>>>>>>>>>>>>>>>>>>>."+data.get(i).getScreen_name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,getShowName());
        userListView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<UserInfoVO>> loader) {

    }

    private static class UserInfoLoader extends AsyncTaskLoader<List<UserInfoVO>>{
        //TODO 静态内部类加上若引用防止内存泄露
        private Context context;
        public UserInfoLoader(Context context){
            super(context);
            this.context = context;
        }


        @Override
        public List<UserInfoVO> loadInBackground() {
            try {
                Log.e("Test","loadInBackground....");
                Dao<UserInfoVO,String> dao = DBHelper.getDao(context,UserInfoVO.class,String.class);
                return dao.queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
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
