package com.lyk.ai_2048.component;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.lyk.ai_2048.util.InfoHolder;

/**
 * Created by lyk on 23/6/16.
 */
public class TouchLayer extends View {
    public TouchLayer(Context context) {
        super(context);
        setUpDisplay();
    }

    //this grid must be put inside a relative layout
    private void setUpDisplay(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(InfoHolder.getGridSize(),InfoHolder.getGridSize());
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        this.setLayoutParams(params);
        this.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
    }
}
