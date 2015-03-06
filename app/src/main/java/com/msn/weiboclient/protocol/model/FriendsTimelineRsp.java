package com.msn.weiboclient.protocol.model;

import com.msn.weiboclient.db.vo.UserInfoVO;
import com.msn.weiboclient.protocol.model.base.IWeiBoResponse;
import com.msn.weiboclient.protocol.vo.TimelineVO;

import java.util.List;

/**
 * Created by Msn on 2015/2/6.
 */
public class FriendsTimelineRsp extends IWeiBoResponse {
    private List<TimelineVO> statuses;


    public List<TimelineVO> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<TimelineVO> statuses) {
        this.statuses = statuses;
    }

}
