package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Abhinav Ankur on 4/19/2016.
 */
public class AppointmentsLoader extends AsyncTask<String, Void, String>{

    ProgressDialog dialog;
    public ReceiveData rd;
    String id;
    MyDatabase database;
    String result;
    ArrayList<String> name, disease, number, appointmentId;
    private static final String TAG = "giatros";

    public void setData(ProgressDialog dialog, AppointmentActivity aa, String id){
        this.dialog = dialog;
        this.rd = aa;
        this.id = id;
        name = new ArrayList<>();
        number = new ArrayList<>();
        disease = new ArrayList<>();
        appointmentId = new ArrayList<>();
    }

    @Override
    protected String doInBackground(String... params) {
        String data = "";
        try{
            data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            database = new MyDatabase("http://giatros.net23.net/load_appointments.php", data);
            result = database.receive();

            JSONArray array= new JSONArray(result);
            JSONObject object;
            for (int i=0;i<array.length();i++){
                object = array.getJSONObject(i);
                name.add(object.getString("patient_first_name") + " " + object.getString("patient_last_name"));
                number.add(object.getString("patient_phone_number"));
                disease.add(object.getString("disease_name"));
                appointmentId.add(object.getString("appointment_id"));
            }
        }
        catch (UnsupportedEncodingException | NullPointerException | JSONException e){
            Log.i(TAG, e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        dialog.dismiss();
        rd.getData(name, number, disease, appointmentId);
    }
}
