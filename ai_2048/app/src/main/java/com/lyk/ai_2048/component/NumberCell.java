package com.lyk.ai_2048.component;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
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

    public static int getCellBg(int number){
        switch(number){
            case 0:
                return R.drawable.bg_0;
            case 2:
                return R.drawable.bg_2;
            case 4:
                return R.drawable.bg_4;
            case 8:
                return R.drawable.bg_8;
            case 16:
                return R.drawable.bg_16;
            case 32:
                return R.drawable.bg_32;
            case 64:
                return R.drawable.bg_64;
            case 128:
                return R.drawable.bg_128;
            case 256:
                return R.drawable.bg_256;
            case 512:
                return R.drawable.bg_512;
            case 1024:
                return R.drawable.bg_1024;
            case 2048:
                return R.drawable.bg_2048;
            case 4096:
                return R.drawable.bg_4096;
            case 8192:
                return R.drawable.bg_8192;
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
            this.setTextColor(ContextCompat.getColor(getContext(),R.color.md_white_1000));
            if(number < 1000){
                if(number <= 4){
                    this.setTextColor(ContextCompat.getColor(getContext(),R.color.color_special_number));
                }
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