package com.msn.support.gallery;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * Created by Msn on 2015/3/10.
 */
public class ImageloaderUtils {

    /**
     * 判断图片是不是大图,大图的显示方式不同
     * @param url
     * @return
     */
    public static boolean isLargePicture(String url){
        File file = getImageDiscCached(url);
        if(!file.exists()){
            return false;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(),options);
        Log.e("Test","options.outWidth==="+options.outWidth+"     options.outHeight==="+options.outHeight);
        if(options.outWidth > 2048 || options.outHeight >2048){
            return true;
        }
        return false;
    }


    public static boolean isImageCached(String url){
        if(getImageDiscCached(url) == null){
            return false;
        }else{
            return true;
        }
    }

    public static File getImageDiscCached(String url){
        return ImageLoader.getInstance().getDiskCache().get(url);
    }
}
