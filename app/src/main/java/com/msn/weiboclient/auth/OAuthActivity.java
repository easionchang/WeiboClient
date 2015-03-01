package com.msn.weiboclient.auth;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.SimpleCursorAdapter;

import com.j256.ormlite.dao.Dao;
import com.msn.weiboclient.R;
import com.msn.weiboclient.common.utils.URLHelper;
import com.msn.weiboclient.common.utils.Utility;
import com.msn.weiboclient.db.CommonDao;
import com.msn.weiboclient.db.DBHelper;
import com.msn.weiboclient.db.vo.UserInfoVO;
import com.msn.weiboclient.protocol.http.WeiBoResponseListener;
import com.msn.weiboclient.protocol.http.XHttpClient;
import com.msn.weiboclient.protocol.model.AccessTokenReq;
import com.msn.weiboclient.protocol.model.AccessTokenRsp;
import com.msn.weiboclient.protocol.model.UsersShowReq;
import com.msn.weiboclient.protocol.model.UsersShowRsp;
import com.msn.weiboclient.protocol.model.base.IWeiBoResponse;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class OAuthActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        String oauthUrl = getWeiboOAuthUrl();
        webView.loadUrl(oauthUrl);
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e("Test", "url=====" + url);
            handleURL(url);
        }
    }

    private void handleURL(String url) {
        if (url.startsWith(URLHelper.DIRECT_URL+"?code=")) {
            String code = url.replace(URLHelper.DIRECT_URL+"?code=", "");
            Log.e("Test", "code======" + code);
            AccessTokenReq req = new AccessTokenReq();
            req.setCode(code);
            req.setClient_id("797849368");
            req.setClient_secret("212384aa45b564035e66c768c705ad4d");
            req.setRedirect_uri(URLHelper.DIRECT_URL);
            XHttpClient.getInstance(this).post(req,
                    new WeiBoResponseListener<AccessTokenRsp>() {
                        public void onSuccess(AccessTokenRsp rsp) {

                            Log.e("Test", rsp.getAccess_token());
                            getUserInfo(rsp.getAccess_token(),rsp.getUid());
                        }


                    });
        }

    }

    private void getUserInfo(String token,String uid){
        UsersShowReq usersShowReq = new UsersShowReq();
        usersShowReq.setAccess_token(token);
        usersShowReq.setUid(uid);
        XHttpClient.getInstance(this).get(usersShowReq,new WeiBoResponseListener<UsersShowRsp>() {
            public void onSuccess(UsersShowRsp rsp) throws Exception{
                UserInfoVO vo = new UserInfoVO();
                vo.setId(rsp.getId());
                vo.setIdstr(rsp.getIdstr());
                vo.setName(rsp.getName());
                vo.setScreen_name(rsp.getScreen_name());
                Dao<UserInfoVO,String> dao = DBHelper.getDao(OAuthActivity.this,UserInfoVO.class,String.class);
                dao.createOrUpdate(vo);

                Log.e("Test", rsp.getScreen_name());
            }

            @Override
            public void onError(IWeiBoResponse rsp) {
                super.onError(rsp);
            }
        });
    }




    private String getWeiboOAuthUrl() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", URLHelper.APP_KEY);
        //TODO 怎么和例子不一样呢 response_type=TOKEN;估计是高级接口？
        parameters.put("response_type", "code");
        //返回redirect_uri_mismatch错误，就是后台的配置的URL和回调URL不一致
        parameters.put("redirect_uri", URLHelper.DIRECT_URL);
        parameters.put("display", "mobile");
        return URLHelper.URL_OAUTH2_ACCESS_AUTHORIZE + "?" + Utility.encodeUrl(parameters)
                + "&scope=friendships_groups_read,friendships_groups_write";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_oauth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
