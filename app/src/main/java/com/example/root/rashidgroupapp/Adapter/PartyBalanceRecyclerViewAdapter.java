package com.example.root.rashidgroupapp.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.root.rashidgroupapp.Modal.PartyBalanceModal;
import com.example.root.rashidgroupapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by root on 1/28/20.
 */

public class PartyBalanceRecyclerViewAdapter extends RecyclerView.Adapter<PartyBalanceRecyclerViewAdapter.ViewHolder> {

    List<PartyBalanceModal> partyBalanceModals;
    Context mContext;
    ArrayList<PartyBalanceModal> balanceModalArrayList;

    public PartyBalanceRecyclerViewAdapter(List<PartyBalanceModal> partyBalanceModals, Context mContext) {
        this.partyBalanceModals = partyBalanceModals;
        this.mContext = mContext;
        balanceModalArrayList = new ArrayList<>();
        balanceModalArrayList.addAll(partyBalanceModals);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.party_balance_list,parent,false);
        return new PartyBalanceRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.partyId.setText(partyBalanceModals.get(position).getCustomerCode());
        holder.partyName.setText(partyBalanceModals.get(position).getCustomerName());
        holder.partyBalance.setText(partyBalanceModals.get(position).getBalance());
    }

    @Override
    public int getItemCount() {
        return partyBalanceModals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView partyId,partyName,partyBalance;
        TableRow tableRow;
        public ViewHolder(View itemView){
            super(itemView);
            partyId          = itemView.findViewById(R.id.party_id_list);
            partyName        = itemView.findViewById(R.id.party_name_list);
            partyBalance     = itemView.findViewById(R.id.party_balance_list);
        }
    }


    public void searchFilter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        partyBalanceModals.clear();
        if(charText.length()== 0){
            partyBalanceModals.addAll(balanceModalArrayList);
        }else{
            for ( PartyBalanceModal modal: balanceModalArrayList) {
                if(modal.getCustomerName().toLowerCase(Locale.getDefault()).contains(charText)){
                    partyBalanceModals.add(modal);
                }
            }
        }

        notifyDataSetChanged();

    }
}
