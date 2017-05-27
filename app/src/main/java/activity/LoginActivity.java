package activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.snoy.helen.R;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import HConstants.HConstants;
import base.HBaseActivity;
import bean.MessageEvent;
//import me.shaohui.shareutil.LoginUtil;
//import me.shaohui.shareutil.ShareConfig;
//import me.shaohui.shareutil.ShareManager;
//import me.shaohui.shareutil.login.LoginListener;
//import me.shaohui.shareutil.login.LoginPlatform;
//import me.shaohui.shareutil.login.LoginResult;
//import me.shaohui.shareutil.login.result.BaseUser;
//import me.shaohui.shareutil.login.result.QQToken;
//import me.shaohui.shareutil.login.result.QQUser;

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

      //  mListener = new QQLoginListener();
//        // 实例化Tencent
//        if (mTencent == null) {
//            mTencent = Tencent.createInstance("1106094537", this.getApplicationContext());
//        }
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
                        goToActivityByClass(PhoneActivity.class);
                        break;
                    case R.id.qq:
                        qq();
                        break;
                }
            }
        });
    }

    private void qq() {
        L("666665");
      //  QQLogin();
    }

    @Override
    public void onReceiveMessage(@NonNull MessageEvent event) {
        super.onReceiveMessage(event);

        if (event.getType() == HConstants.EVENT.LOGINACTIVITY_CLOSE) {
            finish();
        }
    }





    private Tencent mTencent;
    private QQLoginListener mListener;
    private UserInfo mInfo;
    private String name, figureurl;



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
                    name = jb.getString("nickname");
                    L(name);
                    figureurl = jb.getString("figureurl_qq_2");  //头像图片的url
                    //nickName.setText(name);

                  //  Picasso.with(MainActivity.this).load(figureurl).into(figure);
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


}
