package com.softwork.ydk.beacontestapp.Beacon;

import android.content.Context;

import com.softwork.ydk.beacontestapp.BeaconActivity;

/**
 * Created by DongKyu on 2016-04-24.
 */
public class RefreshBeaconService extends Thread {

    private BeaconActivity mainActivity;

    public RefreshBeaconService(Context context) {
        mainActivity = (BeaconActivity)context;
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(10000);
                mainActivity.resetBeaconService();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
