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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginAsync extends AsyncTask<String,Void,String>{
    LoginActivity loginActivity;
    ProgressDialog dialog;
    String data;
    String result;
    String firstName, lastName, password, emailId, phoneNumber;
    HttpURLConnection connection;
    Boolean flag = false;
    Logger l;
    Context context;
    private static final String TAG = "giatros";

    public LoginAsync(String emailId, String password, LoginActivity loginActivity, ProgressDialog dialog){
        this.emailId = emailId;
        this.password = password;
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

            URL url = new URL("http://192.168.43.164/server/sign_in.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(data);
            writer.flush();

            InputStream input = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(input,"UTF-8"), 8);
            StringBuilder builder = new StringBuilder();
            String row;
            while ((row=reader.readLine())!=null){
                builder.append(row);
            }
            input.close();
            result = builder.toString();
            Log.i(TAG,String.valueOf(result.length()));

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
        finally {
            connection.disconnect();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        if (flag){
            SharedPreferences preferences = context.getSharedPreferences("Credentials",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("firstName",firstName);
            editor.putString("lastName",lastName);
            editor.putString("emailId",emailId);
            editor.putString("password", password);
            editor.putString("phoneNumber", phoneNumber);
            editor.apply();

            Intent i = new Intent(context,SymptomsActivity.class);
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
