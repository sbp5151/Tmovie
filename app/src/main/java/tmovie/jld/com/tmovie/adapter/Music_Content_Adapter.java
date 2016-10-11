package tmovie.jld.com.tmovie.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import tmovie.jld.com.tmovie.R;
import tmovie.jld.com.tmovie.modle.MusicInfo;

/**
 * 项目名称：Tmovie
 * 晶凌达科技有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
 *
 * @creator boping
 * @create-time 2016/9/1 9:53
 */
public class Music_Content_Adapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MusicInfo> infos;
    private final Picasso picasso;

    public Music_Content_Adapter(List<MusicInfo> infos, Context mContext) {

        this.mContext = mContext;
        this.infos = (ArrayList<MusicInfo>) infos;
        picasso = Picasso.with(mContext);
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

        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_music_content, null);
            holder.iv_music_icon = (ImageView) convertView.findViewById(R.id.iv_music_icon);
            holder.iv_music_play = (ImageView) convertView.findViewById(R.id.iv_music_play);
            holder.tv_music_name = (TextView) convertView.findViewById(R.id.tv_music_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MusicInfo info = infos.get(position);
        holder.tv_music_name.setText(info.getName());
        if (info.getPlayState() == 1) {//播放状态
            holder.tv_music_name.setTextColor(mContext.getResources().getColor(R.color.colorClick));
            holder.iv_music_play.setVisibility(View.VISIBLE);
            holder.iv_music_play.setImageResource(R.mipmap.music_pause2);
        } else if (info.getPlayState() == 2) {//暂停状态
            holder.tv_music_name.setTextColor(mContext.getResources().getColor(R.color.colorClick));
            holder.iv_music_play.setVisibility(View.VISIBLE);
            holder.iv_music_play.setImageResource(R.mipmap.music_play2);
        } else {//未播放状态
            holder.tv_music_name.setTextColor(Color.BLACK);
            holder.iv_music_play.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(info.getPic()))
            picasso.load(info.getPic()).error(R.mipmap.happy2).into(holder.iv_music_icon);
        return convertView;
    }

    class ViewHolder {

        public ImageView iv_music_icon;
        public ImageView iv_music_play;
        public TextView tv_music_name;

    }
}
