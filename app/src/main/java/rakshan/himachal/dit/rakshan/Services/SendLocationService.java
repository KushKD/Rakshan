package rakshan.himachal.dit.rakshan.Services;

/**
 * Created by kuush on 10/28/2016.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import rakshan.himachal.dit.rakshan.Helper.GPSTracker;


public class SendLocationService extends Service{


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        GPSTracker gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            String stringLongitude = String.valueOf(gpsTracker.longitude);
            String country = gpsTracker.getCountryName(this);
            String city = gpsTracker.getLocality(this);
            String postalCode = gpsTracker.getPostalCode(this);
            String addressLine = gpsTracker.getAddressLine(this);
            String message = " SOS MESSAGE:- \t"+stringLatitude + " " + stringLongitude + " " + country + " " + city + " " + postalCode + " " + addressLine;
            Log.d("INFO LOC", message);
            SmsManager.getDefault().sendTextMessage("+919418497722", null, message, null, null);
        } else {
            gpsTracker.showSettingsAlert();
        }

        return START_STICKY;
    }
}