package com.msn.weiboclient.protocol.http.security;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.msn.weiboclient.common.utils.URLHelper;
import com.msn.weiboclient.protocol.model.base.IWeiBoRequest;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Msn on 2015/2/5.
 */
public class XStringRequest extends StringRequest {

    public static String CODE;

    private IWeiBoRequest request;

    public XStringRequest(IWeiBoRequest request
            ,Response.Listener<String> listener,Response.ErrorListener errorListener){
        super(Method.POST, request.getUrl(), listener, errorListener);
        this.request = request;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return convertReq2Map();
//        Map<String,String> paramsMap = new HashMap<>();
//        paramsMap.put("client_id","797849368");
//        paramsMap.put("client_secret","212384aa45b564035e66c768c705ad4d");
//        paramsMap.put("grant_type","authorization_code");
//        paramsMap.put("code",CODE);
//        paramsMap.put("redirect_uri", URLHelper.DIRECT_URL);
//        return paramsMap;
    }

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
