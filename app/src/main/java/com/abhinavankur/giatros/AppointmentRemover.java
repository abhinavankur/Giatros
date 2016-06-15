package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URLEncoder;

/**
 * Created by Abhinav Ankur on 4/19/2016.
 */
public class AppointmentRemover extends AsyncTask<String, Void, Void>{

    ProgressDialog dialog;
    String id, data;
    AppointmentActivity aa;
    int position;
    private static final String TAG = "giatros";

    public void setData(String id, ProgressDialog dialog, AppointmentActivity aa, int pos){
        this.id = id;
        this.dialog = dialog;
        this.aa = aa;
        this.position = pos;
    }

    @Override
    protected Void doInBackground(String... params) {
        data = "";
        try{
            data = URLEncoder.encode("appointment_id","UTF-8") + "=" + URLEncoder.encode(id,"UTF-8");
            MyDatabase db = new MyDatabase("http://giatros.net23.net/delete_appointment.php", data);
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
        aa.deleteAppointment(position);
    }
}
