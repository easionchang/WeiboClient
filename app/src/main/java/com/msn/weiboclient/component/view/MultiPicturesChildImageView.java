package com.msn.weiboclient.component.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.msn.weiboclient.R;


/**
 * User: qii
 * Date: 13-9-4
 */
public class MultiPicturesChildImageView extends PerformanceImageView
        implements IWeiciyuanDrawable {

    private Paint paint = new Paint();
    private boolean pressed = false;
    private boolean showGif = false;
    private Bitmap gif;

    public MultiPicturesChildImageView(Context context) {
        this(context, null);
    }

    public MultiPicturesChildImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiPicturesChildImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        gif = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showGif) {
            int bitmapHeight = gif.getHeight();
            int bitmapWidth = gif.getWidth();
            int x = (getWidth() - bitmapWidth) / 2;
            int y = (getHeight() - bitmapHeight) / 2;
            canvas.drawBitmap(gif, x, y, paint);
        }
        if (pressed) {
            canvas.drawColor(Color.RED);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                pressed = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                pressed = false;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public ImageView getImageView() {
        return this;
    }

    @Override
    public void setProgress(int value, int max) {

    }

    @Override
    public ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public void setGifFlag(boolean value) {
        if (showGif != value) {
            showGif = value;
            invalidate();
        }
    }



    @Override
    public void setPressesStateVisibility(boolean value) {

    }
}
