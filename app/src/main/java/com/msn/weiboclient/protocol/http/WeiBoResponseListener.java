package com.msn.weiboclient.protocol.http;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.msn.weiboclient.WeiBoApplication;
import com.msn.weiboclient.protocol.model.base.IWeiBoResponse;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;

/**
 * Created by Msn on 2015/2/6.
 */
public abstract class WeiBoResponseListener<T extends IWeiBoResponse> implements
        Response.Listener<String>,Response.ErrorListener{


    public void onResponse(String response) {
        Log.e("Test", "successRsp=="+response) ;
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        T rsp = (T)gson.fromJson(response,(Class)params[0]);
        try {
            onSuccess(rsp);
        } catch (SQLException e) {
            Log.e("Test","数据库出错了",e);
            Toast.makeText(WeiBoApplication.context,"数据库出错了",Toast.LENGTH_LONG).show();
        }catch (Exception e) {
            Log.e("Test","出错了",e);
            Toast.makeText(WeiBoApplication.context,"出错了",Toast.LENGTH_LONG).show();
        }
    }

    public void onErrorResponse(VolleyError error) {
        try {
            String errorRsp = new String(error.networkResponse.data, "UTF-8");
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            IWeiBoResponse rsp = gson.fromJson(errorRsp,IWeiBoResponse.class);
            Log.e("Test", "errorRsp=="+errorRsp) ;
            Toast.makeText(WeiBoApplication.context,rsp.getError_description(),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(WeiBoApplication.context,"出错了",Toast.LENGTH_LONG).show();
        }
    }
    public abstract void onSuccess (T rsp) throws Exception;

    public void onError  (IWeiBoResponse rsp) {

    }
}
