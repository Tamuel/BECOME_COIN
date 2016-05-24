package com.softwork.ydk.beacontestapp.Server;

/**
 * Created by DongKyu on 2016-05-24.
 */
public class ServerManager {
    public static ServerManager instance = null;

    public ServerManager() {

    }

    public static ServerManager getInstance() {
        if(instance == null)
            instance = new ServerManager();
        return instance;
    }
}
