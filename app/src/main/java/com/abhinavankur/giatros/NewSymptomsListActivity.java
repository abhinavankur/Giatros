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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class NewSymptomsListActivity extends AppCompatActivity implements ReceiveData{

    NewSymptomsLoader nsl;
    ProgressDialog dialog;
    ArrayList<String> symptoms;
    ArrayAdapter<String> adapter;
    ListView newSymptomsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_symptoms_list);

        newSymptomsList = (ListView)findViewById(R.id.newSymptomsList);
        symptoms = new ArrayList<>();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Symptoms...");
        dialog.show();
        nsl = new NewSymptomsLoader();
        nsl.setData(dialog, this);
        nsl.execute();

        newSymptomsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(NewSymptomsListActivity.this, NewSymptomsDoctorActivity.class);
                i.putExtra("symptom",symptoms.get(position));
                startActivity(i);
            }
        });
    }


    @Override
    public void getData(ArrayList<String>... symptoms) {
        this.symptoms = symptoms[0];
        if (this.symptoms.size()==0){
            Toast.makeText(NewSymptomsListActivity.this, "No New Symptoms", Toast.LENGTH_SHORT).show();
            try{
                wait(Toast.LENGTH_LONG);
            }
            catch (InterruptedException e){

            }
            finish();
        }
        adapter = new ArrayAdapter<>(this, R.layout.my_list, this.symptoms);
        newSymptomsList.setAdapter(adapter);
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
