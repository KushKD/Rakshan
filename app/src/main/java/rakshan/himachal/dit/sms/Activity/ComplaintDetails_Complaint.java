package rakshan.himachal.dit.sms.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import rakshan.himachal.dit.sms.R;

public class ComplaintDetails_Complaint extends AppCompatActivity {

    private Button back_bt,
            save_bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_details__complaint);

        back_bt = (Button) findViewById(R.id.back);
        save_bt = (Button) findViewById(R.id.save);

        /**
         * Buttons Listener
         */

        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComplaintDetails_Complaint.this.finish();
            }
        });

        save_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ToDo
            }
        });
    }
}
