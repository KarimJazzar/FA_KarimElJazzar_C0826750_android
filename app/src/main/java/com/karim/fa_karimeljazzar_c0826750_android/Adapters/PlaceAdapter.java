package com.karim.fa_karimeljazzar_c0826750_android.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.karim.fa_karimeljazzar_c0826750_android.Models.PlaceModel;
import com.karim.fa_karimeljazzar_c0826750_android.R;

import java.util.ArrayList;

public class PlaceAdapter extends BaseAdapter {
    ArrayList<PlaceModel> data=new ArrayList<>();
    LayoutInflater inflater;//
    Activity activity;

    //constructor
    public PlaceAdapter(Context context, ArrayList<PlaceModel>data)
    {
        this.data=data;
        inflater=LayoutInflater.from(context);
        //activity = a;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null)
        {
            view=inflater.inflate(R.layout.place_layout,null);
            holder=new ViewHolder();
            holder.name =view.findViewById(R.id.nameP);
            holder.longitude =view.findViewById(R.id.longitudeP);
            holder.latitude = view.findViewById(R.id.latitudeP);
            holder.isVisited = view.findViewById(R.id.visitedP);
            view.setTag(holder);

        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(data.get(i).getName());
        holder.latitude.setText(String.valueOf(data.get(i).getLatitude()));
        holder.longitude.setText(String.valueOf(data.get(i).getLongitude()));
        if(data.get(i).getVisited() == 1){
            holder.isVisited.setText("Yes");
            holder.isVisited.setBackgroundColor(Color.GREEN);
        }else{
            holder.isVisited.setText("No");
            holder.isVisited.setBackgroundColor(Color.RED);
        }


        return view;
    }
    static class ViewHolder{
        private TextView name;
        private TextView longitude;
        private TextView latitude;
        private TextView isVisited;

    }
}

