package tmovie.jld.com.tmovie.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tmovie.jld.com.tmovie.R;
import tmovie.jld.com.tmovie.adapter.ViewPagerAdapter;
import tmovie.jld.com.tmovie.fragment.FeedbackFragment;
import tmovie.jld.com.tmovie.fragment.MovieFragment;
import tmovie.jld.com.tmovie.fragment.MusicFragment;
import tmovie.jld.com.tmovie.fragment.NewsFragment;
import tmovie.jld.com.tmovie.util.LogUtil;
import tmovie.jld.com.tmovie.view.myViewPager;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    public static final String TAG = "BaseActivity";
    private TabLayout mTablayout;
    private myViewPager mViewPager;
    private List<Fragment> mFragments = new ArrayList<>();
    private int mTabImage[] = {R.mipmap.movie, R.mipmap.music, R.mipmap.news, R.mipmap.feedback};
    private int mTabClickImage[] = {R.mipmap.movie_click, R.mipmap.music_click, R.mipmap.news_click, R.mipmap.feedback_click};
    private String[] mTabText;
    private TextView tv_title;
    private MusicFragment mMusicFragment;
    private View title_bar;
    public static int MovieFragmentId = 1;
    public static int MusicFragmentId = 2;
    public static int NewsFragmentId = 3;
    public static int FeedbackFragmentId = 4;
    public static int currentFragment = MovieFragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabText = new String[]{getResources().getString(R.string.movie), getResources().getString(R.string.music), getResources().getString(R.string.news), getResources().getString(R.string.feedback)};
        initView();
        initViewPager();
        initEvents();
    }

    private void initView() {
        /**titleBar*/
        title_bar = findViewById(R.id.title_bar_main);
        ImageView iv_back = (ImageView) title_bar.findViewById(R.id.iv_bar_back);
        iv_back.setVisibility(View.INVISIBLE);
        iv_back.setClickable(false);
        ImageView iv_pull = (ImageView) title_bar.findViewById(R.id.iv_bar_pull);
        iv_pull.setVisibility(View.INVISIBLE);
        iv_pull.setClickable(false);
        tv_title = (TextView) title_bar.findViewById(R.id.tv_bar_title);

        mTablayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (myViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTabText[position];
            }

        });
        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.addTab(mTablayout.newTab().setText(mTabText[0]).setIcon(mTabClickImage[0]));
        mTablayout.addTab(mTablayout.newTab().setText(mTabText[1]).setIcon(mTabImage[1]));
        mTablayout.addTab(mTablayout.newTab().setText(mTabText[2]).setIcon(mTabImage[2]));
        mTablayout.addTab(mTablayout.newTab().setText(mTabText[3]).setIcon(mTabImage[3]));
    }

    private void initViewPager() {
        MovieFragment mMovieFragment = new MovieFragment();
        mMusicFragment = new MusicFragment();
        NewsFragment mNewsFragment = new NewsFragment();
        FeedbackFragment mFeedbackFragment = new FeedbackFragment();

        mFragments.add(mMovieFragment);
        mFragments.add(mMusicFragment);
        mFragments.add(mNewsFragment);
        mFragments.add(mFeedbackFragment);
        LogUtil.d(TAG, "------------------1");
        ViewPagerAdapter mTabAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        LogUtil.d(TAG, "------------------2");

        mTabAdapter.notifyDataSetChanged();
        LogUtil.d(TAG, "------------------3");

        mViewPager.setAdapter(mTabAdapter);
        LogUtil.d(TAG, "------------------4");

        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(this);
        tv_title.setText(mTabText[0]);
        LogUtil.d(TAG, "------------------5");

    }

    @Override
    protected void onDestroy() {
        LogUtil.d(TAG, "onDestroy:" + mMusicFragment);
        mMusicFragment.onDestroy();
        super.onDestroy();
    }

    /**
     * 设置tabLayout监听
     */
    private void initEvents() {

        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LogUtil.d(TAG, "initEvents:" + tab);
                if (tab == mTablayout.getTabAt(0)) {
                    mTablayout.getTabAt(0).setIcon(mTabClickImage[0]);
                    mViewPager.setCurrentItem(0);
                    title_bar.setVisibility(View.VISIBLE);
                    tv_title.setText(mTabText[0]);
                    currentFragment = 1;
                } else if (tab == mTablayout.getTabAt(1)) {
                    mTablayout.getTabAt(1).setIcon(mTabClickImage[1]);
                    mViewPager.setCurrentItem(1);
                    title_bar.setVisibility(View.VISIBLE);
                    tv_title.setText(mTabText[1]);
                    currentFragment = 2;
                } else if (tab == mTablayout.getTabAt(2)) {
                    mTablayout.getTabAt(2).setIcon(mTabClickImage[2]);
                    mViewPager.setCurrentItem(2);
                    tv_title.setText(mTabText[2]);
                    title_bar.setVisibility(View.GONE);
//                    mViewPager.sets
                    currentFragment = 3;
                } else if (tab == mTablayout.getTabAt(3)) {
                    mTablayout.getTabAt(3).setIcon(mTabClickImage[3]);
                    mViewPager.setCurrentItem(3);
                    title_bar.setVisibility(View.VISIBLE);
                    tv_title.setText(mTabText[3]);
                    currentFragment = 4;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == mTablayout.getTabAt(0)) {
                    mTablayout.getTabAt(0).setIcon(mTabImage[0]);
                } else if (tab == mTablayout.getTabAt(1)) {
                    mTablayout.getTabAt(1).setIcon(mTabImage[1]);
                } else if (tab == mTablayout.getTabAt(2)) {
                    mTablayout.getTabAt(2).setIcon(mTabImage[2]);
                } else if (tab == mTablayout.getTabAt(3)) {
                    mTablayout.getTabAt(3).setIcon(mTabImage[3]);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentFragment = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
