package com.example.root.rashidgroupapp.Activity;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.root.rashidgroupapp.R;

import java.util.ArrayList;

/**
 * Created by root on 5/21/19.
 */

public class SalesStatusAdapter extends ArrayAdapter<String>{

    ArrayList<String> salesItems;
    Context context;
    public SalesStatusAdapter(@NonNull Context context, @NonNull ArrayList<String> objects) {
        super(context, R.layout.sales_status_recycler_layout, objects);
        this.context = context;
        this.salesItems = objects;
    }

    static class ViewHolder {

        public TextView territoryName;
        public TextView territoryCode;
        public TextView tp;
        public TextView target;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(R.layout.sales_status_recycler_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.territoryName = rowView.findViewById(R.id.territory);
            viewHolder.territoryCode = rowView.findViewById(R.id.territory_code);
            viewHolder.tp = rowView.findViewById(R.id.tp);
            viewHolder.target = rowView.findViewById(R.id.target);
            rowView.setTag(viewHolder);
        }
        String[] items = new String[salesItems.size()];
        items = salesItems.toArray(items);
        String[] item = items[position].split(" ");
        ViewHolder holder = (ViewHolder)rowView.getTag();

        holder.territoryName.setText(item[0]);
        holder.territoryCode.setText(item[2]);
        holder.tp.setText(item[4]);
        holder.target.setText(item[6]);
        return rowView;
    }
}

