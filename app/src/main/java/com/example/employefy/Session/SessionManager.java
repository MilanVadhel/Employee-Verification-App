package com.example.employefy.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.employefy.CardGenerationActivity;
import com.example.employefy.LoginActivity_Employe;
import com.example.employefy.requestProfileActivity;

import java.util.HashMap;

public class SessionManager
{
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE=0;

    private static final String PREFERENCE_NAME="LOGIN";
    private static final String STATE="IS_LOGIN";
    public static final String USERNAME="USERNAME";
    public static final String EMAIL="EMAIL";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences(PREFERENCE_NAME,PRIVATE_MODE);
        editor=sharedPreferences.edit();
    }


    public void createSession(String username,String email)
    {
        editor.putBoolean(STATE,true);
        editor.putString(USERNAME,username);
        editor.putString(EMAIL,email);
        editor.apply();
    }

    public boolean isLoggin()
    {
        return sharedPreferences.getBoolean(STATE,false); //by default user is not login
    }
    public void checkLogin()
    {
        if(!this.isLoggin()) //if not login then go to login screen
        {
            context.startActivity(new Intent(context, LoginActivity_Employe.class));
            ((requestProfileActivity)context).finish();
        }
    }
    public HashMap<String,String> getUserDetail()
    {
        HashMap<String,String> user=new HashMap<>();
        user.put(USERNAME,sharedPreferences.getString(USERNAME,null));
        user.put(EMAIL,sharedPreferences.getString(EMAIL,null));
        return user;
    }
    public void logout()
    {
        editor.clear();
        editor.commit();
        context.startActivity(new Intent(context,LoginActivity_Employe.class));
        ((CardGenerationActivity)context).finish();
    }
}
