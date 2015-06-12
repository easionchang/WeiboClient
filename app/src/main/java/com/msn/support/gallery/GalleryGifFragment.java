package com.msn.support.gallery;


import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.msn.support.utils.DisplayUtil;
import com.msn.support.utils.ImageUtils;
import com.msn.support.view.DonutProgress;
import com.msn.weiboclient.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryGifFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryGifFragment extends Fragment {
    private static final String ARG_IMG_URL = "ARG_IMG_URL";
    private static final String ARG_IMG_LOCATION = "ARG_IMG_LOCATION";

    /** 显示图片是否附带动画，如果使用动画请在manitest文件中使用android:theme="@style/CommonGalleryTheme"
     * 并设置将galery_animate布局文件背景色android:background="#000"去掉 */
    private boolean isShowWithAnimate = false;
    private String mImgUrl;
    private int[] mLocation;
    private PhotoView itemImgv;
    private DonutProgress itemProgressView;

    private float mScale;
    private float mDeltaX;
    private float mDeltaY;
    private View mParentView;

    /**
     * 系统“短”动画持续时间（毫秒）。该时间用于细微的动画或者发生频次很高的动画上。
     */
    private int mShortAnimationDuration;

    public static GalleryGifFragment newInstance(String url,int[] location) {
        GalleryGifFragment fragment = new GalleryGifFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMG_URL, url);
        args.putIntArray(ARG_IMG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    public GalleryGifFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImgUrl = getArguments().getString(ARG_IMG_URL);
            mLocation = getArguments().getIntArray(ARG_IMG_LOCATION);
        }
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_item, container, false);
        itemImgv = (PhotoView)view.findViewById(R.id.item_imgv);
        itemImgv.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v2) {
                    getActivity().finish();
            }
        });
        itemProgressView = (DonutProgress)view.findViewById(R.id.item_pbar);

        if(ImageloaderUtils.isImageCached(mImgUrl)){
            itemProgressView.setVisibility(View.GONE);
            showImgWithoutAinimate(view, itemImgv, mLocation);
        }else{
            showLoading(view, itemImgv, mLocation);
        }
        mParentView = view;
        return view;
    }

    private void showLoading(final View parentView,final ImageView itemImgv,final int[] location){
        ImageLoader.getInstance().displayImage(mImgUrl,itemImgv, null,new ImageLoadingListener() {
                    public void onLoadingStarted(String imageUri, View view) {}
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}
                    public void onLoadingCancelled(String imageUri, View view) {}

                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        itemProgressView.setVisibility(View.GONE);
                    }
                },
                new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        //Log.e("Test","url="+imageUri+" current="+current+" total="+total+" progress="+(int)(current*100.0f/total));
                        itemProgressView.setProgress((int)(current*100.0f/total));
                    }
                });
    }

    private void showImgWithoutAinimate(final View parentView, final ImageView itemImgv, final int[] location){
        ImageLoader.getInstance().displayImage(mImgUrl,itemImgv);
    }

}
