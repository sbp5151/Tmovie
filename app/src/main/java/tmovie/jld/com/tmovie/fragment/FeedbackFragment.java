package tmovie.jld.com.tmovie.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import tmovie.jld.com.tmovie.R;
import tmovie.jld.com.tmovie.activity.MainActivity;
import tmovie.jld.com.tmovie.util.Constants;
import tmovie.jld.com.tmovie.util.DialogUtil;
import tmovie.jld.com.tmovie.util.Interface;
import tmovie.jld.com.tmovie.util.LogUtil;
import tmovie.jld.com.tmovie.util.OkHttpUtil;
import tmovie.jld.com.tmovie.util.ToastUtil;


public class FeedbackFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "FeedbackFragment";
    private Context mContext;
    private CheckBox cb_lyt;
    private CheckBox cb_app;
    private EditText et_content;
    private EditText et_name;
    private EditText et_phone;
    private Button bt_submit;
    private SharedPreferences sp;
    private Dialog mDialog;
    public static final int RESPONSE_MAC_WIN = 1;
    public static final int TOAST = 2;
    public static final int UPLOADING_WIN = 3;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case TOAST:
                    if (MainActivity.currentFragment == MainActivity.FeedbackFragmentId) {
                        String str = (String) msg.obj;
                        ToastUtil.showToast(mContext, str, 3000);
                    }
                    break;
                case RESPONSE_MAC_WIN:
                    mac = sp.getString(Constants.MAC_ADDRESS, "");
                    if (!TextUtils.isEmpty(mac)) {
                        new Thread(uploadingRun).start();
                    } else {
                        mDialog.dismiss();
                        ToastUtil.showToast(mContext, getString(R.string.feedback_mac_error), 3000);
                    }
                    break;
                case UPLOADING_WIN:
                    ToastUtil.showToast(mContext, getString(R.string.feedback_win), 3000);
                    et_content.setText("");
                    et_name.setText("");
                    et_phone.setText("");
                    break;
            }
        }
    };
    private String mac;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        sp = mContext.getSharedPreferences(Constants.SHARE_KEY, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_feedback, container, false);
        initView(mView);
        return mView;
    }

    private void initView(View mView) {
        et_content = (EditText) mView.findViewById(R.id.et_feedback_content);
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                LogUtil.d("TextWatcher:", "beforeTextChanged:" + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtil.d("TextWatcher:", "onTextChanged:" + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                LogUtil.d("TextWatcher:", "afterTextChanged:" + s.toString());
                if (TextUtils.isEmpty(s.toString())) {
                    bt_submit.setEnabled(false);
                    bt_submit.setClickable(false);
                } else {
                    bt_submit.setEnabled(true);
                    bt_submit.setClickable(true);
                }
            }
        });
        et_name = (EditText) mView.findViewById(R.id.et_feedback_name);
        et_phone = (EditText) mView.findViewById(R.id.et_feedback_phone);
        RelativeLayout feedback_app = (RelativeLayout) mView.findViewById(R.id.feed_back_app);
        feedback_app.setOnClickListener(this);
        cb_app = (CheckBox) mView.findViewById(R.id.cb_feedback_app);
        cb_app.setChecked(true);
        RelativeLayout feedback_lyt = (RelativeLayout) mView.findViewById(R.id.feed_back_lyt);
        feedback_lyt.setOnClickListener(this);
        cb_lyt = (CheckBox) mView.findViewById(R.id.cb_feedback_lt);
        bt_submit = (Button) mView.findViewById(R.id.bt_feedback_submit);
        bt_submit.setOnClickListener(this);
        bt_submit.setEnabled(false);
        bt_submit.setClickable(false);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.feed_back_app:
                if (!cb_app.isChecked())
                    cb_app.setChecked(true);
                if (cb_lyt.isChecked())
                    cb_lyt.setChecked(false);
                break;
            case R.id.feed_back_lyt:
                if (cb_app.isChecked())
                    cb_app.setChecked(false);
                if (!cb_lyt.isChecked())
                    cb_lyt.setChecked(true);
                break;
            case R.id.bt_feedback_submit:
                mDialog = DialogUtil.createLoadingDialog(mContext, getString(R.string.request_ing), false);
                mDialog.show();
                if (cb_lyt.isChecked()) {//对旅游团的反馈
                    new Thread(macRun).start();
                } else {
                    mac = "-1000";
                    new Thread(uploadingRun).start();
                }
                break;
        }
    }

    Runnable uploadingRun = new Runnable() {
        @Override
        public void run() {
            HashMap<String, String> map = new HashMap<>();
            Editable name = et_name.getText();
            Editable phone = et_phone.getText();
            Editable content = et_content.getText();
            if (!TextUtils.isEmpty(name.toString())) {
                map.put("username", name.toString());
            }
            if (!TextUtils.isEmpty(phone.toString())) {
                map.put("mobile", phone.toString());
            }
            if ("-1000".equals(mac))
                map.put("support", "1");//对APP建议
            else
                map.put("support", "2");//对旅游团建议
            map.put("info", content.toString());
            map.put("mac", mac);
            map.put("type", "1");
            JSONObject jsonObject = new JSONObject(map);
            LogUtil.d(TAG, "jsonObject:" + jsonObject);
            try {
                String json = OkHttpUtil.okPost(Interface.FEEDBACK_URL, map);
                LogUtil.d(TAG, "json:" + json);
                if (!TextUtils.isEmpty(json)) {
                    JSONObject jsonObject1 = new JSONObject(json);
                    String result = jsonObject1.getString("result");
                    if (!TextUtils.isEmpty(result) && result.equals("1")) {
                        mHandler.sendEmptyMessage(UPLOADING_WIN);
                    } else {
                        Message message = new Message();
                        message.obj = getString(R.string.feedback_error);
                        message.what = TOAST;
                        mHandler.sendMessage(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Message message = new Message();
                message.obj = getString(R.string.network_request_error);
                message.what = TOAST;
                mHandler.sendMessage(message);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                mDialog.dismiss();
            }

        }
    };
    /**
     * 获取mac地址
     */
    Runnable macRun = new Runnable() {
        @Override
        public void run() {
            try {
                String json = OkHttpUtil.okGet(Interface.URL + Interface.GET_MAC);
                LogUtil.d(TAG, " macRun json" + json);

                if (!TextUtils.isEmpty(json)) {
                    JSONObject jsonObject = new JSONObject(json);
                    String result = jsonObject.getString("result");
                    if (!TextUtils.isEmpty(result) && result.equals("1")) {
                        String mac = jsonObject.getString("item");
                        if (!TextUtils.isEmpty(mac)) {//保存mac值
                            sp.edit().putString(Constants.MAC_ADDRESS, mac).apply();
                        }
                    }
                }
            } catch (IOException e) {
                LogUtil.d(TAG, "macRun IOException");
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                mHandler.sendEmptyMessage(RESPONSE_MAC_WIN);
            }
        }
    };
}
