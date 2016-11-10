package rakshan.himachal.dit.autosmsread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by kuush on 11/9/2016.
 */

class RakshanSmsReceiver extends BroadcastReceiver {
    private RakshanOnSmsCatchListener<String> callback;
    private String phoneNumberFilter;
    private String filter;

    /**
     * Set result callback
     * @param callback RakshanOnSmsCatchListener
     */
    public void setCallback(RakshanOnSmsCatchListener<String> callback) {
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    if (phoneNumberFilter != null && !phoneNumber.equals(phoneNumberFilter)) {
                        return;
                    }
                    String message = currentMessage.getDisplayMessageBody();
                    if (filter != null && !message.matches(filter)) {
                        return;
                    }

                    if (callback != null) {
                        callback.onSmsCatch(message);
                    }
                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("RakshanSmsReceiver", "Exception smsReceiver" + e);
        }
    }

    /**
     * Set phone number filter
     *
     * @param phoneNumberFilter phone number
     */
    public void setPhoneNumberFilter(String phoneNumberFilter) {
        this.phoneNumberFilter = phoneNumberFilter;
    }

    /**
     * set message filter with regexp
     *
     * @param regularExpression regexp
     */
    public void setFilter(String regularExpression) {
        this.filter = regularExpression;
    }
}
