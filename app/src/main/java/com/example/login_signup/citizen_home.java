package com.example.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class citizen_home extends AppCompatActivity {

    Button vdelete_account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_home);

        vdelete_account = (Button) findViewById(R.id.delete_account);

        /*vdelete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String key = firebaseUser.getEmail();
                final AlertDialog.Builder dialog = new AlertDialog.Builder(citizen_home.this);
                dialog.setMessage(key);
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });*/

        vdelete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(citizen_home.this);
                dialog.setTitle("Are you sure?");
                dialog.setMessage("Deleting this account will result in completely removing your account from the system and you wont be able to access the app.");

                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseUser  firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(citizen_home.this,"1 -- " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                        String uid = firebaseUser.getUid();
                        //Toast.makeText(citizen_home.this,key,Toast.LENGTH_LONG).show();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User_citizen").child(uid);
                        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(citizen_home.this,"Account Deleted",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(citizen_home.this,MainActivity.class);
                                    Shared shared = new Shared(getApplicationContext());
                                    shared.putflase();
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(citizen_home.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                    }
                });

                dialog.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog  =  dialog.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        Shared shared = new Shared(getApplicationContext());
        shared.putflase();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }
}
