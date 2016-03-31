package com.abhinavankur.giatros;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class GiatrosActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;
    String emailId, password;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_giatros);

        SharedPreferences preferences = this.getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        emailId = preferences.getString("emailId",null);
        password = preferences.getString("password",null);

        if (emailId==null || password==null){
            i = new Intent(this,LoginActivity.class);
        }
        else{
            i = new Intent(this,SymptomsActivity.class);
        }
        startActivity(i);
        finish();

        /*new Handler().postDelayed(new Runnable() {
             Showing splash screen with a timer. This will be useful when you want to show case your app logo/company
            @Override
            public void run() {
                // This method will be executed once the timer is over.
                Intent i = new Intent(GiatrosActivity.this, LoginActivity.class);
                startActivity(i);
                finish();   Close this activity
            }
        }, SPLASH_TIME_OUT);*/
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}