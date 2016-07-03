package com.lyk.ai_2048.ai;

import android.os.AsyncTask;
import android.util.Log;

import com.lyk.ai_2048.component.LogicNumberGrid;
import com.lyk.ai_2048.component.NumberGrid;

/**
 * Created by lyk on 3/7/16.
 */
public class MonteCarloAI {
    private static final String TAG = "MonteCarloAI";
    private NumberGrid numberGrid;
    private static final int GAMES_PER_MOVE = 100, TARGET_STEP = 4;
    private int currentStep;
    private RandomRun[] runs;

    public MonteCarloAI(NumberGrid numberGrid) {
        this.numberGrid = numberGrid;
        this.numberGrid.setMcAI(this);
    }

    public void getBestMove() {
        currentStep = 0;
        runs = new RandomRun[4];
        for(int i=0; i<4; i++){
            runs[i] = new RandomRun(i);
            runs[i].executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //runs[i].start();
        }
//        while(currentStep<TARGET_STEP){
//
//        }
//        float bestScore = 0.f;
//        int bestMove = -1;
//        for(int i=0; i<4; i++){
//            if(runs[i].getScore() >= bestScore){
//                bestScore = runs[i].getScore();
//                bestMove = runs[i].getMove();
//            }
//        }
//        Log.d(TAG, "random best score: "+bestScore);
//        switch (bestMove) {
//            case 0:
//                numberGrid.moveUp();
//                break;
//            case 1:
//                numberGrid.moveDown();
//                break;
//            case 2:
//                numberGrid.moveLeft();
//                break;
//            case 3:
//                numberGrid.moveRight();
//                break;
//            default:
//                Log.d(TAG, "no best move!");
//                break;
//        }
    }

    private void performMove(){
        if(currentStep >= TARGET_STEP){
            currentStep = 0;
            float bestScore = 0.f;
            int bestMove = -1;
            for(int i=0; i<4; i++){
                if(runs[i].getScore() >= bestScore){
                    bestScore = runs[i].getScore();
                    bestMove = runs[i].getMove();
                }
            }
            Log.d(TAG, "random best score: "+bestScore);
            switch (bestMove) {
                case 0:
                    numberGrid.moveUp();
                    break;
                case 1:
                    numberGrid.moveDown();
                    break;
                case 2:
                    numberGrid.moveLeft();
                    break;
                case 3:
                    numberGrid.moveRight();
                    break;
                default:
                    Log.d(TAG, "no best move!");
                    break;
            }
        }
    }

    private class RandomRun extends AsyncTask<Void, Void, Void> {

        private int move;
        private float score;
        private LogicNumberGrid tempNumberGrid;

        public RandomRun(int move){
            this.move = move;
            tempNumberGrid = numberGrid.getLogicBoard();
        }

        public int getMove() {
            return move;
        }

        public float getScore() {
            return score;
        }

        @Override
        protected Void doInBackground(Void... params) {
            score = 0;
            for(int i = 0; i<GAMES_PER_MOVE; i++){
                score += tempNumberGrid.fakeRunTillOver(move);
            }
            score/= GAMES_PER_MOVE;

            synchronized (this){
                currentStep++;
            }


            //performMove();
            Log.d(TAG, "finished random run: "+move);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            performMove();
        }
    }
}