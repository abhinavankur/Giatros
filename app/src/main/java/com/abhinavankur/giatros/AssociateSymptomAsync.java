package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.URLEncoder;

/**
 * Created by Abhinav Ankur on 5/3/2016.
 */
public class AssociateSymptomAsync extends AsyncTask<String, Void, Void> {
    ProgressDialog dialog;
    String symptom, disease, probability, data;
    private static final String TAG = "giatros";
    MyDatabase db;
    String result;
    SymptomDiseaseAssociateActivity sdaa;

    public void setData(ProgressDialog dialog, String disease, String symptom, String probability, SymptomDiseaseAssociateActivity sdaa){
        this.dialog = dialog;
        this.disease = disease;
        this.symptom = symptom;
        this.probability = probability;
        this.sdaa = sdaa;

    }
    @Override
    protected Void doInBackground(String... params) {
        data = "";
        try {
            data = URLEncoder.encode("disease", "UTF-8") + "=" + URLEncoder.encode(disease, "UTF-8");
            data += "&" + URLEncoder.encode("symptom", "UTF-8") + "=" + URLEncoder.encode(symptom, "UTF-8");
            data += "&" + URLEncoder.encode("probability", "UTF-8") + "=" + URLEncoder.encode(probability, "UTF-8");

            db = new MyDatabase("http://giatros.net23.net/associate_symptom.php", data);
            result = db.receive();
        }
        catch(Exception e){
            Log.i(TAG, e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        dialog.dismiss();
        Toast.makeText(sdaa, "Saved", Toast.LENGTH_LONG).show();
    }
}
