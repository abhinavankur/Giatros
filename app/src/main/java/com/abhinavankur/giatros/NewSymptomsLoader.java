package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Abhinav Ankur on 5/3/2016.
 */
public class NewSymptomsLoader extends AsyncTask<Void, Void, String> {

    ProgressDialog dialog;
    NewSymptomsListActivity nsla;
    MyDatabase database;
    JSONArray jsonArray;
    ArrayList<String> symptoms;
    ReceiveData rd;
    private static final String TAG = "giatros";

    public void setData(ProgressDialog dialog,NewSymptomsListActivity nsla){
        this.dialog = dialog;
        this.nsla = nsla;
        this.rd = nsla;
    }
    @Override
    protected String doInBackground(Void... params) {
        String result = "";
        symptoms = new ArrayList<>();
        try{
            database = new MyDatabase("http://giatros.net23.net/show_new_symptoms.php", null);
            result = database.receive();

            jsonArray = new JSONArray(result);
            JSONObject object;
            String temp;
            for (int i=0;i<jsonArray.length();i++){
                object = jsonArray.getJSONObject(i);
                temp = object.getString("symptom_name");
                symptoms.add(temp);
            }
        }
        catch(Exception e){
            Log.i(TAG,e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        dialog.dismiss();
        rd.getData(symptoms);
    }
}

