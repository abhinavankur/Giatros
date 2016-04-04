package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

public class SignUpAsync extends AsyncTask<String,Void,String>{

    MyDatabase db;
    String firstName, lastName, password, emailId, phoneNumber;
    String result, data;
    ProgressDialog dialog;
    Context context;
    private static final String TAG = "giatros";

    public SignUpAsync(String firstName, String lastName, String password, String emailId, String phoneNumber, ProgressDialog dialog, Context context){
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.emailId = emailId;
        this.phoneNumber = phoneNumber;
        this.dialog = dialog;
        this.context = context;
    }
    @Override
    protected String doInBackground(String... params) {
        data="";
        try{
            data = URLEncoder.encode("first_name","UTF-8")+"="+URLEncoder.encode(firstName,"UTF-8");
            data += "&" + URLEncoder.encode("last_name","UTF-8")+"="+URLEncoder.encode(lastName,"UTF-8");
            data += "&" + URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
            data += "&" + URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(emailId,"UTF-8");
            data += "&" + URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phoneNumber,"UTF-8");

            db = new MyDatabase("http://giatros.net23.net/sign_up.php", data);
            result = db.receive();

            JSONArray array = new JSONArray(result);
            JSONObject object;
            for (int i=0;i<array.length();i++){
                object = array.getJSONObject(i);
                data = object.getString("key");
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
        if (data.equals("registered")){
            Toast.makeText(context, "Email Id exists", Toast.LENGTH_SHORT).show();
        }
        else if (data.equals("new")){
            Toast.makeText(context,"User succesfully registered",Toast.LENGTH_SHORT).show();
        }
    }
}
