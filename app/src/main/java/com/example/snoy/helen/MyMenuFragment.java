package com.example.snoy.helen;

import android.graphics.Color;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import activity.LoginActivity;
import activity.SetActivity;
import adapter.AdapterMenu;
import base.HBaseFragment;
import bean.MessageEvent;
import bean.UserBean;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by SNOY on 2017/5/10.
 */
public class MyMenuFragment extends HBaseFragment {

    private CircleImageView cir;
    private TextView tv1;
    private ListView myListview;
    AdapterMenu adapter;

    TextView set;

    @Override
    public int getContentView() {
        return R.layout.fragment_my_menu;
    }

    @Override
    public void findViews() {
        cir = (CircleImageView)contentView.findViewById(R.id.menu_cir);
        tv1 = (TextView)contentView.findViewById(R.id.tv_1);
        myListview = (ListView)contentView.findViewById(R.id.my_listview);
        set = (TextView) contentView.findViewById(R.id.menu_set);
    }

    @Override
    public void initData() {
        cir.setBorderColor(Color.WHITE);
        cir.setBorderWidth(2);
        cir.setImageResource(R.mipmap.quila);
        tv1.setText("游客 请登录");

        ArrayList<String> data = new ArrayList<>();
        data.add("我的公司");
        data.add("我的钱包");
        data.add("我的录音");
        data.add("我的礼物");

        adapter = new AdapterMenu(getActivity());
        myListview.setAdapter(adapter);
        adapter.setData(data);
    }

    @Override
    public void setListeners() {
        setOnListeners(cir,set);
        setOnClick(new onClick() {
            @Override
            public void onClick(View v, int id) {
                switch (id) {
                    case R.id.menu_cir:
                        goToActivityByClass(LoginActivity.class);
                        break;
                    case R.id.menu_set:
                        set();
                        break;
                }
            }
        });
    }

    private void set() {
        goToActivityByClass(SetActivity.class);

    }

    public void refresh(MessageEvent event){
        UserBean userBean = (UserBean) event.getData();
        tv1.setText(userBean.getUserNickName());
    }
}
