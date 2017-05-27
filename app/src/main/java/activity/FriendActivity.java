package activity;

import android.os.Handler;
import android.widget.ListView;

import com.example.snoy.helen.R;

import java.util.ArrayList;
import java.util.HashMap;

import HConstants.HConstants;
import Utils.ControlUtils;
import adapter.AdapterFriend;
import base.HBaseActivity;
import bean.ComBean;

/**
 * Created by Helen on 2017/5/23.
 */
public class FriendActivity extends HBaseActivity {

    ListView lv;
    AdapterFriend adapterFriend;
    Handler handler = new Handler();

    @Override
    public int getContentView() {
        return R.layout.activity_friend;
    }

    @Override
    public void findViews() {
        lv = (ListView) contentView.findViewById(R.id.friend_listview);
    }

    @Override
    public void initData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userCode", "4");
        ControlUtils.getsEveryTime(HConstants.URL.findFriendList, map, ComBean.class, new ControlUtils.OnControlUtils<ComBean>() {
            @Override
            public void onSuccess(String url, ComBean comBean, String result) {

                final ArrayList<ComBean> data =  ControlUtils.jsonToArrayList(result, ComBean.class);
              ComBean comBean1 = data.get(0);
                final ArrayList<ComBean.FriendListBean> dataTmp = (ArrayList<ComBean.FriendListBean>) comBean1.getFriendList();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapterFriend = new AdapterFriend(FriendActivity.this);
                        lv.setAdapter(adapterFriend);
                        adapterFriend.setData(dataTmp);
                    }
                });



            }

            @Override
            public void onFailure(String url) {

            }
        });


    }

    @Override
    public void setListeners() {

    }
}
