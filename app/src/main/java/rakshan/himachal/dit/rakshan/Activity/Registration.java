package rakshan.himachal.dit.rakshan.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rakshan.himachal.dit.rakshan.Helper.AppStatus;
import rakshan.himachal.dit.rakshan.Presentation.Custom_Dialog;
import rakshan.himachal.dit.rakshan.R;
import rakshan.himachal.dit.rakshan.Utils.EConstants;

public class Registration extends AppCompatActivity {

    EditText OTP_Server_et,etmobile_et,etname_et;
    Button back,register;
    Custom_Dialog CD = new Custom_Dialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        OTP_Server_et = (EditText)findViewById(R.id.otp_server);
        etmobile_et   = (EditText)findViewById(R.id.etmobile);
        etname_et     = (EditText)findViewById(R.id.etname);
        back          = (Button)findViewById(R.id.back);
        register      = (Button)findViewById(R.id.register);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Testing","Testing");
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Testing","Testing");
                String PhoneNumber_Service = etmobile_et.getText().toString().trim();
                String Name_Service = etname_et.getText().toString().trim();


                if(Name_Service.length()!= 0 && Name_Service!= null){
                    if (PhoneNumber_Service.length() == 10 && Integer.parseInt(PhoneNumber_Service.substring(0,1)) > 6) {


                            if(AppStatus.getInstance(Registration.this).isOnline()) {

                                //Send Data To Server
                               }else{
                                CD.showDialog(Registration.this,"Please Connect to Internet");
                                CD.showDialog(Registration.this,"Data Saved Offline");

                                //Save Data
                                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                               String IMEI_SERVER =  telephonyManager.getDeviceId().toString().trim();
                                try {
                                    saveDataSharedPref(Name_Service, PhoneNumber_Service, IMEI_SERVER);
                                }catch(Exception e){
                                    CD.showDialog(Registration.this,e.getLocalizedMessage().toString());
                                }
                            }




                    } else {
                        CD.showDialog(Registration.this, "Please enter a valid 10 digit Mobile number");
                    }

                }else{
                    CD.showDialog(Registration.this,"Please enter your Name.");
                }
            }
        });






    }

    private void saveDataSharedPref(String name_service, String phoneNumber_service, String imei_server) {

        // User has successfully logged in, save this information
        //  We need an Editor object to make preference changes.
        SharedPreferences settings = getSharedPreferences(EConstants.PREF_SHARED, 0); // 0 - for private mode
        SharedPreferences.Editor editor = settings.edit();
        //Set "hasLoggedIn" to true
        editor.putBoolean("RegistrationFlag", true);
        editor.putString("Name",name_service);
        Log.e("Name",name_service);
        editor.putString("phonenumber",phoneNumber_service);
        Log.e("phonenumber",phoneNumber_service);
        editor.putString("IMEI",imei_server);
        Log.e("IMEI",imei_server);
        // Commit the edits!
        editor.commit();
        Intent i = new Intent(Registration.this,MainActivity_Navigation_Drawer.class);
        startActivity(i);
        Registration.this.finish();
    }

}
