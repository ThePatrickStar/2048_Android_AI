package com.lyk.ai_2048.component;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.lyk.ai_2048.R;
import com.lyk.ai_2048.ai.MonteCarloAI;
import com.lyk.ai_2048.base.GameHolder;
import com.lyk.ai_2048.util.BoardUtil;
import com.lyk.ai_2048.util.Config;
import com.lyk.ai_2048.util.InfoHolder;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by lyk on 22/6/16.
 */
public class NumberGrid extends GridLayout {

    private static final String TAG = "NumberGrid";

    private ArrayList<NumberCell> cells;
    private int[][] board;
    private int[][] revertBoard;
    private boolean[][] hasConflicted;
    private int score;
    private int revertScore;
    private int secondMove = -1;

    private GameHolder gameHolder;

    //for the move animation listener
    private int currentStep;

    private SweetAlertDialog sDialog;

    private boolean revertible, aiMode = false;

    private MonteCarloAI mcAI;


    public NumberGrid(Context context) {
        super(context);
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
        this.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        this.setColumnCount(4);
        this.setRowCount(4);

        init();
    }

    public void init(){
        revertBoard = new int[4][4];
        board = new int[4][4];
        hasConflicted = new boolean[4][4];
        score = 0;
        revertScore = 0;
        revertible = false;

        for (int i=0; i<4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = 0;
            }
        }

