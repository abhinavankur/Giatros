package com.abhinavankur.giatros;


import android.app.ProgressDialog;
import android.content.Context;
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

public class SignUpAsync extends AsyncTask<String,Void,String>{

    String firstName, lastName, password, emailId, phoneNumber;
    String result, data;
    ProgressDialog dialog;
    Context context;
    HttpURLConnection connection;
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
            URL url = new URL("http://giatros.net23.net/sign_up.php");
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(data);
            writer.flush();

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"),8);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
            inputStream.close();
            result = builder.toString();
            result = result.substring(0,result.indexOf("<!-- Hosting24 Analytics Code --><script type=\"text/javascript\" src=\"http://stats.hosting24.com/count.php\"></script><!-- End Of Analytics Code -->"));

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
        finally {
            connection.disconnect();
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
