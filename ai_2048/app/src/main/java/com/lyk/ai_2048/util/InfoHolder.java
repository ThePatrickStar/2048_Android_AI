package com.lyk.ai_2048.util;

/**
 * Created by lyk on 22/6/16.
 */
public class InfoHolder {

    public static int deviceX, deviceY;

    public static int getGridPadding(){
        return getGridSize()/60;
    }

    public static int getGridSize(){
        return deviceX*9/10;
    }

    public static int getCellSize(){
        return (getGridSize() - 5*getGridPadding())/4;
    }

    public static int getDeviceX() {
        return deviceX;
    }

    public static void setDeviceX(int deviceX) {
        InfoHolder.deviceX = deviceX;
    }

    public static int getDeviceY() {
        return deviceY;
    }

    public static void setDeviceY(int deviceY) {
        InfoHolder.deviceY = deviceY;
    }
}
