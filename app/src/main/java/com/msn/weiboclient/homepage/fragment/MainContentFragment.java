package com.msn.weiboclient.homepage.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
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
    /** 刷新 */
    public static final int TYPE_REFRESH = 0;
    /** 加载更多 */
    public static final int TYPE_LOAD_MORE = 1;
    /** 初始化 */
    public static final int TYPE_INIT = 2;


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
                refreshTimeline(mAccessToken,TYPE_REFRESH);
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
                        loadTimeLine(TYPE_LOAD_MORE);
                    }
                }

            }
        });

        loadingPbar = (ProgressBar)view.findViewById(R.id.loading_pbar);
        swipeContainer.setVisibility(View.GONE);
        return  view;
    }


    public void init(String accessToken){
        try {
            timelineVOList.addAll(TimeLineDaoUtil.findAll(MainContentFragment.this.getActivity(), "1", "1"));
            myAdapter.notifyDataSetChanged();
            refreshTimeline(accessToken,TYPE_INIT);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /** 刷新列表 */
    public void refreshTimeline(String accessToken,int loadType){
        Log.e("Test","refreshTimeline..........");
        mAccessToken = accessToken;
        mCurrPage = 1;

        loadTimeLine(loadType);
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

    private void loadTimeLine(final int loadType){
        FriendsTimelineReq timelineReq = new FriendsTimelineReq();
        timelineReq.setAccess_token(mAccessToken);
        if(loadType == TYPE_INIT || loadType == TYPE_REFRESH){//刷新
            timelineReq.setCount("50");
            timelineReq.setPage("1");
        }else{ //加载更多
            timelineReq.setCount("20");
            //有了max_id就不需要页数来控制了
            timelineReq.setMax_id(timelineVOList.get(timelineVOList.size() -1).getIdstr());
            Log.e("Test", "Max_id=====" + timelineVOList.get(timelineVOList.size() -1).getIdstr());
        }
        XHttpClient.getInstance(this.getActivity()).get(timelineReq, new WeiBoResponseListener<FriendsTimelineRsp>() {
            @Override
            public void onSuccess(FriendsTimelineRsp rsp) throws Exception {
                Log.e("Test","onSuccess........loadType="+loadType);
                List<TimelineVO> statuses = rsp.getStatuses();
                if (loadType == TYPE_INIT || loadType == TYPE_REFRESH) {
                    merg(statuses);
                } else {//maxid会导致第一条和上一页的最后一条内容一致
                    if (statuses.size() > 1) {
                        TimeLineDaoUtil.addTimeLine(MainContentFragment.this.getActivity(),"1",statuses);
                        timelineVOList.addAll(statuses.subList(1, statuses.size()));
                    }
                }

                myAdapter.notifyDataSetChanged();
                if (loadType == TYPE_INIT) {
                    shownTimelineAnimation(true);
                }
                swipeContainer.setRefreshing(false);
                isLoading = false;
                //Toast.makeText(MainContentFragment.this.getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(IWeiBoResponse rsp) {
                Log.e("Test", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>.onError");
                //出现异常，显示缓存数据
                if (loadType == TYPE_INIT || loadType == TYPE_REFRESH) {
                    shownTimelineAnimation(true);
                }
            }
        });
    }


    private void merg(List<TimelineVO> newStatuses) throws SQLException{
        if(timelineVOList.size() == 0){//没有缓存数据，直接添加
            addMemoryAndCache(newStatuses);
            return;
        }

        if(newStatuses.size() == 0){
            Log.e("Test","NO====newStatuses");
            return;
        }

        String firstCacheId = timelineVOList.get(0).getId();
        int splitIndex = -1;
        for(int i=0 ; i<newStatuses.size()-1 ; i++){//从前往后找
            if(firstCacheId.equals(newStatuses.get(i).getId())){//后面的数据都是旧的了
                splitIndex = i;
                break;
            }
        }
        Log.e("Test","splitIndex===="+splitIndex);
        if(splitIndex == 0){
            Snackbar snack = Snackbar.make(swipeContainer, "没有再新的数据了", Snackbar.LENGTH_LONG);
            snack.show();

        }
        if(splitIndex != -1){ //加载
            Log.e("Test","splitIndex===="+splitIndex);
            addMemoryAndCache(newStatuses.subList(0,splitIndex));
        }else{//重构缓存
            refactorMemoryAndCache(newStatuses);
        }
    }

    private void addMemoryAndCache(List<TimelineVO> newStatuses)throws SQLException{
        timelineVOList.addAll(0,newStatuses);
        //TODO 数据库操作也是IO型阻塞操作，要放在新的线程里，所以使用Loader
        TimeLineDaoUtil.addTimeLine(MainContentFragment.this.getActivity(),"1",newStatuses);
    }

    private void refactorMemoryAndCache(List<TimelineVO> newStatuses)throws SQLException{
        timelineVOList.clear();
        //TODO 数据库操作也是IO型阻塞操作，要放在新的线程里，所以使用Loader
        TimeLineDaoUtil.deleteAll(MainContentFragment.this.getActivity(),"1","1");
        addMemoryAndCache(newStatuses);
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
