package com.msn.support.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.msn.support.gallery.ImageloaderUtils;

/**
 * Created by Msn on 2015/3/13.
 */
public class ImageUtils {


    public static boolean isThisPictureGif(String url) {
        return !TextUtils.isEmpty(url) && url.endsWith(".gif");
    }

    /**
     * 获取图片的尺寸
     * @param url   图片地址
     * @return
     */
    public static int[] getBitmapDimension(String url){
        Bitmap bitmap = BitmapFactory.decodeFile(ImageloaderUtils.getImageDiscCached(url).getAbsolutePath());
        int imgWidth = bitmap.getWidth();
        int imgHeight = bitmap.getHeight();
        return new int[]{imgWidth,imgHeight};
    }

    /**
     * 图片实际显示宽度为imgWidth；ImageView宽度为viewWidth
     * 求padingLeft
     * @return
     */
    public static float getPaddingLeft(float imgWidth,float viewWidth){
        return (imgWidth - viewWidth)/2;
    }

    /**
     * 图片实际显示高度为imgHeight；ImageView高度为viewHeight
     * 求padingTop
     *
     * @return
     */
    public static float getPaddingTop(float imgHeight,float viewHeight){
        return (imgHeight - viewHeight)/2;
    }

    /**
     * 图片尺寸为imgWidth，imgHeight；ImageView的尺寸为startWidth，startHeight
     * 求图片缩放模式为fitcenter时，图片的显示的实际宽度
     * @return
     */
    public static float getBitmapWidth(int startWidth,int startHeight,int imgWidth, int imgHeight){
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

    /**
     * 图片尺寸为imgWidth，imgHeight；ImageView的尺寸为startWidth，startHeight
     * 求图片缩放模式为fitcenter时，图片的显示的实际高度
     * @return
     */
    public static float getBitmapHeight(int startWidth,int startHeight,int imgWidth, int imgHeight){
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

    /**
     * 图片实际尺寸显示在viewWidth，viewHeight中的缩放比例(scaleType=fitcenter)
     * @return
     */
    public static float getBitmapScaleInImageView(int imgWidth,int imgHeight,int viewWidth,int viewHeight){
        float imgScale;
        if(imgWidth >= imgHeight){
            imgScale = viewWidth/(float)imgWidth;
        }else{
            imgScale = viewHeight/(float)imgHeight;
        }
        return imgScale;
    }

    /**
     * ImageView A尺寸为startWidth和startHeight，并且设置了尺寸为imgWidth和imgHeight的图片
     * 求一个尺寸为finalWidth和finalHeight的ImageView B，
     * 要缩小多少倍才能和ImageView A中的图片有一样的显示尺寸
     * @return
     */
    public static float getScale(int imgWidth,int imgHeight,int startWidth,int finalWidth,int startHeight,int finalHeight){
        float imgScale;//图片实际尺寸显示在startWidth，startHeight中的缩放比例
        float scale;//finalWidth，finalHeight要显示startWidth，finalWidth同样尺寸的图片需要的缩放比例
        if(imgWidth >= imgHeight){
            imgScale = startWidth/(float)imgWidth;
        }else{
            imgScale = startHeight/(float)imgHeight;
        }

        if(imgWidth >= imgHeight){
            //宽的肯定没问题，直接按宽度来缩放（不用考虑宽度是否能充满，因为屏幕本来就是高的，
            // 如果宽度无法充满那该图片就是高的）
            scale = (float)startWidth/finalWidth;
            //Toast.makeText(this.getActivity(), "宽的", Toast.LENGTH_SHORT).show();
        }else{
            scale = (float)startHeight/finalHeight;
            //如果图片比屏幕偏胖，那么高度就不能完全充满（图片高度不能充满ImageView），则要相应的放大些
            if(imgWidth/(float)imgHeight > finalWidth/(float)finalHeight){
                //Toast.makeText(this.getActivity(),"高的-偏胖",Toast.LENGTH_SHORT).show();
//                float percent = (finalWidth*imgHeight)/((float)finalHeight*imgWidth);//图片高度所占ImageView百分比
//                float _height = imgScale*bitmap.getHeight()/percent;
//                scale = (float)_height/finalHeight;
                //高度无法充满，宽度肯定充满，所以宽度是限制，只需要把ImageView宽度缩小为图片实际显示的宽度即可
                float _width = imgScale*imgWidth;
                scale = _width/finalWidth;
            }else{//反之偏瘦的话高度肯定会充满
                //Toast.makeText(this.getActivity(),"高的-偏瘦",Toast.LENGTH_SHORT).show();
            }
        }
        Log.e("Test", "scale====" + scale);
        return scale;
    }
}
