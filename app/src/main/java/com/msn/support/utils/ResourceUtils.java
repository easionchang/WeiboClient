package com.msn.support.utils;

import android.content.res.Resources;

/**
 * Created by Msn on 2015/3/9.
 */
public class ResourceUtils {
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
