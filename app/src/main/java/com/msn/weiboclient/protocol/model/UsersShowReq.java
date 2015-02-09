package com.msn.weiboclient.protocol.model;

import com.msn.weiboclient.common.utils.URLHelper;
import com.msn.weiboclient.protocol.model.base.IWeiBoRequest;

/**
 * Created by Msn on 2015/2/6.
 */
public class UsersShowReq extends IWeiBoRequest {
    private String access_token;
    private String uid;


    public UsersShowReq(){
        url = URLHelper.WEIBO_HOST_URL+"2/users/show.json";
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
