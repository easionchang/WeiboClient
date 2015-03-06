package com.msn.weiboclient.db.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Msn on 2015/2/11.
 */
@DatabaseTable(tableName = "user")
public class UserInfoVO {

    public static final String ID = "ID";
    public static final String IDSTR = "IDSTR";
    public static final String SCREEN_NAME = "SCREEN_NAME";
    public static final String NAME = "NAME";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";


    @DatabaseField(id = true)
    private String id;
    @DatabaseField
    private String idstr;
    @DatabaseField
    private String screen_name;
    @DatabaseField
    private String name;
    @DatabaseField
    private String access_token;

    private String profile_image_url;

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

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


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }


}
