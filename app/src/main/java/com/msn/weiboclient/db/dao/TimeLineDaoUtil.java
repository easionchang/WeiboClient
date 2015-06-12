package com.msn.weiboclient.db.dao;

import android.content.Context;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.msn.weiboclient.db.DBHelper;
import com.msn.weiboclient.db.vo.StatusTimeLine;
import com.msn.weiboclient.protocol.vo.TimelineVO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Msn on 2015/6/10.
 */
public class TimeLineDaoUtil {

    public static List<TimelineVO> findAll(Context context,String accountId,String groupId) throws SQLException{
        Gson gson = new Gson();
        Dao<StatusTimeLine,String> timelineDao =  DBHelper.getDao(context, StatusTimeLine.class,String.class);
        QueryBuilder<StatusTimeLine,String> queryBuilder = timelineDao.queryBuilder();
        queryBuilder.where().eq(StatusTimeLine.COUNT_ID, accountId);
        queryBuilder.orderBy(StatusTimeLine.MSG_ID,false);
        List<StatusTimeLine> timeLineList = queryBuilder.query();
        List<TimelineVO> voList = new ArrayList<>();
        for(StatusTimeLine timeLine:timeLineList){
            voList.add(gson.fromJson(timeLine.getJsonData(), TimelineVO.class));
        }
        return voList;
    }

    public static void deleteAll(Context context,String accountId,String groupId) throws SQLException {
        Dao<StatusTimeLine,String> timelineDao =  DBHelper.getDao(context, StatusTimeLine.class,String.class);
        DeleteBuilder<StatusTimeLine,String> deleteBuilder = timelineDao.deleteBuilder();
        deleteBuilder.where().eq(StatusTimeLine.COUNT_ID,accountId);
        deleteBuilder.delete();
    }

    public static void addTimeLine(Context context,String accountId,List<TimelineVO> voList) throws SQLException{
        Gson gson = new Gson();
        Dao<StatusTimeLine,String> timelineDao =  DBHelper.getDao(context, StatusTimeLine.class,String.class);
        for (TimelineVO vo: voList) {
            StatusTimeLine timeLine = new StatusTimeLine();
            timeLine.setCountId(accountId);
            timeLine.setMsgId(vo.getId());
            timeLine.setJsonData(gson.toJson(vo));
            timelineDao.create(timeLine);
        }

    }
}
