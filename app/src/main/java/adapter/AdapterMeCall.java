package adapter;

import android.content.Context;

import com.example.snoy.helen.R;

import base.HBaseAdapter;
import bean.MeCallBean;

/**
 * Created by Helen on 2017/5/23.
 */
public class AdapterMeCall extends HBaseAdapter<MeCallBean> {
    public AdapterMeCall(Context context) {
        super(context);
    }

    @Override
    public int getContentView() {
        return R.layout.item_me_call;
    }

    @Override
    public void convert(ViewHolder holder, MeCallBean meCallBean, int position) {

    }
}
