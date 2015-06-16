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
public class DBAddLoader extends AsyncTaskLoader<List<TimelineVO>> {

    private Context mContext;
    private List<TimelineVO> mNewStatuses;

    public DBAddLoader(Context context, List<TimelineVO> newStatuses){
        super(context);
        mContext = context;
        mNewStatuses = newStatuses;
    }


    @Override
    public List<TimelineVO> loadInBackground() {
        try {
            TimeLineDaoUtil.addTimeLine("1","1",mNewStatuses);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
