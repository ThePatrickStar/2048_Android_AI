package com.lyk.ai_2048.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyk.ai_2048.R;
import com.lyk.ai_2048.component.Grid;
import com.lyk.ai_2048.util.InfoHolder;

/**
 * Created by lyk on 22/6/16.
 */
public class MainGameActivity extends AppCompatActivity{
    private static final String TAG = "MainGameActivity";

    private Grid grid;

    private int score = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_game);

        setUpInfo();

        setUpUIComponents();


    }

    private void setUpInfo(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        if (displaymetrics.widthPixels > displaymetrics.heightPixels){
            InfoHolder.setDeviceX(displaymetrics.heightPixels);
            InfoHolder.setDeviceY(displaymetrics.widthPixels);
        }
        else{
            InfoHolder.setDeviceX(displaymetrics.widthPixels);
            InfoHolder.setDeviceY(displaymetrics.heightPixels);
        }
    }

    private void setUpUIComponents(){

        setScoreDisplay();

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_main);

        grid = new Grid(this);

        if (rl != null) {
            rl.addView(grid);
        }
        else {
            Log.d(TAG,"ll_main is null!");
        }
    }

    private void setScoreDisplay(){
        TextView tvScore = (TextView) findViewById(R.id.tv_score);
        String scoreText = String.format(getResources().getString(R.string.tag_score), score);
        tvScore.setText(Html.fromHtml(scoreText), TextView.BufferType.SPANNABLE);
    }
}
