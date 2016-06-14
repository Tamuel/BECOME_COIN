package com.softwork.ydk.beacontestapp.Beacon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.perples.recosdk.RECOBeacon;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECORangingListener;
import com.softwork.ydk.beacontestapp.BeaconActivity;

import java.util.Collection;

/**
 * Created by DongKyu on 2016-04-23.
 */
public class BeaconRangingListener extends Service implements RECORangingListener {

    public static final int numberOfBeacons = 5;
    public static int beacons[] = new int[numberOfBeacons];

    private int count = 1;
    private int i = 0;

    private int beaconAvg[][] = new int[numberOfBeacons][count];

    public void didRangeBeaconsInRegion(Collection<RECOBeacon> recoBeacons, RECOBeaconRegion recoRegion) {
        //ranging 중인 region에서 1초 간격으로 감지된
        //RECOBeacon들 리스트와 함께 이 callback 메소드를 호출합니다.
        //recoRegion에서 감지된 RECOBeacon 리스트 수신 시 작성 코드
        Log.i("aa", "==================================");
        Log.i("REGION ", recoRegion.getUniqueIdentifier() + " " + recoRegion.getRegionExpirationTimeMillis());
        for(RECOBeacon beacon : recoBeacons) {
            beaconAvg[beacon.getMinor() / 100 - 1][i] = ((int)(beacon.getAccuracy() * 10)) * 10;
        }
        i++;
        if(i == count) {
            int j;
            for(j = 0; j < numberOfBeacons; j++)
                for (i = 1; i < count; i++)
                    beaconAvg[j][0] += beaconAvg[j][i];
            for (i = 0; i < numberOfBeacons; i++)
                beacons[i] = beaconAvg[i][0] / count;
            i = 0;
        }
    }

    public void rangingBeaconsDidFailForRegion(RECOBeaconRegion recoRegion, RECOErrorCode errorCode) {
        //ranging을 정상적으로 시작하지 못했을 경우 이 callback 메소드가 호출됩니다.
        //RECOErrorCode는 "Error Code"를 확인하시기 바랍니다.
        //ranging 실패 시 코드 작성
        Log.e("RANGING FAIL", "");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
