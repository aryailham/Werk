package com.example.werk;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.werk.model.User;

public class SharedPref {

    private SharedPreferences SP;

    public SharedPref(Context context){
        SP = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
    }

    public void save(User user){
        SharedPreferences.Editor editor = SP.edit();
        editor.putString("email", user.getEmail());
        editor.putString("password", user.getPassword());
        editor.apply();
    }

    public User load(){
        User user = new User();
        user.setEmail(SP.getString("email", ""));
        user.setPassword(SP.getString("password", ""));
        return user;
    }

    public void clearAll(Context context){
        SP.edit().clear().commit();
    }
}
