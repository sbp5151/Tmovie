package tmovie.jld.com.tmovie.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tmovie.jld.com.tmovie.R;
import tmovie.jld.com.tmovie.activity.MainActivity;
import tmovie.jld.com.tmovie.adapter.Music_Content_Adapter;
import tmovie.jld.com.tmovie.modle.MusicInfo;
import tmovie.jld.com.tmovie.service.MusicService;
import tmovie.jld.com.tmovie.util.Interface;
import tmovie.jld.com.tmovie.util.LogUtil;
import tmovie.jld.com.tmovie.util.OkHttpUtil;
import tmovie.jld.com.tmovie.util.ToastUtil;
import tmovie.jld.com.tmovie.view.CircleSeekBar;

public class MusicFragment extends Fragment implements AdapterView.OnItemClickListener {
    public static final String TAG = "MusicFragment";

    public static final int MUSIC_PLAY = 0;//直接播放
    public static final int MUSIC_PAUSE = 1;//暂停
    public static final int MUSIC_STOP = 2;//停止
    public static final int MUSIC_PRIVIOUS = 3;//上一曲
    public static final int MUSIC_NEXT = 4;//下一曲
    public static final int MUSIC_PROGRES = 5;//更新进度
    public static final int MUSIC_CONTINUE = 6;//继续播放

