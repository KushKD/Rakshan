package rakshan.himachal.dit.rakshan.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import rakshan.himachal.dit.rakshan.Enum.TaskType;
import rakshan.himachal.dit.rakshan.Helper.AppStatus;
import rakshan.himachal.dit.rakshan.Helper.Date_Time;
import rakshan.himachal.dit.rakshan.Interfaces.AsyncTaskListener;
import rakshan.himachal.dit.rakshan.JsonManager.Vehicle_In_Out_Json;
import rakshan.himachal.dit.rakshan.Presentation.Custom_Dialog;
import rakshan.himachal.dit.rakshan.R;
import rakshan.himachal.dit.rakshan.Services.SendLocationService;
import rakshan.himachal.dit.rakshan.Utils.EConstants;
import rakshan.himachal.dit.rakshan.Utils.Generic_Async_Post;
import rakshan.himachal.dit.rakshan.Utils.Generic_Async_Post_Activity;

public class Registration extends AppCompatActivity implements AsyncTaskListener {

    EditText OTP_Server_et,etmobile_et,etname_et,email_et,aadhaar_et,dob_server_et;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    Button back,register;
    Custom_Dialog CD = new Custom_Dialog();

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
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        dob_server_et = (EditText)findViewById(R.id.dob_server);
        back          = (Button)findViewById(R.id.back);
        register      = (Button)findViewById(R.id.register);

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
                Log.e("Testing","Testing");
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
                                if(AppStatus.getInstance(Registration.this).isOnline()) {

                                    new Generic_Async_Post_Activity(Registration.this, Registration.this, TaskType.REGISTRATION).execute(
                                            "getUserRegistration_JSON",
                                            EConstants.URL+"getUserRegistration_JSON",
                                            Name_Service,
                                            PhoneNumber_Service,
                                            Aadhaar_Service,
                                            Email_Service,
                                            IMEI_SERVER,
                                            Gender_Service,
                                            DOB_Server);
                                }else{
                                    CD.showDialog(Registration.this,"Please Connect to Internet");
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
    }

    @Override
    public void onTaskCompleted(String result, TaskType taskType) {

        if(taskType==TaskType.REGISTRATION){
            Log.e("Getting Result",result);
           // String Result_to_Show = null;
          //  Result_to_Show = Vehicle_In_Out_Json.Registration_Parse(result);
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

        }else{
            CD.showDialog(Registration.this,"Something went wrong. Please restart the application");
        }
    }
}
