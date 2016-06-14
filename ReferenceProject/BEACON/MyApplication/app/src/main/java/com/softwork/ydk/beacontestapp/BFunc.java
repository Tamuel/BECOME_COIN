package com.softwork.ydk.beacontestapp;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by DongKyu on 2016-06-12.
 */
public class BFunc {

    public static int getDP(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }

    public static int getLength(int x1, int x2, int y1, int y2) {
        return (int)Math.sqrt(
                Math.pow(x1 - x2, 2) +
                        Math.pow(y1 - y2, 2)
        );
    }
}
