package rakshan.himachal.dit.sms.Activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rakshan.himachal.dit.sms.R;

public class VacationTravelDetails extends AppCompatActivity {

    public String Latitude = null;
    public String Longitude = null;

    TextView longitude_tv, latitude_tv;
    static final int TIME_DIALOG_ID = 1111;
    static final int TIME_DIALOG_ID_TWO = 2222;
    TextView tv_todate ,tv_fromdate ;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_travel_details);
        Bundle bundle = getIntent().getExtras();

        Latitude = bundle.getString("LATITUDE");
        Longitude = bundle.getString("LONGITUDE");

        longitude_tv = (TextView)findViewById(R.id.longitude);
        latitude_tv = (TextView)findViewById(R.id.latitude);
        tv_todate = (TextView)findViewById(R.id.fromdate);
        tv_todate = (TextView)findViewById(R.id.todate);

        try{
            longitude_tv.setText(Longitude);
            latitude_tv.setText(Latitude);
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
        }

        SimpleDateFormat todateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tv_todate.setText(todateFormat.format(new Date()));


        tv_fromdate = (TextView)findViewById(R.id.fromdate);
        SimpleDateFormat fromdateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        // Current Hour
        hour = c.get(Calendar.HOUR_OF_DAY);
        // Current Minute
        minute = c.get(Calendar.MINUTE);

        // set current time into output textview
        //updateTime(hour, minute);
       // updateTime_Two(hour, minute);



    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tv_fromdate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tv_todate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }



}
