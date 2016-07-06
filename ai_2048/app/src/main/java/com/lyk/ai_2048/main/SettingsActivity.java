package com.lyk.ai_2048.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lyk.ai_2048.R;
import com.lyk.ai_2048.util.Config;
import com.lyk.ai_2048.util.PrefUtil;
import com.vstechlab.easyfonts.EasyFonts;

/**
 * Created by lyk on 6/7/16.
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_settings);
        setTextTypeface();
        setUpButtons();
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
    }

    private void setTextTypeface(){
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        TextView tvAIDepth = (TextView) findViewById(R.id.tv_ai_depth);
        tvTitle.setTypeface(EasyFonts.caviarDreamsBold(this));
        tvAIDepth.setTypeface(EasyFonts.caviarDreamsBold(this));
    }
}
