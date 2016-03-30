package user.taghere.taghere;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class ImageActivity extends Activity {

    private String TAG = "나침반";
    private Compass compass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("test", "my new activit1y");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        //bitmap parsing
        byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Log.i("test", String.valueOf(bitmap.getWidth()));

        //drawing with bitmap from server
        ImageView xmlDrawingView = (ImageView) findViewById(R.id.drawing);
        xmlDrawingView.setImageBitmap(bitmap);

        //compass
        compass = new Compass(this);
        ImageView hand = (ImageView) findViewById(R.id.main_image_hands);
        hand.setRotation(-45);
        compass.arrowView = hand;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //compass life cycle
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "start compass");
        compass.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compass.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compass.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "stop compass");
        compass.stop();
    }

}
