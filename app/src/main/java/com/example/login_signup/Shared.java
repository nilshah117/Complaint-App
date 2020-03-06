package com.example.login_signup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class Shared {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    int mode = 0;
    String Filename = "sdfile";
    String data = "b";

    public Shared(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Filename,mode);
        editor = sharedPreferences.edit();
    }

    public void secondTime(){
        editor.putBoolean(data,true);
        editor.commit();
    }

    public void firstTime(){
        if(this.login()){
            Intent intent = new Intent(context,citizen_home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

    public void putflase(){
        editor.putBoolean(data,false);
        editor.commit();
    }

    private boolean login() {
        return sharedPreferences.getBoolean(data,false);
    }
}
