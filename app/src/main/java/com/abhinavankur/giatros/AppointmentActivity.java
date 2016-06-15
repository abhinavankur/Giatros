package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AppointmentActivity extends AppCompatActivity implements ReceiveData {

    ArrayList<String> patient, disease, number, appointmentId;
    MyAdapter adapter;
    ProgressDialog dialog;
    ListView appointmentsList;
    AppointmentRemover appointmentRemover;
    AppointmentsLoader al;
    private static final String TAG = "giatros";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        patient = new ArrayList<>();
        disease = new ArrayList<>();
        number = new ArrayList<>();
        appointmentId = new ArrayList<>();

        appointmentsList = (ListView)findViewById(R.id.appointmentsList);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            });
        }*/

        SharedPreferences preferences = getSharedPreferences("Credentials", Context.MODE_PRIVATE);
        String id = preferences.getString("id", "Nope");

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Appointments");
        dialog.show();
        al = new AppointmentsLoader();
        al.setData(dialog, this, id);
        al.execute();
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

    @Override
    public void getData(ArrayList<String>... symptoms) {
        this.patient = symptoms[0];
        this.number = symptoms[1];
        this.disease = symptoms[2];
        this.appointmentId = symptoms[3];

        adapter = new MyAdapter();
        appointmentsList.setAdapter(adapter);

    }

    public class MyAdapter extends BaseAdapter {
        /*Adapter holds the reference to the list which is passed.*/
        @Override
        public int getCount() {
            return patient.size();

        }

        @Override
        public Object getItem(int position) {
            return patient.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                /*LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.appointment_row, parent, false);
            }

            TextView patientName = (TextView) convertView.findViewById(R.id.patient_name);
            TextView diseaseName = (TextView) convertView.findViewById(R.id.disease_name);
            ImageButton call = (ImageButton) convertView.findViewById(R.id.call);
            ImageButton delete = (ImageButton) convertView.findViewById(R.id.delete);
            String patientValue = patient.get(position);
            String diseaseValue = disease.get(position);
            patientName.setText(patientValue);
            diseaseName.setText(diseaseValue);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneNumber = number.get(position);
                    Intent in=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumber));
                    try{
                        startActivity(in);
                    }

                    catch (android.content.ActivityNotFoundException ex){
                        Toast.makeText(AppointmentActivity.this,"Call not possible",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = appointmentId.get(position);
                    dialog.setMessage("Deleting");
                    dialog.show();
                    appointmentRemover = new AppointmentRemover();
                    appointmentRemover.setData(id, dialog, AppointmentActivity.this,position);
                    appointmentRemover.execute();
                }
            });
            return convertView;
        }
    }

    public void deleteAppointment(int position){
        patient.remove(position);
        disease.remove(position);
        number.remove(position);
        appointmentId.remove(position);
        adapter.notifyDataSetChanged();
    }
}

