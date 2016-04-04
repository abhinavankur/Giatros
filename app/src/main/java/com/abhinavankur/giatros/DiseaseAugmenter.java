package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DiseaseAugmenter extends AppCompatActivity implements ReceiveData{

    TextView disease;
    AutoCompleteTextView specialization, tests;
    SpecializationTestsAsync loaderAsync;
    ListView list;
    ArrayList<String> specializationList, testsList, diseaseList, selectedTestsList;
    ArrayAdapter<String> specialAdapter, testAdapter;
    MyAdapter selectedTestAdapter;
    String diseaseValue, specializationValue;
    Button add, next;
    Boolean flag = false;
    private static final String TAG = "giatros";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_augmenter);

        disease = (TextView) findViewById(R.id.diseaseName);
        specialization = (AutoCompleteTextView) findViewById(R.id.specialization);
        tests = (AutoCompleteTextView) findViewById(R.id.tests);
        list = (ListView) findViewById(R.id.selectedTests);
        add = (Button) findViewById(R.id.addTest);
        next = (Button) findViewById(R.id.nextDisease);
        specializationList = new ArrayList<>();
        testsList = new ArrayList<>();
        selectedTestsList = new ArrayList<>();
        diseaseList = new ArrayList<>();
        selectedTestAdapter = new MyAdapter();
        list.setAdapter(selectedTestAdapter);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();
        loaderAsync = new SpecializationTestsAsync(dialog, this);
        loaderAsync.execute();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                String test = tests.getText().toString().trim();
                tests.setText(null);
                selectedTestsList.add(test);
                selectedTestAdapter.notifyDataSetChanged();

                if (testsList.indexOf(test) != -1){
                    flag = true;
                    testsList.remove(testsList.indexOf(test));
                    testAdapter.notifyDataSetChanged();
                    Log.i(TAG, testsList.toString());
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diseaseValue = disease.getText().toString().trim();
                specializationValue = specialization.getText().toString().trim();
                if (!diseaseValue.isEmpty() && !specializationValue.isEmpty()){
                    if (diseaseList.indexOf(diseaseValue)!=-1){
                        Toast.makeText(DiseaseAugmenter.this,"Disease Name already exists!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent i = new Intent(DiseaseAugmenter.this, DiseaseAugmenterTwo.class);
                        if (selectedTestsList.isEmpty()){
                            Toast.makeText(DiseaseAugmenter.this, "No test associated with this disease", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            i.putStringArrayListExtra("tests",selectedTestsList);
                        }
                        i.putExtra("disease",diseaseValue);
                        i.putExtra("specialization",specializationValue);
                        startActivity(i);
                    }
                }
                else{
                    Toast.makeText(DiseaseAugmenter.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String toDbCase(String sentence){
        Boolean f = false;
        StringBuilder newSentence = new StringBuilder();
        Character c= Character.toUpperCase(sentence.charAt(0));
        newSentence.append(c);
        for (int i=1;i<sentence.length();i++){
            if (Character.isWhitespace(sentence.charAt(i))){
                c = '-';
                f = true;
            }
            else if(f){
                c = Character.toUpperCase(sentence.charAt(i));
                f = false;
            }
            newSentence.append(c);
        }
        return newSentence.toString();
    }
    @Override
    public void getData(ArrayList<String>... symptoms) {
        specializationList = symptoms[0];
        testsList = symptoms[1];
        diseaseList = symptoms[2];
        specialAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, specializationList);
        testAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, testsList);

        specialization.setThreshold(1);
        tests.setThreshold(1);
        specialization.setAdapter(specialAdapter);
        tests.setAdapter(testAdapter);
    }

    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return selectedTestsList.size();
        }

        @Override
        public Object getItem(int position) {
            return selectedTestsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                LayoutInflater myInflater = getLayoutInflater();
                convertView = myInflater.inflate(R.layout.custom_symptom_row, parent, false);
            }
            TextView test = (TextView) convertView.findViewById(R.id.symptomsTextView);
            Button button = (Button) convertView.findViewById(R.id.symptomsButton);
            String value = selectedTestsList.get(position);
            test.setText(value);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag) {
                        testsList.add(getItem(position).toString());
                        testAdapter.notifyDataSetChanged();
                    }
                    selectedTestsList.remove(position);
                    selectedTestAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }
}
