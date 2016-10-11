package tmovie.jld.com.tmovie.util;

/**
 * 项目名称：Tmovie
 * 晶凌达科技有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
 *
 * @creator boping
 * @create-time 2016/8/30 11:16
 * <p/>
 * 接口
 */
public class Interface {

    /**
     * 获取电影列表
     */
    public static final String URL = "http://192.168.1.1:81";
    public static final String GET_MOVIE_CONTENT = "/mv/api.php?typeid="; //typeid是电影类别的id，默认不传或传0值，则为获取所有列表
    public static final String GET_MOVIE_TYPE = "/mv/api_type.php?typeid=0"; //获取电影分类列表
    public static final String GET_MUSIC_TYPE = "/mv/api_type.php?typeid=1"; //获取音乐分类列表
    public static final String GET_MUSIC_CONTENT = "/mv/api_music.php?typeid=0"; //获取音乐分类列表
    /**
     * 更新点击数
     */
    public static final String UPDATE_NUM = "/mv/nums.php?id="; // mv/nums.php?id=3
    /**
     * 新闻
     */
    public static final String NEWS_URL = "http://v.juhe.cn/toutiao/index"; // mv/nums.php?id=3
    /**
     * 获取MAC地址
     */
    public static final String GET_MAC = "/mv/getmac.php";
    /**
     * 提交反馈地址
     */
//    public static final String FEEDBACK_URL = "/mv/server.php";
    /**
     * 外网反馈地址
     */
    public static final String FEEDBACK_URL = "http://movie.torsun.com.cn/index.php/index/videoFeedBackApi";

}


