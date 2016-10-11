package tmovie.jld.com.tmovie.modle;

/**
 * 项目名称：Tmovie
 * 晶凌达科技有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
 *
 * @creator boping
 * @create-time 2016/9/1 10:09
 */
public class MusicInfo {

    private String id;
    private String name;
    private String pic;
    private String url;
    private String typeid;
    private int playState = 0;//0为未播放状态，1位播放状态，2位暂停状态

    @Override
    public String toString() {
        return "MusicInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", url='" + url + '\'' +
                ", typeid='" + typeid + '\'' +
                ", playState=" + playState +
                '}';
    }

    public int getPlayState() {
        return playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

}
