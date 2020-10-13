package com.saman.groceryshopping.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.saman.groceryshopping.Categories.SubSubCategoriesActivity;
import com.saman.groceryshopping.Models.CategoryModel;
import com.saman.groceryshopping.R;

import java.util.List;

public class SubCategoriesAdapter extends RecyclerView.Adapter<SubCategoriesAdapter.modelViewHolder> {
    Context context;
    List<CategoryModel> list;

    public SubCategoriesAdapter(Context context, List<CategoryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public modelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_category_layout, parent, false);
        modelViewHolder mvh = new modelViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull modelViewHolder holder, final int position) {
        holder.name.setText(list.get(position).getName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SubSubCategoriesActivity.class);
                intent.putExtra("cate_id", list.get(position).getId());
                intent.putExtra("cate_name",list.get(position).getName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class modelViewHolder extends RecyclerView.ViewHolder {
        CardView item;
        TextView name;

        public modelViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item_layout);
            name = itemView.findViewById(R.id.category_name);
        }
    }
}
