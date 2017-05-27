package adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.snoy.helen.R;

import base.HBaseAdapter;
import bean.ComBean;

/**
 * Created by Helen on 2017/5/23.
 */
public class AdapterFriend extends HBaseAdapter<ComBean.FriendListBean> {

    public AdapterFriend(Context context) {
        super(context);
    }

    @Override
    public int getContentView() {
        return R.layout.item_friend;
    }

    @Override
    public void convert(ViewHolder holder, ComBean.FriendListBean comBean, int position) {
       // Log.d("Helen",comBean.getClassCode());
        TextView name = holder.findViewById(R.id.friend_name);

        name.setText(comBean.getLsh()+"");
    }
}
