package tmovie.jld.com.tmovie.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：Tmedia
 * 晶凌达科技有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
 *
 * @creator boping
 * @create-time 2016/8/29 10:49
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mList;

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> mList) {
        super(fm);
        this.mList = (ArrayList<Fragment>) mList;
    }
    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

}
