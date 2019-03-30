package com.example.attharvaj.caresiren;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class user extends AppCompatActivity {

    private ImageView i;
    private EditText e1;
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        e1=findViewById(R.id.editText);
        i  = (ImageView) findViewById(R.id.imageView);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String first=e1.getText().toString().trim();

                if(first.isEmpty()){
                    e1.setError("Enter Contact Number!");
                }

                else{
                    gps = new GPSTracker(getApplicationContext());

                    // Check if GPS enabled
                    if(gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        // Write a message to the database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("message");
                        String url = first+"-https://www.google.com/maps/search/?api=1&query="+latitude +","+longitude;
                        myRef.setValue(url);
                        Toast.makeText(getApplicationContext(), "Your ambulance is on the way!" , Toast.LENGTH_LONG).show();

                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        Toast.makeText(getApplicationContext(), "Unable to access the location!" , Toast.LENGTH_LONG).show();
                        gps.showSettingsAlert();
                    }
                }

            }
        });
    }

}
