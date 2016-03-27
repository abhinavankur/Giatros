package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DiseaseLoaderAsync extends AsyncTask<String, Void, String> {
    ArrayList<String> symptoms, disease_id = new ArrayList<>(), disease_name = new ArrayList<>();
    DiseaseActivity diseaseActivity;
    HttpURLConnection httpURLConnection;
    JSONObject values;
    ProgressDialog dialog;
    ReceiveData rd;
    String result;
    String data = "";
    private static final String TAG = "giatros";

    public DiseaseLoaderAsync(ProgressDialog dialog, ArrayList<String> symptoms, DiseaseActivity da) {
        this.dialog = dialog;
        this.symptoms = symptoms;
        this.rd = da;
        this.diseaseActivity = da;
    }


    @Override
    protected String doInBackground(String... params) {

        try {
            values = new JSONObject();

            for (int i = 0; i < symptoms.size(); i++) {
                if (i == 0)
                    data += URLEncoder.encode("symptom" + String.valueOf(i + 1), "UTF-8") + "=" + URLEncoder.encode(symptoms.get(i), "UTF-8");
                else
                    data += "&" + URLEncoder.encode("symptom" + String.valueOf(i + 1), "UTF-8") + "=" + URLEncoder.encode(symptoms.get(i), "UTF-8");
            }

            URL url = new URL("http://192.168.43.164/server/show_diseases.php");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);    /*set connection output to true*/
            /*httpURLConnection.setRequestMethod("POST")*/ /*instead of a GET, method="POST"*/
           /* httpURLConnection.setChunkedStreamingMode(0);
            httpURLConnection.connect();*/

            OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
            writer.write(data);
            writer.flush();

            InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            in.close();
            result = stringBuilder.toString();
            Log.i(TAG, result);

            JSONArray diseases = new JSONArray(result);
            JSONObject disease;
            for (int i = 0; i < diseases.length(); i++) {
                disease = diseases.getJSONObject(i);
                disease_id.add(disease.getString("disease_id"));
                disease_name.add(disease.getString("disease_name"));
            }
        } catch (Exception e) {
            Log.i(TAG, e.toString());
        } finally {
            httpURLConnection.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        dialog.dismiss();
        rd.getData(disease_name);
        diseaseActivity.callback();
    }
}
