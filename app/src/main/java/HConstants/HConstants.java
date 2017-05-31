package HConstants;

/**
 * Created by SNOY on 2017/5/13.
 */
public interface HConstants {
    String url = "http://192.168.1.131:8080/timeLang/";
    String url_man = "http://192.168.1.188:8080/timeLang/";

    /**
     * Eventbus 常量
     */
    interface EVENT {
        int HOMEREFRESH = 0;
        int LOGINACTIVITY_CLOSE = 1;
    }

    interface URL {
        //验证码
        String VER = url + "existsUser";
        //注册
        String Register = url + "saveUser";
        //登录
        String LOGIN = url + "userLogin";

        String Weather = url + "weather";

        String historyToday = url + "historyToday";

        String saveRemind = url_man + "saveRemind";

        String findFriendList = url + "findFriendList";

        String updateRegId = url + "updateRegId";

        String uploadFile = url + "uploadFile";


        String uploadPhoto = url + "uploadPicFile";


    }

    interface KEY {
        String UserId = "userId";
        String RegId = "regId";
        String QQnickName = "QQnickName";
        String QQfigureurl = "QQfigureurl";
        String QQgender = "QQgender";
        String QQcity = "QQcity";
        String QQopenid = "QQopenid";
    }
}
