package com.softwork.ydk.beacontestapp;

import android.graphics.drawable.Drawable;

/**
 * Created by DongKyu on 2016-05-24.
 */
public class FloorPlanListViewItem {
    private Drawable floorPlanImage;
    private String floorPlanName;
    private String floorPlanSize;
    private String floorPlanDescription;

    public FloorPlanListViewItem() {
    }

    public Drawable getFloorPlanImage() {
        return floorPlanImage;
    }

    public void setFloorPlanImage(Drawable floorPlanImage) {
        this.floorPlanImage = floorPlanImage;
    }

    public String getFloorPlanName() {
        return floorPlanName;
    }

    public void setFloorPlanName(String floorPlanName) {
        this.floorPlanName = floorPlanName;
    }

    public String getFloorPlanSize() {
        return floorPlanSize;
    }

    public void setFloorPlanSize(String floorPlanSize) {
        this.floorPlanSize = floorPlanSize;
    }

    public String getFloorPlanDescription() {
        return floorPlanDescription;
    }

    public void setFloorPlanDescription(String floorPlanDescription) {
        this.floorPlanDescription = floorPlanDescription;
    }
}
