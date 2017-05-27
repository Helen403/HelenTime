package activity;

import android.widget.ListView;

import com.example.snoy.helen.R;

import java.util.ArrayList;

import adapter.AdapterMeCall;
import base.HBaseActivity;
import bean.MeCallBean;

/**
 * Created by Helen on 2017/5/23.
 */
public class MeCallActivity extends HBaseActivity {

    ListView lv;
    ArrayList<MeCallBean> data = new ArrayList<>();
    AdapterMeCall adapter;

    @Override
    public int getContentView() {
        return R.layout.activity_me_call;
    }

    @Override
    public void findViews() {
        lv = (ListView) contentView.findViewById(R.id.me_call_listview);
        adapter = new AdapterMeCall(this);
        lv.setAdapter(adapter);
        data.add(new MeCallBean());
        data.add(new MeCallBean());
        data.add(new MeCallBean());
        data.add(new MeCallBean());
        adapter.setData(data);
    }

    @Override
    public void initData() {

    }

    @Override
    public void setListeners() {

    }
}
