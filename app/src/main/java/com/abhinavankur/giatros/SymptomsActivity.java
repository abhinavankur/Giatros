package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class SymptomsActivity extends AppCompatActivity implements ReceiveData, SymptomsInterface {
    ArrayList<String> selectedSymptoms = new ArrayList<>();
    ArrayList<String> symptoms = new ArrayList<>();
    ListView list;
    Button addButton, findDiseases;
    AutoCompleteTextView symptomFiller;
    MyAdapter myAdapter;
    SaveSymptom ss;
    Notif notif;
    ArrayAdapter<String> adapter;
    SymptomsLoaderAsync symptomsLoaderAsync;
    ProgressDialog dialog;
    String user, id;
    private static final String TAG = "giatros";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);

        SharedPreferences preferences = SymptomsActivity.this.getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        user = preferences.getString("user", null);
        id = preferences.getString("id", null);
        notif = new Notif(user, id, SymptomsActivity.this);
        notif.find();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Symptoms loading...");
        dialog.show();
        symptomsLoaderAsync = new SymptomsLoaderAsync(dialog, this);
        symptomsLoaderAsync.execute();

        list = (ListView) findViewById(R.id.list);
        myAdapter = new MyAdapter();
        list.setAdapter(myAdapter);
        symptomFiller = (AutoCompleteTextView) findViewById(R.id.symptomTextView);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new SymptomsListener());

        /*Find Diseases*/
        findDiseases = (Button) findViewById(R.id.findDiseases);
        findDiseases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSymptoms.size()<2) {
                    Toast.makeText(SymptomsActivity.this, "Choose at least two symptoms", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(SymptomsActivity.this, DiseaseShowActivity.class);
                    i.putStringArrayListExtra("symptoms", selectedSymptoms);
                    startActivity(i);
                }
            }
        });
    }

    public boolean validateSymptom() {
        String temp = symptomFiller.getText().toString().trim();
        String s1 = DiseaseAugmenter.toDbCase(temp);
        if (symptoms.indexOf(s1)==-1)
        {
            Log.i(TAG, "Old Symptom " + s1);
            return false;
        }
        Log.i(TAG, "New symptom " + s1);
        return true;
    }

    class SymptomsListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String temp = symptomFiller.getText().toString().trim();
            final String symptom = DiseaseAugmenter.toDbCase(temp);
            if (validateSymptom()) {
                symptomFiller.setText(null);
                selectedSymptoms.add(symptom);
                myAdapter.notifyDataSetChanged();
            }
            else
            {
                new AlertDialog.Builder(SymptomsActivity.this)
                        .setTitle("New Symptom")
                        .setMessage("Do you really want to add a new symptom?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                ProgressDialog dialog1 = new ProgressDialog(SymptomsActivity.this);
                                dialog1.setMessage("Saving Symptom");
                                dialog1.show();
                                ss = new SaveSymptom();
                                ss.setData(dialog1, symptom, SymptomsActivity.this);
                                ss.execute();
                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                symptomFiller.setText(null);
                                Toast.makeText(SymptomsActivity.this, "Enter symptoms again", Toast.LENGTH_SHORT).show();
                            }}).show();
            }
        }
    }

    @Override
    public void saved() {
        symptomFiller.setText(null);
        Toast.makeText(this, "Symptom Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_event_history, menu);
        menu.add("Update Profile");
        menu.add("Enter Symptoms");
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

        if (menuItem == "Enter Symptoms") {
            Intent i = new Intent(this,SymptomAugmenterActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getData(ArrayList<String>... symptoms) {
        this.symptoms = symptoms[0];
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, this.symptoms);
        symptomFiller.setThreshold(1); /*will start working from first character  */
        symptomFiller.setAdapter(adapter);
    }

    public class MyAdapter extends BaseAdapter {
        /*Adapter holds the reference to the list which is passed.*/
        @Override
        public int getCount() {
            return selectedSymptoms.size();
        }

        @Override
        public Object getItem(int position) {
            return selectedSymptoms.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
            /*LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.custom_symptom_row, parent, false);
            }

            TextView symptom = (TextView) convertView.findViewById(R.id.symptomsTextView);
            Button button = (Button) convertView.findViewById(R.id.symptomsButton);
            String value = selectedSymptoms.get(position);
            symptom.setText(value);

            button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    symptoms.add(getItem(position).toString());
                    selectedSymptoms.remove(position);
                    myAdapter.notifyDataSetChanged();    /*notifyDataSetChanged is a method of the Adapter class*/
                    adapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }
}
