package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Abhinav Ankur on 4/16/2016.
 */
public class DoctorsTestsFinder extends AsyncTask<String, Void, String>{

    ProgressDialog dialog;
    String specialName, diseaseName;
    MyDatabase db;
    String doctors, tests;
    ReceiveData rd;
    ArrayList<String> doctorFirstName, doctorLastName, doctorEmail, doctorPhone, testName;
    private static final String TAG = "giatros";

    public void setData(ProgressDialog dialog, String specialName, String diseaseName, DoctorsTestsActivity dta){
        this.dialog = dialog;
        this.specialName = specialName;
        this.diseaseName = diseaseName;
        this.rd = dta;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        doctorFirstName = new ArrayList<>();
        doctorLastName = new ArrayList<>();
        doctorEmail = new ArrayList<>();
        doctorPhone = new ArrayList<>();
        testName = new ArrayList<>();
    }

    @Override
    protected String doInBackground(String... params) {
        String data = "";
        try{
            data = URLEncoder.encode("specialName","UTF-8") + "=" + URLEncoder.encode(specialName,"UTF-8");
            db = new MyDatabase("http://giatros.net23.net/show_doctors_disease.php", data);
            doctors = db.receive();

            JSONArray array = new JSONArray(doctors);
            JSONObject doctor;
            for(int i=0;i<array.length();i++){
                doctor = array.getJSONObject(i);
                doctorFirstName.add(doctor.getString("first_name"));
                doctorLastName.add(doctor.getString("last_name"));
                doctorEmail.add(doctor.getString("email"));
                doctorPhone.add(doctor.getString("phone"));
            }

            data = URLEncoder.encode("diseaseName","UTF-8") + "=" + URLEncoder.encode(diseaseName,"UTF-8");
            Log.i(TAG, data);
            db = new MyDatabase("http://giatros.net23.net/show_tests_disease.php", data);
            tests = db.receive();

            array = new JSONArray(tests);
            JSONObject test;
            for(int i=0;i<array.length();i++){
                test = array.getJSONObject(i);
                testName.add(test.getString("test_name"));
            }
        }
        catch (Exception e){
            Log.i(TAG, e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        dialog.dismiss();
        rd.getData(doctorFirstName, doctorLastName, doctorEmail, doctorPhone, testName);
    }
}
