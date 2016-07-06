package com.lyk.ai_2048.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.lyk.ai_2048.util.PrefUtil;
import com.vstechlab.easyfonts.EasyFonts;

import org.w3c.dom.Text;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by lyk on 22/6/16.
 */
public class MainGameActivity extends AppCompatActivity implements GameHolder {
    private static final String TAG = "MainGameActivity";

    private Grid grid;
    private NumberGrid numberGrid;

    private SweetAlertDialog sDialog;

    private ImageButton ibAI, ibRefresh, ibUndo, ibSetting, ibHelp;

    private MonteCarloAI mcAI;

    private TouchLayer touchLayer;

    private TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Config.setAi2Steps(PrefUtil.getBooleanPreference(PrefUtil.AI_2_STEP, this));

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

    @Override
    protected void onStop() {
        numberGrid.saveBoard();
        super.onStop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        numberGrid.setUpCellPositions();
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

        setScoreDisplay(0, 0);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl_main);

        grid = new Grid(this);
        numberGrid = new NumberGrid(this, this);
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

        mcAI = new MonteCarloAI(numberGrid);



        int deviceSize;
        if(InfoHolder.getDeviceX() > InfoHolder.getDeviceY())
            deviceSize = InfoHolder.getDeviceY();
        else
            deviceSize = InfoHolder.getDeviceX();
        final int sideBtnMargin = (deviceSize - InfoHolder.getGridSize())/2;

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

                Button btnConfirm = (Button) sDialog.findViewById(cn.pedant.SweetAlert.R.id.confirm_button);
                Button btnCancel = (Button) sDialog.findViewById(cn.pedant.SweetAlert.R.id.cancel_button);
                TextView tvTitle = (TextView) sDialog.findViewById(cn.pedant.SweetAlert.R.id.title_text);
                TextView tvContent = (TextView) sDialog.findViewById(cn.pedant.SweetAlert.R.id.content_text);

                if(btnConfirm!=null)
                    btnConfirm.setTypeface(EasyFonts.caviarDreamsBold(getApplicationContext()));
                if(btnCancel!=null)
                    btnCancel.setTypeface(EasyFonts.caviarDreamsBold(getApplicationContext()));
                if(tvTitle!=null)
                    tvTitle.setTypeface(EasyFonts.caviarDreamsBold(getApplicationContext()));
                if(tvContent!=null) {
                    tvContent.setTypeface(EasyFonts.caviarDreamsBold(getApplicationContext()));
                    Log.d(TAG, "tvContent is not null!");
                } else
                    Log.d(TAG, "tvContent is null!");
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

        ibSetting = (ImageButton) findViewById(R.id.ib_setting);
        RelativeLayout.LayoutParams ibLLayoutParams = (RelativeLayout.LayoutParams) ibSetting.getLayoutParams();

        ibLLayoutParams.setMarginStart(sideBtnMargin);

        ibSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainGameActivity.this, SettingsActivity.class));
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
                startActivity(new Intent(MainGameActivity.this, HelpActivity.class));
            }
        });


    }

    @Override
    public void resetGame(){
        stopAI();
        setScoreDisplay(0, 0);
        numberGrid.init();
    }

    private void setTextTypeface(){
        TextView tvScoreTag = (TextView) findViewById(R.id.tv_score_tag);
        TextView tvHighScoreTag = (TextView) findViewById(R.id.tv_high_score_tag);
        TextView tvScore = (TextView) findViewById(R.id.tv_score);
        TextView tvHighScore = (TextView) findViewById(R.id.tv_high_score);
        tvTitle = (TextView) findViewById(R.id.tv_title);

        tvScoreTag.setTypeface(EasyFonts.caviarDreamsBold(this));
        tvHighScoreTag.setTypeface(EasyFonts.caviarDreamsBold(this));
        tvScore.setTypeface(EasyFonts.caviarDreamsBold(this));
        tvHighScore.setTypeface(EasyFonts.caviarDreamsBold(this));
        tvTitle.setTypeface(EasyFonts.caviarDreamsBold(this));
    }


    private void setScoreDisplay(int scoreNew, int scoreOld){
        TextView tvScore = (TextView) findViewById(R.id.tv_score);
        tvScore.setText(String.valueOf(scoreNew));
        if(scoreNew > scoreOld){
            TextView tvScoreChange = (TextView) findViewById(R.id.tv_score_change);
            tvScoreChange.setText("+"+String.valueOf(scoreNew-scoreOld));
            ViewAnimator.animate(tvScoreChange).alpha(0.f, 1.f, 0.f).duration(2*Config.VIEW_FADE_DURATION).start();
        }
        else if(scoreNew < scoreOld){
            TextView tvScoreChange = (TextView) findViewById(R.id.tv_score_change);
            tvScoreChange.setText("-"+String.valueOf(scoreOld-scoreNew));
            ViewAnimator.animate(tvScoreChange).alpha(0.f, 1.f, 0.f).duration(2*Config.VIEW_FADE_DURATION).start();
        }
    }

    private void setHighScoreDisplay(int scoreNew, int scoreOld){
        TextView tvScore = (TextView) findViewById(R.id.tv_high_score);
        tvScore.setText(String.valueOf(scoreNew));
        if(scoreNew > scoreOld){
            TextView tvScoreChange = (TextView) findViewById(R.id.tv_high_score_change);
            tvScoreChange.setText("+"+String.valueOf(scoreNew-scoreOld));
            ViewAnimator.animate(tvScoreChange).alpha(0.f, 1.f, 0.f).duration(2*Config.VIEW_FADE_DURATION).start();
        }
        else if(scoreNew < scoreOld){
            TextView tvScoreChange = (TextView) findViewById(R.id.tv_high_score_change);
            tvScoreChange.setText("-"+String.valueOf(scoreOld-scoreNew));
            ViewAnimator.animate(tvScoreChange).alpha(0.f, 1.f, 0.f).duration(2*Config.VIEW_FADE_DURATION).start();
        }
    }

    private void startAI(){
        touchLayer.setVisibility(View.GONE);
        ibAI.setImageResource(R.drawable.ic_pause_circle_outline_white_24dp);
        numberGrid.setAiMode(true);
        mcAI.getBestMove();
        float transX, transY;
        transX = ibHelp.getX() - ibAI.getX();
        transY = ibHelp.getY() - ibAI.getY();
        ViewAnimator.animate(ibRefresh, ibUndo, ibSetting, ibHelp).alpha(1.f, 0.f).onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {
                ibRefresh.setVisibility(View.INVISIBLE);
                ibUndo.setVisibility(View.INVISIBLE);
                ibHelp.setVisibility(View.INVISIBLE);
                ibSetting.setVisibility(View.INVISIBLE);
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
        if(numberGrid.getAiMode()){
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
                    .thenAnimate(ibRefresh, ibUndo, ibSetting, ibHelp).alpha(0.f, 1.f).onStop(new AnimationListener.Stop() {
                @Override
                public void onStop() {
                    ibRefresh.setVisibility(View.VISIBLE);
                    ibUndo.setVisibility(View.VISIBLE);
                    ibSetting.setVisibility(View.VISIBLE);
                    ibHelp.setVisibility(View.VISIBLE);
                }
            }).duration(Config.VIEW_FADE_DURATION).start();
        }

    }


    @Override
    public void updateScore(int scoreNew, int scoreOld) {
        setScoreDisplay(scoreNew, scoreOld);
    }

    @Override
    public void updateHighScore(int scoreNew, int scoreOld) {
        setHighScoreDisplay(scoreNew, scoreOld);
    }
}
