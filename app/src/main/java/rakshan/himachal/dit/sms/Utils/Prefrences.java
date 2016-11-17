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
            editor.putString("RegistrationFlag", prefData.get(Boolean.valueOf("RegistrationFlag")));
            editor.putString("Name", prefData.get("Name"));
            editor.putString("phonenumber", prefData.get("phonenumber"));
            editor.putString("IMEI", prefData.get("IMEI"));

            Log.e("Registration",prefData.get(Boolean.valueOf("RegistrationFlag")));
            Log.e("Name",prefData.get("Name"));
            Log.e("phonenumber",prefData.get("phonenumber"));
            Log.e("IMEI",prefData.get("IMEI"));
            editor.commit();
        }

        public static Map<String, String> getStringFromPreferences(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(EConstants.PREF_SHARED, Context.MODE_PRIVATE);

            Boolean Registration = sharedPreferences.getBoolean("RegistrationFlag", Boolean.valueOf(""));
            String name  = sharedPreferences.getString("Name","");
            String Mobile_No = sharedPreferences.getString("phonenumber","");
            String IMEI = sharedPreferences.getString("IMEI","");

            Map<String,String> PrefData = new HashMap<>();
            PrefData.put("RegistrationFlag",Boolean.toString(Registration));
            PrefData.put("Name",name);
            PrefData.put("phonenumber",Mobile_No);
            PrefData.put("IMEI",IMEI);
            return PrefData;
        }

}
