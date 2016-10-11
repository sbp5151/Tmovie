package tmovie.jld.com.tmovie.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 项目名称：Tmovie
 * 晶凌达科技有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
 *
 * @creator boping
 * @create-time 2016/8/30 11:19
 */
public class OkHttpUtil {

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /**
     * GET请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String okGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * POST请求
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String okPost(String url, String json) throws IOException {
        RequestBody body = FormBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        return response.body().string();
    }

    /**
     * POST请求
     *
     * @param url
     * @param map
     * @return
     * @throws IOException
     */
    public static String okPost(String url, HashMap<String, String> map) throws IOException {
        if (map == null || map.size() <= 0)
            return "";
        FormBody.Builder builder = new FormBody.Builder();
        //遍历集合获取键值对
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        return response.body().string();
    }
}
