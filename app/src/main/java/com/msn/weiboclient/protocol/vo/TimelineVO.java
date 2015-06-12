package com.msn.weiboclient.protocol.vo;

import com.msn.weiboclient.db.vo.UserInfoVO;

import java.util.List;

/**
 * Created by Msn on 2015/3/2.
 */
public class TimelineVO {
    private String created_at;
    private String id;
    private String idstr;
    private String text;
    private String source;
    private String favorited;
    private String truncated;
    private String in_reply_to_status_id;
    private String in_reply_to_user_id;
    private String in_reply_to_screen_name;
    private List<ThumbnailVO> pic_urls;
    private UserInfoVO user;

    private TimelineVO retweeted_status;

    public class ThumbnailVO{
        private String thumbnail_pic;

        public String getThumbnail_pic() {
            return thumbnail_pic;
        }

        public void setThumbnail_pic(String thumbnail_pic) {
            this.thumbnail_pic = thumbnail_pic;
        }
    }

    public boolean hasPicture(){
        if(getPic_urls() != null && getPic_urls().size() > 0){
            return true;
        }else{
            return false;
        }
    }

    public boolean isMultiPics(){
        if(getPic_urls() != null && getPic_urls().size() > 1){
            return true;
        }else{
            return false;
        }
    }


    public UserInfoVO getUser() {
        return user;
    }

    public void setUser(UserInfoVO user) {
        this.user = user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFavorited() {
        return favorited;
    }

    public void setFavorited(String favorited) {
        this.favorited = favorited;
    }

    public String getTruncated() {
        return truncated;
    }

    public void setTruncated(String truncated) {
        this.truncated = truncated;
    }

    public String getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public List<ThumbnailVO> getPic_urls() {
        return pic_urls;
    }

    public void setPic_urls(List<ThumbnailVO> pic_urls) {
        this.pic_urls = pic_urls;
    }

    public TimelineVO getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(TimelineVO retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }
}
