package com.softwork.ydk.beacontestapp.Beacon;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOErrorCode;
import com.perples.recosdk.RECOServiceConnectListener;

import java.util.ArrayList;

/**
 * Created by DongKyu on 2016-04-23.
 */
public class BeaconService implements RECOServiceConnectListener {
    private String mProximityUUID = "24DDF411-8CF1-440C-87CD-E368DAF9C93E";
    private int mMajor = 1000;
    private int mMinor = 100;

    private int regionExpirationTime = 60 * 1000;
    private int managerScanPeriod = 1000;
    private int managerSleepPeriod = 3000;

    private boolean mScanRecoOnly = true;
    private boolean mEnableBackgroundTimeout = true;

    private RECOBeaconManager recoManager;
    private ArrayList<RECOBeaconRegion> monitoringRegions;

    private BeaconMonitoringListener beaconMonitoringListener;
    private BeaconRangingListener beaconRangingListener;

    public BeaconService(Context context) {
        beaconMonitoringListener = new BeaconMonitoringListener();
        beaconRangingListener = new BeaconRangingListener();

        recoManager = RECOBeaconManager.getInstance(context, mScanRecoOnly, mEnableBackgroundTimeout);
        recoManager.setDiscontinuousScan(false);
        recoManager.setMonitoringListener(beaconMonitoringListener);
        recoManager.setRangingListener(beaconRangingListener);
        recoManager.bind(this);

        //scan 시간을 설정할 수 있습니다. 기본 값은 1초 입니다.
        recoManager.setScanPeriod(managerScanPeriod);
        recoManager.setSleepPeriod(managerSleepPeriod);
    }

    public void onServiceConnect() {
        //RECOBeaconService와 연결 시 코드 작성
        monitoringRegions = new ArrayList<RECOBeaconRegion>();
//        monitoringRegions.add(new RECOBeaconRegion(mProximityUUID, mMajor, mMinor, "테스트용1"));
        monitoringRegions.add(new RECOBeaconRegion(mProximityUUID, mMajor, "테스트용1"));

        for(RECOBeaconRegion region : monitoringRegions) {
            try {
                //region의 expiration 시간을 설정할 수 있습니다. 기본 값은 60초(1분) 입니다.
                region.setRegionExpirationTimeMillis(regionExpirationTime);

                // Mornitoring을 시작하면 didStartMonitoringForRegion 함수 호출
                recoManager.startMonitoringForRegion(region);
                recoManager.startRangingBeaconsInRegion(region);
                recoManager.requestStateForRegion(region);
            } catch (RemoteException e) {
                //RemoteException 발생 시 작성 코드
                e.printStackTrace();
            } catch (NullPointerException e) {
                //NullPointerException 발생 시 작성 코드
                e.printStackTrace();
            }
        }
    }

    public ArrayList<RECOBeaconRegion> getRegions() {
        return monitoringRegions;
    }

    public void onServiceFail(RECOErrorCode errorCode) {
        Log.e("SERVICE FAIL", "");

        //RECOBeaconService와 연결 되지 않았을 시 코드 작성
        for(RECOBeaconRegion region : monitoringRegions) {
            try {
                recoManager.stopMonitoringForRegion(region);
                recoManager.stopRangingBeaconsInRegion(region);
            } catch (RemoteException e) {
                //RemoteException 발생 시 작성 코드
                e.printStackTrace();
            } catch (NullPointerException e) {
                //NullPointerException 발생 시 작성 코드
                e.printStackTrace();
            }
        }
    }

    public void unBindService() {
        try {
            recoManager.unbind();
        } catch (RemoteException e) {
            //RemoteException 발생 시 작성 코드
            e.printStackTrace();
        } catch (NullPointerException e) {
            //NullPointerException 발생 시 작성 코드
            e.printStackTrace();
        }
    }
}
