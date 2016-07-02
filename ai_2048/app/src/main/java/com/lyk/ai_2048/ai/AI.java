package com.lyk.ai_2048.ai;

import android.util.Log;

import com.lyk.ai_2048.component.NumberGrid;

import java.util.ArrayList;

/**
 * Created by lyk on 30/6/16.
 */
public class AI {

    private static final String TAG = "Artificial";
    private NumberGrid numberGrid;

    private static float EMPTY_BONUS = 10.f;
    //private static float CORNER_BONUS = 100.f;
    private static float MONOTONICITY_BONUS = 50.f;

    public AI(NumberGrid numberGrid){
        this.numberGrid = numberGrid;
        this.numberGrid.setAi(this);
    }

    private float getScoreForPossibleMatrixes(int[][] matrix) {

        float score = 0.f;
        int count = 0;

        //even id for 2, odd id for 4
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                if(matrix[i][j] == 0){
                    matrix[i][j] = 2;
                    //the chance of getting a tile with value 2 is 0.9
                    score += 0.9 * getScoreForMatrix(matrix);

                    matrix[i][j] = 4;
                    score += 0.1 * getScoreForMatrix(matrix);

                    //recover the change
                    matrix[i][j] = 0;

                    count++;

                }
            }
        }

        return score/count;

    }

    public void getBestMove(){
        //order down, left, up, right;
        float[] scores = new float[4];
        int highest = 0;
//        float scoreDown = 0.f, scoreLeft = 0.f, scoreUp = 0.f, scoreRight = 0.f;

        scores[0] = numberGrid.canMoveDown()?getScoreForPossibleMatrixes(numberGrid.fakeMoveDown()):-Float.MAX_VALUE;
        scores[1] = numberGrid.canMoveLeft()?getScoreForPossibleMatrixes(numberGrid.fakeMoveLeft()):-Float.MAX_VALUE;
        scores[2] = numberGrid.canMoveUp()?getScoreForPossibleMatrixes(numberGrid.fakeMoveUp()):-Float.MAX_VALUE;
        scores[3] = numberGrid.canMoveRight()?getScoreForPossibleMatrixes(numberGrid.fakeMoveRight()):-Float.MAX_VALUE;

        for (int i=0; i<4; i++){
            if(scores[i]>scores[highest]){
                highest = i;
            }
        }

        switch(highest){
            case 0:
                numberGrid.moveDown();
                Log.d(TAG, "ai move down");
                break;
            case 1:
                numberGrid.moveLeft();
                Log.d(TAG, "ai move left");
                break;
            case 2:
                numberGrid.moveUp();
                Log.d(TAG, "ai move up");
                break;
            case 3:
                numberGrid.moveRight();
                Log.d(TAG, "ai move right");
                break;
            default:
                Log.d(TAG, "no best move?!");
                break;
        }

//        if(scoreDown >= scoreLeft){
//            if(scoreDown >= scoreUp){
//                if(scoreDown >= scoreRight){
//                    numberGrid.moveDown();
//                }
//                else{
//                    numberGrid.moveRight();
//                }
//            }
//            else{
//                if(scoreUp >= scoreRight){
//                    numberGrid.moveUp();
//                }
//                else{
//                    numberGrid.moveRight();
//                }
//            }
//        }
//        else{
//            if(scoreLeft >= scoreUp){
//                if(scoreLeft >= scoreRight){
//                    numberGrid.moveLeft();
//                }
//                else{
//                    numberGrid.moveRight();
//                }
//            }
//            else{
//                if(scoreUp >= scoreRight){
//                    numberGrid.moveUp();
//                }
//                else{
//                    numberGrid.moveRight();
//                }
//            }
//
//        }
    }

    private float getScoreForMatrix(int[][] matrix){
        float score = 0.f;
        int biggestValue = 2, biggestX = -1, biggestY = -1;
        ArrayList<Integer> biggestXs, biggestYs;
        biggestXs = new ArrayList<>();
        biggestYs = new ArrayList<>();
        int[][] revertedMatrix = new int[4][4];


        // calculate the score for free tiles, setup the reverted matrix, get the biggest value info
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){




                //smoothness value
                if(i<3){
                    score -= Math.abs(matrix[i][j] - matrix[i+1][j]);
                }
                if(j<3){
                    score -= Math.abs(matrix[j][i] - matrix[j+1][i]);
                }

                revertedMatrix[j][i] = matrix[i][j];
                if(matrix[i][j] == 0){
                    score += EMPTY_BONUS;
                }
                //existing score
                else{
                    int log = log2(matrix[i][j]);
                    for(int k =1; k<=log; k++){
                        score += Math.pow(Math.pow(2, k), 2);
                    }
                }
                if(matrix[i][j] > biggestValue){
                    biggestXs.clear();
                    biggestYs.clear();
                    biggestXs.add(i);
                    biggestYs.add(j);
                }
                else if(matrix[i][j] == biggestValue){
                    biggestXs.add(i);
                    biggestYs.add(j);
                }
            }
        }

        for(int i=0; i<biggestXs.size(); i++){
            int position = biggestXs.get(i)*4 + biggestYs.get(i);
            if(position == 0 || position == 3 || position == 15 || position == 12){
                score += Math.pow(biggestValue, 2);
                biggestX = biggestXs.get(i);
                biggestY = biggestYs.get(i);
            }
        }

        //if biggest value is not at corner or edge
        if(biggestX < 0){
            score -= Math.pow(biggestValue, 3);
        }
        else{
            for(int i=0; i<4; i++){
                for(int j=0; j<4; j++){
                    //smoothness value
//                if(i < 3){
//                    score -= Math.abs(matrix[i][j] - matrix[i+1][j]);
//                    score -= Math.abs(revertedMatrix[i][j] - revertedMatrix[i+1][j]);
//                }
                    //distance to the biggest number
                    int distanceCurrent = Math.abs(i - biggestX) + Math.abs(j - biggestY);


                    if(i < 3){
                        int distanceNeighbor = Math.abs(i + 1 - biggestX) + Math.abs(j - biggestY);
                        if(distanceCurrent < distanceNeighbor){
                            if(matrix[i][j] >= matrix[i+1][j]){
                                score += MONOTONICITY_BONUS;
                            }
                            else{
                                score -= 9*MONOTONICITY_BONUS;
                            }
                        }
                        else{
                            if(matrix[i][j] <= matrix[i+1][j]){
                                score += MONOTONICITY_BONUS;
                            }
                            else{
                                score -= 9*MONOTONICITY_BONUS;
                            }
                        }
                    }


                }
            }
        }




        return score/16;

    }

    private int log2(int value){
        return (int) Math.floor(Math.log(value)/Math.log(2.0));
    }


}
