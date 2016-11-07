package rakshan.himachal.dit.rakshan.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;

import rakshan.himachal.dit.rakshan.Enum.TaskType;
import rakshan.himachal.dit.rakshan.Helper.AppStatus;
import rakshan.himachal.dit.rakshan.Helper.Date_Time;
import rakshan.himachal.dit.rakshan.Helper.VerhoeffAlgorithm;
import rakshan.himachal.dit.rakshan.Interfaces.AsyncTaskListener;
import rakshan.himachal.dit.rakshan.JsonManager.Vehicle_In_Out_Json;
import rakshan.himachal.dit.rakshan.Model.Register_User;
import rakshan.himachal.dit.rakshan.Model.UserDetails;
import rakshan.himachal.dit.rakshan.Presentation.CircleImageView;
import rakshan.himachal.dit.rakshan.Presentation.Custom_Dialog;
import rakshan.himachal.dit.rakshan.R;
import rakshan.himachal.dit.rakshan.Utils.EConstants;
import rakshan.himachal.dit.rakshan.Utils.Generic_Async_Post_Activity;


import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class Registration extends AppCompatActivity implements AsyncTaskListener {

    EditText OTP_Server_et,etmobile_et,etname_et,email_et,aadhaar_et,dob_server_et;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    Button back,register;
    CircleImageView edit_Profile_IV;
    Custom_Dialog CD = new Custom_Dialog();
    String Aadhaar_Service = null;
    Bitmap bitmap;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final String IMAGE_DIRECTORY_NAME = "Cloud Uploads";
    public static int flag=0;
    private Uri fileUri = null; // file url to store image/video
    Register_User photo_Details = new Register_User();



    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;

    private DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mSelectedDay = dayOfMonth;
            mSelectedMonth = monthOfYear;
            mSelectedYear = year;
            updateDateUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

       // OTP_Server_et = (EditText)findViewById(R.id.otp_server);
        etmobile_et   = (EditText)findViewById(R.id.etmobile);
        etname_et     = (EditText)findViewById(R.id.etname);
        email_et      = (EditText)findViewById(R.id.email_server);

        aadhaar_et    = (EditText)findViewById(R.id.aadhaar_server);
         /* Set Text Watcher listener */


        aadhaar_et.addTextChangedListener(AadhaarWatcher);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        dob_server_et = (EditText)findViewById(R.id.dob_server);
        back          = (Button)findViewById(R.id.back);
        register      = (Button)findViewById(R.id.register);
        edit_Profile_IV = (CircleImageView) findViewById(R.id.user_profile_photo);




        edit_Profile_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToDo
               // Toast.makeText(getApplicationContext(), "We are Here", Toast.LENGTH_SHORT).show();
                captureImage();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Testing","Testing");
            }
        });

        dob_server_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        showDatePickerDialog(mSelectedYear, mSelectedMonth, mSelectedDay, mOnDateSetListener);

            }
        });






        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String PhoneNumber_Service = etmobile_et.getText().toString().trim();
                String Name_Service = etname_et.getText().toString().trim();
                String Aadhaar_Service = aadhaar_et.getText().toString().trim();
                String Email_Service = email_et.getText().toString().trim();
                //Save Data
                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                String IMEI_SERVER =  telephonyManager.getDeviceId().toString().trim();
                // get selected radio button from radioGroup
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) findViewById(selectedId);
               // Toast.makeText(Registration.this, radioSexButton.getText(), Toast.LENGTH_SHORT).show();

                String Gender_Service = radioSexButton.getText().toString().trim();
                String DOB_Server = dob_server_et.getText().toString().trim();


                if(Name_Service.length()!= 0 && Name_Service!= null){
                    if (PhoneNumber_Service.length() == 10 && Integer.parseInt(PhoneNumber_Service.substring(0,1)) > 6) {

                        if(Gender_Service!=null){

                            if(DOB_Server!=null&& DOB_Server.length()!=0){

                                if( Aadhaar_Service.length()==12 && VerhoeffAlgorithm.validateVerhoeff(Aadhaar_Service)){

                                    if (fileUri != null && !fileUri.getPath().isEmpty()) {
                                        Log.e("Pic Path",fileUri.getPath().toString());

                                        String fileNameSegments[] = fileUri.getPath().toString().split("/");
                                        String fileName = fileNameSegments[fileNameSegments.length - 1];

                                        // Put file name in Async Http Post Param which will used in Php web app
                                        photo_Details.setPhotoname(fileName);
                                        Log.e("Params",photo_Details.getPhotoname().toString());

                                        if(AppStatus.getInstance(Registration.this).isOnline()) {

                                            try {
                                                // Convert image to String using Base64
                                                encodeImagetoString(Name_Service,PhoneNumber_Service,Aadhaar_Service,Email_Service,IMEI_SERVER,Gender_Service,DOB_Server);
                                            }catch(Exception ex){
                                                Log.e("Image Error",ex.getLocalizedMessage().toString());
                                            }

                                        }else{
                                            CD.showDialog(Registration.this,"Please Connect to Internet");
                                        }


                                    }else{
                                        CD.showDialog(Registration.this,"Please Upload your profile Pic");
                                    }




                                }else{
                                    CD.showDialog(Registration.this,"Aadhaar Number not Valid.");
                                }

                            }else{
                                CD.showDialog(Registration.this,"Date of Birth cannot be empty.");
                            }

                        }else{
                            CD.showDialog(Registration.this,"Please select Gender.");
                        }
                    } else {
                        CD.showDialog(Registration.this, "Please enter a valid 10 digit Mobile number");
                    }

                }else{
                    CD.showDialog(Registration.this,"Please enter your Name.");
                }
            }
        });






    }

    private final TextWatcher AadhaarWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // textView.setVisibility(View.VISIBLE);
        }

        public void afterTextChanged(Editable s) {
            if(s.length()==12){

                if(VerhoeffAlgorithm.validateVerhoeff(s.toString())){
                    aadhaar_et.setBackgroundResource(R.drawable.rounded_edittext_green);
                    //Log.e("Aadhaar","Green");
                }else{
                    aadhaar_et.setBackgroundResource(R.drawable.rounded_edittext_red);
                   // Log.e("Aadhaar","Red");
                }
            }else{
                aadhaar_et.setBackgroundResource(R.drawable.rounded_edittext);
               // CD.showDialog(Registration.this,"Aadhaar Not Valid");
               // Log.e("Aadhaar ","Not Valid");
            }
        }
    };



    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private DatePickerDialog showDatePickerDialog(int initialYear, int initialMonth, int initialDay, DatePickerDialog.OnDateSetListener listener) {
        DatePickerDialog dialog = new DatePickerDialog(Registration.this, listener, initialYear, initialMonth, initialDay);
        dialog.setTitle("Select Date of Birth");
        dialog.show();
        return dialog;
    }

    private void updateDateUI() {
        String month = ((mSelectedMonth+1) > 9) ? ""+(mSelectedMonth+1): "0"+(mSelectedMonth+1) ;
        String day = ((mSelectedDay) < 10) ? "0"+mSelectedDay: ""+mSelectedDay ;
        dob_server_et.setText(mSelectedYear + "-" + month + "-" + day);
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
           Intent i = new Intent(Registration.this,MainActivity_Navigation_Drawer.class);
           startActivity(i);
           Registration.this.finish();

       }catch(Exception e){

            CD.showDialog(Registration.this,"Something went wrong. Please restart the application. ");
       }
    }

    @Override
    public void onTaskCompleted(String result, TaskType taskType) {

        if(taskType==TaskType.REGISTRATION) {
            Log.e("Getting Result", result);

            if (result.equalsIgnoreCase("Timeout")) {
                CD.showDialog(Registration.this, result);
            } else {
                String Result_to_Show = null;
                Result_to_Show = Vehicle_In_Out_Json.Registration_Parse(result);
                // Toast.makeText(getApplicationContext(),Result_to_Show,Toast.LENGTH_LONG).show();
                //saveDataSharedPref
                Object json = null;
                try {
                    json = new JSONTokener(Result_to_Show).nextValue();
                    if (json instanceof JSONObject) {
                        //Check Weather the String is object or array
                        JSONObject myJson = null;
                        UserDetails PGP = null;

                        try {
                            PGP = new UserDetails();
                            myJson = new JSONObject(Result_to_Show);

                            Log.e("MyJSON", myJson.toString());

                            if (myJson.optString("ResName").length() <= 1) {
                                CD.showDialog(Registration.this, "Unable to register please try again.");
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
                            CD.showDialog(Registration.this, "There is some Error.");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            }else{
                CD.showDialog(Registration.this, "Something went wrong. Please restart the application");
            }

    }

    /*
      Here we store the file url as it will be null after returning from camera app
    */
    @Override


    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            // successfully captured the image
            //refreshing the gallery
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(fileUri);
            this.sendBroadcast(mediaScanIntent);
            flag=1;
            launchUploadActivity(true);             // launching upload activity


        } else if (resultCode == RESULT_CANCELED) {

            // user cancelled Image capture
            Toast.makeText(getApplicationContext(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();

        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void launchUploadActivity(boolean isImage) {
       // Intent i = new Intent(Registration.this, ImageUpload.class);
       // i.putExtra("isImage", isImage);
        if(flag==1){
          //  i.putExtra("filePath", fileUri.getPath());
            previewMedia(fileUri.getPath().toString());
        }else if(flag==2) {
           // i.putExtra("picturePath", picturePath);
        }else{
            Toast.makeText(this,"Flag neither 1 nor 2!",Toast.LENGTH_LONG).show();
        }
      //  startActivity(i);
    }

      /*
     * ------------ Helper Methods ----------------------
     */

    /*
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    // returning image / video
    private File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Registration Screen", "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name

        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_Pic" + Date_Time.GetDateAndTimeImage() + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * Displaying captured image/video on the screen
     * */
    private void previewMedia(String filePath) {
        // Checking whether captured media is image or video
        //imgPreview.setVisibility(View.VISIBLE);
        BitmapFactory.Options options = new BitmapFactory.Options();
        // down sizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = 8;

        final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        Toast.makeText(this,filePath,Toast.LENGTH_LONG).show();
        edit_Profile_IV.setImageBitmap(getResizedBitmap(bitmap,30,20));
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    // AsyncTask - To convert Image to String
    public void encodeImagetoString(String Name_Service,String PhoneNumber_Service,String Aadhaar_Service,String Email_Service,String IMEI_SERVER,String Gender_Service,String DOB_Server) {



        photo_Details.setUsername_User(Name_Service);
        photo_Details.setPhone_User(PhoneNumber_Service);
        photo_Details.setAadhaar_User(Aadhaar_Service);
        photo_Details.setEmail_User(Email_Service);
        photo_Details.setIMEI_User(IMEI_SERVER);
        photo_Details.setGender_User(Gender_Service);
        photo_Details.setDOB_User(DOB_Server);


        new AsyncTask<Void, Void, Register_User>() {

            String encodedString = null;
           // ProgressDialog prgDialog;
            protected void onPreExecute() {
              //  prgDialog.setMessage("Converting Image to Binary Data");
              //  prgDialog.show();
            }

            @Override
            protected Register_User doInBackground(Void... params) {

                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                bitmap = BitmapFactory.decodeFile(fileUri.getPath().toString(),options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                photo_Details.setPhotobase64encode(encodedString);
                return photo_Details;
            }

            @Override
            protected void onPostExecute(Register_User userdetails) {
                // prgDialog.setMessage("Calling Upload");
                // Put converted Image string into Async Http Post param
                // params.put("image", encodedString);
                Log.e("Image in String msg: ",photo_Details.getPhotobase64encode());
                Log.e("Length Byte Array ",Integer.toString(photo_Details.getPhotobase64encode().length()));
                Log.e("Image Name msg: ",photo_Details.getPhotoname());
              //  prgDialog.dismiss();
                // Trigger Image upload
                triggerImageUpload(userdetails);
            }
        }.execute(null, null, null);
    }

    public void triggerImageUpload(Register_User photo_Details) {

        try {

            new Generic_Async_Post_Activity(Registration.this, Registration.this, TaskType.REGISTRATION).execute(
                    "getUserRegistration_JSON",
                    EConstants.URL + "getUserRegistration_JSON",
                    photo_Details.getUsername_User(),
                    photo_Details.getPhone_User(),
                    photo_Details.getAadhaar_User(),
                    photo_Details.getEmail_User(),
                    photo_Details.getIMEI_User(),
                    photo_Details.getGender_User(),
                    photo_Details.getDOB_User(),
                    photo_Details.getPhotoname(),
                    photo_Details.getPhotobase64encode());

        }catch (Exception ex){
            CD.showDialog(Registration.this,ex.getLocalizedMessage().toString());
        }
    }



}
