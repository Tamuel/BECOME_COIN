package com.softwork.ydk.beacontestapp.Server;

import com.softwork.ydk.beacontestapp.FloorPlan.FloorPlan;

import java.util.ArrayList;

/**
 * Created by DongKyu on 2016-05-24.
 */
public class ServerManager {
    public static ServerManager instance = null;
    private String userID;
    private ArrayList<FloorPlan> floorPlans = new ArrayList<>();

    public ServerManager() {

        FloorPlan fp1 = new FloorPlan();
        fp1.setName("IT 4호관");
        fp1.setDescription("IT 4호관 이다");
        fp1.setFloorPlanSize("100 x 300m");
        fp1.setLatitude(132.2);
        fp1.setLongitude(132.2);

        FloorPlan fp2 = new FloorPlan();
        fp2.setName("공대 9호관");
        fp2.setDescription("공대 9호관 이다");
        fp2.setFloorPlanSize("100 x 300m");
        fp2.setLatitude(132.2);
        fp2.setLongitude(132.2);

        FloorPlan fp3 = new FloorPlan();
        fp3.setName("IT 2호관");
        fp3.setDescription("IT 2호관 이다");
        fp3.setFloorPlanSize("100 x 300m");
        fp3.setLatitude(132.2);
        fp3.setLongitude(132.2);

        floorPlans.add(fp1);
        floorPlans.add(fp2);
        floorPlans.add(fp3);
        floorPlans.add(fp1);
        floorPlans.add(fp2);
        floorPlans.add(fp3);
        floorPlans.add(fp1);
        floorPlans.add(fp2);
        floorPlans.add(fp3);
    }

    public static ServerManager getInstance() {
        if(instance == null)
            instance = new ServerManager();
        return instance;
    }

    public ArrayList<FloorPlan> getFloorPlans() {
        return floorPlans;
    }

    public void setFloorPlans(ArrayList<FloorPlan> floorPlans) {
        this.floorPlans = floorPlans;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
