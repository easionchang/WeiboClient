package com.msn.support.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.msn.support.gallery.ImageloaderUtils;

/**
 * Created by Msn on 2015/3/13.
 */
public class ImageUtils {

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
}
