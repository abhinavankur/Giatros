package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorsFragment extends Fragment {

    ArrayList<String> firstName, lastName, email, phoneNumber, doctorName, messageList;
    ListView doctorsList;
    String diseaseName;
    MyAdapter adapter;
    SaveAppointment sa;
    private static final String TAG = "giatros";
    public DoctorsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctors, container, false);
        doctorsList = (ListView) view.findViewById(R.id.doctorsList);
        firstName = new ArrayList<>();
        lastName = new ArrayList<>();
        email = new ArrayList<>();
        phoneNumber = new ArrayList<>();
        doctorName = new ArrayList<>();
        messageList = new ArrayList<>();

        try{
            firstName = getArguments().getStringArrayList("DoctorFirstName");
            lastName = getArguments().getStringArrayList("DoctorLastName");
            phoneNumber = getArguments().getStringArrayList("DoctorPhone");
            diseaseName = getArguments().getString("DiseaseName");

            String name;
            if (firstName.size()!=0){
                for (int i=0;i<firstName.size();i++){
                    name = "Dr." + firstName.get(i) + " " + lastName.get(i);
                    doctorName.add(name);
                }
            }
            adapter = new MyAdapter();
            doctorsList.setAdapter(adapter);

            /*doctorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), "HEllo", Toast.LENGTH_SHORT).show();
                    if (doctorsList.getTag(position).equals("message")){
                        doctorsList.setTag(position,"nomessage");
                        parent.getChildAt(position).setBackgroundColor(Color.BLACK);
                        messageList.remove(phoneNumber.get(position));
                    }
                    else{
                        doctorsList.setTag(position,"message");
                        parent.getChildAt(position).setBackgroundColor(Color.DKGRAY);
                        messageList.add(phoneNumber.get(position));
                    }
                }
            });*/
        }
        catch (NullPointerException npe){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Doctors")
                    .setMessage("No Doctors available right now.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }}).show();
        }

        /*FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SmsManager smsManager = SmsManager.getDefault();
                    if (messageList.isEmpty()){
                        Snackbar.make(view, "Select a doctor first", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }else {
                        for (String number : messageList){
                            smsManager.sendTextMessage(number, null, "App Testing!", null, null);    *//*destAddress, srcAddress, Text, Sent PendingIntent, Receive PendingIntent*//*
                            Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_SHORT).show();
                            Log.i(TAG, number);
                        }
                    }

                }
            });
        }*/
        return view;
    }

    public class MyAdapter extends BaseAdapter {
        /*Adapter holds the reference to the list which is passed.*/
        @Override
        public int getCount() {
            return firstName.size();
        }

        @Override
        public Object getItem(int position) {
            return firstName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                /*LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.doctors_list, parent, false);
            }
            /*Context.getSystemService(Context.TELEPHONY_SERVICE);*/
            TextView doctorTextView = (TextView) convertView.findViewById(R.id.doctorName);
            ImageButton callButton = (ImageButton) convertView.findViewById(R.id.callButton);
            ImageButton messageButton = (ImageButton) convertView.findViewById(R.id.messageButton);
            String value = doctorName.get(position);
            doctorTextView.setText(value);

            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = phoneNumber.get(position);
                    Intent in=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+number));
                    try{
                        startActivity(in);
                    }

                    catch (android.content.ActivityNotFoundException ex){
                        Toast.makeText(getActivity(),"Call not possible",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = phoneNumber.get(position);
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, "Please Help!", null, null);    /*destAddress, srcAddress, Text, Sent PendingIntent, Receive PendingIntent*/
                    Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, number);
                }
            });

            doctorTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Set Appointment")
                            .setMessage("Do you really want to set an appointment?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String first = firstName.get(position);
                                    String last = lastName.get(position);
                                    SharedPreferences preferences = getActivity().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
                                    String patientFirstName = preferences.getString("firstName","Nope");
                                    String patientLastName = preferences.getString("lastName","Nope");
                                    String patientEmailId = preferences.getString("emailId","Nope");
                                    String patientPhoneNumber = preferences.getString("phoneNumber","Nope");
                                    ProgressDialog dialog1 = new ProgressDialog(getActivity());
                                    dialog1.setMessage("Setting Appointment");
                                    dialog1.show();
                                    sa = new SaveAppointment();
                                    sa.setData((DoctorsTestsActivity)getActivity(), dialog1, first, last, diseaseName, patientFirstName, patientLastName, patientEmailId, patientPhoneNumber);
                                    sa.execute();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                    return true;
                }
            });

            return convertView;
        }
    }
}
