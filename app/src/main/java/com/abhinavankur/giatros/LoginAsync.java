package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;

public class LoginAsync extends AsyncTask<String,Void,String>{

    MyDatabase db;
    LoginActivity loginActivity;
    ProgressDialog dialog;
    String data;
    String result;
    String firstName, lastName, password, emailId, phoneNumber, user;
    Boolean flag = false;
    Logger l;
    Context context;
    private static final String TAG = "giatros";

    public LoginAsync(String emailId, String password, String user, LoginActivity loginActivity, ProgressDialog dialog){
        this.emailId = emailId;
        this.password = password;
        this.user = user;
        this.dialog = dialog;
        this.loginActivity = loginActivity;
        this.l = loginActivity;
        this.context = loginActivity;
    }
    @Override
    protected String doInBackground(String... params) {
        data = "";
        try{
            data = URLEncoder.encode("email","UTF-8") + "=" + URLEncoder.encode(emailId,"UTF-8");
            data += "&" + URLEncoder.encode("password","UTF-8") + "=" + URLEncoder.encode(password,"UTF-8");
            data += "&" + URLEncoder.encode("user","UTF-8") + "=" + URLEncoder.encode(user,"UTF-8");

            db = new MyDatabase("http://giatros.net23.net/sign_in.php", data);
            result = db.receive();

            if (result.length()!=2){
                flag = true;
                Log.i(TAG,"FLAG = True");
                JSONArray array = new JSONArray(result);
                JSONObject object;
                for (int i=0;i<array.length();i++){
                    object = array.getJSONObject(i);
                    firstName = object.getString("first_name");
                    lastName = object.getString("last_name");
                    phoneNumber = object.getString("phone");
                    emailId = object.getString("email");
                    password = object.getString("password");
                }
            }
            else{
                Log.i(TAG,"FLAG = False");
                flag = false;
            }

        }
        catch(Exception e){
            Log.i(TAG,e.toString());
        }
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        if (flag){
            SharedPreferences preferences = context.getSharedPreferences("Credentials",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user",user);
            editor.putString("firstName",firstName);
            editor.putString("lastName",lastName);
            editor.putString("emailId",emailId);
            editor.putString("password", password);
            editor.putString("phoneNumber", phoneNumber);
            editor.apply();

            Intent i;
            if (user.equals("Doctor")){
                i = new Intent(context,DiseaseAugmenter.class);
            }
            else{
                i = new Intent(context,SymptomsActivity.class);
            }
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        else{
            Toast.makeText(context, "Invalid Username/Password !!", Toast.LENGTH_SHORT).show();
        }
        l.getData(flag);
        dialog.dismiss();
    }
}
