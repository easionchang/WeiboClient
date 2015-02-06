package com.msn.weiboclient.protocol.http;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.msn.weiboclient.common.utils.URLHelper;
import com.msn.weiboclient.protocol.http.security.HttpsClientStack;
import com.msn.weiboclient.protocol.http.security.HttpsHurlStack;
import com.msn.weiboclient.protocol.http.security.XStringRequest;
import com.msn.weiboclient.protocol.model.base.IWeiBoRequest;

/**
 * Created by Msn on 2015/2/3.
 * 使用Volley框架封装HTTP请求
 */
public class XHttpClient {

    private static XHttpClient mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private XHttpClient(Context context){
        mCtx = context;
        //使用HTTPS请求
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext(),getHttpsStack(context));
        //mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized XHttpClient getInstance(Context context){
        if(mInstance == null){
            return new XHttpClient(context);
        }else{
            return mInstance;
        }
    }

    public void request(IWeiBoRequest request, Response.Listener<String> listener,
                        Response.ErrorListener errorListener){
        request(new XStringRequest(request,listener,errorListener));
    }

    public <T> void request(Request<T> request){
        mRequestQueue.add(request);
    }

    /**
     * 获取HTTPS请求
     * @param context
     * @return
     */
    private HttpStack getHttpsStack(Context context){
        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {}

        if (Build.VERSION.SDK_INT >= 9) {
            return new HttpsHurlStack();
        } else {
            // Prior to Gingerbread, HttpUrlConnection was unreliable.
            // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
            return new HttpsClientStack(AndroidHttpClient.newInstance(userAgent));
        }
    }
}
