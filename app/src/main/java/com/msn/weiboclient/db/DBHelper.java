package com.msn.weiboclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.msn.weiboclient.db.vo.UserInfoVO;

import java.sql.SQLException;

/**
 * Created by Msn on 2015/2/11.
 * http://touchlabblog.tumblr.com/post/24474750219/single-sqlite-connection
 */
public class DBHelper extends OrmLiteSqliteOpenHelper{
    public static final String DB_NAME = "WeiBo.db";
    public static final int DB_VERSION = 1;

    private static DBHelper instace;

    private DBHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    public static synchronized  DBHelper getInstance(Context context){
        if(instace == null){
            instace = new DBHelper(context);
        }
        return instace;
    }

    public static <T,ID> Dao<T, ID> getDao(Context context,Class<T> clazzT,Class<ID> clazzID)
            throws SQLException{
        return getInstance(context).getDao(clazzT);
    }



    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource,UserInfoVO.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,UserInfoVO.class,true);
            onCreate(database,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
