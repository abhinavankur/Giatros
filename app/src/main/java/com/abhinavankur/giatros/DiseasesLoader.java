package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Abhinav Ankur on 5/3/2016.
 */
public class DiseasesLoader extends AsyncTask<Void, Void, String> {

    MyDatabase db;
    ProgressDialog dialog;
    String result;
    JSONArray jsonArray;
    ArrayList<String> diseases;
    ReceiveData rd;
    private static final String TAG = "giatros";

    public void setData(ProgressDialog dialog, ReceiveData rd){
        this.dialog = dialog;
        this.rd = rd;
    }
    @Override
    protected String doInBackground(Void... params) {

        diseases = new ArrayList<>();
        try{
            db = new MyDatabase("http://giatros.net23.net/show_all_diseases.php", null);
            result = db.receive();
            jsonArray = new JSONArray(result);
            String temp;
            for (int i = 0; i < jsonArray.length(); i++) {
                temp = jsonArray.getString(i);
                diseases.add(temp);
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
        rd.getData(diseases);
    }
}
