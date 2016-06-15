package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import com.abhinavankur.giatros.DiseaseAugmenter;

public class DiseaseAugmenterTwo extends AppCompatActivity implements ReceiveData{

    AutoCompleteTextView symptomFiller;
    Button add, save;
    ProgressDialog dialog;
    ListView listView;
    SymptomsLoaderAsync symptomsLoaderAsync;
    NewAdapter adapter;
    ArrayAdapter<String> arrayAdapter;
    String disease, specialization, newSpecialization, symptom = "blank";
    ArrayList<String> testsList, symptomsList, selectedSymptomsList, newTests, newSymptoms;
    ArrayList<Double> filledAssocList;
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
        newSpecialization = i.getStringExtra("newSpecialization");
        if (i.getStringExtra("symptom")!=null){
            symptom = i.getStringExtra("symptom");
        }

        newTests = i.getStringArrayListExtra("newTests");
        testsList = i.getStringArrayListExtra("tests");

        symptomFiller = (AutoCompleteTextView) findViewById(R.id.symptomFiller);
        listView = (ListView) findViewById(R.id.listView2);
        add = (Button) findViewById(R.id.addSymptoms);
        save = (Button) findViewById(R.id.saveDisease);
        symptomsList = new ArrayList<>();
        newSymptoms = new ArrayList<>();
        selectedSymptomsList = new ArrayList<>();
        filledAssocList = new ArrayList<>();

        if (symptom != "blank") {
            symptomFiller.setText(symptom);
        }

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
                if (selectedSymptomsList.isEmpty()) {
                    Toast.makeText(DiseaseAugmenterTwo.this, "Enter at least one symptom", Toast.LENGTH_SHORT).show();
                } else {
                    String temp;
                    for (int i = 0; i < selectedSymptomsList.size(); i++) {
                        temp = selectedSymptomsList.get(i);
                        selectedSymptomsList.set(i, DiseaseAugmenter.toDbCase(temp));
                    }
                    dialog.setMessage("Saving...");
                    dialog.show();
                    serializer = new DiseaseSerializer(DiseaseAugmenterTwo.this, dialog, disease, specialization, newSpecialization, testsList, newTests, selectedSymptomsList, newSymptoms, filledAssocList);
                    serializer.execute();
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = symptomFiller.getText().toString().trim();
                symptomFiller.setText(null);
                String value = DiseaseAugmenter.toDbCase(temp);
                selectedSymptomsList.add(value);
                adapter.notifyDataSetChanged();

                if (symptomsList.indexOf(value)!=-1){
                    newSymptoms.add("false");
                    symptomsList.remove(value);
                    arrayAdapter.notifyDataSetChanged();
                }
                else{
                    newSymptoms.add("true");
                }
            }
        });
    }

    public void diseaseSaved(){
        Toast.makeText(DiseaseAugmenterTwo.this, "Disease Saved", Toast.LENGTH_SHORT).show();
        try{
            Thread.sleep(Toast.LENGTH_LONG);
        }
        catch (InterruptedException e){

        }
        new AlertDialog.Builder(DiseaseAugmenterTwo.this)
                .setTitle("Save another disease")
                .setMessage("Do you want to save another disease?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(DiseaseAugmenterTwo.this, DiseaseAugmenter.class);
                        startActivity(i);
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(DiseaseAugmenterTwo.this, AppointmentActivity.class);
                        startActivity(i);
                    }}).show();
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
            Intent i = new Intent(DiseaseAugmenterTwo.this, AppointmentActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.custom_symptom_filler, parent, false);
            }

            TextView symptomsTextView = (TextView) convertView.findViewById(R.id.symptomsTextView);
            Button removeSymptom = (Button) convertView.findViewById(R.id.removeSymptom);
            final EditText assoc = (EditText) convertView.findViewById(R.id.assoc);
            String value = selectedSymptomsList.get(position);
            symptomsTextView.setText(value);

            removeSymptom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value = getItem(position).toString();
                    selectedSymptomsList.remove(position);
                    filledAssocList.remove(position);
                    adapter.notifyDataSetChanged();
                    if (symptomsList.indexOf(value) != -1) {
                        symptomsList.add(value);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            });

            class MyTextWatcher implements TextWatcher {

                View view;
                public MyTextWatcher(View view){
                    this.view = view;
                }
                @Override
                public void afterTextChanged(Editable s) {
                    Validation.hasText((EditText)view);
                    Validation.isNumber((EditText)view,true);

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
            }


            assoc.addTextChangedListener(new MyTextWatcher(assoc));

            assoc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        filledAssocList.add(Double.valueOf(assoc.getText().toString()));
                    }
                }
            });
            return convertView;
        }
    }
}
