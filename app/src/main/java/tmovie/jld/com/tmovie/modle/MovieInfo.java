package tmovie.jld.com.tmovie.modle;

/**
 * 项目名称：Tmovie
 * 晶凌达科技有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
 *
 * @creator boping
 * @create-time 2016/8/30 16:46
 */
public class MovieInfo {

    private String id;
    private String name;
    private String pic;
    private String url;
    private String adTime;
    private String typeName;
    private String typeId;
    private String time;
    private String nums;

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

    public String getAdTime() {
        return adTime;
    }

    public void setAdTime(String adTime) {
        this.adTime = adTime;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    @Override
    public String toString() {
        return "MovieInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", url='" + url + '\'' +
                ", adTime='" + adTime + '\'' +
                ", typeName='" + typeName + '\'' +
                ", typeId='" + typeId + '\'' +
                ", time='" + time + '\'' +
                ", nums='" + nums + '\'' +
                '}';
    }
}
