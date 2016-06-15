package com.abhinavankur.giatros;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Abhinav Ankur on 5/14/2016.
 */
public class UpdateProfile {
    Context context;
    String user, id, firstName, lastName, emailId, password, phoneNumber;
    boolean flag;
    public UpdateProfile(Context context) {
        this.context = context;
    }

    public void setFields() {
        SharedPreferences preferences = context.getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        user = preferences.getString("user", null);
        id = preferences.getString("id", null);
        firstName = preferences.getString("firstName", null);
        lastName = preferences.getString("lastName", null);
        emailId = preferences.getString("emailId", null);
        password = preferences.getString("password", null);
        phoneNumber = preferences.getString("phoneNumber", null);
        flag = true;
        Intent i = new Intent(context, SignUpActivity.class);
        i.putExtra("user",user);
        i.putExtra("id",id);
        i.putExtra("firstName",firstName);
        i.putExtra("lastName",lastName);
        i.putExtra("emailId",emailId);
        i.putExtra("password",password);
        i.putExtra("phoneNumber",phoneNumber);
        i.putExtra("flag",flag);
        context.startActivity(i);
    }

}
