package rakshan.himachal.dit.sms.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rakshan.himachal.dit.sms.R;
import rakshan.himachal.dit.sms.Utils.EConstants;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences settings = getSharedPreferences(EConstants.PREF_SHARED, 0);
                //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
                //   boolean hasParkingSelected_ = settings.getBoolean("hasParkingSelected", false);

                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                   /* Intent login_Intent = new Intent(SplashScreen.this, Permissions.class); //Login_Activity
                    SplashScreen.this.startActivity(login_Intent);
                    SplashScreen.this.finish();*/
                    boolean Flag_Permissions = settings.getBoolean("PermissionsFlag", false);
                    boolean Flag_Registration = settings.getBoolean("Login", false);  //Registration flag is the login one

                    if (Flag_Permissions) {

                        if (Flag_Registration) {
                            Intent mainIntent = new Intent(SplashScreen.this, MainActivity_Navigation_Drawer.class);
                            SplashScreen.this.startActivity(mainIntent);
                            SplashScreen.this.finish();
                        } else {
                            Intent mainIntent = new Intent(SplashScreen.this, Login.class);
                            SplashScreen.this.startActivity(mainIntent);
                            SplashScreen.this.finish();
                        }


                    } else {
                        Intent login_Intent = new Intent(SplashScreen.this, Permissions.class); //Login_Activity
                        SplashScreen.this.startActivity(login_Intent);
                        SplashScreen.this.finish();

                    }
                } else {
                    // do something for phones running an SDK before lollipop
                    boolean Flag_Registration = settings.getBoolean("Login", false);
                    if (Flag_Registration) {
                        Intent mainIntent = new Intent(SplashScreen.this, MainActivity_Navigation_Drawer.class);
                        SplashScreen.this.startActivity(mainIntent);
                        SplashScreen.this.finish();
                    } else {
                        Intent mainIntent = new Intent(SplashScreen.this, Login.class);
                        SplashScreen.this.startActivity(mainIntent);
                        SplashScreen.this.finish();
                    }
                /*    Intent mainIntent = new Intent(SplashScreen.this, MainActivity_Navigation_Drawer.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();*/
                }


            }
        }, 2000);
    }
}

