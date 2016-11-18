package rakshan.himachal.dit.sms.Activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rakshan.himachal.dit.sms.Enum.TaskType;
import rakshan.himachal.dit.sms.Helper.AppStatus;
import rakshan.himachal.dit.sms.Interfaces.AsyncTaskListener;
import rakshan.himachal.dit.sms.JsonManager.JsonParser;
import rakshan.himachal.dit.sms.Model.vacationTravelDetailsPojo;
import rakshan.himachal.dit.sms.Presentation.Custom_Dialog;
import rakshan.himachal.dit.sms.R;
import rakshan.himachal.dit.sms.Utils.EConstants;
import rakshan.himachal.dit.sms.Utils.Generic_Async_Post;
import rakshan.himachal.dit.sms.Utils.Generic_Async_Post_Activity;

public class VacationTravelDetails extends AppCompatActivity implements AsyncTaskListener {

    private Custom_Dialog CD = new Custom_Dialog();
    vacationTravelDetailsPojo VTDP = null;
    public String Latitude,
            Longitude = null;


    private TextView longitude_tv,
            latitude_tv;
    private static final int TIME_DIALOG_ID = 1111;
    static final int TIME_DIALOG_ID_TWO = 2222;
    private TextView tv_todate,
            tv_fromdate;
    private EditText et_aadhaarno,
            et_mobilenumber,
            et_policestation,
            et_contactname,
            et_emergencymobilenumber,
            et_emergencymobilenumbertwo,
            et_address;

    private Button bt_back,
            bt_inform;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private String GetFromDate = null;
    private String GetToDate = null;
    private int hour;
    private int minute;
    private String ID_Parking = null;
    private String Aadhaar_Parking = null;
    private String flag = null;
    private SimpleDateFormat todateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat fromdateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_travel_details);
        Bundle bundle = getIntent().getExtras();


        try {
            Latitude = bundle.getString("LATITUDE");
            Longitude = bundle.getString("LONGITUDE");
        } catch (Exception ex) {
            CD.showDialog(VacationTravelDetails.this, ex.getLocalizedMessage().toString().trim());
        }


        try {
            initialize_View();
        } catch (Exception ex) {
            CD.showDialog(VacationTravelDetails.this, ex.getLocalizedMessage().toString().trim());
        }
        try {
            longitude_tv.setText(Longitude);
            latitude_tv.setText(Latitude);
        } catch (Exception e) {
            CD.showDialog(VacationTravelDetails.this, e.getLocalizedMessage().toString().trim());
        }


        tv_todate.setText(todateFormat.format(new Date()));
        tv_fromdate = (TextView) findViewById(R.id.fromdate);
        tv_fromdate.setText(fromdateFormat.format(new Date()));

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        setDateTimeField();


        tv_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });


        tv_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });


        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VacationTravelDetails.this.finish();
            }
        });

        bt_inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getData();


                } catch (Exception ex) {
                    CD.showDialog(VacationTravelDetails.this, ex.getLocalizedMessage().toString().trim());
                }
            }
        });

    }

    private void getData() {
        VTDP = new vacationTravelDetailsPojo();
        try {
            VTDP.setHouseLongitude(longitude_tv.getText().toString().trim());
            VTDP.setHouseLatitude(latitude_tv.getText().toString().trim());
            VTDP.setFromDate(tv_fromdate.getText().toString().trim());
            VTDP.setToDate(tv_todate.getText().toString().trim());
            VTDP.setAadhaarNo(et_aadhaarno.getText().toString().trim());
            VTDP.setMobileNumber(et_mobilenumber.getText().toString().trim());
            VTDP.setPoliceStation(et_policestation.getText().toString().trim());
            VTDP.setContactName(et_contactname.getText().toString().trim());
            VTDP.setEmergencyMobileNumber(et_emergencymobilenumber.getText().toString().trim());
            VTDP.setEmergencyMobileNumberTwo(et_emergencymobilenumbertwo.getText().toString().trim());
            VTDP.setAddress(et_address.getText().toString().trim());
            VTDP.setIMEI(AppStatus.GetIMEI(VacationTravelDetails.this));

            if (VTDP.getHouseLatitude().length() != 0 && VTDP.getHouseLongitude().length() != 0) {
                if (VTDP.getFromDate().length() != 0 && VTDP.getToDate().length() != 0) {
                    if (AppStatus.getInstance(VacationTravelDetails.this).isOnline()) {
                        //Send Data To Server
                        try {
                            new Generic_Async_Post_Activity(VacationTravelDetails.this, VacationTravelDetails.this, TaskType.VACATIONTRAVELDETAILS)
                                    .execute("getVacationTravel_JSON",
                                            EConstants.URL + "getVacationTravel_JSON",
                                            VTDP.getHouseLatitude(),
                                            VTDP.getHouseLongitude(),
                                            VTDP.getFromDate(),
                                            VTDP.getToDate(),
                                            VTDP.getAadhaarNo(),
                                            VTDP.getMobileNumber(),
                                            VTDP.getPoliceStation(),
                                            VTDP.getContactName(),
                                            VTDP.getEmergencyMobileNumber(),
                                            VTDP.getEmergencyMobileNumberTwo(),
                                            VTDP.getAddress(),
                                            VTDP.getIMEI());

                        } catch (Exception e) {
                            CD.showDialog(VacationTravelDetails.this, e.getLocalizedMessage().toString().trim());
                        } finally {
                            VTDP = null;
                        }


                    } else {
                        CD.showDialog(VacationTravelDetails.this, "You are not connected to any network. Please connect to Internet and try again.");
                    }

                } else {
                    CD.showDialog(VacationTravelDetails.this, "Please enter the Start and End date of te vacation.");
                }
            } else {
                CD.showDialog(VacationTravelDetails.this, "Something went wrong. Please try again.");
            }
        } catch (Exception e) {
            CD.showDialog(VacationTravelDetails.this, e.getLocalizedMessage().toString().trim());
        }

    }

    private void initialize_View() {
        longitude_tv = (TextView) findViewById(R.id.longitude);
        latitude_tv = (TextView) findViewById(R.id.latitude);
        tv_fromdate = (TextView) findViewById(R.id.fromdate);
        tv_todate = (TextView) findViewById(R.id.todate);
        et_aadhaarno = (EditText) findViewById(R.id.aadhaarno);
        et_mobilenumber = (EditText) findViewById(R.id.mobilenumber);
        et_policestation = (EditText) findViewById(R.id.policestation);
        et_contactname = (EditText) findViewById(R.id.contactname);
        et_emergencymobilenumber = (EditText) findViewById(R.id.emergencymobilenumber);
        et_emergencymobilenumbertwo = (EditText) findViewById(R.id.emergencymobilenumbertwo);
        et_address = (EditText) findViewById(R.id.address);
        bt_back = (Button) findViewById(R.id.back);
        bt_inform = (Button) findViewById(R.id.it);
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tv_fromdate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tv_todate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public void onTaskCompleted(String result, TaskType taskType) {

        if(taskType == TaskType.VACATIONTRAVELDETAILS){
           // CD.showDialog(VacationTravelDetails.this,result);

            String Result_to_Show = null;
            Result_to_Show = JsonParser.VacationTravel_Parse(result);
            CD.showDialog(VacationTravelDetails.this, Result_to_Show);
           // VacationTravelDetails.this.finish();
        }else{
            CD.showDialog(VacationTravelDetails.this, "Something went wrong.Please try again.");
        }

    }
}
