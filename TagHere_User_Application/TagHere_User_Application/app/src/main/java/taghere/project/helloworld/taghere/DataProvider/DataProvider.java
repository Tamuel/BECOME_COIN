package taghere.project.helloworld.taghere.DataProvider;

import android.graphics.Point;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import taghere.project.helloworld.taghere.FloorPlanObjects.FPObject;
import taghere.project.helloworld.taghere.FloorPlanObjects.Icon;
import taghere.project.helloworld.taghere.FloorPlanObjects.IconType;
import taghere.project.helloworld.taghere.FloorPlanObjects.Line;
import taghere.project.helloworld.taghere.FloorPlanObjects.Oval;
import taghere.project.helloworld.taghere.FloorPlanObjects.Rectangle;
import taghere.project.helloworld.taghere.FloorPlanObjects.Tag;

/**
 * Created by DongKyu on 2015-12-01.
 */
public class DataProvider {

    /** For Singleton design pattern */
    private static DataProvider singleton = null;

    private String data = "";
    private int numberOfAttribute = 6;

    private String floorPlanId;
    private ArrayList<FPObject> currentObjects;

    public DataProvider() {
        currentObjects = new ArrayList<FPObject>();
        data = "0:168:134:168:226:0:0:168:134:190:113:0:0:190:113:281:113:0:0:168:226:190:243:0:0:281:113:299:133:0:0:190:243:190:377:0:0:190:377:168:394:0:0:168:394:168:507:0:0:168:507:190:526:0:0:190:526:281:526:0:0:299:133:315:133:0:0:315:133:315:94:0:0:315:94:541:94:0:0:299:506:883:506:0:0:883:506:883:526:0:0:883:526:909:547:0:0:1010:526:985:548:0:0:909:547:986:547:0:0:541:94:541:55:0:0:541:55:492:55:0:0:492:55:492:16:0:0:492:16:584:16:0:0:584:16:584:24:0:0:584:24:714:24:0:0:714:24:714:94:0:0:714:94:877:94:0:0:877:94:877:135:0:0:877:135:893:135:0:0:893:135:893:124:0:0:893:124:907:106:0:0:907:106:986:106:0:0:986:106:1010:128:0:0:1010:128:1010:214:0:0:1010:214:985:233:0:0:985:233:985:382:0:0:985:382:1010:402:0:0:1010:402:1010:525:0:0:281:526:299:506:0:0:299:133:299:266:0:0:299:266:576:266:0:0:576:266:576:149:0:0:576:149:299:149:0:0:576:149:904:149:0:0:904:149:904:267:0:0:686:150:686:266:0:0:686:266:903:266:0:0:190:354:985:354:0:0:167:484:300:484:0:0:300:484:300:506:0:0:578:355:578:505:0:0:458:354:458:505:0:0:775:149:775:266:0:0:686:209:775:209:0:0:883:506:883:391:0:0:865:391:902:391:0:0:902:391:902:355:0:0:865:391:865:355:0:0:816:353:816:507:0:0:731:355:731:506:0:1:752:208:752:208:0:1:715:266:715:266:0:1:808:266:808:266:0:1:872:266:872:266:0:1:985:305:985:305:0:1:189:299:189:299:0:1:346:267:346:267:0:1:530:267:530:267:0:1:603:151:603:151:0:1:662:150:662:150:0:1:228:354:228:354:0:1:346:354:346:354:0:1:428:355:428:355:0:1:523:355:523:355:0:1:614:354:614:354:0:1:694:354:694:354:0:1:770:355:770:355:0:1:841:353:841:353:0:1:938:355:938:355:0:1:225:483:225:483:0:1:201:173:201:173:2:1:260:174:260:174:2:1:980:157:980:157:2:0:904:172:934:172:0:0:934:172:934:228:0:0:934:228:904:228:0:1:918:229:918:229:0:1:848:443:848:443:3:1:944:443:944:443:3:1:384:525:384:525:2:1:927:154:927:154:2:4:431:433:431:433:0";
//        System.out.println(loadFromServer(1));
        parsingObject(data);
    }

    /** For Singleton design.
     * This function provide get instance of singleton */
    public static DataProvider getInstance()
    {
        if(singleton == null)
            singleton  = new DataProvider();

        return singleton;
    }

    public String loadFromServer(int id) {//throws Exception {
        try{
            URL url = new URL("http://155.230.118.252/webserver/documents/loadFromFloorPlan.php");
            HttpURLConnection urlConnection;
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDefaultUseCaches(false);
            urlConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");

            StringBuffer buffer = new StringBuffer();
            buffer.append("id").append("=").append(id);
            OutputStream outputSream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputSream, "UTF-8"));
            writer.write(buffer.toString());
            writer.flush();
            writer.close();
            outputSream.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            urlConnection.disconnect();

            return response.toString();
        }
        catch(Exception e){
            return null;
        }
    }

    /**
     * 0 : Object type
     * 1 : Start x
     * 2 : Start y
     * 3 : End x
     * 4 : End y
     * 5 : Icon type
     * */
    public void parsingObject(String data) {
        String[] strings = data.split(":");
        for(int i = 0; i < strings.length;) {
            FPObject newObject = null;
            switch(Integer.parseInt(strings[i])) {
                case 0:
                    newObject = new Line(
                            Integer.parseInt(strings[i + 1]),
                            Integer.parseInt(strings[i + 2])
                    );
                    break;

                case 1:
                    newObject = new Icon(
                            Integer.parseInt(strings[i + 1]),
                            Integer.parseInt(strings[i + 2])
                    );
                    ((Icon)newObject).setIconType(IconType.values()[Integer.parseInt(strings[i + 5])]);
                    break;

                case 2:
                    newObject = new Oval(
                            Integer.parseInt(strings[i + 1]),
                            Integer.parseInt(strings[i + 2])
                    );
                    break;

                case 3:
                    newObject = new Rectangle(
                            Integer.parseInt(strings[i + 1]),
                            Integer.parseInt(strings[i + 2])
                    );
                    break;

                case 4:
                    newObject = new Tag(
                            Integer.parseInt(strings[i + 1]),
                            Integer.parseInt(strings[i + 2])
                    );
                    break;
            }

            if(newObject != null) {
                newObject.setEndPosition(
                        new Point(
                                Integer.parseInt(strings[i + 3]),
                                Integer.parseInt(strings[i + 4])
                        )
                );
            }

            if(newObject != null)
                currentObjects.add(newObject);

            i += numberOfAttribute;
        }
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ArrayList<FPObject> getCurrentObjects() {
        return currentObjects;
    }

    public void setCurrentObjects(ArrayList<FPObject> currentObjects) {
        this.currentObjects = currentObjects;
    }
}
