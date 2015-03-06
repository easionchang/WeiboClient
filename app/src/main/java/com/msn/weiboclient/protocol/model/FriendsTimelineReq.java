package com.msn.weiboclient.protocol.model;

import com.msn.weiboclient.common.utils.URLHelper;
import com.msn.weiboclient.protocol.model.base.IWeiBoRequest;

/**
 * Created by Msn on 2015/2/6.
 */
public class FriendsTimelineReq extends IWeiBoRequest {
    private String source;
    private String access_token;
    private String since_id;
    private String max_id;
    private String count;
    private String page;
    private String base_app;
    private String feature;
    private String trim_user;


    public FriendsTimelineReq(){
        url = URLHelper.WEIBO_HOST_URL+"2/statuses/friends_timeline.json";
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSince_id() {
        return since_id;
    }

    public void setSince_id(String since_id) {
        this.since_id = since_id;
    }

    public String getMax_id() {
        return max_id;
    }

    public void setMax_id(String max_id) {
        this.max_id = max_id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getBase_app() {
        return base_app;
    }

    public void setBase_app(String base_app) {
        this.base_app = base_app;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getTrim_user() {
        return trim_user;
    }

    public void setTrim_user(String trim_user) {
        this.trim_user = trim_user;
    }
}
