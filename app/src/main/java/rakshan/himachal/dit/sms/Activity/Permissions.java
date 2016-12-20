package rakshan.himachal.dit.sms.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rakshan.himachal.dit.permissions.RakshamPermissionResponse;
import rakshan.himachal.dit.permissions.RakshamPermissions;
import rakshan.himachal.dit.sms.Presentation.Custom_Dialog;
import rakshan.himachal.dit.sms.R;
import rakshan.himachal.dit.sms.Utils.EConstants;

public class Permissions extends AppCompatActivity implements View.OnClickListener,RakshamPermissions.OnRequestPermissionsBack{

    private static final String TAG = "MainActivity";
    private TextView camera,gps,call,sms_status,internet_status,imei_status;
    private Button checkButton,proceed;
    Custom_Dialog CD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        initViews();
    }

    private void initViews() {
        gps = (TextView)findViewById(R.id.gpsStatus);
        call = (TextView)findViewById(R.id.callStatus);
        camera = (TextView) findViewById(R.id.cameraStatus);
        sms_status = (TextView)findViewById(R.id.sms_status);
        checkButton = (Button) findViewById(R.id.checkButton);
        internet_status = (TextView)findViewById(R.id.internet_status);
        imei_status = (TextView)findViewById(R.id.imei_status);
        checkButton.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        try {

            new RakshamPermissions.Builder(this)
                    .withPermissions(Manifest.permission.CAMERA,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.INTERNET,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS)
                    .requestId(1)
                    .setListener(this)
                    .check();



        }catch(Exception e){
            CD.showDialog(Permissions.this,"Something Went wrong while setting the permissions.");
        }

    }

    @Override
    public void onRequestBack(RakshamPermissionResponse RakshamResponse) {

        SharedPreferences settings = getSharedPreferences(EConstants.PREF_SHARED, 0); // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();
        //Set "hasLoggedIn" to true
        editor.putBoolean("PermissionsFlag", true);
        editor.commit();

        Intent i = new Intent(Permissions.this,Login.class);   //Working
       // Intent i = new Intent(Permissions.this,MainActivity_Navigation_Drawer.class);
        startActivity(i);
        Permissions.this.finish();
      /*  if(RakshamResponse.isGranted(Manifest.permission.CAMERA)) {
            camera.setText("Allow");
            camera.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        if(RakshamResponse.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            gps.setText("Allow");
            gps.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        }
        if(RakshamResponse.isGranted(Manifest.permission.CALL_PHONE)) {
            call.setText("Allow");
            call.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        } if(RakshamResponse.isGranted(Manifest.permission.SEND_SMS)) {
            sms_status.setText("Allow");
            sms_status.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        }if(RakshamResponse.isGranted(Manifest.permission.INTERNET)) {
            internet_status.setText("Allow");
            internet_status.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        }if(RakshamResponse.isGranted(Manifest.permission.READ_PHONE_STATE)) {
            internet_status.setText("Allow");
            internet_status.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        }

        if(RakshamResponse.isOnNeverAskAgain(Manifest.permission.CAMERA))
            Log.d(TAG, "onRequestBack: CAMERA");*/
    }

    }

