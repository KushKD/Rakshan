package rakshan.himachal.dit.sms.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kuush on 11/16/2016.
 */

public class Prefrences {


        public static void putStringsInPreferences(Context context, Map<String, String> prefData) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(EConstants.PREF_SHARED, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Login", prefData.get(Boolean.valueOf("Login")));
            editor.putString("Name", prefData.get("Name"));
            editor.putString("phonenumber", prefData.get("phonenumber"));
            editor.putString("IMEI", prefData.get("IMEI"));

            Log.e("Login",prefData.get(Boolean.valueOf("Login")));
            Log.e("Name",prefData.get("Name"));
            Log.e("phonenumber",prefData.get("phonenumber"));
            Log.e("IMEI",prefData.get("IMEI"));
            editor.commit();
        }

        public static Map<String, String> getDataFromPreferences(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(EConstants.PREF_SHARED, Context.MODE_PRIVATE);

            Boolean Registration = sharedPreferences.getBoolean("Login", Boolean.valueOf(""));
            String name  = sharedPreferences.getString("Name","");
            String Mobile_No = sharedPreferences.getString("phonenumber","");
            String IMEI = sharedPreferences.getString("IMEI","");

            Map<String,String> PrefData = new HashMap<>();
            PrefData.put("Login",Boolean.toString(Registration));
            PrefData.put("Name",name);
            PrefData.put("phonenumber",Mobile_No);
            PrefData.put("IMEI",IMEI);
            return PrefData;
        }

    public static String getPhoneNumberFromPrefrences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(EConstants.PREF_SHARED, Context.MODE_PRIVATE);
        String Mobile_No = sharedPreferences.getString("phonenumber","");
        return Mobile_No;
    }

    public static String getNameFromPrefrences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(EConstants.PREF_SHARED, Context.MODE_PRIVATE);
        String name  = sharedPreferences.getString("Name","");
        return name;
    }

    public static String getIMEIFromPrefrences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(EConstants.PREF_SHARED, Context.MODE_PRIVATE);
        String IMEI = sharedPreferences.getString("IMEI","");
        return IMEI;
    }

}
