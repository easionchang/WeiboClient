package com.msn.support.gallery;


import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

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
 * Use the {@link GalleryItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryItemFragment extends Fragment {
    private static final String ARG_IMG_URL = "ARG_IMG_URL";
    private static final String ARG_IMG_LOCATION = "ARG_IMG_LOCATION";

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

    public static GalleryItemFragment newInstance(String url,int[] location) {
        GalleryItemFragment fragment = new GalleryItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMG_URL, url);
        args.putIntArray(ARG_IMG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    public GalleryItemFragment() {
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
                if(mScale != 0){
                    itemImgv.animate().setDuration(mShortAnimationDuration)
                            .scaleX(mScale)
                            .scaleY(mScale)
                            .translationX(mDeltaX)
                            .translationY(mDeltaY).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            GalleryItemFragment.this.getActivity().finish();
                        }
                    });
                    ObjectAnimator alpha = ObjectAnimator.ofInt(mParentView.getBackground(), "alpha", 255, 0);
                    alpha.start();
                }
                //getActivity().finish();
            }
        });
        itemProgressView = (DonutProgress)view.findViewById(R.id.item_pbar);

        if(ImageloaderUtils.isImageCached(mImgUrl)){
            itemProgressView.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(mImgUrl,itemImgv);
            //itemImgv.setLayoutParams(new FrameLayout.LayoutParams(mLocation[2],mLocation[3]));
            //itemImgv.setX(mLocation[0]);
            //Y的值是相对屏幕的坐标，而不是父View；所以要减去状态栏的高度（标题栏在该页面不显示）
            //itemImgv.setY(mLocation[1] - DisplayUtil.getStatusBarHeight());
            //view.getBackground().setAlpha(10);
            startAnimate(view,itemImgv,mLocation);
        }else{
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
        mParentView = view;
        return view;
    }


    private void startAnimate(final View parentView,final ImageView itemImgv,final int[] location) {
        Log.e("Test", "itemImgv.heigh" +
                "t=" + itemImgv.getHeight() + " itemImgv.width=" + itemImgv.getWidth());
        int startWidth = location[2];
        int startHeight = location[3];
        int finalWidth = DisplayUtil.getScreenWidth();
        int finalHeight = DisplayUtil.getScreenHeight() - DisplayUtil.getStatusBarHeight();
        int[] imageDimension = ImageUtils.getBitmapDimension(mImgUrl);
        int imageWidth = imageDimension[0];
        int imageHeight = imageDimension[1];
        float startScale = ImageUtils.getScale(imageWidth, imageHeight, startWidth, finalWidth, startHeight, finalHeight);

        float imageActureHeight = ImageUtils.getBitmapHeight(startWidth, startHeight, imageWidth, imageHeight);
        float imageActureWidth = ImageUtils.getBitmapWidth(startWidth, startHeight, imageWidth, imageHeight);
        float scaleFinalPaddingTop = ImageUtils.getPaddingTop(imageActureHeight, finalHeight * startScale);
        float scaleFinalPaddingLeft = ImageUtils.getPaddingLeft(imageActureWidth, finalWidth * startScale);
        float startPaddingTop = ImageUtils.getPaddingTop(imageActureHeight, startHeight);
        float startPaddingLeft = ImageUtils.getPaddingLeft(imageActureWidth, startWidth);
        itemImgv.setScaleX(startScale);
        itemImgv.setScaleY(startScale);
        mScale = startScale;
        Log.e("Test", "startPaddingLeft=" + startPaddingLeft + " startPaddingTop=" + startPaddingTop);
        mDeltaX = location[0] + scaleFinalPaddingLeft - startPaddingLeft + (startScale * finalWidth - finalWidth) / 2;
        mDeltaY = location[1] - DisplayUtil.getStatusBarHeight() + scaleFinalPaddingTop - startPaddingTop + (startScale * finalHeight - finalHeight) / 2;
        itemImgv.setX(mDeltaX);
        itemImgv.setY(mDeltaY);
        //parentView.getBackground().setAlpha(10);

        itemImgv.animate().setDuration(mShortAnimationDuration)
                .scaleX(1)
                .scaleY(1)
                .translationX(0).translationY(0);

        ObjectAnimator alpha = ObjectAnimator.ofInt(parentView.getBackground(), "alpha", 0, 255);
        alpha.start();
    }
}
