package rakshan.himachal.dit.sms.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import rakshan.himachal.dit.sms.Presentation.Custom_Dialog;
import rakshan.himachal.dit.sms.R;

public class AddComplaint extends AppCompatActivity {

    private Button complainant_details_bt,
            accused_details_bt,
            incident_details_bt,
            complaint_detils_bt,
            submission_details_bt;

    private Button back_bt,
            save_bt;

    Custom_Dialog CD = new Custom_Dialog();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaint);

        complainant_details_bt = (Button) findViewById(R.id.button1);
        accused_details_bt = (Button) findViewById(R.id.button2);
        incident_details_bt = (Button) findViewById(R.id.button3);
        complaint_detils_bt = (Button) findViewById(R.id.button4);
        submission_details_bt = (Button) findViewById(R.id.button5);

        back_bt = (Button) findViewById(R.id.back);
        save_bt = (Button) findViewById(R.id.save);


        /**
         * Buttons Listener
         */

        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddComplaint.this.finish();
            }
        });

        save_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ToDo
            }
        });

        /**
         * Layout Button Listener
         */

        accused_details_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I_1 = new Intent(AddComplaint.this, ComplaintDetails_Accused.class);
                startActivity(I_1);
            }
        });

        incident_details_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I_2 = new Intent(AddComplaint.this, ComplaintDetails_Incident.class);
                startActivity(I_2);
            }
        });


        complainant_details_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I_1 = new Intent(AddComplaint.this, ComplaintDetails_complainant.class);
                startActivity(I_1);
            }
        });

        complaint_detils_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I_1 = new Intent(AddComplaint.this, ComplaintDetails_Complaint.class);
                startActivity(I_1);
            }
        });

        submission_details_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent I_1 = new Intent(AddComplaint.this, ComplaintDetails_Complaint.class);
               // startActivity(I_1);
                CD.showDialog(AddComplaint.this,"Still Under Construction");

            }
        });
    }
}
