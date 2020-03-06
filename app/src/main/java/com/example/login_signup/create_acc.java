package com.example.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class create_acc extends AppCompatActivity {

    EditText vemail,vpassword,vphonenumber,vcitizenname;
    Button vsignup;
    TextView vlogin_screen;
    FirebaseAuth fauth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);

        vcitizenname = (EditText) findViewById(R.id.citizen_name);
        vphonenumber = (EditText) findViewById(R.id.phone_number);
        vemail = (EditText) findViewById(R.id.email);
        vpassword = (EditText) findViewById(R.id.password);
        vsignup = (Button) findViewById(R.id.signup);
        vlogin_screen = (TextView) findViewById(R.id.login_screen);

        databaseReference = FirebaseDatabase.getInstance().getReference("User_citizen");

        vsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = vcitizenname.getText().toString();
                final String phoneNumber = vphonenumber.getText().toString();
                final String email = vemail.getText().toString();
                final String password = vpassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    vemail.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    vpassword.setError("Password is required");
                    return;
                }

                if(TextUtils.isEmpty(name)){
                    vcitizenname.setError("Pls Enter Name");
                    return;
                }

                if(TextUtils.isEmpty(phoneNumber)){
                    vphonenumber.setError("Pls Enter Phone Number");
                    return;
                }

                if(phoneNumber.length() != 10){
                    vphonenumber.setError("Pls Enter Valid Phone Number");
                    return;
                }
                else{
                    int f = 0;
                    for(int i=0;i<10;i++){
                        if(!(phoneNumber.charAt(i) >= '0' && phoneNumber.charAt(i)<= '9')){
                            f = 1;
                        }
                    }
                    if(f == 1){
                        vphonenumber.setError("Pls Enter valid phone Number");
                        return;
                    }
                }

                if(password.length() <= 5){
                    vpassword.setError("Password length must be >= 6");
                    return;
                }

                fauth = FirebaseAuth.getInstance();
                fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User_citizen c1 = new User_citizen(
                                    name,
                                    password,
                                    email,
                                    phoneNumber
                            );

                            FirebaseDatabase.getInstance().getReference("User_citizen")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(c1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(create_acc.this,"User Created",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                            });
                            //Toast.makeText(create_acc.this,"User Created",Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText(create_acc.this,"Error! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        vlogin_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(create_acc.this,MainActivity.class);
                startActivity(intent);
            }
        });

        /*fauth = FirebaseAuth.getInstance();


        if(fauth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        vsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = vemail.getText().toString().trim();
                String password = vpassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    vemail.setError("Email is required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    vpassword.setError("Password is required");
                    return;
                }

                if(password.length() <= 5){
                    vpassword.setError("Password length must be >= 6");
                    return;
                }

                fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(create_acc.this,"User Created",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else{
                            Toast.makeText(create_acc.this,"Error! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        TextView login_acc = (TextView) findViewById(R.id.login_screen);
        login_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent login_acc_intent = new Intent(create_acc.this,MainActivity.class);
                startActivity(login_acc_intent);
            }
        });*/
    }
}
