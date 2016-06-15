package com.abhinavankur.giatros;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyDatabase {

    URL url;
    String sendData;
    HttpURLConnection connection;
    InputStream input;
    BufferedReader reader;
    StringBuilder builder;
    String line, result;
    private static final String TAG = "giatros";

    public MyDatabase(String url, String data) {
        try{
            this.url = new URL(url);
            this.sendData = data;
        }
        catch(MalformedURLException e){
            Log.i(TAG,e.toString());
        }
    }

    public String receive(){
        try{
            connection = (HttpURLConnection) url.openConnection();
            /*httpURLConnection.setRequestMethod("POST")*/ /*instead of a GET, method="POST"*/
           /* httpURLConnection.setChunkedStreamingMode(0);
            httpURLConnection.connect();*/

            if (!(sendData==null)){
                connection.setDoOutput(true);             /*set connection output to true*/
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(sendData);
                writer.flush();

            }
            input = new BufferedInputStream(connection.getInputStream());
            reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 8);
            builder = new StringBuilder();
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
            input.close();
            result = builder.toString();
            result = result.substring(0,result.indexOf("<!-- Hosting24 Analytics Code --><script type=\"text/javascript\" src=\"http://stats.hosting24.com/count.php\"></script><!-- End Of Analytics Code -->"));
        }
        catch (Exception e){
            Log.i(TAG,e.toString());

        }
        finally {
            connection.disconnect();
        }
        return result;
    }
}
