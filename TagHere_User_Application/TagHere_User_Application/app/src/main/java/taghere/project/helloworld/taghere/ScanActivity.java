package taghere.project.helloworld.taghere;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;


import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import taghere.project.helloworld.taghere.CameraSources.*;
import taghere.project.helloworld.taghere.ForCommunicate.ExploitingText;
import taghere.project.helloworld.taghere.ForCommunicate.KeyValue;
import taghere.project.helloworld.taghere.ForCommunicate.KeyValueHandler;

public class ScanActivity extends Activity {
    private CameraSurface cameraSurface;
    private TextView qrCodeDataView;
    private boolean scanQR;
    private boolean linkPreview;

    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        cameraSurface = (CameraSurface) findViewById(R.id.camera_surdface);
        qrCodeDataView = (TextView) findViewById(R.id.qr_code_data);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(!(nfcAdapter != null && nfcAdapter.isEnabled()))
            Toast.makeText(this,"Nfc adapter is unavailable",Toast.LENGTH_LONG).show();

        scanQR = false;
        linkPreview = false;
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {
        if(cameraSurface != null && cameraSurface.getCamera() != null) {
            cameraSurface.releaseCameraAndPreview();
        }
        super.onStop();
    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
        }
    }

    public void onCameraPreviewClick(View v) {
        scanQR = true;

        if(!linkPreview) {
            cameraSurface.getCamera().setPreviewCallback(mPreview);
            linkPreview = true;
        }
        cameraSurface.getCamera().autoFocus(mAutoFocus);
    }

    Camera.AutoFocusCallback mAutoFocus = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                scanQR = true;
            }
        }
    };

    Camera.PreviewCallback mPreview = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data,
                    camera.getParameters().getPreviewSize().width,
                    camera.getParameters().getPreviewSize().height, 0, 0,
                    camera.getParameters().getPreviewSize().width,
                    camera.getParameters().getPreviewSize().height, false);

            BinaryBitmap b = new BinaryBitmap(new HybridBinarizer(source));
            QRCodeReader reader = new QRCodeReader();

            try {
                Result result = reader.decode(b);

                // When QR code read Success
                if (result.getText() != null) {
                    Log.i("FIND QR!", ">>" + result.getText());
                    qrCodeDataView.setText(result.getText());
                    Intent floorPlanActivity = new Intent(ScanActivity.this, FloorPlanActivity.class);
                    startActivity(floorPlanActivity);
                }

            } catch (NotFoundException e) {
                Log.i("err QR", "Cannot find QR code");
            } catch (ChecksumException e) {
                Log.i("err QR", "Checksum error in QR code");
            } catch (FormatException e) {
                Log.i("err QR", "Format error in QR code");
            }
        }
    };

    //put a priority to this activity for NFC
    private void enableForegroundDispatchSystem(){
        Intent intent = new Intent(this, ScanActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem(){

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Toast.makeText(this,"NFC Intent received!!!",Toast.LENGTH_LONG).show();

        if(true)//nfcOnBox.isChecked())        //read
        {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if(parcelables != null && parcelables.length > 0)
            {
                readTextFromMessage((NdefMessage) parcelables[0]);
                Intent floorPlanActivity = new Intent(this, FloorPlanActivity.class);
                startActivity(floorPlanActivity);

//                ExploitingText et = new ExploitingText();
//                KeyValue keyvalue = new KeyValue()      ;
//                keyvalue.setKeyValue(et.readTextFromMessage((NdefMessage) parcelables[0]));
//
//                Bitmap bitmap = null;
//                try {
//                    bitmap = new KeyValueHandler().execute(keyvalue.getKeyValue()).get();
//                    Log.i("test", String.valueOf(bitmap.getWidth()) + "1224나오면댐");
//
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
//                    byte[] byteArray = stream.toByteArray();
//
//                    Intent intentForNFC = new Intent(this, ImageActivity.class);
//                    intentForNFC.putExtra("bitmap",byteArray);
//                    startActivity(intentForNFC);
//                    finish();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//                Log.i("test", String.valueOf(bitmap.getWidth()) + "1224나오면댐");


            }
            else
            {
                Toast.makeText(this,"No NDEF message found!",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length>0){

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            qrCodeDataView.setText(tagContent);
        }
        else{
            Toast.makeText(this,"No NDEF records found!", Toast.LENGTH_SHORT).show();
        }
    }


    public String getTextFromNdefRecord(NdefRecord ndefRecord)
    {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding;
            if((payload[0] & 128) == 0) {
                textEncoding = "UTF-8";
            }
            else {
                textEncoding = "UTF-16";
            }
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1,
                    payload.length - languageSize - 1, textEncoding);
        }catch (UnsupportedEncodingException e)
        {
            Log.e("getTextFromNdefRecord", e.getMessage(),e);
        }
        return tagContent;
    }

    @Override
    protected void onResume() {

//        enableForegroundDispatchSystem();

        super.onResume();
    }

    @Override
    protected void onPause() {

//        nfcAdapter.disableForegroundNdefPush(this);

        disableForegroundDispatchSystem();

        super.onPause();
    }
}
