package com.lyk.ai_2048.util;

/**
 * Created by lyk on 3/7/16.
 */
public abstract class Config {
    private static final int MERGE_DURATION = 150;
    private static final int MOVE_DURATION = 200;
    private static final int GENERATE_DURATION = 150;
    public static final int VIEW_FADE_DURATION = 150;
    public static final int VIEW_MOVE_DURATION = 150;
    private static boolean AI_2_STEPS;
    private static float SPEED_FACTOR = 1;

    public static boolean isAi2Steps() {
        return AI_2_STEPS;
    }

    public static void setAi2Steps(boolean ai2Steps) {
        AI_2_STEPS = ai2Steps;
    }

    public static void setSpeedFactor(float speedFactor) {
        SPEED_FACTOR = speedFactor;
    }

    public static float getSpeedFactor() {
        return SPEED_FACTOR;
    }

    public static float getGenerateDuration() {
        return GENERATE_DURATION*SPEED_FACTOR;
    }

    public static float getMergeDuration() {
        return MERGE_DURATION*SPEED_FACTOR;
    }

    public static float getMoveDuration() {
        return MOVE_DURATION*SPEED_FACTOR;
    }
}
