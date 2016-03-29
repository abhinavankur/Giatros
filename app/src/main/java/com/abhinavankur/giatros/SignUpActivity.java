package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    String firstName, lastName, password, emailId, phoneNumber;
    EditText first_name, last_name, pass, email, phone_number;
    Button submit;
    ImageButton showPassword;
    private static final int SHOW_FLAG = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        pass = (EditText) findViewById(R.id.pass);
        email = (EditText) findViewById(R.id.email);
        phone_number = (EditText) findViewById(R.id.phone_number);
        submit = (Button) findViewById(R.id.submit);
        showPassword = (ImageButton) findViewById(R.id.showPassword);

        first_name.addTextChangedListener(new MyTextWatcher(first_name));
        last_name.addTextChangedListener(new MyTextWatcher(last_name));
        pass.addTextChangedListener(new MyTextWatcher(pass));
        email.addTextChangedListener(new MyTextWatcher(email));
        phone_number.addTextChangedListener(new MyTextWatcher(phone_number));

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()){
                    firstName = first_name.getText().toString().trim();
                    lastName = last_name.getText().toString().trim();
                    emailId = email.getText().toString().trim();
                    password = pass.getText().toString().trim();
                    phoneNumber = phone_number.getText().toString().trim();
                    ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
                    dialog.setMessage("Saving Data...");
                    dialog.show();
                    SignUpAsync signUpAsync = new SignUpAsync(firstName, lastName, password, emailId, phoneNumber, dialog, getBaseContext());
                    signUpAsync.execute();

                    Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(i);
                    finish();

                }
                else{
                    Toast.makeText(getApplicationContext(),"Please enter details correctly",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private class MyTextWatcher implements TextWatcher{

        View view;
        public MyTextWatcher(View view){
            this.view = view;
        }
        @Override
        public void afterTextChanged(Editable s) {
            Validation.hasText((EditText)view);
            if (view.equals(email)){
                Validation.isEmailAddress((EditText)view,true);
            }
            if (view.equals(phone_number)){
                Validation.isPhoneNumber((EditText)view,false);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(first_name)) ret = false;
        if (!Validation.hasText(last_name)) ret = false;
        if (!Validation.isEmailAddress(email, true)) ret = false;
        if (!Validation.hasText(pass)) ret = false;
        if (!Validation.hasText(phone_number)) ret = false;
        return ret;
    }
}
