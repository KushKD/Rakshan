package rakshan.himachal.dit.sms.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import rakshan.himachal.dit.sms.R;

public class VacationTravelDetails extends AppCompatActivity {

    public String Latitude = null;
    public String Longitude = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_travel_details);
        Bundle bundle = getIntent().getExtras();

        Latitude = bundle.getString("LATITUDE");
        Longitude = bundle.getString("LONGITUDE");

        Log.e("Latitude new Activity",Latitude);
        Log.e("Longitude new Activity",Longitude);

    }
}
