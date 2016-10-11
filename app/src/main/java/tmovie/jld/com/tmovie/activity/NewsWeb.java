package tmovie.jld.com.tmovie.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import tmovie.jld.com.tmovie.R;
import tmovie.jld.com.tmovie.util.LogUtil;

public class NewsWeb extends BaseActivity {

    private View title_bar;
    private WebView wv_content;
    private String url;
    private String title;
    private ProgressBar mProgressB;
    public static final String TAG = "NewsWeb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_web);

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        initView();
    }

    public void initView() {
        title_bar = findViewById(R.id.news_web_title);
        wv_content = (WebView) findViewById(R.id.wv_news_web_content);
        mProgressB = (ProgressBar) findViewById(R.id.pb_news_web);

        ImageView iv_back = (ImageView) title_bar.findViewById(R.id.iv_bar_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wv_content.canGoBack())
                    wv_content.goBack();
                else
                    finish();
            }
        });
        ImageView iv_pull = (ImageView) title_bar.findViewById(R.id.iv_bar_pull);
        iv_pull.setVisibility(View.INVISIBLE);
        iv_pull.setClickable(false);
        TextView tv_title = (TextView) title_bar.findViewById(R.id.tv_bar_title);
        if (title != null)
            tv_title.setText(title);

        wv_content.getSettings().setJavaScriptEnabled(true);
        wv_content.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;//不用调用其他网页打开
            }
        });
        wv_content.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                LogUtil.d(TAG, "newProgress:" + newProgress);
                mProgressB.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressB.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        if (url != null)
            wv_content.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && wv_content.canGoBack()) {
            wv_content.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
