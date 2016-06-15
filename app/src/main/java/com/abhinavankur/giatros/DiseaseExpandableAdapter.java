package com.abhinavankur.giatros;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Abhinav Ankur on 4/6/2016.
 */
public class DiseaseExpandableAdapter extends BaseExpandableListAdapter{

    Context context;
    DiseaseShowActivity dsa;
    List<String> group;
    HashMap<String, List<String>> child, childVal;
    Intent i;
    private static final String TAG = "giatros";
    public DiseaseExpandableAdapter(Context context, List<String> group, HashMap<String, List<String>> child, HashMap<String, List<String>> childVal) {
        this.context = context;
        this.dsa = (DiseaseShowActivity) context;
        this.group = group;
        this.child = child;
        this.childVal = childVal;
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(group.get(groupPosition)).size();
    }

    public int getChildrenValCount(int groupPosition) {
        return childVal.get(group.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(group.get(groupPosition)).get(childPosition);
    }

    public Object getChildVal(int groupPosition, int childPosition) {
        return childVal.get(group.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public long getChildValId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);
        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.disease_group, null);
        }

        TextView diseaseGroup = (TextView) convertView.findViewById(R.id.diseaseGroup);
        diseaseGroup.setTypeface(null, Typeface.BOLD);
        diseaseGroup.setText(groupTitle);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);
        String childValText = (String) getChildVal(groupPosition, childPosition);
        if (convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.disease_child, null);
        }
        TextView diseaseChild = (TextView) convertView.findViewById(R.id.diseaseChild);
        TextView diseaseChildVal = (TextView) convertView.findViewById(R.id.diseaseChildVal);
        Button showTests = (Button) convertView.findViewById(R.id.showDoctorsTests);
        showTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String disease = (String) getChild(groupPosition, childPosition);
                String special = dsa.findSpecialization(disease);
                i = new Intent(context, DoctorsTestsActivity.class);
                i.putExtra("specialName",special);
                i.putExtra("diseaseName",disease);
                dsa.startActivity(i);
            }
        });
        diseaseChild.setText(childText);
        diseaseChildVal.setText(childValText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
