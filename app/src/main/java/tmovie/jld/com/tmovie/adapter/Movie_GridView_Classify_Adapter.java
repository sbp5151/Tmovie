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
import tmovie.jld.com.tmovie.fragment.MovieFragment;

/**
 * 项目名称：Tmovie
 * 晶凌达科技有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
 *
 * @creator boping
 * @create-time 2016/8/30 10:09
 */
public class Movie_GridView_Classify_Adapter extends BaseAdapter {


    public List<MovieFragment.ResponseItem> items;
    public Context mContext;

    public Movie_GridView_Classify_Adapter(List<MovieFragment.ResponseItem> movieType, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_movie_classify, null);
            holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_movie_classify);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_movie_classify);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String name = items.get(position).getName() + " (" + items.get(position).getNum() + ")";
        String imag = items.get(position).getImg();
        holder.tv_name.setText(name);
        if (!TextUtils.isEmpty(imag))
            Picasso.with(mContext).load(imag).error(R.mipmap.load_error).into(holder.iv_img);
        return convertView;
    }

    public class ViewHolder {
        public ImageView iv_img;
        public TextView tv_name;
    }
}
