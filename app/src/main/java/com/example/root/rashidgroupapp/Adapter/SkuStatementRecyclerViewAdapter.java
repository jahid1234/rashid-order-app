package com.example.root.rashidgroupapp.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.root.rashidgroupapp.Modal.SkuStatementModal;
import com.example.root.rashidgroupapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by root on 1/26/20.
 */

public class SkuStatementRecyclerViewAdapter extends RecyclerView.Adapter<SkuStatementRecyclerViewAdapter.ViewHolder>{

    List<SkuStatementModal> skuStatementModals;
    Context mContext;
    ArrayList<SkuStatementModal> modalArrayList;

    public SkuStatementRecyclerViewAdapter(List<SkuStatementModal> skuStatementModals, Context mContext) {
        this.skuStatementModals = skuStatementModals;
        this.mContext = mContext;
        modalArrayList = new ArrayList<>();
        this.modalArrayList.addAll(skuStatementModals);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sku_statement_list,parent,false);
        return new SkuStatementRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productTextView.setText(skuStatementModals.get(position).getProductName());
        holder.quantityTextView.setText(skuStatementModals.get(position).getAvlQty());
        holder.unitTextView.setText(skuStatementModals.get(position).getUnit());
    }

    @Override
    public int getItemCount() {
        return skuStatementModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView productTextView,quantityTextView,unitTextView;
        TableRow tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            tableRow         = itemView.findViewById(R.id.record_row);
            productTextView  = itemView.findViewById(R.id.product_name_stmt);
            quantityTextView = itemView.findViewById(R.id.quantity_stmt);
            unitTextView     = itemView.findViewById(R.id.unit_stmt);
        }
    }

    public void searchFilter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        skuStatementModals.clear();
        if(charText.length()== 0){
            skuStatementModals.addAll(modalArrayList);
        }else{
            for ( SkuStatementModal modal: modalArrayList) {
                if(modal.getProductName().toLowerCase(Locale.getDefault()).contains(charText)){
                    skuStatementModals.add(modal);
                }
            }
        }

        notifyDataSetChanged();

    }
}
