package com.example.root.rashidgroupapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.root.rashidgroupapp.Modal.ApprovalProductDetailsModal;
import com.example.root.rashidgroupapp.R;

import java.util.List;

public class ApprovalProductsListRecyclerViewAdapter extends RecyclerView.Adapter<ApprovalProductsListRecyclerViewAdapter.ViewHolder> {
    List<ApprovalProductDetailsModal> approvalProductDetailsModals;
    Context mContext;

    public ApprovalProductsListRecyclerViewAdapter(List<ApprovalProductDetailsModal> approvalProductDetailsModals, Context mContext) {
        this.approvalProductDetailsModals = approvalProductDetailsModals;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_appoval_list,parent,false);
        return new ApprovalProductsListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.slTextView.setText(String.valueOf(position + 1));
        holder.productTextView.setText(approvalProductDetailsModals.get(position).getProductName());
        holder.kgTextView.setText(approvalProductDetailsModals.get(position).getKgQty());
        holder.bagTextView.setText(approvalProductDetailsModals.get(position).getBagQty());
        holder.rateTextView.setText(approvalProductDetailsModals.get(position).getRate());
        holder.takaTextView.setText(approvalProductDetailsModals.get(position).getTotalTaka());


    }

    @Override
    public int getItemCount() {
        return approvalProductDetailsModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView slTextView,productTextView,kgTextView,bagTextView,rateTextView,takaTextView;

        public ViewHolder(View itemView){
            super(itemView);
            slTextView  = itemView.findViewById(R.id.sl_id);
            productTextView = itemView.findViewById(R.id.product_name_id);
            kgTextView = itemView.findViewById(R.id.qty_kg_id);
            bagTextView = itemView.findViewById(R.id.qty_bag_id);
            rateTextView = itemView.findViewById(R.id.rate_id);
            takaTextView = itemView.findViewById(R.id.total_taka_id);
           // editButton =  itemView.findViewById(R.id.edit_btn_id);
        }
    }
}
