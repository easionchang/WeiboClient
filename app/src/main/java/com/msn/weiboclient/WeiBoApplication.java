package com.msn.weiboclient;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Msn on 2015/2/6.
 */
public class WeiBoApplication extends Application {
    //singleton
    private static WeiBoApplication globalContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        globalContext = this;
        initImageLoader();
    }

    public static WeiBoApplication getInstance(){
        return globalContext;
    }


    private void initImageLoader(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置 如Bitmap.Config.ARGB_8888
                //.showImageOnLoading(R.drawable.ic_launcher)   //默认图片
                //.showImageForEmptyUri(R.drawable.ic_launcher)    //url爲空會显示该图片，自己放在drawable里面的
                // .showImageOnFail(R.drawable.ic_launcher)// 加载失败显示的图片
                //.displayer(new RoundedBitmapDisplayer(500))  //圆角
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                //.memoryCacheExtraOptions(480, 800)// 缓存在内存的图片的宽和高度
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(2 * 1024 * 1024) //缓存到内存的最大数据
                .diskCacheSize(100 * 1024 * 1024) //缓存到文件的最大数据
                .diskCacheFileCount(1000)            //文件数量
                .defaultDisplayImageOptions(options)  //上面的options对象，一些属性配置
                //.writeDebugLogs() // Remove for release app
                .build();

        ImageLoader.getInstance().init(config); //初始化
    }
}
