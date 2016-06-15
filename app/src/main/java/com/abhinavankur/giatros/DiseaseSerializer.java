package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.SimpleTimeZone;

public class DiseaseSerializer extends AsyncTask<String, Void, Void>{

    String disease, specialization, newSpecialization;
    ArrayList<String> tests, symptoms, newTests, newSymptoms;
    ArrayList<Double> assoc;
    MyDatabase database;
    String data, result;
    ProgressDialog dialog;
    DiseaseAugmenterTwo da2;
    private static final String TAG = "giatros";

    public DiseaseSerializer(DiseaseAugmenterTwo da2, ProgressDialog dialog, String disease, String specialization, String newSpecialization, ArrayList<String> tests, ArrayList<String> newTests, ArrayList<String> symptoms, ArrayList<String> newSymptoms, ArrayList<Double> assoc){
        this.tests = new ArrayList<>();
        this.newTests = new ArrayList<>();
        this.symptoms = new ArrayList<>();
        this.newSymptoms = new ArrayList<>();
        this.assoc = new ArrayList<>();
        this.disease = disease;
        this.specialization = specialization;
        this.newSpecialization = newSpecialization;
        this.tests = tests;
        this.newTests = newTests;
        this.symptoms = symptoms;
        this.newSymptoms = newSymptoms;
        this.assoc = assoc;
        this.dialog = dialog;
        this.da2 = da2;
    }

    @Override
    protected Void doInBackground(String... params) {
        data="";
        try{
            data = URLEncoder.encode("disease_name","UTF-8")+"="+URLEncoder.encode(disease,"UTF-8");
            data += "&" + URLEncoder.encode("specialization","UTF-8")+"="+URLEncoder.encode(specialization,"UTF-8");
            data += "&" + URLEncoder.encode("newSpecialization","UTF-8")+"="+URLEncoder.encode(newSpecialization,"UTF-8");
            data += "&" + URLEncoder.encode("symptomsSize","UTF-8")+"="+URLEncoder.encode(String.valueOf(symptoms.size()),"UTF-8");
            data += "&" + URLEncoder.encode("testsSize","UTF-8")+"="+URLEncoder.encode(String.valueOf(tests.size()),"UTF-8");
            for (int i = 0; i < tests.size(); i++) {
                data += "&" + URLEncoder.encode("test"+(i+1),"UTF-8")+"="+URLEncoder.encode(tests.get(i),"UTF-8");
            }
            for (int i = 0; i < newTests.size(); i++) {
                data += "&" + URLEncoder.encode("newTests"+(i+1),"UTF-8")+"="+URLEncoder.encode(newTests.get(i),"UTF-8");
            }
            for (int i = 0; i < symptoms.size(); i++) {
                data += "&" + URLEncoder.encode("symptom"+(i+1),"UTF-8")+"="+URLEncoder.encode(symptoms.get(i),"UTF-8");
            }
            for (int i = 0; i < symptoms.size(); i++) {
                data += "&" + URLEncoder.encode("newSymptoms"+(i+1),"UTF-8")+"="+URLEncoder.encode(newSymptoms.get(i),"UTF-8");
            }
            for (int i = 0; i < assoc.size(); i++) {
                data += "&" + URLEncoder.encode("assoc"+(i+1),"UTF-8")+"="+URLEncoder.encode(String.valueOf(assoc.get(i)),"UTF-8");
            }
            Log.i(TAG, data);
            database = new MyDatabase("http://giatros.net23.net/save_disease.php", data);
            result = database.receive();
        }
        catch(Throwable e){
            Log.i(TAG,e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        dialog.dismiss();
        da2.diseaseSaved();
    }
}
