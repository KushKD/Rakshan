package rakshan.himachal.dit.rakshan;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rakshan.himachal.dit.permissions.RakshamPermissionResponse;
import rakshan.himachal.dit.permissions.RakshamPermissions;

public class Permissions extends AppCompatActivity implements View.OnClickListener,RakshamPermissions.OnRequestPermissionsBack{

    private static final String TAG = "MainActivity";
    private TextView camera,gps,call,sms_status,internet_status;
    private Button checkButton,proceed;

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
        proceed = (Button)findViewById(R.id.proceed);
        internet_status = (TextView)findViewById(R.id.internet_status);
        checkButton.setOnClickListener(this);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Permissions.this,MainActivity_Navigation_Drawer.class);
                startActivity(i);
               Permissions.this.finish();
            }
        });
    }


    @Override
    public void onClick(View v) {

        new RakshamPermissions.Builder(this)
                .withPermissions(Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.SEND_SMS,Manifest.permission.INTERNET)
                .requestId(1)
                .setListener(this)
                .check();

    }

    @Override
    public void onRequestBack(RakshamPermissionResponse RakshamResponse) {
        if(RakshamResponse.isGranted(Manifest.permission.CAMERA)) {
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

        }

        if(RakshamResponse.isOnNeverAskAgain(Manifest.permission.CAMERA))
            Log.d(TAG, "onRequestBack: CAMERA");
    }

    }

