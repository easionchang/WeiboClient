package com.msn.support.gallery;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.msn.support.view.DonutProgress;
import com.msn.weiboclient.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GalleryItemFragment extends Fragment {
    private static final String ARG_IMG_URL = "ARG_IMG_URL";

    private String mImgUrl;
    private PhotoView itemImgv;
    private DonutProgress itemProgressView;

    public static GalleryItemFragment newInstance(String url) {
        GalleryItemFragment fragment = new GalleryItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMG_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public GalleryItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImgUrl = getArguments().getString(ARG_IMG_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_item, container, false);
        itemImgv = (PhotoView)view.findViewById(R.id.item_imgv);
        itemImgv.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v2) {
                getActivity().finish();
            }
        });
        itemProgressView = (DonutProgress)view.findViewById(R.id.item_pbar);
        //itemProgressView.setInnerBackgroundColor(Color.rgb(154,204,2));
        //itemProgressView.setTextColor(Color.rgb(154,204,2));
        //itemProgressView.setFinishedStrokeColor(Color.rgb(154,204,2));
        ImageLoader.getInstance().displayImage(mImgUrl,itemImgv, null,new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {}

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {}

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        itemProgressView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) { }
                },
                new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        Log.e("Test","url="+imageUri+" current="+current+" total="+total+" progress="+(int)(current*100.0f/total));
                        itemProgressView.setProgress((int)(current*100.0f/total));
                    }
        });
        return view;
    }


}
