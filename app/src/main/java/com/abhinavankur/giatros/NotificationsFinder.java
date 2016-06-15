package com.abhinavankur.giatros;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Abhinav Ankur on 5/13/2016.
 */
public class NotificationsFinder extends AsyncTask<String, Void, String> {
    String id, data, result, user, url;
    String appointmentsCount, symptomsCount;
    MyDatabase database;
    ArrayList<String> symptoms;
    Notif notif;
    private static final String TAG = "giatros";
    public NotificationsFinder(String id, String user, Notif notif) {
        this.id = id;
        this.user = user;
        this.notif = notif;
        symptoms = new ArrayList<>();
        if (user.equals("Doctor")){
            url = "http://giatros.net23.net/doctor_notifs.php";
        }
        else{
            url = "http://giatros.net23.net/patient_notifs.php";
        }
    }

    @Override
    protected String doInBackground(String... params) {
        data = "";
        try {
            data = URLEncoder.encode("id","UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            database = new MyDatabase(url, data);
            result = database.receive();

            JSONObject object;
            if (user.equals("Doctor")){
                object = new JSONObject(result);
                appointmentsCount = object.getString("appointments_count");
                symptomsCount = object.getString("symptoms_count");
            }
            else{
                JSONArray array = new JSONArray(result);
                for (int i=0;i<array.length();i++){
                    object = array.getJSONObject(i);
                    symptoms.add(object.getString("symptom_name"));
                }
            }
        }
        catch (Exception e){
            Log.i(TAG, e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (user.equals("Doctor")){
            notif.setDoctor(appointmentsCount, symptomsCount);
        }
        else{
            int temp = 11001;
            for (String symptom:symptoms) {
                notif.setPatient(symptom, temp);
                temp++;
            }
        }

    }
}
