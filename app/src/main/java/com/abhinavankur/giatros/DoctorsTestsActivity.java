package com.abhinavankur.giatros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.abhinavankur.giatros.TestFragment;
import com.abhinavankur.giatros.DoctorsFragment;

public class DoctorsTestsActivity extends AppCompatActivity implements ReceiveData{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TestFragment testFragment;
    DoctorsFragment doctorsFragment;
    Bundle bundleDoctor;
    Bundle bundleTest;

    DoctorsTestsFinder dtf;
    Intent i;
    String specialName, diseaseName;
    ArrayList<String> doctorFirstName, doctorLastName, doctorEmail, doctorPhone, testName;
    private static final String TAG = "giatros";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_tests);

        testFragment = new TestFragment();
        doctorsFragment = new DoctorsFragment();

        i = getIntent();
        specialName = i.getStringExtra("specialName");
        diseaseName = i.getStringExtra("diseaseName");
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        dtf = new DoctorsTestsFinder();
        dtf.setData(dialog, specialName, diseaseName, this);
        dtf.execute();
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

        if (menuItem == "Update Profile") {
            /*Snackbar.make(View view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();*/
            UpdateProfile updateProfile = new UpdateProfile(this);
            updateProfile.setFields();
            Toast.makeText(this, menuItem, Toast.LENGTH_SHORT).show();
        }

        if (menuItem == "Enter Symptoms") {
            Intent i = new Intent(this,SymptomAugmenterActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(testFragment, "TESTS");
        adapter.addFragment(doctorsFragment, "DOCTORS");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void getData(ArrayList<String>... symptoms) {
        doctorFirstName = symptoms[0];
        doctorLastName = symptoms[1];
        doctorEmail = symptoms[2];
        doctorPhone = symptoms[3];
        testName = symptoms[4];

        bundleDoctor = new Bundle();
        bundleTest = new Bundle();

        if (!doctorFirstName.isEmpty()){
            bundleDoctor.putStringArrayList("DoctorFirstName", doctorFirstName);
            bundleDoctor.putStringArrayList("DoctorLastName", doctorLastName);
            bundleDoctor.putStringArrayList("DoctorPhone", doctorPhone);
            bundleDoctor.putString("DiseaseName", diseaseName);
            doctorsFragment.setArguments(bundleDoctor);

        }
        if (!testName.isEmpty()){
            bundleTest.putStringArrayList("Test", testName);
            testFragment.setArguments(bundleTest);
        }

        /*
        Sets the action bar if not set true
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Log.i(TAG, doctorFirstName.toString());
        Log.i(TAG, testName.toString());
    }
}
