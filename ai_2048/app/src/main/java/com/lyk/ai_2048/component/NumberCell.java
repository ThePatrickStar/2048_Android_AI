package com.lyk.ai_2048.component;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.lyk.ai_2048.R;

/**
 * Created by lyk on 22/6/16.
 */
public class NumberCell extends TextView {
    private int number;

    public NumberCell(Context context) {
        super(context);
        number = 0;
        this.setGravity(Gravity.CENTER);
        this.setTextColor(ContextCompat.getColor(context, R.color.md_white_1000));

        this.setTypeface(null, Typeface.BOLD);
    }

    public static int getCellColor(int number){
        switch(number){
            case 0:
                return android.R.color.transparent;
            case 2:
                return R.color.md_amber_300;
            case 4:
                return R.color.md_amber_600;
            case 8:
                return R.color.md_orange_500;
            case 16:
                return R.color.md_orange_A700;
            case 32:
                return R.color.md_deep_orange_500;
            case 64:
                return R.color.md_deep_orange_A700;
            case 128:
                return R.color.md_purple_500;
            case 256:
                return R.color.md_indigo_500;
            case 512:
                return R.color.md_blue_700;
            case 1024:
                return R.color.md_blue_300;
            case 2048:
                return R.color.md_blue_grey_500;
            default:
                return R.color.md_black_1000;
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        if (number != 0){
            this.setText(String.valueOf(number));
            if(number < 1000){
                this.setTextSize(35);
            }
            else if (number < 10000){
                this.setTextSize(25);
            }
            else{
                this.setTextSize(15);
            }
        }
    }
}