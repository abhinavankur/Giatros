package com.abhinavankur.giatros;

import android.os.AsyncTask;
import android.util.Log;
import android.app.ProgressDialog;

import org.json.JSONArray;

import java.net.URL;
import java.util.ArrayList;

public class SymptomsLoaderAsync extends AsyncTask<Void,Void,String>{

    MyDatabase db;
    String result;
    JSONArray jsonArray;
    ArrayList<String> symptoms = new ArrayList<>();
    ProgressDialog dialog;
    public ReceiveData rd;
    public static final String TAG = "giatros";

    public SymptomsLoaderAsync(){

    }

    public SymptomsLoaderAsync(ProgressDialog progressDialog,SymptomsActivity sa){
        this.dialog = progressDialog;
        this.rd = sa;
    }

    public void setData(ProgressDialog dialog, DiseaseAugmenterTwo da2){
        this.dialog = dialog;
        this.rd = da2;
    }
    @Override
    protected String doInBackground(Void... params) {
        try{
            db = new MyDatabase("http://giatros.net23.net/show_symptoms.php", null);
            result = db.receive();

            jsonArray = new JSONArray(result);
            String temp;
            for (int i = 0; i < jsonArray.length(); i++) {
                temp = jsonArray.getString(i);
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
        /*super.onPostExecute(s);*/
        dialog.dismiss();
        rd.getData(symptoms);
    }
}
