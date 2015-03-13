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
                getActivity().finish();
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
            startAnimate2(view,itemImgv,mLocation);
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
        return view;
    }


    private void startAnimate2(final View parentView,final ImageView itemImgv,final int[] location){

                        Log.e("Test","itemImgv.heigh" +
                                "t="+itemImgv.getHeight()+" itemImgv.width="+itemImgv.getWidth());
                        int startWidth = location[2];
                        int startHeight = location[3];
                        int finalWidth = DisplayUtil.getScreenWidth();
                        int finalHeight = DisplayUtil.getScreenHeight() - DisplayUtil.getStatusBarHeight();

                        float startScale = getScale(startWidth,finalWidth,startHeight,finalHeight);

                        itemImgv.setScaleX(startScale);
                        itemImgv.setScaleY(startScale);
                        float paddingTop= getPaddingTop(startScale,startWidth,finalWidth,startHeight,finalHeight);
                        float PaddingLeftByStart = getPaddingLeftByStart(startWidth,startHeight);
                        float PaddingTopByStart = getPaddingTopByStart(startWidth,startHeight);
                        itemImgv.setX(PaddingLeftByStart+location[0]+(startScale*finalWidth-finalWidth)/2+getPaddingLeft(startScale,startWidth,finalWidth,startHeight,finalHeight));
                        itemImgv.setY(PaddingTopByStart+location[1]- DisplayUtil.getStatusBarHeight()+(startScale*finalHeight-finalHeight)/2+paddingTop);
                        //parentView.getBackground().setAlpha(10);

        itemImgv.animate().setDuration(mShortAnimationDuration)
                .scaleX(1)
                .scaleY(1)
        .translationX(0).translationY(0);

        ObjectAnimator alpha = ObjectAnimator.ofInt(parentView.getBackground(),"alpha",0,255);
        alpha.start();


        //Bitmap bitmap = BitmapFactory.decodeFile(ImageloaderUtils.getImageDiscCached(mImgUrl).getAbsolutePath());

        //int imgWidth = bitmap.getWidth();
        //int imgHeight = bitmap.getHeight();


        //放大后的图片尺寸，将图片最大放大到全屏

//        itemImgv.setPivotX(0);
//        itemImgv.setPivotY(0);
//
//        itemImgv.animate().setDuration(mShortAnimationDuration)
//                .scaleX(1)
//                .scaleY(1)
//                .translationX(screenWidth/2-location[2]*scale/2.0f)
//                .translationY((screenHeight/2-location[3]*scale/2.0f));
//                //.translationX(screenWidth/2-imgWidth*scale/2.0f)
//                //.translationY(screenHeight/2-imgHeight*scale/2.0f);
//        Log.e("Test"," screenWidth="+screenWidth+" screenHeight="+screenHeight+" imgWidth="+imgWidth+" imgHeight="+imgHeight);
//        Log.e("Test","scale="+scale+" widthScale="+screenWidth/(float) bitmap.getWidth()
//                +" heightscale="+screenHeight/(float)bitmap.getHeight()
//                +" translationX="+(screenWidth/2-imgWidth*scale/2.0f)
//                +" translationY="+(screenHeight/2-imgHeight*scale/2.0f));

        //Log.e("Test","mRect.left="+mRect.left+" mRect.top="+mRect.top);
    }

    private float getPaddingTop(float scale,int startWidth,int finalWidth,int startHeight,int finalHeight){
        Bitmap bitmap = BitmapFactory.decodeFile(ImageloaderUtils.getImageDiscCached(mImgUrl).getAbsolutePath());
        int imgWidth = bitmap.getWidth();
        int imgHeight = bitmap.getHeight();

        float finalImageViewHeight = finalHeight*scale;
        float bitmapHeight = getBitmapHeight(startWidth,startHeight,imgWidth,imgHeight);
        Log.e("Test","getPaddingTop==="+(bitmapHeight - finalImageViewHeight)/2);
        return (bitmapHeight - finalImageViewHeight)/2;
    }

    private float getPaddingLeft(float scale,int startWidth,int finalWidth,int startHeight,int finalHeight){
        Bitmap bitmap = BitmapFactory.decodeFile(ImageloaderUtils.getImageDiscCached(mImgUrl).getAbsolutePath());
        int imgWidth = bitmap.getWidth();
        int imgHeight = bitmap.getHeight();

        float finalImageViewWidth = finalWidth*scale;
        float bitmapWidth = getBitmapWidth(startWidth,startHeight,imgWidth,imgHeight);
        Log.e("Test","getPaddingLeft==="+(bitmapWidth - finalImageViewWidth)/2);
        return (bitmapWidth - finalImageViewWidth)/2;
    }

    private float getPaddingTopByStart(int startWidth,int startHeight){
        Bitmap bitmap = BitmapFactory.decodeFile(ImageloaderUtils.getImageDiscCached(mImgUrl).getAbsolutePath());
        int imgWidth = bitmap.getWidth();
        int imgHeight = bitmap.getHeight();
        float bitmapHeight = getBitmapHeight(startWidth,startHeight,imgWidth,imgHeight);
        Log.e("Test","getPaddingTopByStart==="+(startHeight-bitmapHeight)/(float)2);
        return (startHeight-bitmapHeight)/(float)2;
    }

    private float getPaddingLeftByStart(int startWidth,int startHeight){
        Bitmap bitmap = BitmapFactory.decodeFile(ImageloaderUtils.getImageDiscCached(mImgUrl).getAbsolutePath());
        int imgWidth = bitmap.getWidth();
        int imgHeight = bitmap.getHeight();
        float bitmapWidth = getBitmapWidth(startWidth,startHeight,imgWidth,imgHeight);
        Log.e("Test","getPaddingLeftByStart==="+(startWidth-bitmapWidth)/(float)2);
        return (startWidth-bitmapWidth)/(float)2;
    }

    private float getBitmapWidth(int startWidth,int startHeight,int imgWidth, int imgHeight){
        float actureWidth;

        if(imgWidth >= imgHeight){//宽的图片，最终宽度为ImageView宽度
            actureWidth = startWidth;
        }else{//高的，
            float actureHeight = startHeight;
            float bitmapScale = actureHeight/imgHeight;
            actureWidth = bitmapScale*imgWidth;
        }

        return actureWidth;
    }

    private float getBitmapHeight(int startWidth,int startHeight,int imgWidth, int imgHeight){
        float actureHeight;

        if(imgWidth <= imgHeight){//高的图片，最终高度为ImageView高度
            actureHeight = startHeight;
        }else{//宽的，
            float actureWidth = startWidth;
            float bitmapScale = actureWidth/imgWidth;
            actureHeight = bitmapScale*imgHeight;
        }

        return actureHeight;
    }

    private float getBitmapScaleInImageView(int imgWidth,int imgHeight,int startWidth,int startHeight){
        float imgScale;//图片实际尺寸显示在startWidth，startHeight中的缩放比例
        if(imgWidth >= imgHeight){
            imgScale = startWidth/(float)imgWidth;
        }else{
            imgScale = startHeight/(float)imgHeight;
        }
        return imgScale;
    }

    private float getScale(int startWidth,int finalWidth,int startHeight,int finalHeight){
        float imgScale;//图片实际尺寸显示在startWidth，startHeight中的缩放比例
        float scale;//finalWidth，finalHeight要显示startWidth，finalWidth同样尺寸的图片需要的缩放比例
        Bitmap bitmap = BitmapFactory.decodeFile(ImageloaderUtils.getImageDiscCached(mImgUrl).getAbsolutePath());
        int imgWidth = bitmap.getWidth();
        int imgHeight = bitmap.getHeight();

        if(imgWidth >= imgHeight){
            imgScale = startWidth/(float)imgWidth;
        }else{
            imgScale = startHeight/(float)imgHeight;
        }

        if(imgWidth >= imgHeight){
            //宽的肯定没问题，直接按宽度来缩放（不用考虑宽度是否能充满，因为屏幕本来就是高的，
            // 如果宽度无法充满那该图片就是高的）
            scale = (float)startWidth/finalWidth;
            Toast.makeText(this.getActivity(),"宽的",Toast.LENGTH_SHORT).show();
        }else{
            scale = (float)startHeight/finalHeight;
            //如果图片比屏幕偏胖，那么高度就不能完全充满（图片高度不能充满ImageView），则要相应的放大些
            if(imgWidth/(float)imgHeight > finalWidth/(float)finalHeight){
                Toast.makeText(this.getActivity(),"高的-偏胖",Toast.LENGTH_SHORT).show();
//                float percent = (finalWidth*imgHeight)/((float)finalHeight*imgWidth);//图片高度所占ImageView百分比
//                float _height = imgScale*bitmap.getHeight()/percent;
//                scale = (float)_height/finalHeight;
                //高度无法充满，宽度肯定充满，所以宽度是限制，只需要把ImageView宽度缩小为图片实际显示的宽度即可
                float _width = imgScale*imgWidth;
                scale = _width/finalWidth;
            }else{//反之偏瘦的话高度肯定会充满
                Toast.makeText(this.getActivity(),"高的-偏瘦",Toast.LENGTH_SHORT).show();
            }
        }
        Log.e("Test","scale===="+scale);
        return scale;
    }
}
