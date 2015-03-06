package com.msn.weiboclient.homepage.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.msn.weiboclient.R;
import com.msn.weiboclient.common.utils.TimeUtility;
import com.msn.weiboclient.homepage.adapter.MultiPicsGridViewAdapter;
import com.msn.weiboclient.homepage.adapter.WeiboStatusAdapter;
import com.msn.weiboclient.protocol.http.WeiBoResponseListener;
import com.msn.weiboclient.protocol.http.XHttpClient;
import com.msn.weiboclient.protocol.model.FriendsTimelineReq;
import com.msn.weiboclient.protocol.model.FriendsTimelineRsp;
import com.msn.weiboclient.protocol.vo.TimelineVO;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainContentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainContentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SwipeRefreshLayout swipeContainer;
    private ProgressBar loadingPbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<TimelineVO> timelineVOList = new ArrayList<>();
    private WeiboStatusAdapter myAdapter;


    private OnFragmentInteractionListener mListener;

    private String mAccessToken;
    private int mCurrPage;
    private boolean isLoading = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainContentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainContentFragment newInstance(String param1, String param2) {
        MainContentFragment fragment = new MainContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainContentFragment() {
        // Required empty public constructor
    }

    public void showTimeline(String accessToken){
        mAccessToken = accessToken;
        mCurrPage = 1;

        loadTimeLine(mCurrPage,true);
    }

    private void setListShown(){
        swipeContainer.startAnimation(AnimationUtils.loadAnimation(
                getActivity(), android.R.anim.fade_in));
        loadingPbar.startAnimation(AnimationUtils.loadAnimation(
                getActivity(), android.R.anim.fade_out));
        loadingPbar.setVisibility(View.GONE);
        swipeContainer.setVisibility(View.VISIBLE);
    }

    private void loadTimeLine(final int page,final boolean isInit){
        FriendsTimelineReq timelineReq = new FriendsTimelineReq();
        timelineReq.setAccess_token(mAccessToken);
        timelineReq.setPage(page+"");
        XHttpClient.getInstance(this.getActivity()).get(timelineReq, new WeiBoResponseListener<FriendsTimelineRsp>() {
            @Override
            public void onSuccess(FriendsTimelineRsp rsp) throws Exception {
                List<TimelineVO> statuses = rsp.getStatuses();
                if(page == 1){
                    timelineVOList.clear();
                }
                timelineVOList.addAll(statuses);
                myAdapter.notifyDataSetChanged();

                if(isInit){
                    setListShown();
                }
                swipeContainer.setRefreshing(false);
                isLoading = false;
                //Toast.makeText(MainContentFragment.this.getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<String> getText(List<TimelineVO> statuses){
        List<String> textList = new ArrayList<>();
        for(TimelineVO vo:statuses){
            textList.add(vo.getText());
        }
        return textList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_content,container,false);

        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        swipeContainer.setColorSchemeColors(Color.rgb(2,199,84),Color.rgb(217,68,55),Color.rgb(73,135,231));
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showTimeline(mAccessToken);
            }
        });

        recyclerView = (RecyclerView)view.findViewById(R.id.content_lstv);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        myAdapter = new WeiboStatusAdapter(timelineVOList,this.getActivity());
        recyclerView.setAdapter(myAdapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(!isLoading) {
                    int lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                    int totalItemCount = mLayoutManager.getItemCount();
                    //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载
                    // dy>0 表示向下滑动
                    if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                        mCurrPage++;
                        isLoading = true;
                        Log.e("Test","load......page="+mCurrPage);
                        loadTimeLine(mCurrPage,false);
                    }
                }

            }
        });

        loadingPbar = (ProgressBar)view.findViewById(R.id.loading_pbar);
        swipeContainer.setVisibility(View.GONE);
        return  view;
    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
