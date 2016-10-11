package tmovie.jld.com.tmovie.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import tmovie.jld.com.tmovie.R;
import tmovie.jld.com.tmovie.adapter.ViewPagerAdapter;
import tmovie.jld.com.tmovie.view.myViewPager;


public class NewsFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private ImageView iv_back;
    private TextView tv_title;
    private ImageView iv_pull;
    private TabLayout tl_title;
    private myViewPager vp_content;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> jsons = new ArrayList<>();
    public static final String type[] = {"top", "shehui", "guonei", "guoji", "yule", "tiyu", "junshi", "keji", "caijing", "shishang"};
    private FragmentActivity mContext;
    private ViewPagerAdapter mTabAdapter;
    private String[] str;
    public static HashMap<String, Item_News_Fragment.Response> responses = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        str = new String[]{mContext.getString(R.string.news_top), mContext.getString(R.string.news_shehui),
                mContext.getString(R.string.news_guonei), mContext.getString(R.string.news_guoji),
                mContext.getString(R.string.news_yule), mContext.getString(R.string.news_tiyu),
                mContext.getString(R.string.news_junshi), mContext.getString(R.string.news_keji),
                mContext.getString(R.string.news_caijing), mContext.getString(R.string.news_shishang),};

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_news, container, false);
        tl_title = (TabLayout) mView.findViewById(R.id.tl_news_title);
        vp_content = (myViewPager) mView.findViewById(R.id.vp_news_content);
        mTabAdapter = new ViewPagerAdapter(mContext.getSupportFragmentManager(), fragments);
        vp_content.setAdapter(mTabAdapter);

        tl_title.setupWithViewPager(vp_content);
        tl_title.addTab(tl_title.newTab().setText(str[0]));
        tl_title.addTab(tl_title.newTab().setText(str[1]));
        tl_title.addTab(tl_title.newTab().setText(str[2]));
        tl_title.addTab(tl_title.newTab().setText(str[3]));
        tl_title.addTab(tl_title.newTab().setText(str[4]));
        tl_title.addTab(tl_title.newTab().setText(str[5]));
        tl_title.addTab(tl_title.newTab().setText(str[6]));
        tl_title.addTab(tl_title.newTab().setText(str[7]));
        tl_title.addTab(tl_title.newTab().setText(str[8]));
        tl_title.addTab(tl_title.newTab().setText(str[9]));

        tl_title.setOnTabSelectedListener(this);
        for (int i = 0; i < type.length; i++) {
            Item_News_Fragment fragment = new Item_News_Fragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", type[i]);
            bundle.putString("str", str[i]);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
//        fragmentTop.setArguments();
//        Item_News_Fragment fragmentShehui = new Item_News_Fragment(type[1],str[1]);
//        Item_News_Fragment fragmentGuonei = new Item_News_Fragment(type[2],str[2]);
//        Item_News_Fragment fragmentGuoji = new Item_News_Fragment(type[3],str[3]);
//        Item_News_Fragment fragmentYule = new Item_News_Fragment(type[4],str[4]);
//        Item_News_Fragment fragmentTiyu = new Item_News_Fragment(type[5],str[5]);
//        Item_News_Fragment fragmentJunshi = new Item_News_Fragment(type[6],str[6]);
//        Item_News_Fragment fragmentKeji = new Item_News_Fragment(type[7],str[7]);
//        Item_News_Fragment fragmentCaijing = new Item_News_Fragment(type[8],str[8]);
//        Item_News_Fragment fragmentShishang = new Item_News_Fragment(type[9],str[9]);
//
//        fragments.add(fragmentTop);
//        fragments.add(fragmentShehui);
//        fragments.add(fragmentGuonei);
//        fragments.add(fragmentGuoji);
//        fragments.add(fragmentYule);
//        fragments.add(fragmentTiyu);
//        fragments.add(fragmentJunshi);
//        fragments.add(fragmentKeji);
//        fragments.add(fragmentCaijing);
//        fragments.add(fragmentShishang);
        mTabAdapter.notifyDataSetChanged();
        vp_content.setCurrentItem(0);

        return mView;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        if (tab == tl_title.getTabAt(0)) {
            vp_content.setCurrentItem(0);
        } else if (tab == tl_title.getTabAt(1)) {
            vp_content.setCurrentItem(1);
        } else if (tab == tl_title.getTabAt(2)) {
            vp_content.setCurrentItem(2);
        } else if (tab == tl_title.getTabAt(3)) {
            vp_content.setCurrentItem(3);
        } else if (tab == tl_title.getTabAt(4)) {
            vp_content.setCurrentItem(4);
        } else if (tab == tl_title.getTabAt(5)) {
            vp_content.setCurrentItem(5);
        } else if (tab == tl_title.getTabAt(6)) {
            vp_content.setCurrentItem(6);
        } else if (tab == tl_title.getTabAt(7)) {
            vp_content.setCurrentItem(7);
        } else if (tab == tl_title.getTabAt(8)) {
            vp_content.setCurrentItem(8);
        } else if (tab == tl_title.getTabAt(9)) {
            vp_content.setCurrentItem(9);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTabAdapter != null)
            mTabAdapter.notifyDataSetChanged();

    }
}
