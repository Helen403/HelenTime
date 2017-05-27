package activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.snoy.helen.R;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import Utils.AudioRecoderUtils;
import Utils.PopupWindowFactory;
import Utils.TimeUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import service.FileUpLoadService;

/**
 * Created by Helen on 2017/5/24.
 */
public class SoundActivity extends AppCompatActivity {

    static final int VOICE_REQUEST_CODE = 66;

    private Button mButton;
    private ImageView mImageView;
    private TextView mTextView;
    private AudioRecoderUtils mAudioRecoderUtils;
    private Context context;
    private PopupWindowFactory mPop;
    private RelativeLayout rl;

    TextView path;
    TextView play;
    TextView upFile;


    /**
     * 系统播放器
     */
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        context = this;

        rl = (RelativeLayout) findViewById(R.id.rl);

        mButton = (Button) findViewById(R.id.button);
        path = (TextView) findViewById(R.id.sound_path);
        play = (TextView) findViewById(R.id.sound_play);
        upFile = (TextView) findViewById(R.id.sound_upfile);
        //PopupWindow的布局文件
        final View view = View.inflate(this, R.layout.layout_microphone, null);

        mPop = new PopupWindowFactory(this, view);

        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) view.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) view.findViewById(R.id.tv_recording_time);

        mAudioRecoderUtils = new AudioRecoderUtils();

        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(TimeUtils.long2String(time));
            }

            //录音结束，filePath为保存路径
            @Override
            public void onStop(String filePath) {
                Toast.makeText(SoundActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                mTextView.setText(TimeUtils.long2String(0));
                path.setText(filePath);

            }
        });


        //6.0以上需要权限申请
        requestPermissions();

    }

    /**
     * 开启扫描之前判断权限是否打开
     */
    private void requestPermissions() {
        //判断是否开启摄像头权限
        if ((ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                ) {
            StartListener();

            //判断是否开启语音权限
        } else {
            //请求获取摄像头权限
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, VOICE_REQUEST_CODE);
        }

    }

    /**
     * 请求权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == VOICE_REQUEST_CODE) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                StartListener();
            } else {
                Toast.makeText(context, "已拒绝权限！", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void StartListener() {

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer = new MediaPlayer();

                    /**
                     * 播放过程中展示的动画
                     */
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            if (mp != null) {
                                mp.start();
                            }
                        }
                    });

                    /**
                     *  播放完成监听
                     */
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            if (mp.isPlaying()) {
                                mp.release();// 释放资源
                            }

                        }
                    });


                    mediaPlayer.setDataSource(path.getText().toString());
                    // 缓冲
                    mediaPlayer.prepare();

                } catch (Exception e) {

                    // Utils.showToast(MainActivity.this, "语音异常，加载失败");
                }
            }
        });


        //Button的touch监听
        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        mPop.showAtLocation(rl, Gravity.CENTER, 0, 0);

                        mButton.setText("松开保存");
                        mAudioRecoderUtils.startRecord();


                        break;

                    case MotionEvent.ACTION_UP:

                        mAudioRecoderUtils.stopRecord();        //结束录音（保存录音文件）
//                        mAudioRecoderUtils.cancelRecord();    //取消录音（不保存录音文件）
                        mPop.dismiss();
                        mButton.setText("按住说话");

                        break;
                }
                return true;
            }
        });

        upFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUpLoadService fileUpLoadService = new FileUpLoadService(SoundActivity.this, "HelenText", path.getText().toString(), "android_audio");
                fileUpLoadService.upload();

            }
        });

    }

    private void uploadMultiFile() {
        final String url = "upload url";
        File file = new File("fileDir", "test.jpg");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "test.jpg", fileBody)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();


        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient  = httpBuilder
                //设置超时
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Helen", "uploadMultiFile() e=" + e);
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("Helen", "uploadMultiFile() response=" + response.body().string());
            }
        });
    }


    private int frequence = 16000;// 8000;
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

//    public void play(){
//        ThreadPoolUtils.runTaskInThread(new Runnable() {
//            @Override
//            public void run() {
//               int bufferSize = AudioTrack.getMinBufferSize(frequence,
//                       channelConfig, audioEncoding);
//            short[] buffer = new short[bufferSize / 4];
//            try {
//                // 定义输入流，将音频写入到AudioTrack类中，实现播放
//                DataInputStream dis = new DataInputStream(
//                        new BufferedInputStream(new FileInputStream(audioFile)));
//                // 实例AudioTrack
//                AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC,
//                        frequence, channelConfig, audioEncoding, bufferSize,
//                        AudioTrack.MODE_STREAM);
//                // 开始播放
//                track.play();
//                // 由于AudioTrack播放的是流，所以，我们需要一边播放一边读取
//                while (dis.available() > 0) {
//                    int i = 0;
//                    while (dis.available() > 0 && i < buffer.length) {
//                        buffer[i] = dis.readShort();
//                        i++;
//                    }
//                    // 然后将数据写入到AudioTrack中
//                    track.write(buffer, 0, buffer.length);
//
//                }
//
//                // 播放结束
//                track.stop();
//                dis.close();
//            } catch (Exception e) {
//                // TODO: handle exception
//            }
//            }
//        });
//

    // }


}
