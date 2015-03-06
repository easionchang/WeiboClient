package com.msn.weiboclient.protocol.http;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.msn.weiboclient.R;
import com.msn.weiboclient.protocol.http.security.HttpsClientStack;
import com.msn.weiboclient.protocol.http.security.HttpsHurlStack;
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

    public void post(IWeiBoRequest request, WeiBoResponseListener listener){
        request(new XStringRequest(request,listener));
    }

    public void get(IWeiBoRequest request, WeiBoResponseListener listener){
        request(new XStringRequest(XStringRequest.buildUrl(request),listener));
    }

    public void loadImage(String url,ImageView imgView){
        ImageLoader imageLoader = new ImageLoader(mRequestQueue, new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imgView,R.drawable.ic_launcher, R.drawable.ic_launcher);
        imageLoader.get(url, listener);
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


    public class BitmapCache implements ImageLoader.ImageCache {

        private LruCache<String, Bitmap> cache;

        public BitmapCache() {
            cache = new LruCache<String, Bitmap>(8 * 1024 * 1024) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return cache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            cache.put(url, bitmap);
        }
    }

}
