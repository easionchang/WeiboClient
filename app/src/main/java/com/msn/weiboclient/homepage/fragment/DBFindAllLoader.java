package com.msn.weiboclient.homepage.fragment;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.msn.weiboclient.db.dao.TimeLineDaoUtil;
import com.msn.weiboclient.protocol.vo.TimelineVO;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Msn on 2015/6/16.
 */
public class DBFindAllLoader extends AsyncTaskLoader<List<TimelineVO>> {
    //TODO 要改为弱引用
    private Context mContext;

    public DBFindAllLoader(Context context){
        super(context);
        mContext = context;
    }


    @Override
    public List<TimelineVO> loadInBackground() {
        try {
            return TimeLineDaoUtil.findAll( "1", "1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
