package com.abhinavankur.giatros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class DiseaseActivity extends AppCompatActivity {

    private static final String TAG = "abhinav";
    Bundle bundle;
    Intent i;
    ArrayList<String> symptoms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        i = getIntent();
        symptoms = i.getStringArrayListExtra("symptoms");
        for (String str:symptoms){
            Log.i(TAG,str);
        }
    }
}
