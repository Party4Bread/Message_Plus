package com.example.david0926.messageplus.Auth;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class UserDB {

    static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void add(Context context, UserModel data){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("nickname", data.getNickname());
        editor.putString("email", data.getEmail());
        editor.putString("userkey", data.getUserkey());
        editor.putString("intro", data.getIntro());
        editor.putInt("profilenum", data.getProfilenum());
        editor.apply();
}

    public String getUserNickname(Context context){
        return getSharedPreferences(context).getString("nickname", "");
    }
    public String getUserEmail(Context context){
        return getSharedPreferences(context).getString("email", "");
    }
    public String getUserKey(Context context){
        return getSharedPreferences(context).getString("userkey", "");
    }
    public String getIntro(Context context){
        return getSharedPreferences(context).getString("intro", "");
    }
    public int getProfilenum(Context context){
        return getSharedPreferences(context).getInt("profilenum", 0);
    }
}
