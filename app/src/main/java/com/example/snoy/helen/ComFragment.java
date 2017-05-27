package com.example.snoy.helen;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import activity.CallMeActivity;
import activity.FindActivity;
import activity.FriendActivity;
import activity.MeCallActivity;
import base.HBaseFragment;

/**
 * Created by SNOY on 2017/5/12.
 */
public class ComFragment extends HBaseFragment {


    // Content View Elements

    private LinearLayout mCom_frame;
    private TextView mCom_find;
    private TextView mCon_frend;
    private TextView mCom_call_me;
    private TextView mCom_me_call;


    @Override
    public int getContentView() {
        return R.layout.fragment_com;
    }

    @Override
    public void findViews() {
        mCom_frame = (LinearLayout) contentView.findViewById(R.id.com_frame);
        mCom_find = (TextView) contentView.findViewById(R.id.com_find);
        mCon_frend = (TextView) contentView.findViewById(R.id.con_frend);
        mCom_call_me = (TextView) contentView.findViewById(R.id.com_call_me);
        mCom_me_call = (TextView) contentView.findViewById(R.id.com_me_call);
    }

    @Override
    public void initData() {

    }

    @Override
    public void setListeners() {
        setOnListeners(mCom_find, mCon_frend, mCom_call_me, mCom_me_call);
        setOnClick(new onClick() {
            @Override
            public void onClick(View v, int id) {
                switch (id) {
                    case R.id.com_find:
                        find();
                        break;
                    case R.id.con_frend:
                        friend();
                        break;
                    case R.id.com_call_me:
                        callMe();
                        break;
                    case R.id.com_me_call:
                        MeCall();
                        break;
                }
            }
        });
    }

    private void MeCall() {
        goToActivityByClass(MeCallActivity.class);
    }

    private void callMe() {
        goToActivityByClass(CallMeActivity.class);
    }

    private void friend() {
        goToActivityByClass(FriendActivity.class);
    }

    private void find() {
        goToActivityByClass(FindActivity.class);
    }

}
