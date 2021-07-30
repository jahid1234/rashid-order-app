package com.example.root.rashidgroupapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.root.rashidgroupapp.Activity.OrderListActivity;
import com.example.root.rashidgroupapp.Activity.DatabaseHelper;
import com.example.root.rashidgroupapp.Modal.OrderListModal;
import com.example.root.rashidgroupapp.R;

import java.util.List;

public class OrderListRecyclerViewAdapter extends RecyclerView.Adapter<OrderListRecyclerViewAdapter.ViewHolder> {

    List<OrderListModal> orderListActivities;
    Context mContext;

    public OrderListRecyclerViewAdapter(List<OrderListModal> orderListActivities, Context mContext) {
        this.orderListActivities = orderListActivities;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list,parent,false);
        return new OrderListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.customerTextView.setText(orderListActivities.get(position).getCustomerName());
            holder.productTextView.setText(orderListActivities.get(position).getProduct());
            holder.unitPriceTextView.setText(orderListActivities.get(position).getUnitPrice());
            holder.quantityTextView.setText(orderListActivities.get(position).getQuantity());
            holder.unitTextView.setText(orderListActivities.get(position).getUom());
            holder.priceTextView.setText(String.valueOf(orderListActivities.get(position).getPrice()));

            holder.tableRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
                    final Dialog dialog = new Dialog(mContext);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setContentView(R.layout.custom);

                    Button yesBtn = dialog.findViewById(R.id.dialogButtonOK);
                    Button noBtn  = dialog.findViewById(R.id.cancel);

                    yesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseHelper.deleteSalesOrderLine(orderListActivities.get(position).getProductID(),orderListActivities.get(position).getDocNo());
                            orderListActivities.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,orderListActivities.size());
                            OrderListActivity orderListActivity = new OrderListActivity();
                            orderListActivity.loadOrderPrice(mContext);
                            dialog.dismiss();
                        }
                    });

                    noBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    return false;
                }
            });
    }

    @Override
    public int getItemCount() {
        return orderListActivities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView customerTextView,productTextView,unitPriceTextView,quantityTextView,unitTextView,priceTextView;
        LinearLayout tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            tableRow         = itemView.findViewById(R.id.orderListRow);
            customerTextView = itemView.findViewById(R.id.customer_name);
            productTextView  = itemView.findViewById(R.id.product_name);
            unitPriceTextView = itemView.findViewById(R.id.price_unit);
            quantityTextView = itemView.findViewById(R.id.quantity_order);
            unitTextView     = itemView.findViewById(R.id.uom);
            priceTextView    = itemView.findViewById(R.id.price_total);
        }
    }
}
