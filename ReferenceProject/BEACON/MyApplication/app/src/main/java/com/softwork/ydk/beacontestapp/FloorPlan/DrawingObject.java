package com.softwork.ydk.beacontestapp.FloorPlan;

import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.ColorInt;

/**
 * Created by DongKyu on 2016-05-26.
 */
public class DrawingObject {

    private boolean fill = false;
    private boolean line = false;
    private ToolMode toolMode;
    private Point beginPoint;
    private Point endPoint;
    @ColorInt private int lineColor;
    @ColorInt private int fillColor;
    private int thickness;
    private String majorKey;
    private String minorKey;

    public DrawingObject() {
        thickness = 1;
    }

    public boolean isFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public boolean isLine() {
        return line;
    }

    public void setLine(boolean line) {
        this.line = line;
    }

    public ToolMode getToolMode() {
        return toolMode;
    }

    public void setToolMode(ToolMode toolMode) {
        this.toolMode = toolMode;
    }

    public Point getBeginPoint() {
        return beginPoint;
    }

    public void setBeginPoint(Point beginPoint) {
        this.beginPoint = beginPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public String getMajorKey() {
        return majorKey;
    }

    public void setMajorKey(String majorKey) {
        this.majorKey = majorKey;
    }

    public String getMinorKey() {
        return minorKey;
    }

    public void setMinorKey(String minorKey) {
        this.minorKey = minorKey;
    }


}