package tmovie.jld.com.tmovie.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tmovie.jld.com.tmovie.R;
import tmovie.jld.com.tmovie.modle.MovieInfo;
import tmovie.jld.com.tmovie.util.LogUtil;

/**
 * 项目名称：Tmovie
 * 晶凌达科技有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
 *
 * @creator boping
 * @create-time 2016/8/30 10:09
 */
public class Movie_GridView_Content_Adapter extends BaseAdapter {


    public List<MovieInfo> items;
    public Context mContext;

    public Movie_GridView_Content_Adapter(List<MovieInfo> movieType, Context mContext) {
        this.items = movieType;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String myGetItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_movie_content, null);
            holder.iv_back = (ImageView) convertView.findViewById(R.id.iv_movie_content);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_movie_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = items.get(position).getName();
        String pic = items.get(position).getPic();
        LogUtil.d("pic:", pic);
        holder.tv_name.setText(name);
        if (!TextUtils.isEmpty(pic))
            Picasso.with(mContext).load(pic).error(R.mipmap.load_error).into(holder.iv_back);
        return convertView;
    }

    public class ViewHolder {
        public ImageView iv_back;
        public TextView tv_name;
    }
}
