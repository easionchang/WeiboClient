package com.msn.support.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Window;

/**
 * Created by Msn on 2015/3/11.
 */
public class DisplayUtil {
    private static int statusBarHeight;
    private static int titleBarHeight;

    private static int screenHeight;
    private static int screenWidth;

    /**
     * 该方法不能在oncreate中调用不然获取到状态栏和标题栏的值为0
     * 可以再onWindowFocusChanged中调用
     * @param activity
     */
    public static void init(Activity activity){
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        // 标题栏跟状态栏的总体高度
        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        titleBarHeight = contentTop -statusBarHeight;

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
    }


    public static int getTitleBarHeight() {
        return titleBarHeight;
    }

    public static int getStatusBarHeight() {
        return statusBarHeight;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }
}
