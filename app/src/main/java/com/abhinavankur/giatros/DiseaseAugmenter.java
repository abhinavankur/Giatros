package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
    ArrayList<String> specializationList, testsList, diseaseList, selectedTestsList, newTests;
    ArrayAdapter<String> specialAdapter, testAdapter;
    MyAdapter selectedTestAdapter;
    String diseaseValue, specializationValue, user, id;
    Button add, next;
    Boolean flag = false;
    Intent intent;
    Notif notif;
    String symptom = "blank";
    private static final String TAG = "giatros";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_augmenter);

        SharedPreferences preferences = DiseaseAugmenter.this.getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        user = preferences.getString("user", null);
        id = preferences.getString("id", null);
        notif = new Notif(user, id, DiseaseAugmenter.this);
        notif.find();

        intent = getIntent();
        if (intent.getStringExtra("symptom")!=null){
            symptom = intent.getStringExtra("symptom");
        }

        disease = (TextView) findViewById(R.id.diseaseName);
        specialization = (AutoCompleteTextView) findViewById(R.id.specialization);
        tests = (AutoCompleteTextView) findViewById(R.id.tests);
        list = (ListView) findViewById(R.id.selectedTests);
        add = (Button) findViewById(R.id.addTest);
        next = (Button) findViewById(R.id.nextDisease);
        specializationList = new ArrayList<>();
        testsList = new ArrayList<>();
        newTests = new ArrayList<>();
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
                    newTests.add("false");
                    testsList.remove(testsList.indexOf(test));
                    testAdapter.notifyDataSetChanged();
                }
                else{
                    newTests.add("true");
                }
                Log.i(TAG, newTests.get(newTests.size()-1));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = disease.getText().toString().trim();
                if (!temp.isEmpty()) {
                    diseaseValue = toDbCase(temp);
                    temp = specialization.getText().toString().trim();
                    if (!temp.isEmpty()) {
                        specializationValue = toDbCase(temp);
                        if (diseaseList.indexOf(diseaseValue) != -1) {
                            Toast.makeText(DiseaseAugmenter.this, "Disease Name already exists!", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(DiseaseAugmenter.this, DiseaseAugmenterTwo.class);
                            if (selectedTestsList.isEmpty()) {
                                Toast.makeText(DiseaseAugmenter.this, "No test associated with this disease", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < selectedTestsList.size(); i++) {
                                    temp = selectedTestsList.get(i);
                                    String value = toDbCase(temp);
                                    selectedTestsList.set(i, value);
                                }
                                intent.putStringArrayListExtra("tests", selectedTestsList);
                                intent.putStringArrayListExtra("newTests", newTests);
                            }
                            if (specializationList.indexOf(specializationValue) != -1) {
                                intent.putExtra("newSpecialization", "false");
                            } else {
                                intent.putExtra("newSpecialization", "true");
                            }
                            intent.putExtra("disease", diseaseValue);
                            intent.putExtra("specialization", specializationValue);
                            if (symptom!="blank"){
                                intent.putExtra("symptom", symptom);
                            }
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(DiseaseAugmenter.this, "Fill specialization name!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DiseaseAugmenter.this, "Fill disease name!", Toast.LENGTH_SHORT).show();
                }
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

        if (menuItem == "Update") {
            /*Snackbar.make(View view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();*/
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

    public static String toDbCase(String sentence){
        Boolean f = false;
        StringBuilder newSentence = new StringBuilder();
        Character c= Character.toUpperCase(sentence.charAt(0));
        newSentence.append(c);
        for (int i=1;i<sentence.length();i++){
            c = sentence.charAt(i);
            if (Character.isWhitespace(c)){
                c = '-';
                f = true;
            }
            else if(f){
                c = Character.toUpperCase(c);
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
                    newTests.remove(position);
                    selectedTestsList.remove(position);
                    selectedTestAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }
}
