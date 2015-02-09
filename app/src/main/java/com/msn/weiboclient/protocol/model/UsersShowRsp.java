package com.msn.weiboclient.protocol.model;

import com.msn.weiboclient.protocol.model.base.IWeiBoResponse;

/**
 * Created by Msn on 2015/2/6.
 */
public class UsersShowRsp extends IWeiBoResponse {
    private String id;
    private String idstr;
    private String screen_name;
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
