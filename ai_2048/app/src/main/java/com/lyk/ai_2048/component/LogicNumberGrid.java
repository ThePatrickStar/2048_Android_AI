package com.lyk.ai_2048.component;

import android.util.Log;

import com.lyk.ai_2048.util.BoardUtil;

import java.util.ArrayList;

/**
 * Created by lyk on 3/7/16.
 */
public class LogicNumberGrid {

    private static final String TAG = "LogicNumberGrid";
    private int[][] board, fakeBoard;
    private boolean[][] hasConflicted, fakeHasConflicted;
    private int score, fakeScore, fakeMoves;
    private boolean canMoveRight, canMoveLeft, canMoveUp, canMoveDown;


    public void fakeMoveLeft(){

        for(int i=0; i<4; i++){
            for(int j=1; j<4; j++){
                if(fakeBoard[i][j]!=0){
                    for(int k=0; k<j; k++){
                        if(fakeBoard[i][k] == 0 && BoardUtil.noBlockHorizontal(fakeBoard, i, k, j)){
                            fakeBoard[i][k]=fakeBoard[i][j];
                            fakeBoard[i][j]=0;
                            break;
                        }
                        else if (fakeBoard[i][k] == fakeBoard[i][j] && BoardUtil.noBlockHorizontal(fakeBoard, i, k, j) && !fakeHasConflicted[i][k]){
                            fakeBoard[i][k]+=fakeBoard[i][j];
                            fakeBoard[i][j]=0;
                            fakeHasConflicted[i][k] = true;
                            fakeScore += fakeBoard[i][k];
                            break;
                        }
                    }
                }
            }
        }
        fakeMoves++;
        fakeGenerateNumber();
    }

    public void fakeMoveRight(){

        for( int i = 0; i < 4 ; i ++ ){
            for( int j = 2; j >= 0; j -- ){
                if( fakeBoard[i][j] != 0 ){
                    for(int k = 3; k > j; k--){
                        if( fakeBoard[i][k] == 0 && BoardUtil.noBlockHorizontal(fakeBoard, i, j, k) ){
                            fakeBoard[i][k] = fakeBoard[i][j];
                            fakeBoard[i][j] = 0;
                            break;
                        }
                        else if( fakeBoard[i][k] == fakeBoard[i][j] && BoardUtil.noBlockHorizontal(fakeBoard, i, j, k) && !fakeHasConflicted[i][k] ){
                            fakeBoard[i][k] *= 2;
                            fakeBoard[i][j] = 0;
                            fakeHasConflicted[i][k] = true;
                            fakeScore += fakeBoard[i][k];
                            break;
                        }
                    }
                }
            }
        }
        fakeMoves++;
        fakeGenerateNumber();
    }

    public void fakeMoveUp(){

        for(int j = 0 ; j < 4 ; j ++ ) {
            for (int i = 1; i < 4; i++) {
                if (fakeBoard[i][j] != 0) {
                    for (int k = 0; k < i; k++) {
                        if (fakeBoard[k][j] == 0 && BoardUtil.noBlockVertical(fakeBoard, j, k, i)) {
                            fakeBoard[k][j] = fakeBoard[i][j];
                            fakeBoard[i][j] = 0;
                            break;
                        } else if (fakeBoard[k][j] == fakeBoard[i][j] && BoardUtil.noBlockVertical(fakeBoard, j, k, i) && !fakeHasConflicted[k][j]) {
                            fakeBoard[k][j] *= 2;
                            fakeBoard[i][j] = 0;
                            fakeHasConflicted[k][j] = true;
                            fakeScore += fakeBoard[k][j];
                            break;
                        }
                    }

                }
            }
        }
        fakeMoves++;
        fakeGenerateNumber();
    }

    public void fakeMoveDown(){

        for(int j = 0; j < 4; j++) {
            for (int i = 2; i >= 0; i--) {
                if (fakeBoard[i][j] != 0) {
                    for (int k = 3; k > i; k--) {
                        if (fakeBoard[k][j] == 0 && BoardUtil.noBlockVertical(fakeBoard, j, i, k)) {
                            fakeBoard[k][j] = fakeBoard[i][j];
                            fakeBoard[i][j] = 0;
                            break;
                        } else if (fakeBoard[k][j] == fakeBoard[i][j] && BoardUtil.noBlockVertical(fakeBoard, j, i, k) && !fakeHasConflicted[k][j]) {
                            fakeBoard[k][j] *= 2;
                            fakeBoard[i][j] = 0;
                            fakeHasConflicted[k][j] = true;
                            fakeScore += fakeBoard[k][j];
                            break;
                        }
                    }
                }
            }
        }
        fakeMoves++;
        fakeGenerateNumber();
    }

