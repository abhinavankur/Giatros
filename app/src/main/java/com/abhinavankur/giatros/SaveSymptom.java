package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.URLEncoder;

/**
 * Created by Abhinav Ankur on 5/2/2016.
 */
public class SaveSymptom extends AsyncTask<String, Void, Void> {

    ProgressDialog dialog;
    String symptom;
    public SymptomsInterface si;;
    MyDatabase db;
    private static final String TAG = "giatros";

    public void setData(ProgressDialog dialog, String symptom, SymptomsInterface si){
        this.dialog = dialog;
        this.symptom = symptom;
        this.si = si;
    }
    @Override
    protected Void doInBackground(String... params) {
        String data = "";

        try {
            data = URLEncoder.encode("symptom", "UTF-8") + "=" + URLEncoder.encode(symptom, "UTF-8");
            db = new MyDatabase("http://giatros.net23.net/save_symptom.php", data);
            db.receive();

        }
        catch (Exception e){
            Log.i(TAG, e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        dialog.dismiss();
        si.saved();
    }
}
