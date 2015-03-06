package com.msn.weiboclient.homepage;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.msn.weiboclient.R;
import com.msn.weiboclient.homepage.adapter.MultiPicsGridViewAdapter;
import com.msn.weiboclient.protocol.vo.TimelineVO;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        GridView gridView = (GridView)findViewById(R.id.thumbnail_grdv);

        TimelineVO timelineVO = new TimelineVO();
        List<TimelineVO.ThumbnailVO> data = new ArrayList<>();
        for(int i=0;i<10;i++){
            TimelineVO.ThumbnailVO vo = timelineVO.new ThumbnailVO();
            vo.setThumbnail_pic("http://ww2.sinaimg.cn/thumbnail/86a9ab32jw1epsv3imfktj20hs0no76r.jpg");
            data.add(vo);
        }
        timelineVO.setPic_urls(data);


        gridView.setAdapter(
                new MultiPicsGridViewAdapter(
                        this,
                        timelineVO.getPic_urls()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
