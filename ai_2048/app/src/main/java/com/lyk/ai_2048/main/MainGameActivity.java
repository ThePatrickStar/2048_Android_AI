package com.lyk.ai_2048.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.lyk.ai_2048.R;
import com.lyk.ai_2048.ai.MonteCarloAI;
import com.lyk.ai_2048.base.GameHolder;
import com.lyk.ai_2048.component.Grid;
import com.lyk.ai_2048.component.NumberGrid;
import com.lyk.ai_2048.component.TouchLayer;
import com.lyk.ai_2048.util.Config;
import com.lyk.ai_2048.util.InfoHolder;
import com.lyk.ai_2048.util.OnSwipeTouchListener;
import com.vstechlab.easyfonts.EasyFonts;

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

    private ImageButton ibAI, ibRefresh, ibUndo, ibLike, ibHelp;

    private MonteCarloAI mcAI;

    private TouchLayer touchLayer;

    private TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_game);

        setUpInfo();

        setUpUIComponents();

        setTextTypeface();


    }

    @Override
    protected void onPause() {
        if(numberGrid.getAiMode()) {
            stopAI();
        }
        super.onPause();
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
        touchLayer = new TouchLayer(this);
        touchLayer.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeTop() {
                Log.d(TAG, "swiped top");
                numberGrid.moveUp();
            }
            public void onSwipeRight() {
                Log.d(TAG, "swiped right");
                numberGrid.moveRight();
            }
            public void onSwipeLeft() {
                Log.d(TAG, "swiped left");
                numberGrid.moveLeft();
            }
            public void onSwipeBottom() {
                Log.d(TAG, "swiped bottom");
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

        mcAI = new MonteCarloAI(numberGrid);



        int deviceSize;
        if(InfoHolder.getDeviceX() > InfoHolder.getDeviceY())
            deviceSize = InfoHolder.getDeviceY();
        else
            deviceSize = InfoHolder.getDeviceX();
        int sideBtnMargin = (deviceSize - InfoHolder.getGridSize())/2;

        ibRefresh = (ImageButton) findViewById(R.id.ib_refresh);
        RelativeLayout.LayoutParams ibRLayoutParams = (RelativeLayout.LayoutParams) ibRefresh.getLayoutParams();

        ibRLayoutParams.setMarginStart(sideBtnMargin);
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

        ibUndo = (ImageButton) findViewById(R.id.ib_undo);
        RelativeLayout.LayoutParams ibULayoutParams = (RelativeLayout.LayoutParams) ibUndo.getLayoutParams();

        ibULayoutParams.setMarginEnd(sideBtnMargin);

        ibUndo.setLayoutParams(ibULayoutParams);

        ibUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberGrid.revertOneStep();
            }
        });

        ibLike = (ImageButton) findViewById(R.id.ib_like);
        RelativeLayout.LayoutParams ibLLayoutParams = (RelativeLayout.LayoutParams) ibLike.getLayoutParams();

        ibLLayoutParams.setMarginStart(sideBtnMargin);

        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ibAI = (ImageButton) findViewById(R.id.ib_ai);
        RelativeLayout.LayoutParams ibAILayoutParams = (RelativeLayout.LayoutParams) ibAI.getLayoutParams();

        ibAILayoutParams.setMarginEnd(sideBtnMargin);

        ibAI.setLayoutParams(ibAILayoutParams);

        ibAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!numberGrid.getAiMode()){
                    startAI();
                }
                else{
                    stopAI();
                }
            }
        });

        ibHelp = (ImageButton) findViewById(R.id.ib_help);

        ibHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public void resetGame(){
        stopAI();
        score = 0;
        setScoreDisplay();
        numberGrid.init();
        numberGrid.generateNumber();
        numberGrid.generateNumber();
    }

    private void setTextTypeface(){
        TextView tvScoreTag = (TextView) findViewById(R.id.tv_score_tag);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        tvScoreTag.setTypeface(EasyFonts.caviarDreamsBold(this));
        tvTitle.setTypeface(EasyFonts.caviarDreamsBold(this));


    }


    private void setScoreDisplay(){
        TextView tvScore = (TextView) findViewById(R.id.tv_score);
        tvScore.setText(String.valueOf(score));
    }

    private void startAI(){
        touchLayer.setVisibility(View.GONE);
        ibAI.setImageResource(R.drawable.ic_pause_circle_outline_white_24dp);
        numberGrid.setAiMode(true);
        mcAI.getBestMove();
        float transX, transY;
        transX = ibHelp.getX() - ibAI.getX();
        transY = ibHelp.getY() - ibAI.getY();
        ViewAnimator.animate(ibRefresh, ibUndo, ibLike, ibHelp).alpha(1.f, 0.f).onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {
                ibRefresh.setVisibility(View.INVISIBLE);
                ibUndo.setVisibility(View.INVISIBLE);
                ibHelp.setVisibility(View.INVISIBLE);
                ibLike.setVisibility(View.INVISIBLE);
            }
        }).duration(Config.VIEW_FADE_DURATION)
                .andAnimate(tvTitle).alpha(1.f, 0.f, 1.f).duration(2*Config.VIEW_FADE_DURATION).onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {
                tvTitle.setText(R.string.title_ai_mode);
            }
        })
                .thenAnimate(ibAI).translationX(transX).translationY(transY).duration(Config.VIEW_MOVE_DURATION)
                .start();
    }

    private void stopAI(){
        touchLayer.setVisibility(View.VISIBLE);
        ibAI.setImageResource(R.drawable.ic_play_circle_outline_white_24dp);
        numberGrid.setAiMode(false);
        float transX, transY;
        transX = ibAI.getX() - ibHelp.getX();
        transY = ibAI.getY() - ibHelp.getY();

        ViewAnimator.animate(ibAI).translationX(transX).translationY(transY).duration(Config.VIEW_MOVE_DURATION)
                .andAnimate(tvTitle).alpha(1.f, 0.f, 1.f).duration(2*Config.VIEW_FADE_DURATION).onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {
                tvTitle.setText(R.string.title_player_mode);
            }
        })
                .thenAnimate(ibRefresh, ibUndo, ibLike, ibHelp).alpha(0.f, 1.f).onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {
                ibRefresh.setVisibility(View.VISIBLE);
                ibUndo.setVisibility(View.VISIBLE);
                ibLike.setVisibility(View.VISIBLE);
                ibHelp.setVisibility(View.VISIBLE);
            }
        }).duration(Config.VIEW_FADE_DURATION).start();
    }


    @Override
    public void updateScore(int score) {
        this.score = score;
        setScoreDisplay();
    }
}
