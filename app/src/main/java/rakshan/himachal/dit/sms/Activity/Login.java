package rakshan.himachal.dit.sms.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import rakshan.himachal.dit.autosmsread.RakshanOnSmsCatchListener;
import rakshan.himachal.dit.autosmsread.RakshanSmsVerifyCatcher;
import rakshan.himachal.dit.sms.Enum.TaskType;
import rakshan.himachal.dit.sms.Helper.AppStatus;
import rakshan.himachal.dit.sms.Helper.VerhoeffAlgorithm;
import rakshan.himachal.dit.sms.Interfaces.AsyncTaskListener;
import rakshan.himachal.dit.sms.JsonManager.JsonParser;
import rakshan.himachal.dit.sms.Model.UserDetails;
import rakshan.himachal.dit.sms.Presentation.Custom_Dialog;
import rakshan.himachal.dit.sms.R;
import rakshan.himachal.dit.sms.Utils.EConstants;
import rakshan.himachal.dit.sms.Utils.Generic_Async_Get;
import rakshan.himachal.dit.sms.Utils.Prefrences;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements AsyncTaskListener {

    private RakshanSmsVerifyCatcher smsVerifyCatcher;

    private TextView signUpTextView;
    private EditText otp;
    String _OTP = null;
    private Button login;
    Custom_Dialog CM = new Custom_Dialog();
    String MobileNumber = null;
    String IMEI = null;
    AutoCompleteTextView phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        login = (Button) findViewById(R.id.login);

        phone = (AutoCompleteTextView) findViewById(R.id.phone_number);
        phone.addTextChangedListener(GET_OTP);
        otp   = (EditText)findViewById(R.id.otp);

        //init RakshanSmsVerifyCatcher
        smsVerifyCatcher = new RakshanSmsVerifyCatcher(this, new RakshanOnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                //String code = parseCode(message);//Parse verification code
                String code = message.substring(message.lastIndexOf(':') + 1).trim();
                Log.e("SMS is:- ",code);
                otp.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });

        //set phone number filter if needed
        smsVerifyCatcher.setPhoneNumberFilter("TM-HPGOVT");

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Registration.class);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getOtpandAadhaa();
                } catch (Exception ex) {
                    CM.showDialog(Login.this, "Something really went bad. Please try again later.");
                }

            }
        });
    }

    private void getOtpandAadhaa() {

        _OTP = otp.getText().toString().trim();
        String _Mobile = phone.getText().toString().trim();
        if(!_OTP.isEmpty()){
            if(_OTP.length()== 6){
                if(AppStatus.getInstance(Login.this).isOnline()) {
                    // OTP_Async OA = new OTP_Async();
                    // OA.execute(aadhaar_a, otp);
                    String url2 = null;
                    StringBuilder sb = new StringBuilder();
                    sb.append(EConstants.URL);
                    sb.append("getValidateOTP_JSON");  //methordname
                    sb.append("/");
                    sb.append(_Mobile);
                    sb.append("/");
                    sb.append(IMEI);
                    sb.append("/");
                    sb.append(_OTP);
                    url2 = sb.toString();
                    Log.e("OTP URL",url2);
                    new Generic_Async_Get(Login.this, Login.this, TaskType.VERIFY_OTP).execute(url2);
                } else {
                    CM.showDialog(Login.this,"Network isn't available");

                }
            }else{
                CM.showDialog(Login.this,"Please Enter a valid OTP");
            }
        }else{
            CM.showDialog(Login.this,"OTP Cannot be empty.");
        }

    }

    private final TextWatcher GET_OTP = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // textView.setVisibility(View.VISIBLE);
        }

        public void afterTextChanged(Editable s) {
            if(s.length()==10 && Integer.parseInt(s.toString().substring(0,1)) > 6){

                MobileNumber();
            }else{
               // aadhaar_et.setBackgroundResource(R.drawable.rounded_edittext);
                // CM.showDialog(Login.this,"Please Enter a valid Phone Number.");
                 Log.e("Aadhaar ","Not Valid");
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    private void MobileNumber() {
        MobileNumber = phone.getText().toString().trim();
        if (!MobileNumber.isEmpty()) {
            if (MobileNumber.length() == 10) {

                if (AppStatus.getInstance(Login.this).isOnline()) {

                    //Save Data
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                     IMEI = telephonyManager.getDeviceId().toString().trim();

                    String url = null;
                    StringBuilder sb = new StringBuilder();
                    sb.append(EConstants.URL);
                    sb.append("getOTPjson");
                    sb.append("/");
                    sb.append(MobileNumber);
                    sb.append("/");
                    sb.append(IMEI);
                    url = sb.toString();
                    Log.e("URL", url.toString());

                    new Generic_Async_Get(Login.this, Login.this, TaskType.LOGIN).execute(url);
                } else CM.showDialog(Login.this, "Network isn't available");
            } else CM.showDialog(Login.this, "Please Enter a valid Mobile Number");
        } else CM.showDialog(Login.this, "Mobile Number cannot be empty.");
    }


    @Override
    public void onTaskCompleted(String result, TaskType taskType) {

        Log.e("Server Message",result);

        String finalResult = null;
        if (taskType == TaskType.LOGIN) {
            JsonParser JP = new JsonParser();
            finalResult = JP.ParseString(result);
            if (finalResult.length() > 100) {
                CM.showDialog(Login.this, finalResult);
                // editText_aadhaarLogin.setEnabled(false);
                // editText_otpLogin.setEnabled(true);
            } else {
                CM.showDialog(Login.this, finalResult);
            }

        }else if(taskType == TaskType.VERIFY_OTP){
            JsonParser JP = new JsonParser();
            finalResult = JP.ParseStringOTP(result);
            if (finalResult.length() > 100) {
                Log.e("GOT IT",finalResult);
                // editText_aadhaarLogin.setEnabled(false);
                // editText_otpLogin.setEnabled(true);
                try{
                        //Save Data to DataBase
                    Object json = null;
                    try {
                        json = new JSONTokener(finalResult).nextValue();
                        if (json instanceof JSONObject) {
                            //Check Weather the String is object or array
                            JSONObject myJson = null;
                            UserDetails PGP = null;

                            try {
                                PGP = new UserDetails();
                                myJson = new JSONObject(finalResult);

                                Log.e("MyJSON", myJson.toString());

                                if (myJson.optString("ResName").length() <= 1) {
                                    CM.showDialog(Login.this, "Unable to register please try again.");
                                } else {
                                    PGP.setName(myJson.optString("ResName"));
                                    PGP.setIMEI(myJson.optString("IMEI"));
                                    PGP.setMobile(myJson.optString("Mobile"));

                                    Log.e("Name From Server", PGP.getName());
                                    Log.e("IMEI From Server", PGP.getIMEI());
                                    Log.e("MOBILE From Server", PGP.getMobile());
                                     saveDataSharedPref(PGP.getName(), PGP.getMobile(), PGP.getIMEI());
                                }


                            } catch (Exception e) {
                                CM.showDialog(Login.this, "There is some Error.");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }catch(Exception e){
                    CM.showDialog(Login.this,"Data Isn't Saved");
                }
            } else {
                CM.showDialog(Login.this, finalResult);
            }
        }else{
            CM.showDialog(Login.this, "Something is not Good");
        }
    }

    private void saveDataSharedPref(String name_service, String phoneNumber_service, String imei_server) {

        try{
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
            Map<String, String> PrefData = null;
        /*    try {
                PrefData = new HashMap<>();
                PrefData.put("RegistrationFlag", Boolean.toString(true));
                PrefData.put("Name", name_service);
                PrefData.put("phonenumber", phoneNumber_service);
                PrefData.put("IMEI", imei_server);
                Prefrences.putStringsInPreferences(Login.this,PrefData);
            }catch(Exception e){

            }finally{

            }*/
            Intent i = new Intent(Login.this,MainActivity_Navigation_Drawer.class);
            startActivity(i);
            Login.this.finish();

        }catch(Exception e){

            CM.showDialog(Login.this,"Something went wrong. Please restart the application. ");
        }
    }



    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only four numbers from massage string
     */
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }
}
