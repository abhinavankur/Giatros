package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class SymptomAugmenterActivity extends AppCompatActivity implements SymptomsInterface{

    EditText newSymptom;
    Button serialize;
    String value;
    ProgressDialog dialog;
    SaveSymptom ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_augmenter);

        newSymptom = (EditText)findViewById(R.id.newSymptom);
        serialize = (Button)findViewById(R.id.serialize);

        serialize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = DiseaseAugmenter.toDbCase(newSymptom.getText().toString().trim());
                dialog = new ProgressDialog(SymptomAugmenterActivity.this);
                dialog.setMessage("Saving Symptom...");
                dialog.show();
                ss = new SaveSymptom();
                ss.setData(dialog, value, SymptomAugmenterActivity.this);
                ss.execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_event_history, menu);
        menu.add("Update Profile");
        menu.add("Logout");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        String menuItem = item.toString();

        //noinspection SimplifiableIfStatement
        if (menuItem == "Logout") {
            SharedPreferences preferences = this.getSharedPreferences("Credentials", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("emailId",null);
            editor.putString("password", null);
            editor.putString("user", null);
            editor.apply();

            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();
        }

        if (menuItem == "Update Profile") {
            /*Snackbar.make(View view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();*/
            UpdateProfile updateProfile = new UpdateProfile(this);
            updateProfile.setFields();
            Toast.makeText(this, menuItem, Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void saved() {
        newSymptom.setText(null);
        Toast.makeText(this, "Symptom Saved", Toast.LENGTH_SHORT).show();
    }
}