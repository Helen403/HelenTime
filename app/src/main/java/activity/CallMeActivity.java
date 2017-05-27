package activity;

import android.util.Log;
import android.widget.ListView;

import com.example.snoy.helen.R;

import java.util.ArrayList;

import adapter.AdapterCallMe;
import base.HBaseActivity;
import bean.CallMeBean;

/**
 * Created by Helen on 2017/5/23.
 */
public class CallMeActivity extends HBaseActivity {


    ListView lv;
    AdapterCallMe adapterCallMe;
    ArrayList<CallMeBean> data = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_call_me;
    }

    @Override
    public void findViews() {
        Log.d("Helen","111");
        lv = (ListView) contentView.findViewById(R.id.call_me_listview);
        adapterCallMe = new AdapterCallMe(this);
        lv.setAdapter(adapterCallMe);
        data.add(new CallMeBean());
        data.add(new CallMeBean());
        data.add(new CallMeBean());
        data.add(new CallMeBean());
        adapterCallMe.setData(data);
    }

    @Override
    public void initData() {

    }

    @Override
    public void setListeners() {

    }
}
