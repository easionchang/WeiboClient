package com.msn.weiboclient.homepage.fragment;

import com.msn.weiboclient.db.dao.TimeLineDaoUtil;
import com.msn.weiboclient.protocol.vo.TimelineVO;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Msn on 2015/6/16.
 */
public class TimelineDBTask {

    public static final int ADD_TYPE = 0;
    public static final int REFACTOR_TYPE = 1;

    /**
     * 缓存的同步使用新的线程，避免阻塞主线程
     * @param type
     * @param accountId
     * @param groupId
     * @param newStatuses
     */
    public static void asyncReplace(final int type, final String accountId,
                                    final String groupId,final List<TimelineVO> newStatuses) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(ADD_TYPE == type){
                        TimeLineDaoUtil.addTimeLine("1", "1", newStatuses);
                    }else if(REFACTOR_TYPE == type){
                        TimeLineDaoUtil.deleteAll("1","1");
                        TimeLineDaoUtil.addTimeLine( "1", "1", newStatuses);
                    }

                } catch (SQLException e) {
                    //TODO 缓存保存失败，通知缓存服务
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
