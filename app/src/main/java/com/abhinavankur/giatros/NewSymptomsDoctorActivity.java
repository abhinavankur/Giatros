package com.abhinavankur.giatros;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NewSymptomsDoctorActivity extends AppCompatActivity {

    Intent i;
    String symptom;
    TextView newSymptom;
    Button newDiseaseButton, oldDiseaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_symptoms_doctor);

        i = getIntent();
        symptom = i.getStringExtra("symptom");
        newSymptom = (TextView)findViewById(R.id.newSymptom);
        newDiseaseButton = (Button)findViewById(R.id.newDiseaseButton);
        oldDiseaseButton = (Button)findViewById(R.id.oldDiseaseButton);

        newSymptom.setText(symptom);

        oldDiseaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(NewSymptomsDoctorActivity.this, SymptomDiseaseAssociateActivity.class);
                i.putExtra("symptom",symptom);
                startActivity(i);
            }
        });

        newDiseaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(NewSymptomsDoctorActivity.this, DiseaseAugmenter.class);
                i.putExtra("symptom",symptom);
                startActivity(i);
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
}
