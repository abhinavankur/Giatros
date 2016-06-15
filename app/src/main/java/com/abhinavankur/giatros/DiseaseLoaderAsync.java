package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class DiseaseLoaderAsync extends AsyncTask<String, Void, String> {

    MyDatabase db;
    ArrayList<String> symptoms, matched = new ArrayList<>(), disease_name = new ArrayList<>(), special_name = new ArrayList<>(), assoc_sum = new ArrayList<>();
    JSONObject values;
    ProgressDialog dialog;
    ReceiveData rd;
    String result;
    private static final String TAG = "giatros";

    public DiseaseLoaderAsync(ProgressDialog dialog, ArrayList<String> symptoms, DiseaseShowActivity da) {
        this.dialog = dialog;
        this.symptoms = symptoms;
        this.rd = da;
    }


    @Override
    protected String doInBackground(String... params) {

        String data = "";
        try {
            for (int i = 0; i < symptoms.size(); i++) {
                if (i == 0)
                    data += URLEncoder.encode("symptom" + String.valueOf(i + 1), "UTF-8") + "=" + URLEncoder.encode(symptoms.get(i), "UTF-8");
                else
                    data += "&" + URLEncoder.encode("symptom" + String.valueOf(i + 1), "UTF-8") + "=" + URLEncoder.encode(symptoms.get(i), "UTF-8");
            }

            db = new MyDatabase("http://giatros.net23.net/show_diseases.php", data);
            result = db.receive();

            JSONArray diseases = new JSONArray(result);
            JSONObject disease;
            for (int i = 0; i < diseases.length(); i++) {
                disease = diseases.getJSONObject(i);
                matched.add(disease.getString("matched"));
                disease_name.add(disease.getString("disease_name"));
                assoc_sum.add(disease.getString("assoc_sum"));
                special_name.add(disease.getString("special_name"));
            }
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        dialog.dismiss();
        rd.getData(matched, disease_name, assoc_sum, special_name);
    }
}
