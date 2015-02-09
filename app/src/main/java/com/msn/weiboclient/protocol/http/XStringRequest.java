package com.msn.weiboclient.protocol.http;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.msn.weiboclient.common.utils.URLHelper;
import com.msn.weiboclient.protocol.http.WeiBoResponseListener;
import com.msn.weiboclient.protocol.model.base.IWeiBoRequest;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Msn on 2015/2/5.
 */
public class XStringRequest extends StringRequest {
    private IWeiBoRequest mReq;

    /**
     * 初始化Post请求
     * @param request
     * @param listener
     */
    public XStringRequest(IWeiBoRequest request ,WeiBoResponseListener listener){
        super(Method.POST, request.getUrl(), listener, listener);
        this.mReq = request;
    }

    /**
     * 初始化Get请求
     * @param url
     * @param listener
     */
    public XStringRequest(String url,WeiBoResponseListener listener){
        super(Method.GET, url, listener, listener);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return convertReq2Map();
    }

    /**
     * 构建Get的URL
     * @param request
     * @return
     */
    public static String buildUrl(IWeiBoRequest request){
        String url = request.getUrl() + "?";
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
        Class c = mReq.getClass();
        Field[] fields  = c.getDeclaredFields();
        for (Field f : fields){
            try {
                f.setAccessible(true);
                paramMap.put(f.getName(),f.get(mReq).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return paramMap;
    }
}
