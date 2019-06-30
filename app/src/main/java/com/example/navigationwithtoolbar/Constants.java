package com.example.navigationwithtoolbar;

import android.content.Context;
import android.content.SharedPreferences;

public class Constants {
    SharedPreferences sharedPreferences;
    Context context;

    public void removeUser(){
        sharedPreferences.edit().clear();
    }
    public String getEmail() {
        email = sharedPreferences.getString("useremail","");
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        sharedPreferences.edit().putString("useremail",email).commit();
    }

    private String email;
    private String ip ="192.168.1.4";
    public String getIp() {
        return ip;
    }

    public Constants(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
    }
}