    public static final int RESPONSE_WIN = 1;
    public static final int TOAST = 2;
    public static final int MUSIC_PROGRESS = 3;
    public static final int PLAY_END = 4;
    public static final int PTRL_STOP = 5;
    private Context mContext;
    private ImageView iv_back;
    private TextView tv_title;
    private ImageView iv_pull;
    private int playNum = -1;
    private boolean isBind = false;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int what = msg.what;
            switch (what) {
                case RESPONSE_WIN:
                    ToastUtil.showToast(mContext, getString(R.string.request_win), 3000);
                    initData();
                    break;
                case TOAST:
                    if (MainActivity.currentFragment == MainActivity.MusicFragmentId) {
                        String str = (String) msg.obj;
                        ToastUtil.showToast(mContext, str, 3000);
                    }
                    break;
                case MUSIC_PROGRESS:
                    break;
                case PLAY_END://自动播放下一曲
                    next();
                    break;
                case PTRL_STOP:
                    if (lv_content != null && lv_content.isRefreshing())
                        lv_content.onRefreshComplete();
            }
        }
    };
    private Response response;
    private PullToRefreshListView lv_content;
    private List<MusicInfo> infos = new ArrayList<>();
    private Music_Content_Adapter mAdapter;
    private CircleSeekBar mSeekBar;
    private int currentPlayState;
    private Intent intent2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        LogUtil.d(TAG, "onCreate");
        new Thread(run).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.d(TAG, "onCreateView");

        View mView = inflater.inflate(R.layout.fragment_music, container, false);
        lv_content = (PullToRefreshListView) mView.findViewById(R.id.lv_music_content);

        /**只支持下拉*/
        lv_content.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        lv_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Thread(run).start();
                LogUtil.d(TAG, "onPullDownToRefresh::");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });
        initData();
        return mView;
    }

    private void initData() {
        if (response != null) {
            infos = response.getItem();
            if (playNum > 0) {
                MusicInfo info = infos.get(playNum);
                info.setPlayState(currentPlayState);
            }
            mAdapter = new Music_Content_Adapter(response.getItem(), mContext);
            lv_content.setAdapter(mAdapter);
            lv_content.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position--;
        MusicInfo info = infos.get(position);
        if (playNum == position) {//播放正在播放歌曲
            if (info.getPlayState() == 1) {//暂停
                pause(position);
            } else if (info.getPlayState() == 2) {//继续播放
                continuePlay(position);
            }
        } else {//播放
            play(position);
        }
    }

    /**
     * 继续播放
     *
     * @param position
     */
    private void continuePlay(int position) {
        MusicInfo info = infos.get(position);
        startMusicService(info.getUrl(), info.getId(), MUSIC_CONTINUE + "");
        info.setPlayState(1);
        currentPlayState = 1;
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 暂停播放
     */
    private void pause(int position) {
        MusicInfo info = infos.get(position);
        info.setPlayState(2);
        currentPlayState = 2;
        startMusicService(info.getUrl(), info.getId(), MUSIC_PAUSE + "");
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 播放当前歌曲
     *
     * @param position
     */
    private void play(int position) {
        MusicInfo info = infos.get(position);
        if (!TextUtils.isEmpty(info.getUrl())) {
            startMusicService(info.getUrl(), info.getId(), MUSIC_PLAY + "");
            info.setPlayState(1);
            currentPlayState = 1;
            if (playNum != -1)
                infos.get(playNum).setPlayState(0);
            playNum = position;
            mAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showToast(mContext, getString(R.string.play_error), 3000);
        }

    }

    /**
     * 下一首
     */
    private void next() {
        LogUtil.d(TAG, "next()");
        infos.get(playNum).setPlayState(0);
        if (playNum < (infos.size() - 1))
            playNum++;
        else
            playNum = 0;
        LogUtil.d(TAG, "playNum:" + playNum);
        MusicInfo info = infos.get(playNum);
        startMusicService(info.getUrl(), info.getId(), MUSIC_PLAY + "");
        info.setPlayState(1);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 上一首
     */
    private void previous() {
        if (playNum > 0)
            playNum--;
        else
            playNum = infos.size();
        MusicInfo info = infos.get(playNum);
        startMusicService(info.getUrl(), info.getId(), MUSIC_PLAY + "");
        info.setPlayState(1);
        if (playNum != -1)
            infos.get(playNum).setPlayState(0);
        mAdapter.notifyDataSetChanged();
    }

    private void startMusicService(String url, String id, String msg) {
        Intent intent = new Intent(mContext, MusicService.class);
        intent.putExtra("url", url);
        intent.putExtra("id", id);
        intent.putExtra("msg", msg);
        mContext.startService(intent);
        if (!isBind) {
            intent2 = new Intent(mContext, MusicService.class);
            mContext.bindService(intent2, conn, Context.BIND_AUTO_CREATE);
            isBind = true;
        }
    }


    private MusicService.MyBind binder;
    Handler musicHandler;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MusicService.MyBind) service;
            binder.setHandler(mHandler);
            musicHandler = binder.getHandler();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
    @Override
    public void onDestroy() {
        LogUtil.d(TAG,"onDestroy:"+isBind);
        LogUtil.d(TAG,"onDestroy:"+conn);
        if (isBind && conn != null) {
            mContext.unbindService(conn);
            isBind = false;
            mContext.stopService(intent2);
        }
        super.onDestroy();
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            try {
                String json = OkHttpUtil.okGet(Interface.URL + Interface.GET_MUSIC_CONTENT);
                LogUtil.d(TAG, "json:" + json);
                if (!TextUtils.isEmpty(json)) {
                    Gson gson = new Gson();
                    response = gson.fromJson(json, Response.class);
                    LogUtil.d(TAG, "response:" + response);
                    if (response != null && "1".equals(response.getResult())) {
                        mHandler.sendEmptyMessage(RESPONSE_WIN);
                        return;
                    }
                }
                Message message = new Message();
                message.what = TOAST;
                message.obj = getString(R.string.request_error);
                mHandler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                Message message = new Message();
                message.what = TOAST;
                message.obj = getString(R.string.network_request_error);
                mHandler.sendMessage(message);
            } finally {
                mHandler.sendEmptyMessage(PTRL_STOP);
            }
        }
    };

    class Response {
        private String result;
        private List<MusicInfo> item;

        public String getResult() {
            return result;
        }

        public List<MusicInfo> getItem() {
            return item;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "result='" + result + '\'' +
                    ", item=" + item +
                    '}';
        }
    }
}
