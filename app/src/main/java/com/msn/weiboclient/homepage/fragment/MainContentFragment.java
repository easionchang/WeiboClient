package com.msn.weiboclient.homepage.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.msn.weiboclient.R;
import com.msn.weiboclient.db.dao.TimeLineDaoUtil;
import com.msn.weiboclient.homepage.adapter.WeiboStatusAdapter;
import com.msn.weiboclient.protocol.http.WeiBoResponseListener;
import com.msn.weiboclient.protocol.http.XHttpClient;
import com.msn.weiboclient.protocol.model.FriendsTimelineReq;
import com.msn.weiboclient.protocol.model.FriendsTimelineRsp;
import com.msn.weiboclient.protocol.model.base.IWeiBoResponse;
import com.msn.weiboclient.protocol.vo.TimelineVO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 缓存策略：
 * 加载失败后，如果当前的列表为空（即初始化时）则显示数据库中缓存数据
 * 每次加载更多都朝数据库中添加数据
 * 每次刷新先删除所有数据接着添加第一页数据
 *
 */
public class MainContentFragment extends Fragment {
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

    public static MainContentFragment newInstance(String param1, String param2) {
        MainContentFragment fragment = new MainContentFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainContentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_content,container,false);
        swipeContainer = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        swipeContainer.setColorSchemeColors(Color.rgb(2, 199, 84), Color.rgb(217, 68, 55), Color.rgb(73,135,231));
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeline();
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

    /**
     * 初始化列表，首先显示缓存数据，同时获取最新的数据
     * @param accessToken
     */
    public void initTimeline(String accessToken){
        Log.e("Test","initTimeline..........");
        mAccessToken = accessToken;
        mCurrPage = 1;
        try {
            timelineVOList.addAll(TimeLineDaoUtil.findAll(MainContentFragment.this.getActivity(), "1", "1"));
            myAdapter.notifyDataSetChanged();
            shownTimelineAnimation(false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshTimeline();
                }
            },1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 刷新列表 */
    public void refreshTimeline(){
        Log.e("Test","refreshTimeline..........");
        mCurrPage = 1;
        timelineVOList.clear();
        loadTimeLine(mCurrPage, true);
    }

    private void shownTimelineAnimation(boolean withAnimation){
        if(withAnimation){
            swipeContainer.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_in));
            loadingPbar.startAnimation(AnimationUtils.loadAnimation(
                    getActivity(), android.R.anim.fade_out));
            loadingPbar.setVisibility(View.GONE);
            swipeContainer.setVisibility(View.VISIBLE);
        }else{
            loadingPbar.setVisibility(View.GONE);
            swipeContainer.setVisibility(View.VISIBLE);
        }

    }

    private void loadTimeLine(final int page,final boolean isInit){
        FriendsTimelineReq timelineReq = new FriendsTimelineReq();
        timelineReq.setAccess_token(mAccessToken);
        //timelineReq.setPage(page+"");//有了max_id就不需要页数来控制了
        if(timelineVOList.size() != 0){ //加载更多，
            timelineReq.setMax_id(timelineVOList.get(timelineVOList.size() -1).getIdstr());
            Log.e("Test", "Max_id=====" + timelineVOList.get(timelineVOList.size() -1).getIdstr());
        }
        XHttpClient.getInstance(this.getActivity()).get(timelineReq, new WeiBoResponseListener<FriendsTimelineRsp>() {
            @Override
            public void onSuccess(FriendsTimelineRsp rsp) throws Exception {
                Log.e("Test","onSuccess........");
                List<TimelineVO> statuses = rsp.getStatuses();
                if (page == 1) {
                    //TODO 数据库操作也是IO型阻塞操作，要放在新的线程里，所以使用Loader
                    TimeLineDaoUtil.deleteAll(MainContentFragment.this.getActivity(),"1","1");
                    TimeLineDaoUtil.addTimeLine(MainContentFragment.this.getActivity(),"1",statuses);
                    timelineVOList.addAll(statuses);
                } else {//maxid会导致第一条和上一页的最后一条内容一致
                    if (statuses.size() > 1) {
                        TimeLineDaoUtil.addTimeLine(MainContentFragment.this.getActivity(),"1",statuses);
                        timelineVOList.addAll(statuses.subList(1, statuses.size()));
                    }
                }

                myAdapter.notifyDataSetChanged();

                if (isInit) {
                    shownTimelineAnimation(true);
                }
                swipeContainer.setRefreshing(false);
                isLoading = false;
                //Toast.makeText(MainContentFragment.this.getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(IWeiBoResponse rsp) {
                Log.e("Test",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>.onError");
                //出现异常，并且list为空，则从数据库中查询
                try {
                    if(timelineVOList.size() == 0){
                        timelineVOList.addAll(TimeLineDaoUtil.findAll(MainContentFragment.this.getActivity(), "1", "1"));
                        myAdapter.notifyDataSetChanged();
                        shownTimelineAnimation(true);
                        swipeContainer.setRefreshing(false);
                        isLoading = false;


                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
