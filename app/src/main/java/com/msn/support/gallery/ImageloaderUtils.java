package com.msn.support.gallery;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * Created by Msn on 2015/3/10.
 */
public class ImageloaderUtils {
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
