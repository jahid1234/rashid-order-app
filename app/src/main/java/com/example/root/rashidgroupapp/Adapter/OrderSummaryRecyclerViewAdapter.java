package com.example.root.rashidgroupapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.root.rashidgroupapp.Activity.OrderHistoryDetailsActivity;
import com.example.root.rashidgroupapp.Modal.OrderSummaryModal;
import com.example.root.rashidgroupapp.R;

import java.util.List;

public class OrderSummaryRecyclerViewAdapter extends RecyclerView.Adapter<OrderSummaryRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<OrderSummaryModal> orderSummaryModals;

    public OrderSummaryRecyclerViewAdapter(Context context, List<OrderSummaryModal> orderSummaryModals) {
        this.context = context;
        this.orderSummaryModals = orderSummaryModals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_summary_list,parent,false);
        return new OrderSummaryRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.docNoTextView.setText(orderSummaryModals.get(position).getDocNo());
        holder.nameTextView.setText(orderSummaryModals.get(position).getCustomerName());
        holder.dateTextView.setText(orderSummaryModals.get(position).getDate());
        holder.priceTextView.setText(orderSummaryModals.get(position).getOrderPrice());

        holder.detailsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderHistoryDetailsActivity.class);
                intent.putExtra("docNo",orderSummaryModals.get(position).getDocNo());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderSummaryModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView docNoTextView,nameTextView,dateTextView,priceTextView,detailsTextView;
        TableRow tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            docNoTextView = itemView.findViewById(R.id.summary_doc);
            nameTextView  = itemView.findViewById(R.id.summary_customer);
            dateTextView  = itemView.findViewById(R.id.summary_date);
            priceTextView = itemView.findViewById(R.id.summary_price);
            detailsTextView = itemView.findViewById(R.id.summary_details);
        }
    }
}
