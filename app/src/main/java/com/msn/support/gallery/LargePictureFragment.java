package com.msn.support.gallery;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.msn.weiboclient.R;

import java.io.File;

/**
 * Created by Msn on 2015/7/13.
 */
public class LargePictureFragment extends Fragment{
    public static String ARG_URL = "URL";

    private String url;

    public LargePictureFragment(){

    }

    public static LargePictureFragment newInstance(String url){
        LargePictureFragment fragment = new LargePictureFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL,url);
        fragment.setArguments(args);
        return  fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            url = getArguments().getString(ARG_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_large_pic,container,false);
        final WebView imgWebview = (WebView) view.findViewById(R.id.img_webview);
        imgWebview.setBackgroundColor(getResources().getColor(R.color.transparent));
        imgWebview.setVisibility(View.INVISIBLE);
        //@TargetApi(11)
        imgWebview.setOverScrollMode(View.OVER_SCROLL_NEVER);


        imgWebview.getSettings().setJavaScriptEnabled(true);
        imgWebview.getSettings().setUseWideViewPort(true);
        imgWebview.getSettings().setLoadWithOverviewMode(true);
        imgWebview.getSettings().setBuiltInZoomControls(true);
        imgWebview.getSettings().setDisplayZoomControls(false);

        imgWebview.setVerticalScrollBarEnabled(false);
        imgWebview.setHorizontalScrollBarEnabled(false);
        imgWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        imgWebview.setOnTouchListener(new ImageWebViewOnTouch(imgWebview));


        showContent(ImageloaderUtils.getImageDiscCached(url), imgWebview);
        return view;
    }


    private void showContent(File file, WebView large) {
        String str1 = "file://" + file.getAbsolutePath()
                .replace("/mnt/sdcard/", "/sdcard/");
        String str2 =
                "<html>\n<head>\n     <style>\n          html,body{background:transparent;margin:0;padding:0;}          *{-webkit-tap-highlight-color:rgba(0, 0, 0, 0);}\n     </style>\n     <script type=\"text/javascript\">\n     var imgUrl = \""
                        + str1 + "\";" + "     var objImage = new Image();\n"
                        + "     var realWidth = 0;\n" + "     var realHeight = 0;\n" + "\n"
                        + "     function onLoad() {\n"
                        + "          objImage.onload = function() {\n"
                        + "               realWidth = objImage.width;\n"
                        + "               realHeight = objImage.height;\n" + "\n"
                        + "               document.gagImg.src = imgUrl;\n"
                        + "               onResize();\n" + "          }\n"
                        + "          objImage.src = imgUrl;\n" + "     }\n" + "\n"
                        + "     function onResize() {\n" + "          var scale = 1;\n"
                        + "          var newWidth = document.gagImg.width;\n"
                        + "          if (realWidth > newWidth) {\n"
                        + "               scale = realWidth / newWidth;\n"
                        + "          } else {\n"
                        + "               scale = newWidth / realWidth;\n" + "          }\n"
                        + "\n"
                        + "          hiddenHeight = Math.ceil(30 * scale);\n"
                        + "          document.getElementById('hiddenBar').style.height = hiddenHeight + \"px\";\n"
                        + "          document.getElementById('hiddenBar').style.marginTop = -hiddenHeight + \"px\";\n"
                        + "     }\n" + "     </script>\n" + "</head>\n"
                        + "<body onload=\"onLoad()\" onresize=\"onResize()\" onclick=\"Android.toggleOverlayDisplay();\">\n"
                        + "     <table style=\"width: 100%;height:100%;\">\n"
                        + "          <tr style=\"width: 100%;\">\n"
                        + "               <td valign=\"middle\" align=\"center\" style=\"width: 100%;\">\n"
                        + "                    <div style=\"display:block\">\n"
                        + "                         <img name=\"gagImg\" src=\"\" width=\"100%\" style=\"\" />\n"
                        + "                    </div>\n"
                        + "                    <div id=\"hiddenBar\" style=\"position:absolute; width: 100%; background: transparent;\"></div>\n"
                        + "               </td>\n" + "          </tr>\n" + "     </table>\n"
                        + "</body>\n" + "</html>";
        large.loadDataWithBaseURL("file:///android_asset/", str2, "text/html", "utf-8",
                null);
        large.setVisibility(View.VISIBLE);
    }


    public class ImageWebViewOnTouch implements View.OnTouchListener{
        GestureDetector gestureDetector;

        public ImageWebViewOnTouch(View view){
            gestureDetector = new GestureDetector(view.getContext(),
                    new GestureDetector.SimpleOnGestureListener(){
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            getActivity().onBackPressed();
                            return true;
                        }
                    });
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return false;
        }
    }
}