    public int[] fakeRunTillOver(int move){
        int[] pair = new int[2];
        fakeMoves = 0;
        fakeScore = score;
        fakeBoard = new int[4][4];
        fakeHasConflicted = new boolean[4][4];
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                fakeBoard[i][j] = board[i][j];
                fakeHasConflicted[i][j] = hasConflicted[i][j];
            }
        }

        if(move < 4){
            switch(move){
                case 0:
                    if(!BoardUtil.canMoveUp(fakeBoard)) {
                        pair[0] = -1;
                        pair[1] = 0;
                        return pair;
                    }
                    fakeMoveUp();
                    break;
                case 1:
                    if(!BoardUtil.canMoveDown(fakeBoard)){
                        pair[0] = -1;
                        pair[1] = 0;
                        return pair;
                    }
                    fakeMoveDown();
                    break;
                case 2:
                    if(!BoardUtil.canMoveLeft(fakeBoard)){
                        pair[0] = -1;
                        pair[1] = 0;
                        return pair;
                    }
                    fakeMoveLeft();
                    break;
                case 3:
                    if(!BoardUtil.canMoveRight(fakeBoard)){
                        pair[0] = -1;
                        pair[1] = 0;
                        return pair;
                    }
                    fakeMoveRight();
                    break;
                default:
                    Log.d(TAG, "invalid move");
                    pair[0] = -1;
                    pair[1] = 0;
                    return pair;
            }
        }
        else{
            move -= 4;
            int move1 = move/4, move2 = move%4;
            switch(move1){
                case 0:
                    if(!BoardUtil.canMoveUp(fakeBoard)) {
                        pair[0] = -1;
                        pair[1] = 0;
                        return pair;
                    }
                    fakeMoveUp();
                    break;
                case 1:
                    if(!BoardUtil.canMoveDown(fakeBoard)){
                        pair[0] = -1;
                        pair[1] = 0;
                        return pair;
                    }
                    fakeMoveDown();
                    break;
                case 2:
                    if(!BoardUtil.canMoveLeft(fakeBoard)){
                        pair[0] = -1;
                        pair[1] = 0;
                        return pair;
                    }
                    fakeMoveLeft();
                    break;
                case 3:
                    if(!BoardUtil.canMoveRight(fakeBoard)){
                        pair[0] = -1;
                        pair[1] = 0;
                        return pair;
                    }
                    fakeMoveRight();
                    break;
                default:
                    Log.d(TAG, "invalid move");
                    pair[0] = -1;
                    pair[1] = 0;
                    return pair;
            }
            switch(move2){
                case 0:
                    if(!BoardUtil.canMoveUp(fakeBoard)) {
                        pair[0] = -1;
                        pair[1] = 0;
                        return pair;
                    }
                    fakeMoveUp();
                    break;
                case 1:
                    if(!BoardUtil.canMoveDown(fakeBoard)){
                        pair[0] = -1;
                        pair[1] = 0;
                        return pair;
                    }
                    fakeMoveDown();
                    break;
                case 2:
                    if(!BoardUtil.canMoveLeft(fakeBoard)){
                        pair[0] = -1;
                        pair[1] = 0;
                        return pair;
                    }
                    fakeMoveLeft();
                    break;
                case 3:
                    if(!BoardUtil.canMoveRight(fakeBoard)){
                        pair[0] = -1;
                        pair[1] = 0;
                        return pair;
                    }
                    fakeMoveRight();
                    break;
                default:
                    Log.d(TAG, "invalid move");
                    pair[0] = -1;
                    pair[1] = 0;
                    return pair;
            }
        }


        while(!BoardUtil.isGameOver(fakeBoard)){

            if(canMoveRight){
                if(canMoveLeft){
                    if(canMoveUp){
                        if(canMoveDown){
                            double rand = Math.random();
                            if(rand < 0.25){
                                fakeMoveUp();
                            }
                            else if (rand < 0.5){
                                fakeMoveDown();
                            }
                            else if (rand < 0.75){
                                fakeMoveLeft();
                            }
                            else{
                                fakeMoveRight();
                            }
                        }
                        else{
                            double rand = Math.random();
                            if(rand < (float)1/3){
                                fakeMoveRight();
                            }
                            else if(rand < (float) 2/3){
                                fakeMoveLeft();
                            }
                            else {
                                fakeMoveUp();
                            }
                        }
                    }
                    else {
                        if(canMoveDown){
                            double rand = Math.random();
                            if(rand < (float)1/3){
                                fakeMoveRight();
                            }
                            else if(rand < (float) 2/3){
                                fakeMoveLeft();
                            }
                            else {
                                fakeMoveDown();
                            }
                        }
                        else{
                            double rand = Math.random();
                            if(rand < 0.5){
                                fakeMoveRight();
                            }
                            else{
                                fakeMoveLeft();
                            }
                        }
                    }
                }
                else{
                    if(canMoveUp){
                        if(canMoveDown){
                            double rand = Math.random();
                            if(rand < (float)1/3){
                                fakeMoveRight();
                            }
                            else if(rand < (float) 2/3){
                                fakeMoveUp();
                            }
                            else {
                                fakeMoveDown();
                            }
                        }
                        else{
                            double rand = Math.random();
                            if(rand < 0.5){
                                fakeMoveRight();
                            }
                            else{
                                fakeMoveUp();
                            }
                        }
                    }
                    else {
                        if(canMoveDown){
                            double rand = Math.random();
                            if(rand < 0.5){
                                fakeMoveRight();
                            }
                            else{
                                fakeMoveDown();
                            }
                        }
                        else{
                            fakeMoveRight();
                        }
                    }
                }
            }
            else{
                if(canMoveLeft){
                    if(canMoveUp){
                        if(canMoveDown){
                            double rand = Math.random();
                            if(rand < (float)1/3){
                                fakeMoveLeft();
                            }
                            else if(rand < (float) 2/3){
                                fakeMoveUp();
                            }
                            else {
                                fakeMoveDown();
                            }
                        }
                        else{
                            double rand = Math.random();
                            if(rand < 0.5){
                                fakeMoveUp();
                            }
                            else{
                                fakeMoveLeft();
                            }
                        }
                    }
                    else {
                        if(canMoveDown){
                            double rand = Math.random();
                            if(rand < 0.5){
                                fakeMoveLeft();
                            }
                            else{
                                fakeMoveDown();
                            }
                        }
                        else{
                            fakeMoveLeft();
                        }
                    }
                }
                else{
                    if(canMoveUp){
                        if(canMoveDown){
                            double rand = Math.random();
                            if(rand < 0.5){
                                fakeMoveUp();
                            }
                            else{
                                fakeMoveDown();
                            }
                        }
                        else{
                            fakeMoveUp();
                        }
                    }
                    else {
                        if(canMoveDown){
                            fakeMoveDown();
                        }
                    }
                }
            }

        }

        pair[0] = fakeScore;
        pair[1] = fakeMoves;
        return pair;
    }

    public void fakeGenerateNumber(){
        ArrayList<Integer> emptyCells = new ArrayList<>();
        canMoveUp = false;
        canMoveDown = false;
        canMoveLeft = false;
        canMoveRight = false;
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                fakeHasConflicted[i][j] = false;
                if(fakeBoard[i][j] != 0) {
                    if (i > 0) {
                        if (fakeBoard[i-1][j]==0 || fakeBoard[i][j] == fakeBoard[i-1][j])
                            canMoveUp = true;
                    }
                    if (i < 3) {
                        if (fakeBoard[i+1][j]==0 || fakeBoard[i][j] == fakeBoard[i+1][j])
                            canMoveDown = true;
                    }
                    if (j > 0) {
                        if (fakeBoard[i][j-1]==0 || fakeBoard[i][j] == fakeBoard[i][j-1])
                            canMoveLeft = true;
                    }
                    if (j < 3) {
                        if (fakeBoard[i][j+1]==0 || fakeBoard[i][j] == fakeBoard[i][j+1])
                            canMoveRight = true;
                    }
                }
                if(fakeBoard[i][j] == 0){
                    emptyCells.add(i*4+j);
                }
            }
        }
        if (emptyCells.size()!=0){
            int position = (int) (Math.random()*emptyCells.size());
            int randNum = Math.random() < 0.9 ? 2:4;
            fakeBoard[emptyCells.get(position)/4][emptyCells.get(position)%4] = randNum;
        }
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHasConflicted(boolean[][] hasConflicted) {
        this.hasConflicted = hasConflicted;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }
}
