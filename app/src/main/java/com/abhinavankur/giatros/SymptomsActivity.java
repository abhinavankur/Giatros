package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

public class SymptomsActivity extends AppCompatActivity implements ReceiveData {
    ArrayList<String> selectedSymptoms = new ArrayList<>();
    ArrayList<String> symptoms = new ArrayList<>();
    ListView list;
    Button addButton, findDiseases;
    AutoCompleteTextView symptomFiller;
    MyAdapter myAdapter;
    ArrayAdapter<String> adapter;
    SymptomsLoaderAsync symptomsLoaderAsync;
    ProgressDialog dialog;
    private static final String TAG = "giatros";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);

        /*Retrieving all the symptoms from the database*/
        dialog = new ProgressDialog(this);
        dialog.setMessage("Symptoms loading...");
        dialog.show();
        symptomsLoaderAsync = new SymptomsLoaderAsync(dialog, this);
        symptomsLoaderAsync.execute();

        list = (ListView) findViewById(R.id.list);
        myAdapter = new MyAdapter();
        list.setAdapter(myAdapter);

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String symptom = symptomFiller.getText().toString();
                symptomFiller.setText(null);
                selectedSymptoms.add(symptom);
                myAdapter.notifyDataSetChanged();

                symptoms.remove(symptoms.indexOf(symptom));
                adapter.notifyDataSetChanged();
            }
        });

        /*Find Diseases*/
        findDiseases = (Button) findViewById(R.id.findDiseases);
        findDiseases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSymptoms.isEmpty()) {
                    Toast.makeText(SymptomsActivity.this, "Choose at least one symptom", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(SymptomsActivity.this, DiseaseActivity.class);
                    i.putStringArrayListExtra("symptoms", selectedSymptoms);
                    startActivity(i);
                }
            }
        });
    }

    public void actvAdapter() {
        adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, symptoms);
        symptomFiller = (AutoCompleteTextView) findViewById(R.id.symptomTextView);
        if (symptomFiller != null) {
            symptomFiller.setThreshold(1); /*will start working from first character  */
            symptomFiller.setAdapter(adapter);
        }
    }

    @Override
    public void getData(ArrayList<String> symptoms) {
        this.symptoms = symptoms;
    }

    public class MyAdapter extends BaseAdapter {
        /*Adapter holds the reference to the list which is passed.*/
        @Override
        public int getCount() {
            return selectedSymptoms.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
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
            final String value = selectedSymptoms.get(position);
            symptom.setText(value);

            button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedSymptoms.remove(position);
                    notifyDataSetChanged();    /*notifyDataSetChanged is a method of the Adapter class*/

                    symptoms.add(getItem(position).toString());
                    adapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }
}
