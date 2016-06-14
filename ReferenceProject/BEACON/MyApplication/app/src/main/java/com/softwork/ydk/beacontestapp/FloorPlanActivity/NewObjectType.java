package com.softwork.ydk.beacontestapp.FloorPlanActivity;

/**
 * Created by DongKyu on 2016-06-12.
 */
public enum NewObjectType {
    BEACON(0),
    ICON(1),
    TEXT(2),
    TAG(3);

    private int value;

    private NewObjectType() {}

    private NewObjectType(int value) {
        this.value = value;
    }

    public int getInt() {
        return this.value;
    }
}
