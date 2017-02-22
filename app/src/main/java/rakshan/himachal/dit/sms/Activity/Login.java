package rakshan.himachal.dit.sms.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.dit.kushkumardhawan.com.materialhelp.MaterialTutorialActivity;
import org.dit.kushkumardhawan.com.materialhelp.TutorialItem;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import rakshan.himachal.dit.autosmsread.RakshanOnSmsCatchListener;
import rakshan.himachal.dit.autosmsread.RakshanSmsVerifyCatcher;
import rakshan.himachal.dit.sms.DatabaseHandler.DatabaseHandler;
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
import rakshan.himachal.dit.sms.Helper.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements AsyncTaskListener {

    private RakshanSmsVerifyCatcher smsVerifyCatcher;
    private static final int REQUEST_CODE = 1234;

    private TextView signUpTextView;
    private EditText otp;
    String _OTP = null;
    private Button login;
    Custom_Dialog CM = new Custom_Dialog();
    String MobileNumber = null;
    String IMEI = null;
    AutoCompleteTextView phone;
    ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // check flag for introduvtion
        loadTutorial();

        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        login = (Button) findViewById(R.id.login);

        phone = (AutoCompleteTextView) findViewById(R.id.phone_number);
        phone.addTextChangedListener(GET_OTP);
        otp = (EditText) findViewById(R.id.otp);
        imageView1 = (ImageView) findViewById(R.id.imageView1);

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
        });     // Working

        //set phone number filter if needed
         smsVerifyCatcher.setPhoneNumberFilter("TM-HPGOVT");  //working

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

    public void loadTutorial() {
        Intent mainAct = new Intent(this, MaterialTutorialActivity.class);
        mainAct.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));
        startActivityForResult(mainAct, REQUEST_CODE);

    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem(R.string.slide_1_african_story_books, R.string.slide_1_african_story_books_Old,
                R.color.slide_1, R.drawable.tut_page_1_front, R.drawable.tut_page_1_background);

        TutorialItem tutorialItem2 = new TutorialItem(R.string.slide_2_volunteer_professionals, R.string.slide_2_volunteer_professionals_subtitle,
                R.color.slide_2, R.drawable.tut_page_2_front, R.drawable.tut_page_2_background);

        TutorialItem tutorialItem3 = new TutorialItem(context.getString(R.string.slide_3_download_and_go), null,
                R.color.slide_3, R.drawable.tut_page_3_foreground);

        TutorialItem tutorialItem4 = new TutorialItem(R.string.slide_4_different_languages, R.string.slide_4_different_languages_subtitle,
                R.color.slide_4, R.drawable.tut_page_4_foreground, R.drawable.tut_page_4_background);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //    super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Toast.makeText(this, "All Set", Toast.LENGTH_LONG).show();

        }
    }

    private void getOtpandAadhaa() {

        _OTP = otp.getText().toString().trim();
        String _Mobile = phone.getText().toString().trim();
        if (!_OTP.isEmpty()) {
            if (_OTP.length() == 6) {
                if (AppStatus.getInstance(Login.this).isOnline()) {
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
                    Log.e("OTP URL", url2);
                    new Generic_Async_Get(Login.this, Login.this, TaskType.VERIFY_OTP).execute(url2);
                } else {
                    CM.showDialog(Login.this, "Network isn't available");

                }
            } else {
                CM.showDialog(Login.this, "Please Enter a valid OTP");
            }
        } else {
            CM.showDialog(Login.this, "OTP Cannot be empty.");
        }

    }

    private final TextWatcher GET_OTP = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // textView.setVisibility(View.VISIBLE);
        }

        public void afterTextChanged(Editable s) {
            if (s.length() == 10 && Integer.parseInt(s.toString().substring(0, 1)) > 6) {

                MobileNumber();
            } else {
                // aadhaar_et.setBackgroundResource(R.drawable.rounded_edittext);
                // CM.showDialog(Login.this,"Please Enter a valid Phone Number.");
                Log.e("Aadhaar ", "Not Valid");
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

        Log.e("Server Message", result);

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

        } else if (taskType == TaskType.VERIFY_OTP) {
            JsonParser JP = new JsonParser();
            finalResult = JP.ParseStringOTP(result);
            if (finalResult.length() > 100) {
                Log.e("GOT IT", finalResult);
                // editText_aadhaarLogin.setEnabled(false);
                // editText_otpLogin.setEnabled(true);
                try {
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
                                    PGP.setPhotoName(myJson.optString("PhotoName"));
                                    PGP.setGender(myJson.optString("Gender"));
                                    PGP.setResAadhaar(myJson.optString("ResAadhaar"));
                                    PGP.setEmail(myJson.optString("EMail"));
                                    PGP.setPhoto(Base64.decode(myJson.getString("Photo"), Base64.DEFAULT));
                                    PGP.setDateTime(Date_Time.GetDateAndTime());

                                    try{
                                        saveDataDatabase(PGP);
                                    }catch(Exception ex){
                                        CM.showDialog(Login.this,ex.getLocalizedMessage().toString());
                                    }


                                }


                            } catch (Exception e) {
                                CM.showDialog(Login.this, "There is some Error.");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    CM.showDialog(Login.this, "Data Isn't Saved");
                }
            } else {
                CM.showDialog(Login.this, finalResult);
            }
        } else {
            CM.showDialog(Login.this, "Something is not Good");
        }
    }

    private void saveDataDatabase(UserDetails pgp) {

        try{
            DatabaseHandler DH = new DatabaseHandler(Login.this);
            if( DH.addContact(pgp)){

                File folder = new File(Environment.getExternalStorageDirectory() + EConstants.FOLDER_NAME);
                boolean foldersuccess = true;
                if (!folder.exists()) {
                    foldersuccess = folder.mkdir();
                }
                if (foldersuccess) {
                    // Do something on success
                } else {
                    // Do something else on failure
                }

                File sdCardDirectory = Environment.getExternalStorageDirectory();
                File image = new File(sdCardDirectory + EConstants.FOLDER_NAME, pgp.getPhotoName());

                boolean success = false;

                // Encode the file as a PNG image.
                FileOutputStream outStream;
                try {
                    byte[] byteArrayphoto = pgp.getPhoto();
                    Bitmap bmp1 = BitmapFactory.decodeByteArray(byteArrayphoto, 0, byteArrayphoto.length);
                    outStream = new FileOutputStream(image);
                    bmp1.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                    success = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (success) {
                   CM.showDialog(Login.this,"Image saved with success");
                } else {
                    CM.showDialog(Login.this,"Error during image saving");
                }


                // CM.showDialog(Login.this,"Data Saved Successfully");

                SharedPreferences settings = getSharedPreferences(EConstants.PREF_SHARED, 0); // 0 - for private mode
                SharedPreferences.Editor editor = settings.edit();
                //Set "hasLoggedIn" to true

                editor.putBoolean("Login", true);
                editor.commit();


                Intent i = new Intent(Login.this, MainActivity_Navigation_Drawer.class);
                startActivity(i);
                Login.this.finish();

            }else{
                CM.showDialog(Login.this,"Unable to Save Data. Something bad happened , please restart the application.");
            }
        }catch(Exception ex){
            CM.showDialog(Login.this,ex.getLocalizedMessage().toString().trim());
        }

    }

    private void saveDataSharedPref(UserDetails PGP) {

        try {
            // User has successfully logged in, save this information
            //  We need an Editor object to make preference changes.
            SharedPreferences settings = getSharedPreferences(EConstants.PREF_SHARED, 0); // 0 - for private mode
            SharedPreferences.Editor editor = settings.edit();
            //Set "hasLoggedIn" to true

            editor.putBoolean("RegistrationFlag", true);
            editor.putString("Name", PGP.getName());
            Log.e("Name", PGP.getName());
            editor.putString("phonenumber", PGP.getMobile());
            Log.e("phonenumber",  PGP.getMobile());
            editor.putString("IMEI",  PGP.getIMEI());
            Log.e("IMEI", PGP.getIMEI());
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
          /*  Intent i = new Intent(Login.this, MainActivity_Navigation_Drawer.class);
            startActivity(i);
            Login.this.finish();*/

        } catch (Exception e) {

            CM.showDialog(Login.this, "Something went wrong. Please restart the application. ");
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
