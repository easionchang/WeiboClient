package com.msn.weiboclient.homepage.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.msn.weiboclient.R;
import com.msn.weiboclient.common.utils.TimeUtility;
import com.msn.support.gallery.GalleryAnimationActivity;
import com.msn.weiboclient.protocol.vo.TimelineVO;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Msn on 2015/3/4.
 */
public class WeiboStatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_FOOT = 1;

    private List<TimelineVO> timelineVOList;
    private Context mContext;

    public WeiboStatusAdapter(List<TimelineVO> timelineVOList,Context context){
        this.timelineVOList = timelineVOList;
        mContext = context;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        public TextView mContentTv;
        public ImageView mProfileImgv;
        public TextView mNameTv;
        public TextView mCreateAndSourceTv;
        public ImageView  mThumbnailImgv;
        public GridView mThumbnailGrdv;

        public View mRepostLine;
        public TextView mRepostContentTv;
        public ImageView mRepostImgv;
        public GridView mRepostGrdv;
        public ItemHolder(View itemView) {
            super(itemView);
            mContentTv = (TextView)itemView.findViewById(R.id.content_tv);
            mProfileImgv = (ImageView)itemView.findViewById(R.id.profile_image);
            mNameTv = (TextView)itemView.findViewById(R.id.name_tv);
            mCreateAndSourceTv = (TextView)itemView.findViewById(R.id.created_and_source_tv);
            mThumbnailImgv = (ImageView)itemView.findViewById(R.id.thumbnail_imgv);
            mThumbnailGrdv = (GridView)itemView.findViewById(R.id.thumbnail_grdv);

            mRepostLine = itemView.findViewById(R.id.repost_line);
            mRepostContentTv = (TextView)itemView.findViewById(R.id.repost_content);
            mRepostImgv = (ImageView)itemView.findViewById(R.id.repost_imgv);
            mRepostGrdv = (GridView)itemView.findViewById(R.id.repost_grdv);
        }
    }

    public static class FootViewHolder extends RecyclerView.ViewHolder {
        public View mFootView;
        public ProgressBar loadingPbar;
        public FootViewHolder(View footView) {
            super(footView);
            mFootView = footView;
            loadingPbar = (ProgressBar)mFootView.findViewById(R.id.loading_pbar);
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType == TYPE_FOOT){
            View footView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.timeline_foot, viewGroup, false);
            return new FootViewHolder(footView);
        }else{
            View itemView = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.timeline_item, viewGroup, false);
            return new ItemHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder myViewHolder, int i) {
        if(myViewHolder instanceof ItemHolder){
            showItem((ItemHolder)myViewHolder,i);
        }else{
            showFoot((FootViewHolder)myViewHolder);
        }
    }

    private void showFoot(FootViewHolder myViewHolder){

    }

    private void showItem(ItemHolder myViewHolder,int i){
        final TimelineVO timelineVO = timelineVOList.get(i);
        myViewHolder.mContentTv.setText(timelineVO.getText());
        if(timelineVO.getUser() != null){
            myViewHolder.mNameTv.setText(timelineVO.getUser().getScreen_name());
        }
        myViewHolder.mCreateAndSourceTv.setText(
                String.format(mContext.getString(R.string.create_source_str),
                        TimeUtility.getListTime(timelineVO.getCreated_at()),
                        Html.fromHtml(timelineVO.getSource()).toString())
        );
        ImageLoader.getInstance().displayImage(
                timelineVO.getUser().getProfile_image_url(),
                myViewHolder.mProfileImgv);

        myViewHolder.mThumbnailGrdv.setVisibility(View.GONE);
        myViewHolder.mThumbnailImgv.setVisibility(View.GONE);
        if(timelineVO.hasPicture()){
            if(timelineVO.isMultiPics()){
                myViewHolder.mThumbnailGrdv.setVisibility(View.VISIBLE);
                myViewHolder.mThumbnailGrdv.setAdapter(
                        new MultiPicsGridViewAdapter(
                                mContext,
                                timelineVO.getPic_urls()));
            }else{
                myViewHolder.mThumbnailImgv.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(
                        timelineVO.getPic_urls().get(0).getThumbnail_pic(),
                        myViewHolder.mThumbnailImgv);
                myViewHolder.mThumbnailImgv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView imgView = (ImageView)v;
                        Log.e("Test","getDrawable.height="+imgView.getDrawable().getBounds().height()
                                 +" viewHeight="+v.getHeight()
                                 +" getDrawable.width="+imgView.getDrawable().getBounds().width()
                                 +" viewWidth="+v.getWidth());
                        int location[] = new int[4];
                        v.getLocationOnScreen(location);
                        location[2] = v.getWidth();
                        location[3] = v.getHeight();
                        mContext.startActivity(
                                GalleryAnimationActivity.newIntent(
                                        getImgUrls(timelineVO.getPic_urls().get(0).getThumbnail_pic()),
                                        0,location));
                    }
                });
            }
        }
        //转发内容
        myViewHolder.mRepostLine.setVisibility(View.GONE);
        myViewHolder.mRepostContentTv.setVisibility(View.GONE);
        myViewHolder.mRepostImgv.setVisibility(View.GONE);
        myViewHolder.mRepostGrdv.setVisibility(View.GONE);
        if(timelineVO.getRetweeted_status() != null){
            final TimelineVO repostVO = timelineVO.getRetweeted_status();
            myViewHolder.mRepostLine.setVisibility(View.VISIBLE);
            myViewHolder.mRepostContentTv.setVisibility(View.VISIBLE);
            if(repostVO.getUser() != null){
                myViewHolder.mRepostContentTv.setText(
                        String.format(mContext.getString(R.string.name_content_str),
                                "@"+repostVO.getUser().getName(),
                                repostVO.getText()));
            }else{
                myViewHolder.mRepostContentTv.setText(
                        String.format(mContext.getString(R.string.name_content_str),
                                "",
                                repostVO.getText()));
            }
            if(repostVO.hasPicture()){
                if(repostVO.isMultiPics()){
                    myViewHolder.mRepostGrdv.setVisibility(View.VISIBLE);
                    myViewHolder.mRepostGrdv.setAdapter(
                            new MultiPicsGridViewAdapter(
                                    mContext,
                                    repostVO.getPic_urls()));
                }else{
                    myViewHolder.mRepostImgv.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(
                            repostVO.getPic_urls().get(0).getThumbnail_pic(),
                            myViewHolder.mRepostImgv);

                    myViewHolder.mRepostImgv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int location[] = new int[4];
                            v.getLocationOnScreen(location);
                            location[2] = v.getWidth();
                            location[3] = v.getHeight();
                            mContext.startActivity(
                                    GalleryAnimationActivity.newIntent(
                                            getImgUrls(repostVO.getPic_urls().get(0).getThumbnail_pic()),
                                            0,location));
                        }
                    });
                }
            }
        }
    }

    private String[] getImgUrls(String url){
        String[] urls = new String[1];
        urls[0] = url.replace("thumbnail", "large");
        return urls;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() -1){
            return TYPE_FOOT;
        }else{
            return TYPE_ITEM;
        }
    }

    /**
     * +1为增加一个FootView
     * @return
     */
    public int getItemCount() {
        if(timelineVOList.size() > 0){
            return timelineVOList.size()+1;
        }else{
            return timelineVOList.size();
        }
    }
}
