package activity;

import android.widget.ListView;

import com.example.snoy.helen.R;

import java.util.ArrayList;

import adapter.AdapterFriend;
import base.HBaseActivity;
import bean.FriendBean;

/**
 * Created by Helen on 2017/5/23.
 */
public class FindActivity extends HBaseActivity {

    ListView lv;
    AdapterFriend adapterFriend;
    ArrayList<FriendBean> data = new ArrayList<>();


    @Override
    public int getContentView() {
        return R.layout.activity_find;
    }

    @Override
    public void findViews() {
        lv = (ListView) contentView.findViewById(R.id.find_listview);
        adapterFriend = new AdapterFriend(this);
        lv.setAdapter(adapterFriend);
//        data.add(new FriendBean());
//        data.add(new FriendBean());
//        data.add(new FriendBean());
//        data.add(new FriendBean());
//        data.add(new FriendBean());
//        adapterFriend.setData(data);
    }

    @Override
    public void initData() {

    }

    @Override
    public void setListeners() {

    }
}
