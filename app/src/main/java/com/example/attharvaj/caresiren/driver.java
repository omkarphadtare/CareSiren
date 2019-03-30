package com.example.attharvaj.caresiren;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class driver extends AppCompatActivity {

    GPSTracker gps;
    private Button b1;
    private TextView e1;
    private TextView e2;
    private EditText e3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        b1 = (Button) findViewById(R.id.button2);
        e1 = (TextView) findViewById(R.id.textView6);
        e2 = (TextView) findViewById(R.id.textView11);
        e3 = (EditText) findViewById(R.id.editText2);
        e1.setClickable(true);
        e2.setClickable(true);
        e2.setMovementMethod(LinkMovementMethod.getInstance());
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Switch s = (Switch) findViewById(R.id.switch1);
                final String contact =e3.getText().toString().trim();
                if(contact.isEmpty()){
                    e3.setError("Enter Contact Number!");
                }
                else if(s.isChecked()) {
                    String assigned="Yes";
                    Toast.makeText(getApplicationContext(), "You will be notified soon", Toast.LENGTH_LONG).show();
                }
                else{
                    String assigned = "No";
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("message");
                    DatabaseReference myRef1 = database.getReference("tokens");

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            String value = dataSnapshot.getValue(String.class);
                            String arr[] = value.split("-");
                            e1.setText(arr[0]);
                            String text = "<a href='"+arr[1]+"'>"+arr[1]+"</a>";
                            e2.setText(Html.fromHtml(text));

                            e1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    try{
                                        String phone = e1.getText().toString().replaceAll("-","");
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:"+phone));
                                        startActivity(callIntent);
                                    }catch(SecurityException e){e.printStackTrace();}
                                }
                            });
//                            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Toast.makeText(getApplicationContext(), "Failed to fetch the value", Toast.LENGTH_LONG).show();
                            }
                        });

                    gps = new GPSTracker(getApplicationContext());

                    // Check if GPS enabled
                    if(gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        // Write a message to the database
                        String url = contact+"-"+assigned+"-https://www.google.com/maps/search/?api=1&query="+latitude +","+longitude;
                        myRef1.setValue(url);
                        Toast.makeText(getApplicationContext(), "Your Location has been traced", Toast.LENGTH_LONG).show();
                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.

                        gps.showSettingsAlert();
                    }
                    }
                 }
             });
     }

}
