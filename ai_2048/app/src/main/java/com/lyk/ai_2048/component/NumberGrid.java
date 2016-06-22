package com.lyk.ai_2048.component;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.github.florent37.viewanimator.ViewAnimator;
import com.lyk.ai_2048.R;
import com.lyk.ai_2048.util.InfoHolder;

import java.util.ArrayList;

/**
 * Created by lyk on 22/6/16.
 */
public class NumberGrid extends GridLayout {

    private static final String TAG = "NumberGrid";

    private ArrayList<NumberCell> cells;

    private Context context;

    public NumberGrid(Context context) {
        super(context);
        this.context = context;
        setUpDisplay();
    }

    public NumberGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpDisplay();
    }

    public NumberGrid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpDisplay();
    }

    //this grid must be put inside a relative layout
    private void setUpDisplay(){
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(InfoHolder.getGridSize(),InfoHolder.getGridSize());
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        this.setLayoutParams(params);
        this.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
        this.setColumnCount(4);
        this.setRowCount(4);

        initCells();
    }

    private void initCells(){
        cells = new ArrayList<>();

        int padding = InfoHolder.getGridPadding();
        for (int i=0; i<4; i++){
            for (int j=0; j<4; j++){

                NumberCell cell = new NumberCell(context);

                cell.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));

                GridLayout.LayoutParams gLayoutParams = new GridLayout.LayoutParams(GridLayout.spec(i, GridLayout.CENTER),
                        GridLayout.spec(j, GridLayout.CENTER));

                gLayoutParams.height = InfoHolder.getCellSize();
                gLayoutParams.width = InfoHolder.getCellSize();


                gLayoutParams.leftMargin = padding;
                gLayoutParams.topMargin = padding;

                if (i == 3)
                    gLayoutParams.bottomMargin = padding;
                if (j == 3)
                    gLayoutParams.rightMargin = padding;

                cell.setLayoutParams(gLayoutParams);

                this.addView(cell);

                cells.add(cell);
            }
        }
    }

    public void resetDisplay(){
        this.removeAllViews();
        initCells();
    }

    public void generateNumber(){
        ArrayList<NumberCell> emptyCells = new ArrayList<>();
        for (NumberCell cell : cells){
            if (cell.getNumber() == 0){
                emptyCells.add(cell);
            }
        }
        if (emptyCells.size()==0){
            return;
        }
        else{
            int position = (int) (Math.random()*emptyCells.size());
            NumberCell cell = emptyCells.get(position);

            int randNum = Math.random() < 0.5 ? 2:4;

            cell.setNumber(randNum);


            ViewAnimator.animate(cell).backgroundColor(
                    ContextCompat.getColor(context,android.R.color.transparent),
                    ContextCompat.getColor(context,NumberCell.getCellColor(cell.getNumber())))
                    .scale(0, 1).duration(50).start();

        }
    }
}
