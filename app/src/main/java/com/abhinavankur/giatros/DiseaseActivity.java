package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DiseaseActivity extends AppCompatActivity implements ReceiveData{
    ArrayList<String> symptoms, diseaseId, diseaseName = new ArrayList<>();
    Bundle bundle;
    DiseaseLoaderAsync diseaseLoaderAsync;
    Intent i;
    ListView listView;
    ListAdapter listAdapter;
    ProgressDialog dialog;
    private static final String TAG = "giatros";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease);

        i = getIntent();
        symptoms = i.getStringArrayListExtra("symptoms");
        dialog = new ProgressDialog(this);
        dialog.setMessage("Diseases loading...");
        dialog.show();
        diseaseLoaderAsync = new DiseaseLoaderAsync(dialog,symptoms,this);
        diseaseLoaderAsync.execute();

    }

    @Override
    public void getData(ArrayList<String> symptoms) {
        this.diseaseName = symptoms;
    }

    public void callback(){
        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,diseaseName);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DiseaseActivity.this,String.valueOf(diseaseName.get(position)),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
