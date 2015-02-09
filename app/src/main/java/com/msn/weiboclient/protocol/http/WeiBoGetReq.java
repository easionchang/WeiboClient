package com.msn.weiboclient.protocol.http;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.msn.weiboclient.protocol.model.base.IWeiBoRequest;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Msn on 2015/2/5.
 */
public class WeiBoGetReq extends StringRequest {
    private IWeiBoRequest request;

    public WeiBoGetReq(IWeiBoRequest request
            , WeiBoResponseListener listener, Response.ErrorListener errorListener){
        super(Method.POST, request.getUrl(), listener, errorListener);
        this.request = request;

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return convertReq2Map();
    }

    private String reBuildUrl(String url){
        url+="?";
        Class c = request.getClass();
        Field[] fields  = c.getDeclaredFields();
        for (Field f : fields){
            try {
                f.setAccessible(true);
                url = url + (f.getName()+"="+f.get(request).toString()+"&");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if(url.endsWith("&")){
            url = url.substring(0,url.length()-1);
        }
        return url;
    }

    /**
     * 将VO对象转换为HashMap对象
     * Map对象最终会被转换为HTTP Parameter
     * @return
     */
    private Map<String,String> convertReq2Map(){
        Map<String,String> paramMap = new HashMap<>();
        Class c = request.getClass();
        Field[] fields  = c.getDeclaredFields();
        for (Field f : fields){
            try {
                f.setAccessible(true);
                paramMap.put(f.getName(),f.get(request).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return paramMap;
    }
}
