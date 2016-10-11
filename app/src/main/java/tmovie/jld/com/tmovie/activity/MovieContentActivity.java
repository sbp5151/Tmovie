package tmovie.jld.com.tmovie.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import tmovie.jld.com.tmovie.R;
import tmovie.jld.com.tmovie.adapter.Movie_GridView_Content_Adapter;
import tmovie.jld.com.tmovie.modle.MovieInfo;
import tmovie.jld.com.tmovie.util.DialogUtil;
import tmovie.jld.com.tmovie.util.Interface;
import tmovie.jld.com.tmovie.util.LogUtil;
import tmovie.jld.com.tmovie.util.OkHttpUtil;
import tmovie.jld.com.tmovie.util.ToastUtil;

public class MovieContentActivity extends BaseActivity implements View.OnTouchListener, BaseActivity.FlingInterface,View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = "MovieContentActivity";
    private GridView gv_content;
    public static final int RESPONSE_WIN = 1;
    public static final int TOAST = 2;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        LogUtil.d(TAG,"onTouch");
        return super.onTouch(v, event);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int what = msg.what;
            switch (what) {
                case RESPONSE_WIN:
                    initData();
                    break;
                case TOAST:
                    String str = (String) msg.obj;
                    ToastUtil.showToast(MovieContentActivity.this, str, 3000);
                    break;
            }
        }
    };
    private String typeId;
    private String name;
    private Response response;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_movie);
        typeId = getIntent().getStringExtra("typeId");
        name = getIntent().getStringExtra("name");
        mDialog = DialogUtil.createLoadingDialog(this, getString(R.string.request_ing),false);
        mDialog.show();
        initView();
        new Thread(run).start();
    }

    private void initView() {
        gv_content = (GridView) findViewById(R.id.gv_movie_content);
        View view = findViewById(R.id.movie_content_title);
        ImageView iv_pull = (ImageView) view.findViewById(R.id.iv_bar_pull);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_bar_title);
        ImageView iv_back = (ImageView) view.findViewById(R.id.iv_bar_back);
        iv_pull.setVisibility(View.INVISIBLE);
        iv_pull.setClickable(false);
        tv_title.setText(name);
        iv_back.setOnClickListener(this);
    }

    private void initData() {
        LogUtil.d(TAG, "response:" + response);
        if (response != null) {
            Movie_GridView_Content_Adapter mAdapter = new Movie_GridView_Content_Adapter(response.getItems(), this);
            gv_content.setAdapter(mAdapter);
            gv_content.setOnItemClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieInfo advert = null;
        MovieInfo item = response.getItems().get(position);
        List<MovieInfo> adverts = response.getAdverts();
        //如果广告太多随机选择一个
        if (adverts.size() > 1) {
            Random random = new Random();
            int i = random.nextInt(adverts.size());
            advert = adverts.get(i);
        } else if(adverts.size() > 0){
            advert = adverts.get(0);
        }
        gotoActivity(item,advert);
    }

    //跳转到播放视频界面
    private void gotoActivity(MovieInfo item, MovieInfo advert) {
        if (item == null || TextUtils.isEmpty(item.getUrl()))
            return;

        Intent intent = new Intent();
        if (item.getUrl().endsWith(".mp4")) {
            intent.setClass(this, VodActivity.class);
        } else {
            intent.setClass(this, PlayerActivity.class);
        }
        LogUtil.d(TAG, "MovieBean : " + item.toString());
        intent.putExtra("url", item.getUrl());
        intent.putExtra("movieName", item.getName());
        if (advert!=null) {
            intent.putExtra("ad_url", advert.getUrl());
            intent.putExtra("ad_pic", advert.getPic());
            intent.putExtra("ad_time", advert.getAdTime());
        }
        startActivity(intent);
    }

    Runnable run = new Runnable() {
        @Override
        public void run() {
            try {
                String json = OkHttpUtil.okGet(Interface.URL + Interface.GET_MOVIE_CONTENT + typeId);
                LogUtil.d(TAG, "getJson:" + json);
                mDialog.dismiss();
                Gson mGson = new Gson();
                response = mGson.fromJson(json, Response.class);
                if ("1".equals(response.getResult())) {
                    mHandler.sendEmptyMessage(RESPONSE_WIN);
                } else {
                    Message message = new Message();
                    message.obj = getString(R.string.request_error);
                    message.what = TOAST;
                    mHandler.sendMessage(message);
                }
            } catch (IOException e) {
                mDialog.dismiss();
                e.printStackTrace();
                Message message = new Message();
                message.obj = getString(R.string.network_request_error);
                message.what = TOAST;
                mHandler.sendMessage(message);
            }
        }
    };

    @Override
    public void onFling() {
        finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    class Response {
        private String result;
        private List<MovieInfo> item;
        private List<MovieInfo> advert;

        public String getResult() {
            return result;
        }

        public List<MovieInfo> getItems() {
            return item;
        }

        public List<MovieInfo> getAdverts() {
            return advert;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "result='" + result + '\'' +
                    ", items=" + item +
                    ", adverts=" + advert +
                    '}';
        }
    }
}
