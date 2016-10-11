package tmovie.jld.com.tmovie.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import tmovie.jld.com.tmovie.R;
import tmovie.jld.com.tmovie.activity.MainActivity;
import tmovie.jld.com.tmovie.activity.NewsWeb;
import tmovie.jld.com.tmovie.adapter.News_Content_Adapter;
import tmovie.jld.com.tmovie.modle.NewsInfo;
import tmovie.jld.com.tmovie.util.Constants;
import tmovie.jld.com.tmovie.util.DialogUtil;
import tmovie.jld.com.tmovie.util.Interface;
import tmovie.jld.com.tmovie.util.LogUtil;
import tmovie.jld.com.tmovie.util.OkHttpUtil;
import tmovie.jld.com.tmovie.util.ToastUtil;

/**
 * 项目名称：Tmovie
 * 晶凌达科技有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
 *
 * @creator boping
 * @create-time 2016/9/3 9:28
 */
public class Item_News_Fragment extends Fragment implements AdapterView.OnItemClickListener {


    public static final String TAG = "Item_News_Fragment";
    private String type = "";
    private String title = "";
    private FragmentActivity mContext;
    public static final int TOAST = 2;
    public static final int RESPONSE_WIN = 1;
    public static final int PTRL_STOP = 3;
    private ArrayList<NewsInfo> infos = new ArrayList<>();
    private Dialog mDialog;
    private boolean isFirst = true;
    private PullToRefreshListView tv;
    private News_Content_Adapter mAdatper;
    private Response mResponse;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case TOAST:
                    if (MainActivity.currentFragment == MainActivity.NewsFragmentId) {
                        String str = (String) msg.obj;
                        ToastUtil.showToast(mContext, str, 3000);
                    }
                    break;
                case RESPONSE_WIN:
                    infos = mResponse.getData();
                    mAdatper.myNotifyDataSetChanged(infos);
                    if (!isFirst) {
                        ToastUtil.showToast(mContext, getString(R.string.request_win), 3000);
                    } else
                        isFirst = false;
                    break;
                case PTRL_STOP:
                    if (tv != null && tv.isRefreshing())
                        tv.onRefreshComplete();
            }
        }
    };
    private String json;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "onCreate");
        mContext = getActivity();
        type = getArguments().getString("type");
        title = getArguments().getString("str");
        mAdatper = new News_Content_Adapter(mContext, infos);
        new Thread(run).start();
        mDialog = DialogUtil.createLoadingDialog(mContext, getString(R.string.request_ing), false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String json = "";
        if (savedInstanceState != null) {
            json = (String) savedInstanceState.get("json");
        }
        LogUtil.d(TAG, "onCreateView：" + title + "--" + json);
        View mView = inflater.inflate(R.layout.fragment_news_item, container, false);
        tv = (PullToRefreshListView) mView.findViewById(R.id.lv_news_content);
        tv.setAdapter(mAdatper);
        tv.setOnItemClickListener(this);
        /**只支持下拉*/
        tv.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        tv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Thread(run).start();
                LogUtil.d(TAG, "onPullDownToRefresh::");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });
        if (!TextUtils.isEmpty(json)) {
            Gson mGson = new Gson();
            mResponse = mGson.fromJson(json, Response.class);
            mHandler.sendEmptyMessage(RESPONSE_WIN);
        }
        return mView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        position--;
        Intent intent = new Intent(mContext, NewsWeb.class);
        intent.putExtra("url", mResponse.getData().get(position).getUrl());
        intent.putExtra("title", title);
        mContext.startActivity(intent);
    }

    Runnable run = new Runnable() {

        @Override
        public void run() {
            try {
                json = OkHttpUtil.okGet(Interface.NEWS_URL + "?" + "type=" + type + "&" + "key=" + Constants.NEWS_KEY);
                LogUtil.d(TAG, "getJson----::" + json);
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        int error_code = jsonObject.getInt("error_code");
                        if (error_code == 0) {
                            json = (jsonObject.get("result")).toString();
                            Gson mGson = new Gson();
                            mResponse = mGson.fromJson(json, Response.class);
                            LogUtil.d(TAG, "mResponse----::" + mResponse);
                            if (mResponse != null) {
//                                NewsFragment.responses.put(type, mResponse);
                                mHandler.sendEmptyMessage(RESPONSE_WIN);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Message message = new Message();
                message.obj = getString(R.string.network_request_error);
                message.what = TOAST;
                mHandler.sendMessage(message);
            } finally {
                if (mDialog != null && mDialog.isShowing())
                    mDialog.dismiss();
                mHandler.sendEmptyMessage(PTRL_STOP);
            }
        }
    };


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.d(TAG, "onSaveInstanceState:" + title + "--" + json);
        outState.putString("json", json);
    }

    class Response {
        public String stat;
        public ArrayList<NewsInfo> data;

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public ArrayList<NewsInfo> getData() {
            return data;
        }

        public void setData(ArrayList<NewsInfo> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "stat='" + stat + '\'' +
                    ", data=" + data +
                    '}';
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy" + title);
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d(TAG, "onStop");
    }
}
