package rakshan.himachal.dit.sms.Helper;

/**
 * Created by kuush on 10/29/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import rakshan.himachal.dit.sms.Utils.EConstants;


public class AppStatus {

    private static AppStatus instance = new AppStatus();
    static Context context;
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;

    public static AppStatus getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected() || networkInfo.isConnectedOrConnecting();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }


    /**
     * GET IMEI
     * @param context
     * @return
     */
    public static String GetIMEI(Context context){
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI_SERVER =  telephonyManager.getDeviceId().toString().trim();
        return IMEI_SERVER;
    }

    /**
     * Returns a Bitmap from SD Card
     * @param context
     * @param name
     * @return
     */

    public static Bitmap getBitmap(Context context, String name){
        String imageInSD = Environment.getExternalStorageDirectory().getAbsolutePath() + EConstants.FOLDER_NAME +"/" + name ;
        Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
        return bitmap;
    }





}