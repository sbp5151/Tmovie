package tmovie.jld.com.tmovie.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import tmovie.jld.com.tmovie.R;
import tmovie.jld.com.tmovie.activity.MainActivity;
import tmovie.jld.com.tmovie.activity.MovieContentActivity;
import tmovie.jld.com.tmovie.adapter.Movie_GridView_Classify_Adapter;
import tmovie.jld.com.tmovie.util.Constants;
import tmovie.jld.com.tmovie.util.EasyMovieTexture;
import tmovie.jld.com.tmovie.util.Interface;
import tmovie.jld.com.tmovie.util.LogUtil;
import tmovie.jld.com.tmovie.util.OkHttpUtil;
import tmovie.jld.com.tmovie.util.ToastUtil;


public class MovieFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "MovieFragment";
    public static final int RESPONSE_WIN = 1;
    public static final int TOAST = 2;
    public static final int PTRL_STOP = 3;
    private Context mContext;
    private Response mResponse;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int what = msg.what;
            LogUtil.d(TAG, "what:" + what);
            switch (what) {
                case RESPONSE_WIN:
                    ToastUtil.showToast(mContext, getString(R.string.request_win), 3000);
                    initData();
                    //获取并保存MAC地址
                    new Thread(macRun).start();
                    break;
                case TOAST:
                    if (MainActivity.currentFragment == MainActivity.MovieFragmentId) {
                        String str = (String) msg.obj;
                        ToastUtil.showToast(mContext, str, 3000);
                    }
                    break;
                case PTRL_STOP:
                    if (gv_classify != null && gv_classify.isRefreshing())
                        gv_classify.onRefreshComplete();
            }
        }
    };
    private PullToRefreshGridView gv_classify;
    private Movie_GridView_Classify_Adapter mAdapter;
    private EasyMovieTexture easyMovieTexture;
    private SharedPreferences sp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        sp = mContext.getSharedPreferences(Constants.SHARE_KEY, Context.MODE_PRIVATE);
        new Thread(run).start();
        LogUtil.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.d(TAG, "onCreateView");
        View mView = inflater.inflate(R.layout.fragment_movie, container, false);
        gv_classify = (PullToRefreshGridView) mView.findViewById(R.id.gv_movie_classify);
        gv_classify.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        gv_classify.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                new Thread(run).start();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
            }
        });
        initData();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(TAG, "onResume");

    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d(TAG, "onPause");

    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d(TAG, "onStop");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy");

    }

    private void initData() {
        if (mResponse != null) {
            Movie_GridView_Classify_Adapter mAdapter = new Movie_GridView_Classify_Adapter(mResponse.getmList(), mContext);
            gv_classify.setAdapter(mAdapter);
            gv_classify.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, MovieContentActivity.class);
        intent.putExtra("typeId", mResponse.getmList().get(position).getId());
        intent.putExtra("name", mResponse.getmList().get(position).getName());
        startActivity(intent);
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            try {
                String json = OkHttpUtil.okGet(Interface.URL + Interface.GET_MOVIE_TYPE);
                LogUtil.d(TAG, "getJson----::" + json);
                Gson mGson = new Gson();
                mResponse = mGson.fromJson(json, Response.class);
                if ("1".equals(mResponse.getResult())) {
                    mHandler.sendEmptyMessage(RESPONSE_WIN);

                } else {//请求失败
                    Message message = new Message();
                    message.obj = getString(R.string.request_error);
                    message.what = TOAST;
                    mHandler.sendMessage(message);
                }
            } catch (IOException e) {//网络错误
                e.printStackTrace();
                Message message = new Message();
                message.obj = getString(R.string.network_request_error);
                message.what = TOAST;
                mHandler.sendMessage(message);
            } finally {
                mHandler.sendEmptyMessage(PTRL_STOP);
            }
        }
    };

    Runnable macRun = new Runnable() {
        @Override
        public void run() {
            try {
                String json = OkHttpUtil.okGet(Interface.URL + Interface.GET_MAC);
                if (!TextUtils.isEmpty(json)) {
                    JSONObject jsonObject = new JSONObject(json);
                    String result = jsonObject.getString("result");
                    if (!TextUtils.isEmpty(result) && result.equals("1")) {
                        String mac = jsonObject.getString("item");
                        if (!TextUtils.isEmpty(mac))//保存mac值
                            sp.edit().putString(Constants.MAC_ADDRESS, mac).apply();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    class Response {
        private String result;
        private List<ResponseItem> type;

        public String getResult() {
            return result;
        }

        public List<ResponseItem> getmList() {
            return type;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "result='" + result + '\'' +
                    ", type=" + type +
                    '}';
        }
    }

    public class ResponseItem {
        private String name;
        private String id;
        private String img;
        private String num;

        @Override
        public String toString() {
            return "ResponseItem{" +
                    "name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    ", img='" + img + '\'' +
                    ", num='" + num + '\'' +
                    '}';
        }

        public String getNum() {
            return num;
        }

        public String getImg() {
            return img;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

    }
}
