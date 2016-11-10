package rakshan.himachal.dit.autosmsread;

/**
 * Created by kuush on 11/9/2016.
 */

import android.Manifest;
import android.app.Activity;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

public class RakshanSmsVerifyCatcher {
    private final static int PERMISSION_REQUEST_CODE = 12;

    private Activity activity;
    private Fragment fragment;
    private RakshanOnSmsCatchListener<String> onSmsCatchListener;
    private RakshanSmsReceiver smsReceiver;
    private String phoneNumber;
    private String filter;

    public RakshanSmsVerifyCatcher(Activity activity, RakshanOnSmsCatchListener<String> onSmsCatchListener) {
        this.activity = activity;
        this.onSmsCatchListener = onSmsCatchListener;
        smsReceiver = new RakshanSmsReceiver();
        smsReceiver.setCallback(this.onSmsCatchListener);
    }

    public RakshanSmsVerifyCatcher(Activity activity, Fragment fragment, RakshanOnSmsCatchListener<String> onSmsCatchListener) {
        this(activity, onSmsCatchListener);
        this.fragment = fragment;
    }

    public void onStart() {
        if (isStoragePermissionGranted(activity, fragment)) {
            registerReceiver();
        }
    }

    private void registerReceiver() {
        smsReceiver = new RakshanSmsReceiver();
        smsReceiver.setCallback(onSmsCatchListener);
        smsReceiver.setPhoneNumberFilter(phoneNumber);
        smsReceiver.setFilter(filter);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        activity.registerReceiver(smsReceiver, intentFilter);
    }

    public void setPhoneNumberFilter(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void onStop() {
        activity.unregisterReceiver(smsReceiver);
    }

    public void setFilter(String regexp) {
        this.filter = regexp;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                registerReceiver();
            }
        }
    }


    //For fragments
    public static boolean isStoragePermissionGranted(Activity activity, Fragment fragment) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_SMS)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS)
                            == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                if (fragment == null) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.RECEIVE_SMS,
                                    Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE);
                } else {
                    fragment.requestPermissions(
                            new String[]{Manifest.permission.RECEIVE_SMS,
                                    Manifest.permission.READ_SMS}, PERMISSION_REQUEST_CODE);
                }
                return false;
            }
        } else {
            return true;
        }
    }
}