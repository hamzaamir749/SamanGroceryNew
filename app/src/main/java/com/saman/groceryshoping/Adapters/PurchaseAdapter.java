package com.saman.groceryshopping.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saman.groceryshopping.Models.PurchaseModel;
import com.saman.groceryshopping.R;

import java.util.List;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.modelViewHolder> {
    List<PurchaseModel> list;
    Context context;

    public PurchaseAdapter(List<PurchaseModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_purchase_history, parent, false);
        modelViewHolder mvh = new modelViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, int position) {
        PurchaseModel model = list.get(position);
        String[] array = model.getProducts().split(",");
        String[] arraydate = model.getDate().split(" ");
        String splist = "";
        for (int i = 0; i < array.length; i++) {
            splist = splist  + array[i]+ "\n";
        }
        holder.name.setText(splist);
        holder.price.setText(model.getPrice());
        holder.status.setText(model.getStatus());
        if (list.get(position).getStatus().equals("delivered")){
            holder.status.setBackgroundColor(context.getResources().getColor(R.color.greencolor));
        }
        String[] arrayDate2=arraydate[0].split("-");
        if (arrayDate2[1].equals("01")){
            holder.date.setText(arrayDate2[2]+" January "+arrayDate2[0]);
        }else if (arrayDate2[1].equals("02")){
            holder.date.setText(arrayDate2[2]+" February "+arrayDate2[0]);
        }else if (arrayDate2[1].equals("03")){
            holder.date.setText(arrayDate2[2]+" March "+arrayDate2[0]);
        }
        else if (arrayDate2[1].equals("04")){
            holder.date.setText(arrayDate2[2]+" April "+arrayDate2[0]);
        }
        else if (arrayDate2[1].equals("05")){
            holder.date.setText(arrayDate2[2]+" May "+arrayDate2[0]);
        }
        else if (arrayDate2[1].equals("06")){
            holder.date.setText(arrayDate2[2]+" June "+arrayDate2[0]);
        }else if (arrayDate2[1].equals("07")){
            holder.date.setText(arrayDate2[2]+" July "+arrayDate2[0]);
        }else if (arrayDate2[1].equals("08")){
            holder.date.setText(arrayDate2[2]+" August "+arrayDate2[0]);
        }else if (arrayDate2[1].equals("09")){
            holder.date.setText(arrayDate2[2]+" September "+arrayDate2[0]);
        }else if (arrayDate2[1].equals("10")){
            holder.date.setText(arrayDate2[2]+" October "+arrayDate2[0]);
        }else if (arrayDate2[1].equals("11")){
            holder.date.setText(arrayDate2[2]+" November "+arrayDate2[0]);
        }else if (arrayDate2[1].equals("12")){
            holder.date.setText(arrayDate2[2]+" December "+arrayDate2[0]);
        }
        holder.time.setText(arraydate[1]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class modelViewHolder extends RecyclerView.ViewHolder {
        TextView name, status, price, date, time;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.purchase_date);
            time = itemView.findViewById(R.id.purchase_time);
        }
    }
}
