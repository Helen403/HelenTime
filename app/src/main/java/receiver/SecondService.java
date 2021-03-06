package receiver;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.snoy.helen.R;


public class SecondService extends Service {

    private MyBinder binder;
    private MyConn conn;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate() {
        super.onCreate();

        binder = new MyBinder();
        if(conn==null)
            conn = new MyConn();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        startForeground(250, builder.build());
        startService(new Intent(this,ThirdService.class));
        SecondService.this.bindService(new Intent(this,FirstService.class),conn, Context.BIND_IMPORTANT);
    }

    class MyBinder extends ProcessService.Stub{
        @Override
        public void doSomething() throws RemoteException {
            Log.i("TAG", "绑定成功!");
            // 启动FirstService
            SecondService.this.startService(new Intent(SecondService.this,FirstService.class));
            //绑定FirstService
            SecondService.this.bindService(new Intent(SecondService.this, FirstService.class), conn, Context.BIND_IMPORTANT);
        }


    }

    class  MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("Info","@@@@@@@@@@@@与FirtService连接成功"+SecondService.this.getApplicationInfo().processName);
            try {
                if (binder != null)
                    binder.doSomething();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
//            Toast.makeText(SecondService.this,"FirstService被杀死",Toast.LENGTH_SHORT).show();
//            // 启动FirstService
            SecondService.this.startService(new Intent(SecondService.this,FirstService.class));
            //绑定FirstService
            SecondService.this.bindService(new Intent(SecondService.this,FirstService.class),conn, Context.BIND_IMPORTANT);

        }
    }

}
