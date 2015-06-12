package com.msn.weiboclient.db.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Msn on 2015/6/10.
 */

@DatabaseTable(tableName = "home_data_table")
public class StatusTimeLine {
    public static final String ID = "id";
    public static final String COUNT_ID = "countId";
    public static final String MSG_ID = "msgId";
    public static final String JSON_DATA = "jsonData";

    //id,generatedIdֻ只能设置一个
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String countId;
    @DatabaseField
    private String msgId;
    @DatabaseField
    private String jsonData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountId() {
        return countId;
    }

    public void setCountId(String countId) {
        this.countId = countId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
