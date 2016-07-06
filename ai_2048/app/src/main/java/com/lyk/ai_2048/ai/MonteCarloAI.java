package com.lyk.ai_2048.ai;

import android.os.AsyncTask;
import android.util.Log;

import com.lyk.ai_2048.component.LogicNumberGrid;
import com.lyk.ai_2048.component.NumberGrid;
import com.lyk.ai_2048.util.Config;

/**
 * Created by lyk on 3/7/16.
 */
public class MonteCarloAI {
    private static final String TAG = "MonteCarloAI";
    private NumberGrid numberGrid;
    private static final int GAMES_PER_MOVE = 100;
    private int currentStep, targetStep;
    private RandomRun[] runs;

    public MonteCarloAI(NumberGrid numberGrid) {
        this.numberGrid = numberGrid;
        this.numberGrid.setMcAI(this);
    }

    public void getBestMove() {
        currentStep = 0;
//        runs = new RandomRun[4];
//        for(int i=0; i<4; i++){
//            runs[i] = new RandomRun(i);
//            runs[i].executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        }

        if(Config.isAi2Steps())
            targetStep = 20;
        else
            targetStep = 4;

        runs = new RandomRun[targetStep];
        for(int i=0; i<targetStep; i++){
            runs[i] = new RandomRun(i);
            runs[i].executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

    }

    private void performMove(){
//        Log.d(TAG, "performing move...");
        if(currentStep >= targetStep){
            currentStep = 0;
            float bestScore = 0.f, totalMoves = 0.f;
            int bestMove = -1;
            for(int i=0; i<targetStep; i++){
                totalMoves += runs[i].getMoves();
            }

            for(int j=0; j<targetStep; j++){
                if(runs[j].getMoves()>0){
                    float ucb1Score = (float) (runs[j].getScore() + Math.sqrt((2*Math.log(totalMoves))/runs[j].getMoves()) );
                    if(ucb1Score >= bestScore){
                        bestScore = ucb1Score;
                        bestMove = runs[j].getMove();
                    }
                }
            }

            Log.d(TAG, "random best score: "+bestScore);
            Log.d(TAG, "best move: "+bestMove);
            numberGrid.performBestMove(bestMove);
        }
    }

    private class RandomRun extends AsyncTask<Void, Void, Void> {

        private int move;
        private float score, moves;
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

        public float getMoves() {
            return moves;
        }

        @Override
        protected Void doInBackground(Void... params) {
            score = 0;
            for(int i = 0; i<GAMES_PER_MOVE; i++){
                int[] pair = tempNumberGrid.fakeRunTillOver(move);
                score += pair[0];
                moves += pair[1];
            }
            score/= GAMES_PER_MOVE;
            moves/= GAMES_PER_MOVE;

            synchronized (MonteCarloAI.this){
                currentStep++;
            }

//          Log.d(TAG, "finished random run: "+move);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            performMove();
        }
    }
}