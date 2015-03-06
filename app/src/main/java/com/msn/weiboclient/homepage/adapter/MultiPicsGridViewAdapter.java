package com.msn.weiboclient.homepage.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.msn.weiboclient.R;
import com.msn.weiboclient.protocol.vo.TimelineVO;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Msn on 2015/3/4.
 */
public class MultiPicsGridViewAdapter extends BaseAdapter{
    private List<TimelineVO.ThumbnailVO> mPicUrls;
    private Context mContext;

    public MultiPicsGridViewAdapter(Context context,List<TimelineVO.ThumbnailVO> pic_urls){
        mPicUrls = pic_urls;
        mContext = context;
    }
    public static class ViewHolder{
        public ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.e("Test","getView================"+position+"  mPicUrls.size="+mPicUrls.size());
        ImageView imageView = null;
        View view = null;
        if(convertView != null){
            view = convertView;

            ViewHolder viewHolder = (ViewHolder)convertView.getTag();
            imageView = viewHolder.imageView;
        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.multi_pics_grid_item,parent,false);
            imageView = (ImageView)view.findViewById(R.id.thumbnail_imgv);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.imageView = imageView;
            view.setTag(viewHolder);
        }

//        ImageView imageView = new ImageView(mContext);
//        imageView.setLayoutParams(new AbsListView.LayoutParams(
//                100,
//                100));
        ImageLoader.getInstance().displayImage(
                mPicUrls.get(position).getThumbnail_pic(),
                imageView);
        return view;
    }

    @Override
    public int getCount() {
        return mPicUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return mPicUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}