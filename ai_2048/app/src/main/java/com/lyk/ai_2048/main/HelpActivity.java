package com.lyk.ai_2048.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.lyk.ai_2048.R;
import com.vstechlab.easyfonts.EasyFonts;

/**
 * Created by lyk on 6/7/16.
 */
public class HelpActivity extends AppCompatActivity {

    private AdView adView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_help);

        setUpButtons();
        setTextTypeface();

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
                HelpActivity.this.finish();
            }
        });
    }

    private void setTextTypeface(){
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setTypeface(EasyFonts.caviarDreamsBold(this));
    }

    private void setUpAdView(){
        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("0782AD5F24AC63BA045110CEBC213342")
                .build();
        adView.loadAd(adRequest);
    }
}
