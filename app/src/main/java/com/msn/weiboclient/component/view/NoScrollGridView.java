package com.msn.weiboclient.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 不带滚动栏，显示所有内容的GridView
 * GridView和ListView，ScrollView嵌套使用会出现只显示部分内容的问题
 * 使用该View可以解决该问题
 * Created by Msn on 2015/3/4.
 */
public class NoScrollGridView extends GridView {
    public NoScrollGridView(Context context) {
        super(context);

    }
    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