        updateCells();
    }

    public void revertOneStep(){
        if(revertible){
            revertible = false;
            gameHolder.updateScore(revertScore, score);
            score = revertScore;
            for(int i=0; i<4; i++){
                for(int j=0; j<4; j++){
                    board[i][j] = revertBoard[i][j];
                }
            }
            updateCells();
        }
    }

    private void saveState(){
        revertScore = score;
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                revertBoard[i][j] = board[i][j];
            }
        }
    }

    private void updateCells(){
        this.removeAllViews();

        cells = new ArrayList<>();

        int padding = InfoHolder.getGridPadding();
        for (int i=0; i<4; i++){
            for (int j=0; j<4; j++){

                NumberCell cell = new NumberCell(getContext());

                cell.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));

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

                cell.setRow(i);
                cell.setCol(j);


                cell.setNumber(board[i][j]);

                this.addView(cell);

                cells.add(cell);
                hasConflicted[i][j] = false;
            }
        }

        //Log.d(TAG, "updated cells");

    }

    public void moveLeft(){
        ArrayList<NumberCell> startCells = new ArrayList<>();
        ArrayList<NumberCell> endCells = new ArrayList<>();
        ArrayList<Integer> mergedCells = new ArrayList<>();
        if(!BoardUtil.canMoveLeft(this.board))
            return;


        saveState();

        for(int i=0; i<4; i++){
            for(int j=1; j<4; j++){
                if(board[i][j]!=0){
                    for(int k=0; k<j; k++){
                        if(board[i][k] == 0 && BoardUtil.noBlockHorizontal(this.board, i, k, j)){
                            //Log.d(TAG, "moving left ...");
                            startCells.add(cells.get(4*i+j));
                            endCells.add(cells.get(4*i+k));
                            //showMoveAnimation(i, j, i, k);
                            board[i][k]=board[i][j];
                            board[i][j]=0;
                            break;
                        }
                        else if (board[i][k] == board[i][j] && BoardUtil.noBlockHorizontal(this.board, i, k, j) && !hasConflicted[i][k]){
                            //Log.d(TAG, "merging left ...");
                            startCells.add(cells.get(4*i+j));
                            endCells.add(cells.get(4*i+k));
                            mergedCells.add(4*i+k);
                            //showMoveAnimation(i, j, i, k);
                            board[i][k]+=board[i][j];
                            board[i][j]=0;
                            score += board[i][k];
                            hasConflicted[i][k] = true;
                            break;
                        }
                    }
                }
            }
        }

        gameHolder.updateScore(score, revertScore);
        showMoveAnimations(startCells, endCells, mergedCells);
    }

    public void moveRight(){
        ArrayList<NumberCell> startCells = new ArrayList<>();
        ArrayList<NumberCell> endCells = new ArrayList<>();
        ArrayList<Integer> mergedCells = new ArrayList<>();
        if(!BoardUtil.canMoveRight(this.board))
            return;


        saveState();

        for( int i = 0; i < 4 ; i ++ ){
            for( int j = 2; j >= 0; j -- ){
                if( board[i][j] != 0 ){
                    for(int k = 3; k > j; k--){
                        if( board[i][k] == 0 && BoardUtil.noBlockHorizontal(this.board, i, j, k) ){
                            //Log.d(TAG, "moving right ...");
                            startCells.add(cells.get(4*i+j));
                            endCells.add(cells.get(4*i+k));
                            board[i][k] = board[i][j];
                            board[i][j] = 0;
                            break;
                        }
                        else if( board[i][k] == board[i][j] && BoardUtil.noBlockHorizontal(this.board, i, j, k) && !hasConflicted[i][k] ){
                            //Log.d(TAG, "merging right ...");
                            startCells.add(cells.get(4*i+j));
                            endCells.add(cells.get(4*i+k));
                            mergedCells.add(4*i+k);
                            board[i][k] *= 2;
                            board[i][j] = 0;
                            score += board[i][k];
                            hasConflicted[i][k] = true;
                            break;
                        }
                    }
                }
            }
        }

        gameHolder.updateScore(score, revertScore);
        showMoveAnimations(startCells, endCells, mergedCells);
    }

    public void moveUp(){
        ArrayList<NumberCell> startCells = new ArrayList<>();
        ArrayList<NumberCell> endCells = new ArrayList<>();
        ArrayList<Integer> mergedCells = new ArrayList<>();
        if(!BoardUtil.canMoveUp(this.board))
            return;


        saveState();

        for(int j = 0 ; j < 4 ; j ++ ) {
            for (int i = 1; i < 4; i++) {
                if (board[i][j] != 0) {
                    for (int k = 0; k < i; k++) {
                        if (board[k][j] == 0 && BoardUtil.noBlockVertical(this.board, j, k, i)) {
                            //Log.d(TAG, "moving up ...");
                            startCells.add(cells.get(4*i+j));
                            endCells.add(cells.get(4*k+j));
                            board[k][j] = board[i][j];
                            board[i][j] = 0;
                            break;
                        } else if (board[k][j] == board[i][j] && BoardUtil.noBlockVertical(this.board, j, k, i) && !hasConflicted[k][j]) {
                            //Log.d(TAG, "merging up ...");
                            startCells.add(cells.get(4*i+j));
                            endCells.add(cells.get(4*k+j));
                            mergedCells.add(4*k+j);
                            board[k][j] *= 2;
                            board[i][j] = 0;
                            score += board[k][j];
                            hasConflicted[k][j] = true;
                            break;
                        }
                    }

                }
            }
        }

        gameHolder.updateScore(score, revertScore);
        showMoveAnimations(startCells, endCells, mergedCells);
    }

    public void moveDown(){
        ArrayList<NumberCell> startCells = new ArrayList<>();
        ArrayList<NumberCell> endCells = new ArrayList<>();
        ArrayList<Integer> mergedCells = new ArrayList<>();
        if(!BoardUtil.canMoveDown(this.board))
            return;


        saveState();

        for(int j = 0; j < 4; j++) {
            for (int i = 2; i >= 0; i--) {
                if (board[i][j] != 0) {
                    for (int k = 3; k > i; k--) {
                        if (board[k][j] == 0 && BoardUtil.noBlockVertical(this.board, j, i, k)) {
                            //Log.d(TAG, "moving down ...");
                            startCells.add(cells.get(4*i+j));
                            endCells.add(cells.get(4*k+j));
                            board[k][j] = board[i][j];
                            board[i][j] = 0;
                            break;
                        } else if (board[k][j] == board[i][j] && BoardUtil.noBlockVertical(this.board, j, i, k) && !hasConflicted[k][j]) {
                            //Log.d(TAG, "merging down ...");
                            startCells.add(cells.get(4*i+j));
                            endCells.add(cells.get(4*k+j));
                            mergedCells.add(4*k+j);
                            board[k][j] *= 2;
                            board[i][j] = 0;
                            score += board[k][j];
                            hasConflicted[k][j] = true;
                            break;
                        }
                    }
                }
            }
        }

        gameHolder.updateScore(score, revertScore);
        showMoveAnimations(startCells, endCells, mergedCells);
    }

    private void showMoveAnimations(ArrayList<NumberCell> startCells, ArrayList<NumberCell> endCells, final ArrayList<Integer> mergedCells){
        currentStep = 0;
        final int targetStep = startCells.size();
        for (int i = 0; i<targetStep; i++){
            float transX = endCells.get(i).getX() - startCells.get(i).getX();
            float transY = endCells.get(i).getY() - startCells.get(i).getY();

            ViewAnimator.animate(startCells.get(i)).translationX(transX).translationY(transY)
                    .duration(Config.MOVE_DURATION).onStop(new AnimationListener.Stop() {
                @Override
                public void onStop() {
                    synchronized (this) {
                        currentStep++;
                        doAfterMove(currentStep, targetStep, mergedCells);
                    }

                }
            }).start();
        }
    }

    private void doAfterMove(int currentStep, int targetStep, ArrayList<Integer> mergedCells){
        if(currentStep >= targetStep){
            revertible = true;
            updateCells();
            if(mergedCells.size()>0){
                animateMerge(mergedCells);
            }
            else{
                generateNumber();
            }
        }
    }

    private void animateMerge(ArrayList<Integer> mergedCells){
        currentStep = 0;
        final int targetStep = mergedCells.size();
        for (Integer i : mergedCells){
            NumberCell mergedCell = cells.get(i);
            ViewAnimator.animate(mergedCell).scale(1.f, (float) 1.3, 1.f)
                    .duration(Config.MERGE_DURATION).onStop(new AnimationListener.Stop() {
                @Override
                public void onStop() {
                    synchronized (this) {
                        currentStep++;
                        doAfterMerge(currentStep, targetStep);
                    }

                }
            }).start();
        }
    }

    private void doAfterMerge(int currentStep, int targetStep){
        if(currentStep >= targetStep){
            generateNumber();
        }
    }

    public void generateNumber(){
        ArrayList<NumberCell> emptyCells = new ArrayList<>();
        for (NumberCell cell : cells){
            if (cell.getNumber() == 0){
                emptyCells.add(cell);
            }
        }
        if (emptyCells.size()==0){
            Log.d(TAG, "no more available cells");
        }
        else{

            int position = (int) (Math.random()*emptyCells.size());

            NumberCell cell = emptyCells.get(position);


            int randNum = Math.random() < 0.9 ? 2:4;

            cell.setNumber(randNum);

            board[cell.getRow()][cell.getCol()] = randNum;

            ViewAnimator.animate(cell).scale(0, 1).alpha(0, 1).duration(Config.GENERATE_DURATION).onStop(new AnimationListener.Stop() {
                @Override
                public void onStop() {

                    if(BoardUtil.isGameOver(board)){
                        sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);

                        sDialog.showCancelButton(false);
                        sDialog.setCancelable(false);
                        sDialog.setTitleText(getResources().getString(R.string.title_game_over));
                        sDialog.setContentText(getResources().getString(R.string.info_game_over));

                        sDialog.setConfirmText(getResources().getString(R.string.btn_confirm));
                        sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                gameHolder.resetGame();
                                sDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                sDialog.setContentText(getResources().getString(R.string.info_game_reseted));
                                sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                });
                            }
                        });

                        sDialog.show();
                    }
                    else if(aiMode){
                        // if no second move
                        if(secondMove < 0){
                            mcAI.getBestMove();
                        }
                        else{
                            Log.d("MonteCarloAI", "performing second move: "+secondMove);
                            performAMove(secondMove);
                            secondMove = -1;
                        }
                    }
                }
            }).start();

            //Log.d(TAG, "generating a number at : "+cell.getRow()+", "+cell.getCol()+" --- "+randNum);

        }
    }

    public void performBestMove(int bestMove){
        Log.d("MonteCarloAI", "performing best move: "+bestMove);
        if(bestMove < 4){
            performAMove(bestMove);
        }
        else{
            bestMove -= 4;
            performAMove(bestMove/4);
            this.secondMove = bestMove%4;
        }
    }

    private void performAMove(int move){
        switch (move) {
            case 0:
                this.moveUp();
                break;
            case 1:
                this.moveDown();
                break;
            case 2:
                this.moveLeft();
                break;
            case 3:
                this.moveRight();
                break;
            default:
                Log.d(TAG, "no best move!");
                break;
        }
    }

    public LogicNumberGrid getLogicBoard(){
        LogicNumberGrid logicBoard = new LogicNumberGrid();
        logicBoard.setScore(this.score);
        int[][] tempBoard = new int[4][4];
        boolean[][] tempHasConflicted = new boolean[4][4];

        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                tempBoard[i][j] = board[i][j];
                tempHasConflicted[i][j] = hasConflicted[i][j];
            }
        }
        logicBoard.setBoard(tempBoard);
        logicBoard.setHasConflicted(tempHasConflicted);
        return logicBoard;
    }

    public void setGameHolder(GameHolder gameHolder) {
        this.gameHolder = gameHolder;
    }

    public void setMcAI(MonteCarloAI mcAI){
        this.mcAI = mcAI;
    }

    public boolean getAiMode(){
        return this.aiMode;
    }

    public void setAiMode(boolean aiMode){
        this.aiMode = aiMode;
    }

}
