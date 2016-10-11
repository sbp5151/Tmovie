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

import java.util.ArrayList;

import tmovie.jld.com.tmovie.R;
import tmovie.jld.com.tmovie.modle.NewsInfo;

/**
 * 项目名称：Tmovie
 * 晶凌达科技有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
 *
 * @creator boping
 * @create-time 2016/9/3 9:54
 */
public class News_Content_Adapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<NewsInfo> infos;

    public News_Content_Adapter(Context mContext, ArrayList<NewsInfo> infos) {
        this.mContext = mContext;
        this.infos = infos;
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_news_item, null);
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_news_item_icon);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_news_item_title);
            holder.tv_author = (TextView) convertView.findViewById(R.id.tv_news_item_author);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_news_item_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NewsInfo info = infos.get(position);
        holder.tv_date.setText(info.getDate());
        holder.tv_author.setText(info.getAuthor_name());
        holder.tv_title.setText(info.getTitle());

        if (!TextUtils.isEmpty(info.getThumbnail_pic_s()))
            Picasso.with(mContext).load(info.getThumbnail_pic_s()).error(R.mipmap.load_error).into(holder.iv_icon);
        return convertView;
    }

    class ViewHolder {

        public ImageView iv_icon;
        public TextView tv_title;
        public TextView tv_author;
        public TextView tv_date;
    }

    public void myNotifyDataSetChanged(ArrayList<NewsInfo> infos) {
        this.infos = infos;
        notifyDataSetChanged();
    }
}
