package com.softwork.ydk.beacontestapp.FloorPlan;

/**
 * Created by DongKyu on 2016-05-26.
 */
public enum ToolMode {
    SELECT(0),
    LINE(1),
    RECT(2),
    CIRCLE(3),
    ICON(4),
    TAG(5),
    BEACON(6),
    TEXT(7);

    private int value;

    private ToolMode() {}

    private ToolMode(int value) {
        this.value = value;
    }

    public int getInt() {
        return this.value;
    }
}