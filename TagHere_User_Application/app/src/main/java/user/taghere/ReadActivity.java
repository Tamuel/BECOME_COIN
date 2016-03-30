package user.taghere.taghere;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;

import net.sourceforge.zbar.Symbol;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class ReadActivity extends Activity {

    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;

    NfcAdapter nfcAdapter;          //Now that we can't use NFC on service routine, I will put the NFC code here.
    CheckBox nfcOnBox;
    EditText txtTagContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(!(nfcAdapter != null && nfcAdapter.isEnabled()))
            Toast.makeText(this,"Nfc adapter is unavailable",Toast.LENGTH_LONG).show();

        nfcOnBox = (CheckBox)findViewById(R.id.nfcon);
    }

    //Qr code reader
    public void launchScanner(View v) {
        if (isCameraAvailable()) {
            Intent intent = new Intent(this, ZBarScannerActivity.class);
            intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
            startActivityForResult(intent, ZBAR_SCANNER_REQUEST);
        } else {
            Toast.makeText(this, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isCameraAvailable() {
        PackageManager pm = getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ZBAR_SCANNER_REQUEST:
            case ZBAR_QR_SCANNER_REQUEST:
                if (resultCode == RESULT_OK) {
                    //Toast.makeText(this, "Scan Result = " + data.getStringExtra(ZBarConstants.SCAN_RESULT), Toast.LENGTH_SHORT).show();
                    String result = data.getStringExtra(ZBarConstants.SCAN_RESULT);

                    InputStream is = null;
                    KeyValue keyvalue = new KeyValue();
                    keyvalue.setKeyValue(result);
                    try {
                        Bitmap bitmap = new KeyValueHandler().execute(keyvalue.getKeyValue()).get();
                        if(bitmap !=null) {
                            Log.i("test", String.valueOf(bitmap.getWidth()) + "1224나오면댐");

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();

                            Intent intent = new Intent(this, ImageActivity.class);
                            intent.putExtra("bitmap", byteArray);
                            startActivity(intent);
                            finish();
                        }else
                            Toast.makeText(this, "Can't find key value", Toast.LENGTH_SHORT).show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.i("test",e.getMessage()+"InterruptedException");
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        Log.i("test", e.getMessage()+"ExecutionException");
                    }

                } else if(resultCode == RESULT_CANCELED && data != null) {
                    String error = data.getStringExtra(ZBarConstants.ERROR_INFO);
                    if(!TextUtils.isEmpty(error)) {
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    /*------------------------------------------from here, nfc code stated.-------------------------------------*/
    //put a priority to this activity for NFC
    private void enableForegroundDispatchSystem(){
        Intent intent = new Intent(this, ReadActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);


    }
    private void disalbeForegroundDispatchSystem(){

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Toast.makeText(this,"NFC Intent received!!!",Toast.LENGTH_SHORT).show();

        if(nfcOnBox.isChecked())        //read
        {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if(parcelables != null && parcelables.length>0)
            {
                ExploitingText et = new ExploitingText();
                KeyValue keyvalue = new KeyValue()      ;
                keyvalue.setKeyValue(et.readTextFromMessage((NdefMessage) parcelables[0]));

                Bitmap bitmap = null;
                try {
                    bitmap = new KeyValueHandler().execute(keyvalue.getKeyValue()).get();
                    if(bitmap != null) {
                        Log.i("test", String.valueOf(bitmap.getWidth()) + "1224나오면댐");

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        Intent intentForNFC = new Intent(this, ImageActivity.class);
                        intentForNFC.putExtra("bitmap", byteArray);
                        startActivity(intentForNFC);
                    }
                    else
                        Toast.makeText(this, "Can't find key value", Toast.LENGTH_SHORT).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(this,"No NDEF message found!",Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onResume() {

        enableForegroundDispatchSystem();

        super.onResume();
    }

    @Override
    protected void onPause() {

        nfcAdapter.disableForegroundNdefPush(this);

        disalbeForegroundDispatchSystem();

        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
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



}