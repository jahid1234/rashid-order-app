package com.example.root.rashidgroupapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.root.rashidgroupapp.Modal.OrderListHistoryModal;
import com.example.root.rashidgroupapp.R;

import java.util.List;

public class OrderListHistoryRecyclerViewAdapter extends RecyclerView.Adapter<OrderListHistoryRecyclerViewAdapter.ViewHolder> {

    List<OrderListHistoryModal> orderListActivities;
    Context mContext;

    public OrderListHistoryRecyclerViewAdapter(List<OrderListHistoryModal> orderListActivities, Context mContext) {
        this.orderListActivities = orderListActivities;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list,parent,false);
        return new OrderListHistoryRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.customerTextView.setText(orderListActivities.get(position).getCustomerName());
            holder.productTextView.setText(orderListActivities.get(position).getProduct());
            holder.unitPriceTextView.setText(orderListActivities.get(position).getUnitPrice());
            holder.quantityTextView.setText(orderListActivities.get(position).getQuantity());
            holder.unitTextView.setText(orderListActivities.get(position).getUom());
            holder.priceTextView.setText(String.valueOf(orderListActivities.get(position).getPrice()));

    }

    @Override
    public int getItemCount() {
        return orderListActivities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView customerTextView,productTextView,unitPriceTextView,quantityTextView,unitTextView,priceTextView;
        TableRow tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            tableRow         = itemView.findViewById(R.id.record_row);
            customerTextView = itemView.findViewById(R.id.customer_name);
            productTextView  = itemView.findViewById(R.id.product_name);
            unitPriceTextView = itemView.findViewById(R.id.price_unit);
            quantityTextView = itemView.findViewById(R.id.quantity_order);
            unitTextView     = itemView.findViewById(R.id.uom);
            priceTextView    = itemView.findViewById(R.id.price_total);
        }
    }
}
