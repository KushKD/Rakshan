package rakshan.himachal.dit.sms.Services;

/**
 * Created by kuush on 10/28/2016.
 */

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import rakshan.himachal.dit.sms.Enum.TaskType;
import rakshan.himachal.dit.sms.Helper.AppStatus;
import rakshan.himachal.dit.sms.Helper.Date_Time;
import rakshan.himachal.dit.sms.Helper.GPSTracker;
import rakshan.himachal.dit.sms.Interfaces.AsyncTaskListener;
import rakshan.himachal.dit.sms.JsonManager.JsonParser;
import rakshan.himachal.dit.sms.Utils.EConstants;
import rakshan.himachal.dit.sms.Utils.Generic_Async_Post;
import rakshan.himachal.dit.sms.Utils.Prefrences;


public class SendLocationService extends Service implements AsyncTaskListener{

    Map<String, String> map = null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       /*  map  = new HashMap<>();
        map = Prefrences.getStringFromPreferences(SendLocationService.this);

        Boolean Registration = Boolean.parseBoolean(map.get("RegistrationFlag"));
        String name = map.get("Name");
        String Mobile_No = map.get("phonenumber");
        String IMEI = map.get("IMEI");

        Log.e("Registration",Boolean.toString(Registration));
        Log.e("Name",name);
        Log.e("phonenumber",Mobile_No);
        Log.e("IMEI",IMEI);*/

        String sent = "SMS_SENT";
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

            if(AppStatus.getInstance(SendLocationService.this).isOnline()){
                Log.e("We are Online","Sending JSON Object");
                new Generic_Async_Post(SendLocationService.this, SendLocationService.this, TaskType.SOS).execute(
                        "getSOSRequest_JSON",
                        EConstants.URL+"getSOSRequest_JSON",
                        stringLatitude,
                        stringLongitude,
                        name,
                        Mobile_No,
                        IMEI,
                        "",
                        Date_Time.GetDateAndTime(),
                        "",
                        "",
                        "");
            }else{
                //SND SMS
                Log.e("We are OffLine","Sending SMS");
                try {
                    PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                            new Intent(sent), 0);

                    //---when the SMS has been sent---
                    registerReceiver(new BroadcastReceiver(){
                        @Override
                        public void onReceive(Context arg0, Intent arg1) {
                            if(getResultCode() == Activity.RESULT_OK)
                            {
                                Toast.makeText(getBaseContext(), "SMS sent",
                                        Toast.LENGTH_SHORT).show();
                                Log.e("SMS Message:-","SMS sent");
                            }
                            else
                            {
                                Toast.makeText(getBaseContext(), "SMS could not sent",
                                        Toast.LENGTH_SHORT).show();
                                Log.e("SMS Message:-","SMS could not sent");
                            }
                        }
                    }, new IntentFilter(sent));
                    SmsManager.getDefault().sendTextMessage("+919418497722", null, message, sentPI, null);
                }catch (Exception e){
                    Log.e("Unable to send Message", e.getLocalizedMessage().toString().trim());
                }
            }


           // Log.e("INFO LOC", message);
        } else {
            gpsTracker.showSettingsAlert();
        }

        return START_STICKY;
    }

    @Override
    public void onTaskCompleted(String result, TaskType taskType) {

        if(taskType == TaskType.SOS){
            Log.e("Getting Result",result);
            String Result_to_Show = null;
            Result_to_Show = JsonParser.Vehicle_Out_Confirm_Parse(result);
            Toast.makeText(getApplicationContext(),Result_to_Show,Toast.LENGTH_LONG).show();
        }else{

        }

    }
}