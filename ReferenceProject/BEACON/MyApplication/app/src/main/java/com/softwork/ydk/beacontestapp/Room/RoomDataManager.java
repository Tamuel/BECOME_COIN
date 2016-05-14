package com.softwork.ydk.beacontestapp.Room;

/**
 * Created by DongKyu on 2016-05-14.
 */
public class RoomDataManager {
    private static RoomDataManager instance = null;

    private int roomWidth = 0;
    private int roomHeight = 0;

    private int horizontalSells = 0;
    private int verticalSells = 0;

    private float MFAvgValue[][] = new float[100][100];

    public RoomDataManager() {
    }

    public static RoomDataManager getInstance() {
        if(instance == null)
            instance = new RoomDataManager();
        return instance;
    }

    public void setInstance(RoomDataManager instance) {
        this.instance = instance;
    }

    public int getRoomWidth() {
        return roomWidth;
    }

    public void setRoomWidth(int roomWidth) {
        this.roomWidth = roomWidth;
    }

    public int getRoomHeight() {
        return roomHeight;
    }

    public void setRoomHeight(int roomHeight) {
        this.roomHeight = roomHeight;
    }

    public int getHorizontalSells() {
        return horizontalSells;
    }

    public void setHorizontalSells(int horizontalSells) {
        this.horizontalSells = horizontalSells;
    }

    public int getVerticalSells() {
        return verticalSells;
    }

    public void setVerticalSells(int verticalSells) {
        this.verticalSells = verticalSells;
    }

    public float[][] getMFAvgValue() {
        return MFAvgValue;
    }

    public float getMFAvgValue(int x, int y) {
        return MFAvgValue[x][y];
    }

    public void setMFAvgValue(float[][] MFAvgValue) {
        this.MFAvgValue = MFAvgValue;
    }

    public void setMFAvgValue(int x, int y, float value) {
        this.MFAvgValue[x][y] = value;
    }
}
