package com.lyk.ai_2048.base;

/**
 * Created by lyk on 23/6/16.
 */
public interface GameHolder {
    void updateScore(int scoreNew, int scoreOld);
    void updateHighScore(int scoreNew, int scoreOld);
    void resetGame();
}
