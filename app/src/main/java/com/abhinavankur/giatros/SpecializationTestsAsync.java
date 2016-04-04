package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SpecializationTestsAsync extends AsyncTask<Void, Void, String>{

    MyDatabase db;
    String result;
    ProgressDialog dialog;
    JSONArray jsonArray;
    public ReceiveData rd;
    ArrayList<String> specialization, tests, diseases;
    private static final String TAG = "giatros";

    public SpecializationTestsAsync(ProgressDialog dialog, DiseaseAugmenter da) {
        specialization = new ArrayList<>();
        tests = new ArrayList<>();
        diseases = new ArrayList<>();
        this.dialog = dialog;
        this.rd = da;
    }

    @Override
    protected String doInBackground(Void... params) {
        try{
            db = new MyDatabase("http://giatros.net23.net/show_specialization.php", null);
            result = db.receive();
            jsonArray = new JSONArray(result);
            String temp;
            for (int i = 0; i < jsonArray.length(); i++) {
                temp = jsonArray.getString(i);
                specialization.add(temp);
            }

            db = new MyDatabase("http://giatros.net23.net/show_all_diseases.php", null);
            result = db.receive();
            jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                temp = jsonArray.getString(i);
                diseases.add(temp);
            }

            db = new MyDatabase("http://giatros.net23.net/show_tests.php", null);
            result = db.receive();
            jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                temp = jsonArray.getString(i);
                tests.add(temp);
            }
        }
        catch (JSONException jsoe) {
            Log.i(TAG,jsoe.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        dialog.dismiss();
        rd.getData(specialization, tests, diseases);
    }
}
