package tmovie.jld.com.tmovie.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import tmovie.jld.com.tmovie.R;
import tmovie.jld.com.tmovie.view.CircularProgressDrawable;

public class MyPlay extends AppCompatActivity implements View.OnClickListener {

    private TextureView tv_play;
    private CircularProgressDrawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_my_play);
        Button bt_1 = (Button) findViewById(R.id.bt_1);
        bt_1.setOnClickListener(this);
        Button bt_2 = (Button) findViewById(R.id.bt_2);
        bt_2.setOnClickListener(this);
        Button bt_3 = (Button) findViewById(R.id.bt_3);
        bt_3.setOnClickListener(this);
        Button bt_4 = (Button) findViewById(R.id.bt_4);
        bt_4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.bt_1:
                break;
            case R.id.bt_2:
                break;
            case R.id.bt_3:
                break;
            case R.id.bt_4:
                break;
        }
    }
    private Animator prepareStyle1Animation() {
        AnimatorSet animation = new AnimatorSet();

        final Animator indeterminateAnimation = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.PROGRESS_PROPERTY, 0, 3600);
        indeterminateAnimation.setDuration(3600);

        Animator innerCircleAnimation = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.CIRCLE_SCALE_PROPERTY, 0f, 0.75f);
        innerCircleAnimation.setDuration(3600);
        innerCircleAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                drawable.setIndeterminate(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                indeterminateAnimation.end();
                drawable.setIndeterminate(false);
                drawable.setProgress(0);
            }
        });

        animation.playTogether(innerCircleAnimation, indeterminateAnimation);
        return animation;
    }
}
