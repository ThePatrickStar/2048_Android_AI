package com.lyk.ai_2048.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lyk.ai_2048.R;
import com.vstechlab.easyfonts.EasyFonts;

/**
 * Created by lyk on 6/7/16.
 */
public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_help);

        setUpButtons();
        setTextTypeface();
    }

    private void setUpButtons(){
        ImageButton ibClose = (ImageButton) findViewById(R.id.ib_close);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpActivity.this.finish();
            }
        });
    }

    private void setTextTypeface(){
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setTypeface(EasyFonts.caviarDreamsBold(this));
    }
}
