package com.softwork.ydk.beacontestapp.FloorPlan;

/**
 * Created by DongKyu on 2016-06-12.
 */
public enum IconMode {
    COIN_TOILET(0),
    COIN_DOOR(1),
    COIN_ELEVATOR(2),
    COIN_ESCALATOR(3),
    COIN_STAIR(4),
    COIN_UAAA(5);

    private int value;

    private IconMode() {}

    private IconMode(int value) {
        this.value = value;
    }

    public int getInt() {
        return this.value;
    }
}
