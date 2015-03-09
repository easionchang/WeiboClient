package com.msn.support.gallery;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.msn.weiboclient.R;
import com.msn.weiboclient.WeiBoApplication;

import java.util.HashMap;

/**
 * Created by Msn on 2015/3/6.
 */
public class GalleryAnimationActivity extends FragmentActivity{
    public static final String IMAGE_KEY = "IMAGE_KEY";
    public static final String POSITION_KEY = "POSITION_KEY";

    private String[] mImgUrls;
    private int mCurrPosition;
    private HashMap<Integer,GalleryItemFragment> itemFragmentHashMap = new HashMap<>();

    private TextView positionTv;
    private TextView sumTv;

    public static Intent newIntent(String[] imgUrls,int position){
        Intent intent = new Intent();
        intent.setClass(WeiBoApplication.getInstance(),GalleryAnimationActivity.class);
        intent.putExtra(IMAGE_KEY, imgUrls);
        intent.putExtra(POSITION_KEY,position);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_animation);
        mImgUrls = getIntent().getStringArrayExtra(IMAGE_KEY);
        mCurrPosition = getIntent().getIntExtra(POSITION_KEY,0);

        ViewPager galleryViewPager = (ViewPager)findViewById(R.id.gallery_vpager);
        if(Build.VERSION.SDK_INT >= 11){
            galleryViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        }
        galleryViewPager.setAdapter(new GalleryViewPageAdapter(getSupportFragmentManager()));
        galleryViewPager.setCurrentItem(mCurrPosition);
        galleryViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                positionTv.setText(String.valueOf(position+1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        positionTv = (TextView)findViewById(R.id.position_tv);
        sumTv = (TextView)findViewById(R.id.sum_tv);
        sumTv.setText(String.valueOf(mImgUrls.length));
        positionTv.setText(String.valueOf(mCurrPosition+1));
    }


    class GalleryViewPageAdapter extends FragmentPagerAdapter{

        public GalleryViewPageAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public int getCount() {
            return mImgUrls.length;
        }

        @Override
        public Fragment getItem(int position) {
            if(itemFragmentHashMap.get(position) == null){
                GalleryItemFragment fragment = GalleryItemFragment.newInstance(mImgUrls[position]);
                itemFragmentHashMap.put(position,fragment);
                return fragment;
            }else{
                return itemFragmentHashMap.get(position);
            }

        }
    }
}
