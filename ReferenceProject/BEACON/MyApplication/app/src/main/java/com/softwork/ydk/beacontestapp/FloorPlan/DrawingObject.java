package com.softwork.ydk.beacontestapp.FloorPlan;

import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.ColorInt;
import android.util.Log;

import com.softwork.ydk.beacontestapp.BFunc;

/**
 * Created by DongKyu on 2016-05-26.
 */
public class DrawingObject {
    // For icon
    public static int iconSize[];

    private boolean fill = false;
    private boolean line = false;
    private boolean isIcon = false;
    private ToolMode toolMode = null;
    private IconMode iconMode = null;
    private Point beginPoint;
    private Point endPoint;
    @ColorInt private int lineColor;
    @ColorInt private int fillColor;
    private int thickness = 0;
    private float rotate;
    private String majorKey;
    private String minorKey;

    public DrawingObject() {
        thickness = 1;
    }

    public float getRotate() {
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }

    public IconMode getIconMode() {
        return iconMode;
    }

    public void setIconMode(IconMode iconMode) {
        this.iconMode = iconMode;
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

    public void setEndPoint(Point endPpint) {
        this.endPoint = endPpint;
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

    public boolean isIcon() {
        return isIcon;
    }

    public void setIsIcon(boolean isIcon) {
        this.isIcon = isIcon;
    }

    // Collision check at point(x, y)
    public boolean isSelected(int x, int y) {
        switch (toolMode) {
            case CIRCLE:
                double aPow2 = Math.pow((endPoint.x - beginPoint.x) / 2, 2);
                if(aPow2 == 0) aPow2 = 0.000000001;
                double bPow2 = Math.pow((endPoint.y - beginPoint.y) / 2, 2);
                if(bPow2 == 0) bPow2 = 0.000000001;
                if(Math.pow(x - (endPoint.x + beginPoint.x) / 2, 2) / aPow2 +
                    Math.pow(y - (endPoint.y + beginPoint.y) / 2, 2) / bPow2 <= 1)
                    return true;
                break;

            case LINE:
                int length = BFunc.getLength(beginPoint.x, endPoint.x, beginPoint.y, endPoint.y);
                int distance = Math.abs(
                        (x - beginPoint.x) * (endPoint.y - beginPoint.y) -
                        (y - beginPoint.y) * (endPoint.x - beginPoint.x)) /
                        length;

                if(distance < 15 &&
                    BFunc.getLength(
                            (beginPoint.x + endPoint.x) / 2, x,
                            (beginPoint.y + endPoint.y) / 2, y
                    ) < length / 2 + 15)
                    return true;

                break;

            case RECT:
                if(getBeginPoint().x <= x && getEndPoint().x >= x &&
                    getBeginPoint().y <= y && getEndPoint().y >= y)
                    return true;
                break;

            case BEACON:
            case ICON:
            case TAG:
            case TEXT:
                if(getBeginPoint().x - 30 <= x && getBeginPoint().x + 30 >= x &&
                    getBeginPoint().y - 30 <= y && getBeginPoint().y + 30 >= y)
                    return true;
                break;
        };

        return false;
    }

    @Override
    public String toString() {
        String r =
                "Position : " + getBeginPoint().toString() + "\n" +
                        "Size : (" + Math.abs(getEndPoint().x - getBeginPoint().x) + ", " +
                        Math.abs(getEndPoint().y - getBeginPoint().y) + ")\n" +
                        "Fill Color : (" + Color.red(getFillColor()) + ", " +
                        +Color.green(getFillColor()) + ", " +
                        +Color.blue(getFillColor()) + ")\n" +
                        "Line Color : (" + Color.red(getLineColor()) + ", " +
                        +Color.green(getLineColor()) + ", " +
                        +Color.blue(getLineColor()) + ")\n";

        if(getThickness() != 0)
            r += "Line Thinkness : " + getThickness() + "\n";
        if(getMajorKey() != null)
            r += "MJData : " + getMajorKey() + "\n";
        if(getMinorKey() != null)
            r += "MNData : " + getMinorKey() + "\n";

        return r;
    }
}