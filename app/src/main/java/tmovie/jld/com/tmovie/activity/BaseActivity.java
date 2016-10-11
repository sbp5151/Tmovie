package tmovie.jld.com.tmovie.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import tmovie.jld.com.tmovie.R;
import tmovie.jld.com.tmovie.util.DensityUtil;
import tmovie.jld.com.tmovie.util.LogUtil;

/**
 * 项目名称：Tmovie
 * 晶凌达科技有限公司所有，
 * 受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。
 *
 * @creator boping
 * @create-time 2016/8/29 17:08
 */
public class BaseActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, View.OnTouchListener {

    public static final String TAG = "BaseActivity";
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        mGestureDetector = new GestureDetector(this);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        LogUtil.d(TAG, "onTouch");
        return mGestureDetector.onTouchEvent(event);
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }
    @Override
    public void onShowPress(MotionEvent e) {
    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        LogUtil.d(TAG, "onFling");

        int verticalMinDistance = DensityUtil.dip2px(this, 120);
        int minVelocity = DensityUtil.dip2px(this, 400);
        if (e1 == null || e2 == null)
            return false;
        if (e1.getY() - e2.getY() > DensityUtil.dip2px(this, 100)) {
            LogUtil.d(TAG, "向上手势");
        } else if (e2.getY() - e1.getY() > DensityUtil.dip2px(this, 100)) {
            LogUtil.d(TAG, "向下手势");

        } else if (e1.getX() - e2.getX() > verticalMinDistance
                && Math.abs(velocityX) > minVelocity) {
            LogUtil.d(TAG, "向左手势");
        } else if (e2.getX() - e1.getX() > verticalMinDistance
                && Math.abs(velocityX) > minVelocity) {
            LogUtil.d(TAG, "向右手势");
            this.finish();
            if(mInterface!=null)
                mInterface.onFling();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
        return false;
    }
    FlingInterface mInterface;
    public void setFlingInterface(FlingInterface mInterface){
        this.mInterface = mInterface;
    }
    interface FlingInterface {
        void onFling();
    }
}
