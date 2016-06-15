package com.abhinavankur.giatros;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public class GiatrosActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;
    String emailId, password, user;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_giatros);

        new Handler().postDelayed(new Runnable() {
            /*Showing splash screen with a timer. This will be useful when you want to show case your app logo/company*/
            @Override
            public void run() {
                // This method will be executed once the timer is over.
                SharedPreferences preferences = GiatrosActivity.this.getSharedPreferences("Credentials", Context.MODE_PRIVATE);
                emailId = preferences.getString("emailId", null);
                password = preferences.getString("password", null);
                user = preferences.getString("user", null);

                if (emailId==null || password==null){
                    i = new Intent(GiatrosActivity.this,LoginActivity.class);
                }
                else if(user.equals("Doctor")){
                    i = new Intent(GiatrosActivity.this,DiseaseAugmenter.class);
                }
                else if(user.equals("Patient")){
                    i = new Intent(GiatrosActivity.this,SymptomsActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}