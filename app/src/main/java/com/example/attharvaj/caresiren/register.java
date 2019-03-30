package com.example.attharvaj.caresiren;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText e1;
    private EditText e2;
    private EditText e3;
    private EditText e4;
    private EditText e9;
    private RadioGroup r1;
    private Button b1;
    private int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        e1=findViewById(R.id.email);
        e2=findViewById(R.id.password);
        e3=findViewById(R.id.password1);
        e4=findViewById(R.id.first);
        e9=findViewById(R.id.contactno);
        r1=findViewById(R.id.registeras);
        b1=findViewById(R.id.registerbtn);
        r1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                View radioButton=r1.findViewById(i);
                int index=r1.indexOfChild(radioButton);
                switch (index){
                    case 0:
                        flag = 1;
                        break;
                    case 1:
                        flag = 2;
                        break;
                }
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String first=e4.getText().toString().trim();
                String e=e1.getText().toString().trim();
                String p=e2.getText().toString().trim();
                String p1=e3.getText().toString().trim();
                final String contact =e9.getText().toString().trim();

                if(first.isEmpty() || e.isEmpty() || p.isEmpty() || p1.isEmpty() || !p.equals(p1) || contact.isEmpty()){

                    if (first.isEmpty()) {
                        e4.setError("Cannot be empty!");
                    }
                    if (e.isEmpty()) {
                        e1.setError("Cannot be empty!");
                    }
                    if (p.isEmpty()) {
                        e2.setError("Cannot be empty!");
                    }
                    if (p1.isEmpty()) {
                        e3.setError("Cannot be empty!");
                    }
                    if (!p.equals(p1)) {
                        e3.setError("Password didn't match!");
                    }
                    if (contact.isEmpty()) {
                        e9.setError("Cannot be empty!");
                    }
                }
                else{
                    final String email=e1.getText().toString().trim();
                    final String password=e2.getText().toString().trim();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                storeData();

                            } else {
                                Toast.makeText(register.this, "Already registered!",Toast.LENGTH_SHORT).show();
                            }// ...
                        }

                        private void storeData() {
                            Map<String, String> user = new HashMap<>();
                            user.put("Name", first);
                            user.put("Email ID",email);
                            user.put("Password",password);
                            user.put("Contact no.",contact);

                            if(flag==1){
                                db.collection("Driver").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(register.this,"Data stored to Firestore", Toast.LENGTH_SHORT).show();
                                        Intent i1=new Intent(register.this,MainActivity.class);
                                        startActivity(i1);
                                    }
                                });


                            }
                            else{
                                db.collection("User").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(register.this,"Data stored to Firestore", Toast.LENGTH_SHORT).show();
                                        Intent i2=new Intent(register.this,MainActivity.class);
                                        startActivity(i2);
                                    }
                                });


                            }

                        }
                    });
                }
            }
        });

    }
}
