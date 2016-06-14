package com.softwork.ydk.beacontestapp.Server;

import android.graphics.Point;

import com.softwork.ydk.beacontestapp.FloorPlan.DrawingObject;
import com.softwork.ydk.beacontestapp.FloorPlan.FloorPlan;
import com.softwork.ydk.beacontestapp.FloorPlan.IconMode;
import com.softwork.ydk.beacontestapp.FloorPlan.ToolMode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by DongKyu on 2016-05-24.
 */
public class ServerManager {
    public final static int REJECT = 0;
    public final static int ACCEPT = 1;

    public static ServerManager instance = null;
    private String userID;
    private String userNickName;
    private ArrayList<FloorPlan> floorPlans = new ArrayList<>();

    private int requestResult;
    private String result = "";

    public ServerManager() {

        FloorPlan fp1 = new FloorPlan();
        fp1.setBuildingName("IT 4호관");
        fp1.setFloor(1);
        fp1.setName("1층");
        fp1.setDescription("IT 4호관 1층 이다");
        fp1.setLatitude(35.887944);
        fp1.setLongitude(128.611260);

        FloorPlan fp2 = new FloorPlan();
        fp2.setBuildingName("공대 9호관");
        fp2.setFloor(1);
        fp2.setName("1층");
        fp2.setDescription("공대 9호관 1층 이다");
        fp2.setLatitude(132.2);
        fp2.setLongitude(132.2);

        FloorPlan fp3 = new FloorPlan();
        fp3.setBuildingName("IT 2호관");
        fp3.setName("1층이다");
        fp3.setFloor(1);
        fp3.setName("1층");
        fp3.setDescription("IT 2호관 1층 이다");
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

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    /**
     * ALL CASES: <br>
     * - data type <br>
     * @param object
     */
    private void data1of8(DrawingObject object) {
        System.out.print(object.getToolMode() + ":");
    }

    /**
     * CASE LINE, RECT, CIRCLE: <br>
     * - thickness <br>
     * CASE ICON, TAG, BEACON: <br>
     * - icon type <br>
     * @param object
     */
    private void data2of8(DrawingObject object) {
        if(object.getToolMode() == ToolMode.ICON ||
                object.getToolMode() == ToolMode.TAG ||
                object.getToolMode() == ToolMode.BEACON ||
                object.getToolMode() == ToolMode.TEXT) {
            try {
                System.out.print(object.getIconMode().name() + ":");
            } catch (Exception e) {
                System.out.print("null" + ":");
            }
        }
        else {
            System.out.print(object.getThickness() + ":");
        }
    }

    /**
     * ALL CASES: <br>
     * - x1 <br>
     * @param object
     */
    private void data3of8(DrawingObject object) {
        System.out.print((int)object.getBeginPoint().x + ":");
    }

    /**
     * ALL CASES: <br>
     * - y1 <br>
     * @param object
     */
    private void data4of8(DrawingObject object) {
        System.out.print((int)object.getBeginPoint().y + ":");
    }

    /**
     * ALL CASES: <br>
     * - x2 (or width) <br>
     * @param object
     */
    private void data5of8(DrawingObject object) {
        System.out.print((int)object.getEndPoint().x + ":");
    }

    /**
     * ALL CASES: <br>
     * - y2 (or height) <br>
     * @param object
     */
    private void data6of8(DrawingObject object) {
        System.out.print((int)object.getEndPoint().y + ":");
    }

    /**
     * CASE LINE, RECT, CIRCLE: <br>
     * - line color sGBA <br>
     * CASE ICON: <br>
     * - theta(radian) <br>
     * CASE TAG, BEACON: <br>
     * - major key (or tag key) <br>
     * @param object
     */
    private void data7of8(DrawingObject object) {
        if(object.getToolMode() == ToolMode.LINE ||
                object.getToolMode() == ToolMode.RECT ||
                object.getToolMode() == ToolMode.CIRCLE) {
            System.out.print(object.getLineColor() + ":");
        }
        else if(object.getToolMode() == ToolMode.ICON) {
            System.out.print(object.getTheta() + ":");
        }
        else {
            System.out.print(object.getMajorKey() + ":");
        }
    }

    /**
     * CASE LINE, RECT, CIRCLE: <br>
     * - fill color sGBA (if no fill color, then null) <br>
     * CASE ICON, TAG: <br>
     * - null <br>
     * CASE BEACON: <br>
     * - minor key <br>
     * @param object
     */
    private void data8of8(DrawingObject object) {
        if(object.getToolMode() == ToolMode.LINE ||
                object.getToolMode() == ToolMode.RECT ||
                object.getToolMode() == ToolMode.CIRCLE) {
            try {
                System.out.print(object.getFillColor());
            } catch (Exception e) {
                System.out.print("null");
            }
        }
        else if(object.getToolMode() == ToolMode.ICON) {
            System.out.print("null");
        }
        else {
            System.out.print(object.getMinorKey());
        }
    }

    public void parseData(ArrayList<DrawingObject> objects) {
        // TODO save procedure
        for(int i = 0; i < objects.size(); i++) {
            DrawingObject object = objects.get(i);
            data1of8(object);
            data2of8(object);
            data3of8(object);
            data4of8(object);
            data5of8(object);
            data6of8(object);
            data7of8(object);
            data8of8(object);
            System.out.println("");
        }
    }


    public ArrayList<DrawingObject> getObject(String objectData) {
        // Get Objects
        String line[] = objectData.split("\n");

        ArrayList<DrawingObject> tempObjects = new ArrayList<>();

        int lineColor, fillColor;
        for(String temp : line) {
            String data[] = temp.split(":");
            DrawingObject newObject = new DrawingObject();
            switch (data[0]) {
                case "RECT":
                    if(newObject.getToolMode() ==  null) newObject.setToolMode(ToolMode.RECT);
                case "CIRCLE":
                    if(newObject.getToolMode() ==  null) newObject.setToolMode(ToolMode.CIRCLE);
                case "LINE":
                    if(newObject.getToolMode() ==  null) newObject.setToolMode(ToolMode.LINE);

                    newObject.setThickness(Integer.parseInt(data[1]));
                    newObject.setBeginPoint(
                            new Point(
                                    Integer.parseInt(data[2]),
                                    Integer.parseInt(data[3])
                            )
                    );
                    if(newObject.getToolMode() != ToolMode.LINE) {
                        newObject.setEndPoint(
                                new Point(
                                        Integer.parseInt(data[4]) + Integer.parseInt(data[2]),
                                        Integer.parseInt(data[5]) + Integer.parseInt(data[3])
                                )
                        );
                    } else {
                        newObject.setEndPoint(
                                new Point(
                                        Integer.parseInt(data[4]),
                                        Integer.parseInt(data[5])
                                )
                        );
                    }
                    if(!data[6].equals("null")) {
                        lineColor = Integer.parseInt(data[6]);
                        newObject.setLineColor(lineColor);
                        newObject.setLine(true);
                    }
                    if(!data[7].equals("null")) {
                        fillColor = Integer.parseInt(data[7]);
                        newObject.setFillColor(fillColor);
                        newObject.setFill(true);
                    }
                    newObject.setIsIcon(false);
                    break;

                case "TEXT":
                    if(newObject.getToolMode() ==  null) newObject.setToolMode(ToolMode.TEXT);
                case "ICON":
                    if(newObject.getToolMode() ==  null) newObject.setToolMode(ToolMode.ICON);
                case "BEACON":
                    if(newObject.getToolMode() ==  null) newObject.setToolMode(ToolMode.BEACON);
                case "TAG":
                    if(newObject.getToolMode() ==  null) newObject.setToolMode(ToolMode.TAG);
                    newObject.setBeginPoint(
                            new Point(
                                    Integer.parseInt(data[2]) + 25,
                                    Integer.parseInt(data[3]) + 25
                            )
                    );
                    newObject.setEndPoint(
                            new Point(
                                    Integer.parseInt(data[2]) + DrawingObject.iconSize[0] + 25,
                                    Integer.parseInt(data[3]) + DrawingObject.iconSize[1] + 25
                            )
                    );
                    if(newObject.getToolMode() != ToolMode.ICON) {
                        newObject.setMajorKey(data[6]);
                        newObject.setMinorKey(data[7]);
                    }
                    newObject.setIconMode(IconMode.valueOf(data[1]));
                    newObject.setLine(true);
                    newObject.setIsIcon(true);
                    break;
            }
            if(newObject.getToolMode() != null)
                tempObjects.add(newObject);
        }
        return tempObjects;
    }


    private String requestServer(final String urlString) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuffer sb = new StringBuffer();

                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn =
                            (HttpURLConnection) url.openConnection();
                    if (conn != null) {
                        conn.setConnectTimeout(2000);
                        conn.setUseCaches(false);
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            BufferedReader br
                                    = new BufferedReader(new InputStreamReader
                                    (conn.getInputStream(), "utf-8"));
                            while (true) {
                                String line = br.readLine();
                                if (line == null) break;
                                sb.append(line);
                            }
                            br.close();
                        }
                        conn.disconnect();
                    }
                    System.out.println(sb.toString());
                    result = sb.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    result = "NONE";
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public int getResult() {
        result = "";
        return requestResult;
    }


    public String requestLoginToServer(String id, String password) {
        String temp = requestServer("http://ksd.iptime.org:8080/userSearch/" + id + "/" + password);

        if(temp.equals("LOGIN_REJECT") || temp.equals("") || temp.equals("NONE"))
            requestResult = REJECT;
        else
            requestResult = ACCEPT;

        return temp;
    }


    public String requestJoinToServer(final String id, final String password, final String nickname) {
        String temp = requestServer("http://ksd.iptime.org:8080/addUser?id=" + id + "&password=" + password + "&name=" + nickname);

        if(temp.equals("JOIN_REJECT") || temp.equals("") || temp.equals("NONE"))
            requestResult = REJECT;
        else if(temp.equals("JOIN_SUCCESS"))
            requestResult = ACCEPT;

        return temp;
    }
}
