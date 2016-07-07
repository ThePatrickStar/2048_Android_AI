package com.lyk.ai_2048.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.lyk.ai_2048.R;
import com.lyk.ai_2048.util.Config;
import com.lyk.ai_2048.util.PrefUtil;
import com.vstechlab.easyfonts.EasyFonts;

/**
 * Created by lyk on 6/7/16.
 */
public class SettingsActivity extends AppCompatActivity {

    private AdView adView;

    private static final String APP_PNAME = "com.lyk.ai_2048";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_settings);
        setTextTypeface();
        setUpButtons();

        setUpAdView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();;
        }
    }

    @Override
    protected void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    private void setUpButtons(){
        ImageButton ibClose = (ImageButton) findViewById(R.id.ib_close);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });
        final ImageButton ibAIDepth = (ImageButton) findViewById(R.id.ib_ai_depth);
        if(Config.isAi2Steps()){
            ibAIDepth.setImageResource(R.drawable.ic_check_box_black_24dp);
        }
        else{
            ibAIDepth.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
        }
        ibAIDepth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Config.isAi2Steps()){
                    Config.setAi2Steps(false);
                    PrefUtil.setBooleanPreference(PrefUtil.AI_2_STEP, false, getApplicationContext());
                    ibAIDepth.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                }
                else{
                    Config.setAi2Steps(true);
                    PrefUtil.setBooleanPreference(PrefUtil.AI_2_STEP, true, getApplicationContext());
                    ibAIDepth.setImageResource(R.drawable.ic_check_box_black_24dp);
                }
            }
        });

        final ImageButton ibSlowAnim = (ImageButton) findViewById(R.id.ib_slow_animation);
        final ImageButton ibNormalAnim = (ImageButton) findViewById(R.id.ib_normal_animation);
        final ImageButton ibFastAnim = (ImageButton) findViewById(R.id.ib_fast_animation);

        float speedFactor = Config.getSpeedFactor();

        if(speedFactor == 0.5){
            ibFastAnim.setImageResource(R.drawable.ic_radio_button_checked_black_24dp);
        }
        else if(speedFactor == 1.5){
            ibSlowAnim.setImageResource(R.drawable.ic_radio_button_checked_black_24dp);
        }
        else{
            ibNormalAnim.setImageResource(R.drawable.ic_radio_button_checked_black_24dp);
        }

        ibSlowAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibSlowAnim.setImageResource(R.drawable.ic_radio_button_checked_black_24dp);
                ibNormalAnim.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                ibFastAnim.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                Config.setSpeedFactor((float)1.5);
            }
        });
        ibNormalAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibSlowAnim.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                ibNormalAnim.setImageResource(R.drawable.ic_radio_button_checked_black_24dp);
                ibFastAnim.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                Config.setSpeedFactor(1.f);
            }
        });
        ibFastAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibSlowAnim.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                ibNormalAnim.setImageResource(R.drawable.ic_radio_button_unchecked_black_24dp);
                ibFastAnim.setImageResource(R.drawable.ic_radio_button_checked_black_24dp);
                Config.setSpeedFactor((float)0.5);
            }
        });

        ImageButton ibRateApp = (ImageButton) findViewById(R.id.ib_rate);
        ibRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+APP_PNAME)));
            }
        });
    }

    private void setTextTypeface(){
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        TextView tvAIDepth = (TextView) findViewById(R.id.tv_ai_depth);
        TextView tvSlowAnim = (TextView) findViewById(R.id.tv_slow_animation);
        TextView tvNormalAnim = (TextView) findViewById(R.id.tv_normal_animation);
        TextView tvFastAnim = (TextView) findViewById(R.id.tv_fast_animation);
        TextView tvRateApp = (TextView) findViewById(R.id.tv_rate);

        tvTitle.setTypeface(EasyFonts.caviarDreamsBold(this));
        tvAIDepth.setTypeface(EasyFonts.caviarDreamsBold(this));
        tvSlowAnim.setTypeface(EasyFonts.caviarDreamsBold(this));
        tvNormalAnim.setTypeface(EasyFonts.caviarDreamsBold(this));
        tvFastAnim.setTypeface(EasyFonts.caviarDreamsBold(this));
        tvRateApp.setTypeface(EasyFonts.caviarDreamsBold(this));
    }

    private void setUpAdView(){
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("0782AD5F24AC63BA045110CEBC213342")
                .build();
        adView.loadAd(adRequest);
    }
}
