package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements Logger{

    EditText email_id,pass;
    boolean flag=false;
    String emailId, password;
    Button login;
    TextView sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sign_up = (TextView) findViewById(R.id.sign_up);
        email_id = (EditText)findViewById(R.id.email_id);
        pass = (EditText)findViewById(R.id.password);
        login = (Button) findViewById(R.id.sign_in);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailId = email_id.getText().toString().trim();
                password = pass.getText().toString().trim();
                ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage("Please Wait..");
                dialog.show();

                LoginAsync loginAsync = new LoginAsync(emailId, password, LoginActivity.this, dialog);
                loginAsync.execute();
                if (flag) {
                    finish();
                } else {
                    //Toast.makeText(this,"Verify Credentials",Toast.LENGTH_LONG).show();
                    email_id.setText("");
                    pass.setText("");
                }
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,SignUpActivity.class);
                finish();
                startActivity(i);
            }
        });

        /*if(!this.isNetworkConnected())
        {
            Toast.makeText(this,"Unable to connect to the Internet",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"Connected to the Internet",Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
        /*if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;*/
    }


    @Override
    public void getData(boolean b) {
        flag=b;
    }
}
