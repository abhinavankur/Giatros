package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;


/**
 * Created by Abhinav Ankur on 4/19/2016.
 */
public class SaveAppointment extends AsyncTask<String, Void, Void> {

    ProgressDialog dialog;
    DoctorsTestsActivity dta;
    MyDatabase database;
    String first, last, diseaseName, patientFirstName, patientLastName, patientEmailId, patientPhoneNumber;
    String appointmentID;
    private static final String TAG = "giatros";

    public void setData(DoctorsTestsActivity dta, ProgressDialog dialog, String first, String last, String diseaseName, String patientFirstName, String patientLastName, String patientEmailId, String patientPhoneNumber){
        this.dta = dta;
        this.dialog = dialog;
        this.first = first;
        this.last = last;
        this.diseaseName = diseaseName;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.patientEmailId = patientEmailId;
        this.patientPhoneNumber = patientPhoneNumber;
    }
    @Override
    protected Void doInBackground(String... params) {
        String data = "";

        try {
            data = URLEncoder.encode("first_name", "UTF-8") + "=" + URLEncoder.encode(first, "UTF-8");
            data += "&" + URLEncoder.encode("last_name", "UTF-8") + "=" + URLEncoder.encode(last, "UTF-8");
            data += "&" + URLEncoder.encode("disease_name", "UTF-8") + "=" + URLEncoder.encode(diseaseName, "UTF-8");
            data += "&" + URLEncoder.encode("patient_first_name", "UTF-8") + "=" + URLEncoder.encode(patientFirstName, "UTF-8");
            data += "&" + URLEncoder.encode("patient_last_name", "UTF-8") + "=" + URLEncoder.encode(patientLastName, "UTF-8");
            data += "&" + URLEncoder.encode("patient_email", "UTF-8") + "=" + URLEncoder.encode(patientEmailId, "UTF-8");
            data += "&" + URLEncoder.encode("patient_phone_no", "UTF-8") + "=" + URLEncoder.encode(patientPhoneNumber, "UTF-8");

            database = new MyDatabase("http://giatros.net23.net/save_appointment.php", data);
            String result = database.receive();
            JSONArray array = new JSONArray(result);
            JSONObject object = array.getJSONObject(0);
            appointmentID = object.getString("appointment_id");
        }
        catch (Exception e){
            Log.i(TAG, e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        dialog.dismiss();
        new AlertDialog.Builder(dta)
                .setTitle("Appointment Saved")
                .setMessage("The appointment Id is : APP" + appointmentID)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                    }}).show();/*
        Toast.makeText(dta, "Appointment Saved", Toast.LENGTH_SHORT).show();*/
    }
}
