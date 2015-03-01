package com.msn.weiboclient.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.msn.weiboclient.db.vo.UserInfoVO;

import java.sql.SQLException;

/**
 * Created by Msn on 2015/2/11.
 */
public class CommonDao<T, ID> {
    public  com.j256.ormlite.dao.Dao<T, ID> userDao = null;

    public CommonDao(Context context){
        //DBHelper.getInstance(context).getDao(T.class);

    }

    public void insert(UserInfoVO userInfoVO){
//        try {
//            userDao.create(userInfoVO);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
