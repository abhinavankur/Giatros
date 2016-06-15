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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SymptomDiseaseAssociateActivity extends AppCompatActivity implements ReceiveData{

    Intent i;
    String symptom;
    TextView symptomAssoc;
    AutoCompleteTextView diseaseAssoc;
    EditText probAssoc;
    Button saveAssoc;
    ProgressDialog dialog;
    ArrayAdapter<String> adapter;
    ArrayList<String> diseases;
    DiseasesLoader dl;

    AssociateSymptomAsync asa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_disease_associate);

        symptomAssoc = (TextView)findViewById(R.id.symptomAssoc);
        diseaseAssoc = (AutoCompleteTextView)findViewById(R.id.diseaseAssoc);
        probAssoc = (EditText)findViewById(R.id.probAssoc);
        saveAssoc = (Button)findViewById(R.id.saveAssoc);

        i = getIntent();
        symptom = i.getStringExtra("symptom");
        symptomAssoc.setText(symptom);
        diseases = new ArrayList<>();

        dl = new DiseasesLoader();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Diseases...");
        dialog.show();
        dl.setData(dialog, this);
        dl.execute();

        saveAssoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String disease = diseaseAssoc.getText().toString().trim();
                String probability = probAssoc.getText().toString().trim();
                dialog.setMessage("Saving...");
                dialog.show();
                asa = new AssociateSymptomAsync();
                asa.setData(dialog, disease, symptom, probability, SymptomDiseaseAssociateActivity.this);
                asa.execute();
                diseaseAssoc.setText(null);
                probAssoc.setText(null);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_event_history, menu);
        menu.add("Update Profile");
        menu.add("Associate Symptoms");
        menu.add("Show appointments");
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

        if (menuItem == "Associate Symptoms") {
            Intent i = new Intent(this,NewSymptomsListActivity.class);
            startActivity(i);
            finish();
        }

        if (menuItem == "Show appointments") {
            Intent i = new Intent(this, AppointmentActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getData(ArrayList<String>... symptoms) {
        this.diseases = symptoms[0];
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, this.diseases);
        diseaseAssoc.setThreshold(1); /*will start working from first character  */
        diseaseAssoc.setAdapter(adapter);

    }
}
