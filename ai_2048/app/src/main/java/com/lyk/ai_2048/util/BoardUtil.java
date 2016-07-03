package com.lyk.ai_2048.util;

/**
 * Created by lyk on 3/7/16.
 */
public class BoardUtil {
    public static boolean canMoveLeft(int[][] board){
        for (int i=0; i<4; i++){
            for (int j=1; j<4; j++){
                if(board[i][j]!=0){
                    if (board[i][j-1]==0 || board[i][j] == board[i][j-1])
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean canMoveRight(int[][] board){
        for (int i=0; i<4; i++){
            for (int j=0; j<3; j++){
                if(board[i][j]!=0){
                    if (board[i][j+1]==0 || board[i][j] == board[i][j+1])
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean canMoveUp(int[][] board){
        for (int i=1; i<4; i++){
            for (int j=0; j<4; j++){
                if(board[i][j]!=0){
                    if (board[i-1][j]==0 || board[i][j] == board[i-1][j])
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean canMoveDown(int[][] board){
        for (int i=0; i<3; i++){
            for (int j=0; j<4; j++){
                if(board[i][j]!=0){
                    if (board[i+1][j]==0 || board[i][j] == board[i+1][j])
                        return true;
                }
            }
        }
        return false;
    }

    public static boolean noBlockHorizontal(int[][] board, int row, int col1, int col2){
        for (int i = col1+1; i< col2; i++){
            if (board[row][i]!=0)
                return false;
        }
        return true;
    }

    public static boolean noBlockVertical(int[][] board, int col, int row1, int row2){
        for (int i=row1+1;i<row2; i++){
            if (board[i][col]!=0)
                return false;
        }
        return true;
    }

    public static boolean noSpace(int[][] board){
        for (int i=0; i<4; i++){
            for (int j=0; j<4; j++){
                if(board[i][j] == 0)
                    return false;
            }
        }
        return true;
    }

    public static boolean noMove(int[][] board){
        return !(canMoveDown(board) || canMoveLeft(board) || canMoveRight(board) || canMoveUp(board));
    }

    public static boolean isGameOver(int[][] board){
        return noSpace(board) && noMove(board);
    }



}
