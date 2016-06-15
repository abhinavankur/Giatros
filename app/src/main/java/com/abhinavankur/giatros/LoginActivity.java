package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements Logger{

    EditText email_id,pass;
    boolean flag=false;
    String emailId, password, user;
    Button login;
    ImageButton showLoginPassword;
    TextView sign_up;
    Spinner sp;
    ArrayList<String> choice;
    private static boolean SHOW_FLAG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        showLoginPassword = (ImageButton) findViewById(R.id.showLoginPassword);
        sign_up = (TextView) findViewById(R.id.sign_up);
        email_id = (EditText)findViewById(R.id.email_id);
        pass = (EditText)findViewById(R.id.password);
        login = (Button) findViewById(R.id.sign_in);
        sp = (Spinner) findViewById(R.id.spinner);
        choice = new ArrayList<>();
        choice.add("Doctor");
        choice.add("Patient");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,choice);
        sp.setAdapter(adapter);

        showLoginPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SHOW_FLAG){
                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    SHOW_FLAG = true;
                }
                else if (SHOW_FLAG){
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    SHOW_FLAG = false;
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailId = email_id.getText().toString().trim();
                password = pass.getText().toString().trim();
                user = sp.getSelectedItem().toString();

                ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage("Please Wait..");
                dialog.show();

                LoginAsync loginAsync = new LoginAsync(emailId, password, user, LoginActivity.this, dialog);
                loginAsync.execute();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,SignUpActivity.class);
                i.putExtra("user",user);
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
        if (flag) {
            finish();
        } else {
            email_id.setText("");
            pass.setText("");
        }
    }
}
