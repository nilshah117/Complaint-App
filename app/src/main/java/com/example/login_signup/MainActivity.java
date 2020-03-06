package com.example.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText vemail,vpassword;
    TextView vforgot_password;
    Button vlogin;
    FirebaseAuth fauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vemail = (EditText) findViewById(R.id.email);
        vpassword = (EditText) findViewById(R.id.password);
        vlogin = (Button) findViewById(R.id.login);
        fauth = FirebaseAuth.getInstance();
        vforgot_password = (TextView) findViewById(R.id.forgot_password);
        final Shared shared = new Shared(getApplicationContext());
        shared.firstTime();
        vlogin.setOnClickListener(new View.OnClickListener() {
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

                fauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
                            {
                                Toast.makeText(MainActivity.this,"Login Succesfully",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this,citizen_home.class);
                                startActivity(intent);
                                shared.secondTime();
                            }
                            else
                            {
                                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(MainActivity.this,"Verification mail sended Successfully",Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(MainActivity.this,"Error! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Error! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        TextView create_acc = (TextView) findViewById(R.id.create_Account);
        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent create_acc_intent = new Intent(MainActivity.this,create_acc.class);
                startActivity(create_acc_intent);
            }
        });

        vforgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetemail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your email to recieve the reset link");
                passwordResetDialog.setView(resetemail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String email = resetemail.getText().toString();

                        fauth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"Reset Link Sent to your mail",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Error! " + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                passwordResetDialog.create().show();
            }
        });
    }
}
