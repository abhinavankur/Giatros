package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DiseaseAugmenterTwo extends AppCompatActivity implements ReceiveData{

    AutoCompleteTextView symptomFiller;
    Button add, save;
    ProgressDialog dialog;
    ListView listView;
    SymptomsLoaderAsync symptomsLoaderAsync;
    NewAdapter adapter;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<Double> assocAdapter;
    String disease, specialization;
    ArrayList<String> testsList, symptomsList, selectedSymptomsList;
    ArrayList<Double> assocList, filledAssocList;
    Intent i;
    DiseaseSerializer serializer;
    private static final String TAG = "giatros";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_augmenter_two);

        i = getIntent();
        disease = i.getStringExtra("disease");
        specialization = i.getStringExtra("specialization");
        testsList = i.getStringArrayListExtra("tests");

        symptomFiller = (AutoCompleteTextView) findViewById(R.id.symptomFiller);
        listView = (ListView) findViewById(R.id.listView2);
        add = (Button) findViewById(R.id.addSymptoms);
        save = (Button) findViewById(R.id.saveDisease);
        testsList = new ArrayList<>();
        symptomsList = new ArrayList<>();
        selectedSymptomsList = new ArrayList<>();
        assocList = new ArrayList<>();
        for (double i=0.01;i<=1.00;i+=0.01){
            assocList.add(i);
        }
        assocAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, assocList);
        adapter = new NewAdapter();
        listView.setAdapter(adapter);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Symptoms loading...");
        dialog.show();
        symptomsLoaderAsync = new SymptomsLoaderAsync();
        symptomsLoaderAsync.setData(dialog, this);
        symptomsLoaderAsync.execute();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSymptomsList.isEmpty()){
                    Toast.makeText(DiseaseAugmenterTwo.this, "Enter at least one symptom", Toast.LENGTH_SHORT).show();
                }
                else{
                    serializer = new DiseaseSerializer(disease, specialization, testsList, selectedSymptomsList, filledAssocList);

                }
            }
        });
    }

    @Override
    public void getData(ArrayList<String>... symptoms) {
        symptomsList = symptoms[0];
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, symptomsList);
        symptomFiller.setThreshold(1);
        symptomFiller.setAdapter(arrayAdapter);
    }

    public class NewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return selectedSymptomsList.size();
        }

        @Override
        public Object getItem(int position) {
            return selectedSymptomsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.custom_symptom_filler, parent, false);
            }

            TextView symptomsTextView = (TextView) convertView.findViewById(R.id.symptomsTextView);
            Button removeSymptom = (Button) convertView.findViewById(R.id.removeSymptom);
            Spinner assoc = (Spinner) convertView.findViewById(R.id.assoc);
            String value = selectedSymptomsList.get(position);
            symptomsTextView.setText(value);
            assoc.setAdapter(assocAdapter);

            removeSymptom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            return convertView;
        }
    }
}
