package rakshan.himachal.dit.rakshan.Services;

/**
 * Created by kuush on 10/28/2016.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import rakshan.himachal.dit.rakshan.Helper.GPSTracker;
import rakshan.himachal.dit.rakshan.Utils.EConstants;

import static android.R.attr.name;


public class SendLocationService extends Service{


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences prfs = getSharedPreferences(EConstants.PREF_SHARED, Context.MODE_PRIVATE);

        String name  = prfs.getString("Name","");
        String Mobile_No = prfs.getString("phonenumber","");
        String IMEI = prfs.getString("IMEI","");


        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            String stringLongitude = String.valueOf(gpsTracker.longitude);
            String country = gpsTracker.getCountryName(this);
            String city = gpsTracker.getLocality(this);
            String message = " SOS MESSAGE:- \t"+ stringLatitude + " " + stringLongitude + " " + country + " " + city + " " + name + " " + Mobile_No +" "+ IMEI;
            Log.d("INFO LOC", message);
            SmsManager.getDefault().sendTextMessage("+919459619235", null, message, null, null);
            Log.e("INFO LOC", message);
        } else {
            gpsTracker.showSettingsAlert();
        }

        return START_STICKY;
    }
}