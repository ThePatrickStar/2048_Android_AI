package com.lyk.ai_2048.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.lyk.ai_2048.R;
import com.lyk.ai_2048.util.InfoHolder;

import java.util.ArrayList;

/**
 * Created by lyk on 22/6/16.
 */
public class Grid extends GridLayout {

    private static final String TAG = "Grid";

    private ArrayList<Cell> cells;

    private Context context;

    public Grid(Context context) {
        super(context);
        this.context = context;
        setUpDisplay();
    }

    public Grid(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpDisplay();
    }

    public Grid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpDisplay();
    }

    //this grid must be put inside a relative layout
    private void setUpDisplay(){
        int padding = InfoHolder.getGridPadding();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(InfoHolder.getGridSize(),InfoHolder.getGridSize());
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        this.setLayoutParams(params);
        this.setBackgroundResource(R.drawable.bg_grid);

        //this.setPadding(padding, padding, padding, padding);
        this.setColumnCount(4);
        this.setRowCount(4);

        cells = new ArrayList<>();
        for (int i=0; i<4; i++){
            for (int j=0; j<4; j++){
                Cell cell = new Cell(context);

                cell.setBackgroundResource(R.drawable.bg_cell);

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
}
