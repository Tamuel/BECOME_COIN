package user.taghere.taghere;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by hojaeson on 11/19/15.
 */

//This class is a ro`le for service handle
public class KeyValueHandler extends AsyncTask<String,Void,Bitmap> {

    KeyValue keyval = new KeyValue();
    String data;
   // String addr ="http://155.230.118.252/webserver/documents/findImage.php";//FIND IMAGE
    String addr ="http://155.230.118.252/webserver/documents/loadFromFloorPlan.php"; //FIND IMAGE!
    //String addr ="http://155.230.118.252/webserver/documents/savetoFloorPlan.php";// INSERT

    HttpURLConnection urlConnection = null;
    OutputStream os = null;
    int test = 0;
    Bitmap bitmap;
    @Override
    protected Bitmap doInBackground(String[] params) {
        Log.i("test", "1");
        try {
            JSONObject json = new JSONObject();
            String requestBody = json.toString();
            URL url = new URL(addr.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDefaultUseCaches(false);
            urlConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");

            StringBuffer buffer = new StringBuffer();
            buffer.append("id").append("=").append(params[0]);
            /*buffer.append("id").append("=").append("1").append("&");
            buffer.append("object_type").append("=").append("oEEl").append("&");;
            buffer.append("x1").append("=").append("2").append("&");;
            buffer.append("y1").append("=").append("3").append("&");;
            buffer.append("x2").append("=").append("4").append("&");;
            buffer.append("y2").append("=").append("5").append("&");;
            buffer.append("icon_type").append("=").append("stair").append("&");*/
            OutputStream outputSream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputSream, "UTF-8"));
            Log.i("test","write result" + buffer.toString());
            writer.write(buffer.toString());
            writer.flush();
            writer.close();
            outputSream.close();

            Log.i("test", buffer.toString());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuffer resposne = new StringBuffer();
            while((inputLine = in.readLine()) != null)
            {
                resposne.append(inputLine);
            }
            in.close();
            urlConnection.disconnect();
            Log.i("test", resposne.toString()+"UU");  //assume this part is a sort of path.
            if(resposne.toString().equals("1") == true) {
                bitmap = null;
                URL imageUrl = new URL("http://155.230.118.252/drawingSample.png");
                URLConnection imageConn = imageUrl.openConnection();
                imageConn.connect();

                BufferedInputStream bis = new BufferedInputStream(imageConn.getInputStream());
                bitmap = BitmapFactory.decodeStream(bis);
                Log.i("test", "bitmap width 1224 in handler" + String.valueOf(bitmap.getWidth()));
                return bitmap;
            }
            return bitmap;
            } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();Log.i("test", e2.getMessage()+" IOException");

        }
        return bitmap;
    }
    // 실제 전송하는 부분
    public String executeClient(String param) {
        return  "nothing";
    }

    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
    }

    protected void KeyValueHandler(Void... params) {
    }
}