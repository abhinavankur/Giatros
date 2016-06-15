package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class DiseaseShowActivity extends AppCompatActivity implements ReceiveData{

    ExpandableListView expandableListView;
    DiseaseExpandableAdapter adapter;
    List<String> group;
    HashMap<String, List<String>> child, childVal;
    ArrayList<String> symptoms, matched, diseaseName, assocName, specialName;
    ProgressDialog dialog;
    DiseaseLoaderAsync diseaseLoaderAsync;
    Intent i;
    private static final String TAG = "giatros";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_show);

        expandableListView = (ExpandableListView) findViewById(R.id.diseaseShowList);
        symptoms = new ArrayList<>();
        matched = new ArrayList<>();
        diseaseName = new ArrayList<>();
        assocName = new ArrayList<>();
        specialName = new ArrayList<>();
        i = getIntent();
        symptoms = i.getStringArrayListExtra("symptoms");
        dialog = new ProgressDialog(this);
        dialog.setMessage("Diseases loading...");
        dialog.show();
        diseaseLoaderAsync = new DiseaseLoaderAsync(dialog,symptoms,this);
        diseaseLoaderAsync.execute();

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(DiseaseShowActivity.this,group.get(groupPosition)+ " : "+ child.get(group.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
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

        if (menuItem == "Update") {
            /*Snackbar.make(View view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();*/
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
    public void onBackPressed() {
        finish();
    }

    @Override
    public void getData(ArrayList<String>... symptoms) {
        this.matched = symptoms[0];
        this.diseaseName = symptoms[1];
        this.assocName = symptoms[2];
        this.specialName = symptoms[3];
        prepareListItems();

        adapter = new DiseaseExpandableAdapter(this, group, child, childVal);
        expandableListView.setAdapter(adapter);
    }

    public String findSpecialization(String disease){
        int i = diseaseName.indexOf(disease);
        return specialName.get(i);
    }
    private void prepareListItems() {
        group = new ArrayList<>();
        child = new HashMap<>();
        childVal = new HashMap<>();

        group.add("Highly Probable Diseases");
        group.add("Less Probable Diseases");

        List<String> most = new ArrayList<>();
        List<String> mostVal = new ArrayList<>();
        List<String> least = new ArrayList<>();
        List<String> leastVal = new ArrayList<>();

        String temp;
        for (int i=0;i<assocName.size();i++){
            for (int j=i+1;j<assocName.size();j++){
                if ((Double.valueOf(assocName.get(i))/Double.valueOf(matched.get(i)))<(Double.valueOf(assocName.get(j))/Double.valueOf(matched.get(j)))){
                    temp = matched.get(i);
                    matched.set(i, matched.get(j));
                    matched.set(j, temp);

                    temp = diseaseName.get(i);
                    diseaseName.set(i, diseaseName.get(j));
                    diseaseName.set(j, temp);

                    temp = assocName.get(i);
                    assocName.set(i, assocName.get(j));
                    assocName.set(j, temp);

                    temp = specialName.get(i);
                    specialName.set(i, specialName.get(j));
                    specialName.set(j, temp);
                }
            }
        }

        Log.i(TAG, matched.toString());
        Log.i(TAG, diseaseName.toString());
        Log.i(TAG, assocName.toString());

        int total = matched.size();
        for (int i=0;i<total;i++){
            /*|| Integer.valueOf(matched.get(i))>1*/
            if (most.size()<(total/2) || (Double.valueOf(assocName.get(i))/Double.valueOf(matched.get(i)))>0.8){
                most.add(diseaseName.get(i));
                mostVal.add(String.valueOf(Math.floor((Double.valueOf(assocName.get(i))/Double.valueOf(matched.get(i)))*100)) + "%");
            }
            else{
                least.add(diseaseName.get(i));
                leastVal.add(String.valueOf(Math.floor((Double.valueOf(assocName.get(i))/Double.valueOf(matched.get(i)))*100)) + "%");
            }
        }
        child.put(group.get(0), most);
        child.put(group.get(1), least);

        childVal.put(group.get(0),mostVal);
        childVal.put(group.get(1),leastVal);
    }
}
