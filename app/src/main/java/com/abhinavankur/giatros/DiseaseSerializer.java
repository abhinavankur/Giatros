package com.abhinavankur.giatros;

import android.os.AsyncTask;

import java.util.ArrayList;

public class DiseaseSerializer extends AsyncTask<String, Void, Void>{

    String disease, specialization;
    ArrayList<String> tests, symptoms;
    ArrayList<Double> assoc;

    public DiseaseSerializer(String disease, String specialization, ArrayList<String> tests, ArrayList<String> symptoms, ArrayList<Double> assoc){
        this.tests = new ArrayList<>();
        this.symptoms = new ArrayList<>();
        this.assoc = new ArrayList<>();
        this.disease = disease;
        this.specialization = specialization;
        this.tests = tests;
        this.symptoms = symptoms;
        this.assoc = assoc;
    }

    @Override
    protected Void doInBackground(String... params) {
        return null;
    }
}
