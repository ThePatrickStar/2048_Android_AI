package com.lyk.ai_2048.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyk.ai_2048.R;
import com.lyk.ai_2048.base.GameHolder;
import com.lyk.ai_2048.component.Grid;
import com.lyk.ai_2048.component.NumberGrid;
import com.lyk.ai_2048.component.TouchLayer;
import com.lyk.ai_2048.util.InfoHolder;
import com.lyk.ai_2048.util.OnSwipeTouchListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by lyk on 22/6/16.
 */
public class MainGameActivity extends AppCompatActivity implements GameHolder {
    private static final String TAG = "MainGameActivity";

    private Grid grid;
    private NumberGrid numberGrid;

    private int score = 0;

    private SweetAlertDialog sDialog;

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
        numberGrid = new NumberGrid(this);
        numberGrid.setGameHolder(this);
        TouchLayer touchLayer = new TouchLayer(this);
        touchLayer.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeTop() {
                Log.d(TAG, "swiped top");
                numberGrid.saveState();
                numberGrid.moveUp();
            }
            public void onSwipeRight() {
                Log.d(TAG, "swiped right");
                numberGrid.saveState();
                numberGrid.moveRight();
            }
            public void onSwipeLeft() {
                Log.d(TAG, "swiped left");
                numberGrid.saveState();
                numberGrid.moveLeft();
            }
            public void onSwipeBottom() {
                Log.d(TAG, "swiped bottom");
                numberGrid.saveState();
                numberGrid.moveDown();
            }
        });

        if (rl != null) {
            rl.addView(grid);
            rl.addView(numberGrid);
            rl.addView(touchLayer);
        }
        else {
            Log.d(TAG,"ll_main is null!");
        }


        numberGrid.generateNumber();
        numberGrid.generateNumber();

        ImageButton ibRefresh = (ImageButton) findViewById(R.id.ib_refresh);
        RelativeLayout.LayoutParams ibRLayoutParams = (RelativeLayout.LayoutParams) ibRefresh.getLayoutParams();
        int deviceSize;
        if(InfoHolder.getDeviceX() > InfoHolder.getDeviceY())
            deviceSize = InfoHolder.getDeviceY();
        else
            deviceSize = InfoHolder.getDeviceX();

        ibRLayoutParams.setMarginStart((deviceSize - InfoHolder.getGridSize())/2);
        ibRefresh.setLayoutParams(ibRLayoutParams);

        ibRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDialog = new SweetAlertDialog(MainGameActivity.this, SweetAlertDialog.WARNING_TYPE);

                sDialog.setTitleText(getResources().getString(R.string.title_new_game));
                sDialog.setContentText(getResources().getString(R.string.info_new_game));
                sDialog.setConfirmText(getResources().getString(R.string.btn_confirm));
                sDialog.setCancelText(getResources().getString(R.string.btn_cancel));
                sDialog.setCancelable(false);

                sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sDialog.dismissWithAnimation();
                    }
                });

                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        resetGame();
                        sDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        sDialog.setContentText(getResources().getString(R.string.info_game_reseted));
                        sDialog.showCancelButton(false);
                        sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        });
                    }
                });
                sDialog.show();
            }
        });

        ImageButton ibUndo = (ImageButton) findViewById(R.id.ib_undo);
        RelativeLayout.LayoutParams ibULayoutParams = (RelativeLayout.LayoutParams) ibUndo.getLayoutParams();

        ibULayoutParams.setMarginEnd((deviceSize - InfoHolder.getGridSize())/2);

        ibUndo.setLayoutParams(ibULayoutParams);

        ibUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberGrid.revertOneStep();
            }
        });


    }

    @Override
    public void resetGame(){
        score = 0;
        setScoreDisplay();
        numberGrid.init();
        numberGrid.generateNumber();
        numberGrid.generateNumber();
    }

    private void setScoreDisplay(){
        TextView tvScore = (TextView) findViewById(R.id.tv_score);
        String scoreText = String.format(getResources().getString(R.string.tag_score), score);
        tvScore.setText(Html.fromHtml(scoreText), TextView.BufferType.SPANNABLE);
    }

    @Override
    public void updateScore(int score) {
        this.score = score;
        setScoreDisplay();
    }
}
