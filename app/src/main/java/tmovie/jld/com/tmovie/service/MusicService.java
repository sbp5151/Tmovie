package tmovie.jld.com.tmovie.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.io.IOException;

import tmovie.jld.com.tmovie.fragment.MusicFragment;
import tmovie.jld.com.tmovie.util.LogUtil;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener {

    public static final String TAG = "MusicService";
    private MediaPlayer mMediaPlayer;
    private String musicUrl;
    private String musicId;
    private int lastTime = -1;
    private String lastId;
    private boolean isPause;
    private Handler musicHandler;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private MyBind myBind;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "onCreate:");

        mMediaPlayer = new MediaPlayer();
        //播放完成监听器
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtil.d(TAG, "onCompletion:" + mHandler);
                if (musicHandler != null) {
                    musicHandler.sendEmptyMessage(MusicFragment.PLAY_END);
                }
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "onStartCommand:");
        LogUtil.d(TAG, "lastTime:" + lastTime);
        LogUtil.d(TAG, "lastId:" + lastId);

        //歌曲播放地址
        musicUrl = intent.getStringExtra("url");
        musicId = intent.getStringExtra("id");
        String musicMsg = intent.getStringExtra("msg");//播放信息

        switch (Integer.parseInt(musicMsg)) {
            case MusicFragment.MUSIC_PLAY://播放
                play();
                break;
            case MusicFragment.MUSIC_PAUSE://暂停
                pause();
                break;
            case MusicFragment.MUSIC_STOP://停止
                break;
            case MusicFragment.MUSIC_CONTINUE://继续播放
                resume();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void resume() {
        if (isPause) {
            isPause = false;
            mMediaPlayer.start();
        }
    }

    private void pause() {
        LogUtil.d(TAG, "pause:");
        LogUtil.d(TAG, "lastTime:" + lastTime);
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            lastId = musicId;
            mMediaPlayer.pause();
            isPause = true;
        }
        LogUtil.d(TAG, "lastTime:" + lastTime);
    }

    private void play() {
        LogUtil.d(TAG, "musicUrl:" + musicUrl);
        LogUtil.d(TAG, "lastTime:" + lastTime);
        try {
            mMediaPlayer.reset();
//            mMediaPlayer.setDataSource(getAssets().openFd("kugua.mp3").getFileDescriptor());
            mMediaPlayer.setDataSource(musicUrl);
            LogUtil.d(TAG, "setDataSource:");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mMediaPlayer.prepare();//缓冲
                    } catch (IOException e) {
                        LogUtil.d(TAG, "IOException:"+e.toString());
                        e.printStackTrace();
                    }
                }
            }).start();
            LogUtil.d(TAG, "prepare:");

            mMediaPlayer.setOnPreparedListener(this);
        } catch (IOException e) {
            LogUtil.d(TAG, "IOException:"+e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        LogUtil.d(TAG, "lastTime:" + lastTime);
        LogUtil.d(TAG, "getDuration:" + mMediaPlayer.getDuration());
        try {
            mMediaPlayer.start();
        }catch (IllegalStateException e){
            LogUtil.d(TAG, "IllegalStateException:"+e.toString());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d(TAG,"onBind");
        if (myBind == null)
            myBind = new MyBind();
        return myBind;
    }
    @Override
    public void onDestroy() {
        LogUtil.d(TAG,"unbindService");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    @Override
    public void unbindService(ServiceConnection conn) {
        LogUtil.d(TAG,"unbindService");
        super.unbindService(conn);
    }

    public class MyBind extends Binder {

        public void setHandler(Handler mHandler) {
            LogUtil.d(TAG, "setHandler:" + mHandler);

            musicHandler = mHandler;
        }
        public Handler getHandler() {
            return mHandler;
        }
    }
}
