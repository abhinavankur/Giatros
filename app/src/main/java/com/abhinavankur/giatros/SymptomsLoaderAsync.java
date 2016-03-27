package com.abhinavankur.giatros;

import android.os.AsyncTask;
import android.util.Log;
import android.app.ProgressDialog;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SymptomsLoaderAsync extends AsyncTask<Void,Void,String>{

    String result;
    JSONArray jsonArray;
    ArrayList<String> symptoms = new ArrayList<>();
    ProgressDialog dialog;
    SymptomsActivity symptomsActivity;
    public ReceiveData rd;
    public static final String TAG = "giatros";

    URL url;
    HttpURLConnection httpURLConnection;

    public SymptomsLoaderAsync(ProgressDialog progressDialog,SymptomsActivity sa){
        this.dialog = progressDialog;
        this.symptomsActivity = sa;
        this.rd = sa;
    }

    @Override
    protected String doInBackground(Void... params) {
        try{
            url = new URL("http://192.168.43.164/server/show_symptoms.php");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            inputStream.close();
            result = stringBuilder.toString();
            Log.i(TAG, result);

            jsonArray = new JSONArray(result);
            String temp;
            for (int i = 0; i < jsonArray.length(); i++) {
                temp = jsonArray.getString(i);
                Log.i(TAG, temp);
                symptoms.add(temp);
            }
        }
        catch(Exception e){
            Log.i(TAG,e.toString());
        }
        finally {
            httpURLConnection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        /*super.onPostExecute(s);*/
        dialog.dismiss();
        rd.getData(symptoms);
        symptomsActivity.actvAdapter();
    }
}
