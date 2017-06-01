package activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.example.snoy.helen.R;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import HConstants.HConstants;
import Utils.ControlUtils;
import Utils.DButils;
import base.HBaseActivity;
import bean.MessageEvent;
import bean.ResultBean;
import bean.UserBean;


/**
 * Created by SNOY on 2017/5/10.
 */
public class LoginActivity extends HBaseActivity {

    private ImageView loginClose;
    private LinearLayout loginLl;
    private ImageView loginIphone;
    private ImageView sina;
    private ImageView weixin;
    private ImageView qq;
    private TextView loginTitle;

    private Tencent mTencent;
    private QQLoginListener mListener;
    private UserInfo mInfo;
    private String name, figureurl, city, gender;
    private String openID;


    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void findViews() {
        //hideHeadView();
        loginClose = (ImageView) findViewById(R.id.login_close);
        loginLl = (LinearLayout) findViewById(R.id.login_ll);
        loginIphone = (ImageView) findViewById(R.id.login_iphone);
        sina = (ImageView) findViewById(R.id.sina);
        weixin = (ImageView) findViewById(R.id.weixin);
        qq = (ImageView) findViewById(R.id.qq);
        loginTitle = (TextView) findViewById(R.id.login_title);
    }


    @Override
    public void initData() {
        initQQ();

    }


    @Override
    public void setListeners() {
        setOnListeners(weixin, loginClose, loginIphone, qq);
        setOnClick(new onClick() {
            @Override
            public void onClick(View v, int id) {
                switch (id) {
                    case R.id.weixin:

                        break;
                    case R.id.login_close:
                        finish();
                        break;
                    case R.id.login_iphone:

                        iphone();
                        break;
                    case R.id.qq:
                        qq();
                        break;
                }
            }
        });
    }

    private void initQQ() {
        mListener = new QQLoginListener();
        // 实例化Tencent
        if (mTencent == null) {
            mTencent = Tencent.createInstance("1106092575", this.getApplicationContext());
        }
    }


    private void iphone() {
        goToActivityByClass(PhoneActivity.class);
    }

    private void qq() {
        QQLogin();
    }

    @Override
    public void onReceiveMessage(@NonNull MessageEvent event) {
        super.onReceiveMessage(event);

        if (event.getType() == HConstants.EVENT.LOGINACTIVITY_CLOSE) {
            finish();
        }
    }


    /**
     * 登录方法
     */
    private void QQLogin() {
        //如果session不可用，则登录，否则说明已经登录
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", mListener);
        }
    }

    // 实现登录成功与否的接口
    private class QQLoginListener implements IUiListener {

        @Override
        public void onComplete(Object object) { //登录成功
            //获取openid和token
            initOpenIdAndToken(object);
            //获取用户信息
            getUserInfo();

        }

        @Override
        public void onError(UiError uiError) {  //登录失败
        }

        @Override
        public void onCancel() {                //取消登录
        }
    }


    private void initOpenIdAndToken(Object object) {
        JSONObject jb = (JSONObject) object;
        try {
            String openID = jb.getString("openid");  //openid用户唯一标识
            String access_token = jb.getString("access_token");
            String expires = jb.getString("expires_in");

            DButils.put(HConstants.KEY.QQopenid, openID);
            this.openID = openID;


            mTencent.setOpenId(openID);
            mTencent.setAccessToken(access_token, expires);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserInfo() {
        QQToken token = mTencent.getQQToken();
        mInfo = new UserInfo(this, token);
        mInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object object) {
                JSONObject jb = (JSONObject) object;
                try {
                    L(jb.toString());
                    name = jb.getString("nickname");
                    DButils.put(HConstants.KEY.QQnickName, name);
                    L(name);
                    figureurl = jb.getString("figureurl_qq_2");  //头像图片的url
                    DButils.put(HConstants.KEY.QQfigureurl, figureurl);
                    L(figureurl);

                    city = jb.getString("city");
                    DButils.put(HConstants.KEY.QQcity, city);

                    gender = jb.getString("gender");
                    DButils.put(HConstants.KEY.QQgender, gender);

                    //退出登录页面 设置数据
                    finishA();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
            }

            @Override
            public void onCancel() {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mTencent.onActivityResultData(requestCode, resultCode, data, mListener);
    }


    private void finishA() {


        HashMap<String, String> map = new HashMap<>();
        map.put("phoneDeviceCode", PhoneUtils.getIMEI());
        map.put("phoneDeviceName", DeviceUtils.getManufacturer() + " " + DeviceUtils.getModel());
        map.put("isThreeLogin", 1 + "");
        map.put("threeLoginType", "QQ");
        map.put("threeLoginID", openID);
        map.put("sex", gender);

        L(DButils.get(HConstants.KEY.UserId));
        if (DButils.get(HConstants.KEY.UserId) != null) {
            map.put("useCode", DButils.get(HConstants.KEY.UserId));
        }else {
            map.put("useCode","");
        }

        L(PhoneUtils.getPhoneType() + "");


        ControlUtils.getsEveryTime(HConstants.URL.saveUser, map, ResultBean.class, new ControlUtils.OnControlUtils<ResultBean>() {
            @Override
            public void onSuccess(String url, ResultBean resultBean, String result) {
                L(result);
                if (resultBean.result.equals("1")) {
                    L("登录成功");
                } else {
                    L("登录失败");
                }

            }

            @Override
            public void onFailure(String url) {
                L("网络失败");
            }
        });

        UserBean userBean = new UserBean();
        userBean.setUserNickName(name);
        userBean.setUserHead(figureurl);

        //发送数据到
        //发一个消息给HomeFragment 替换 名字
        onSendMessage(new MessageEvent(HConstants.EVENT.HOMEREFRESH, userBean));
        finish();
    }
}
