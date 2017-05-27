package adapter;

import android.content.Context;

import com.example.snoy.helen.R;

import base.HBaseAdapter;
import bean.CallMeBean;

/**
 * Created by Helen on 2017/5/23.
 */
public class AdapterCallMe  extends HBaseAdapter<CallMeBean>{
    public AdapterCallMe(Context context) {
        super(context);
    }

    @Override
    public int getContentView() {
        return R.layout.item_call_me;
    }

    @Override
    public void convert(ViewHolder holder, CallMeBean callMeBean, int position) {

    }
}
